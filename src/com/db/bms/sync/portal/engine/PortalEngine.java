
package com.db.bms.sync.portal.engine;

import java.io.File;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.locks.ReentrantLock;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.log4j.Logger;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.alibaba.fastjson.JSON;
import com.db.bms.entity.Album;
import com.db.bms.entity.Operator;
import com.db.bms.entity.Portal;
import com.db.bms.entity.PortalPublishNotice;
import com.db.bms.entity.Video;
import com.db.bms.entity.Portal.PortalStatus;
import com.db.bms.entity.Video.VideoStatus;
import com.db.bms.service.AlbumService;
import com.db.bms.service.OperatorService;
import com.db.bms.service.PictureService;
import com.db.bms.service.PortalService;
import com.db.bms.service.VideoService;
import com.db.bms.sync.portal.client.PortalClient;
import com.db.bms.sync.portal.protocal.CommonResultCode;
import com.db.bms.sync.portal.protocal.Poster;
import com.db.bms.sync.portal.protocal.PublishNoticeREQT;
import com.db.bms.sync.portal.protocal.PublishNoticeRESP;
import com.db.bms.sync.portal.protocal.VideoCutNotifyREQT;
import com.db.bms.sync.portal.protocal.VideoCutNotifyRESP;
import com.db.bms.utils.DateUtil;
import com.db.bms.utils.Delimiters;
import com.db.bms.utils.FTPUtil;
import com.db.bms.utils.FileUtils;
import com.db.bms.utils.HttpUtils;
import com.db.bms.utils.StringUtils;
import com.db.bms.utils.core.PageUtil;


public class PortalEngine {

	private final static Logger logger = Logger.getLogger(PortalEngine.class);

	private ReentrantLock lockCseq = new ReentrantLock();

	private Executor executor;
	boolean terminate = false;

	private PortalService portalService;

	private PortalProcessor processor;

	private PortalClient client;

	private String captureSysUrl;

	private String portalUrl;

	private VideoService videoService;

	private OperatorService operatorService;

	private AlbumService albumService;

	private PictureService pictureService;

	public void setExecutor(Executor executor) {
		this.executor = executor;
	}

	public void setPortalService(PortalService portalService) {
		this.portalService = portalService;
	}

	public void setProcessor(PortalProcessor processor) {
		this.processor = processor;
	}

	public void setClient(PortalClient client) {
		this.client = client;
	}

	public void setCaptureSysUrl(String captureSysUrl) {
		this.captureSysUrl = captureSysUrl;
	}

	public void setPortalUrl(String portalUrl) {
		this.portalUrl = portalUrl;
	}

	public void setVideoService(VideoService videoService) {
		this.videoService = videoService;
	}

	public void setOperatorService(OperatorService operatorService) {
		this.operatorService = operatorService;
	}

	public void setAlbumService(AlbumService albumService) {
		this.albumService = albumService;
	}

	public void setPictureService(PictureService pictureService) {
		this.pictureService = pictureService;
	}

	@PostConstruct
	public void init() {
		processor.init();
		this.executor.execute(new PublishNoticeThread());
		this.executor.execute(new VideoCutNotifyThread());
		this.executor.execute(new PosterHandleThread());
	}
	
	@PreDestroy 
	public void destroy() {
		try {
			terminate = true;
			
			ThreadPoolTaskExecutor c  = (ThreadPoolTaskExecutor)this.executor;
			
			c.shutdown();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	public class PublishNoticeThread implements Runnable {
		@Override
		public void run() {
			while (!terminate) {
				List<PortalPublishNotice> noticeList = null;
				try {
					logger.info("Begin send publish notice to portal...");
					
					noticeList = processor.getNextNotice();
					if (noticeList == null) {
						continue;
					}
					
					PublishNoticeREQT request = new PublishNoticeREQT();
					request.setSerialNo(generateCseq());
					request.setResourceList(noticeList);

					Portal search = new Portal();
					search.setStatus(PortalStatus.ENABLE.getIndex());
					PageUtil page = search.getPageUtil();
					page.setPageCount(1000); 
					int totalCount = portalService.findPortalCount(search);
					page.setRowCount(totalCount);
					if (totalCount > 0) {
						for (int i = 1; i <= page.getPageCount(); i++) {
							List<Portal> portalList = portalService.findPortals(search);
							for (Portal portal : portalList) {
								try {
									request.setSystemId(portal.getSysId());
									// logger.info(request.toString());
									logger.info("Send publish notice [PulishNotice] operation request:" + request.build());
									
									// DDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDebug
									portal.setSysUrl("http://127.0.0.1:3000/gportal");
									
									String reqStr = request.build();
									String responseText = client.sendMsg(portal.getSysUrl() + "/PublishNotice",reqStr);
									logger.info("Send publish notice [PulishNotice] operation response:" + responseText);
									PublishNoticeRESP response = JSON.parseObject(responseText, PublishNoticeRESP.class);
									logger.info("publish notify response:" + response.toString());
								}
								catch(Exception e) {
									logger.error("publish notify Error:" + portal.toString());
								}
								break;// DDDDDDDDDDDDDDDDDDDd
							}

							int pageId = i + 1;
							page.setPageId(pageId);
							page.setRowCount(totalCount);
						}
					}
					logger.info("Send publish notice to portal end.");
				} 
				catch (Exception e) {
					logger.error("Send publish notice to portal exception occurred, cause by:{}",e);
				}
			}
		}
	}

	public class VideoCutNotifyThread implements Runnable {

		@Override
		public void run() {

			while (!terminate) {

				Video video = null;
				try {
					logger.info("Begin send video cut notify to video capture system...");
					video = processor.getNextVideo();
					if (video == null) {
						continue;
					}
					VideoCutNotifyREQT request = new VideoCutNotifyREQT();
					request.setSerialNo(generateCseq());
					request.setSourceUrl(video.getSourceUrl());
					request.setReportUrl(portalUrl + "/" + "VideoCutReport");
					request.setInjectImageUrl(portalUrl + "/" + "InjectVideoImage");
					request.setAssetId(video.getAssetId());
					request.setAssetTitle(video.getVideoName());
					request.setUserName(video.getUserName());
					request.setPassword(video.getPassword());
					request.setWidth(video.getWidth());
					request.setHeight(video.getHeight());
					request.setInterval(video.getInterval());

					logger.info("Send video cut notify operation request:"	+ request.build());
					String responseText = client.sendMsg(captureSysUrl,	request.build());
					logger.info("Send video cut notify operation response:"	+ responseText);
					//JsonInput jsonInput = JsonInput.newInstance(responseText);
					VideoCutNotifyRESP response = JSON.parseObject(responseText, VideoCutNotifyRESP.class);
					//response.mergeFrom(jsonInput);

					video.setOriginResult(response.getResultCode());
					video.setFailReason(response.getResultDesc());
					video.setUpdateTime(DateUtil.getCurrentTime());
					switch (CommonResultCode.getResultCode(response.getResultCode())) {
					case SUCCESS:
						video.setStatus(VideoStatus.COMMIT_SUCCESS.getIndex());
						video.setUpdateTime(DateUtil.getCurrentTime());
						Album album = albumService.findAlbumByAlbumId(video.getAssetId());
						if (album != null){
							pictureService.deletePicturesByAlbumNo(new Long[]{album.getAlbumNo()});
						}
						break;
					case INVALID_PARAM:
						video.setStatus(VideoStatus.COMMIT_FAILED.getIndex());
						if (StringUtils.isEmpty(video.getFailReason())) {
							video.setFailReason("参数非法！");
						}
						break;
					case JSON_STRUCTURE_ERROR:
						video.setStatus(VideoStatus.COMMIT_FAILED.getIndex());
						if (StringUtils.isEmpty(video.getFailReason())) {
							video.setFailReason("json结构错误！");
						}
						break;
					case JSON_PARSE_ERROR:
						video.setStatus(VideoStatus.COMMIT_FAILED.getIndex());
						if (StringUtils.isEmpty(video.getFailReason())) {
							video.setFailReason("json解析错误！");
						}
						break;
					case COMMUNICATION_EXCEPTION:
						video.setStatus(VideoStatus.COMMIT_FAILED.getIndex());
						if (StringUtils.isEmpty(video.getFailReason())) {
							video.setFailReason("系统通信异常！");
						}
						break;
					case NOT_FOUND_SYSTEM:
						video.setStatus(VideoStatus.COMMIT_FAILED.getIndex());
						if (StringUtils.isEmpty(video.getFailReason())) {
							video.setFailReason("未能找到系统ID！");
						}
						break;
					case NO_ACCESS_RIGHTS:
						video.setStatus(VideoStatus.COMMIT_FAILED.getIndex());
						if (StringUtils.isEmpty(video.getFailReason())) {
							video.setFailReason("没有访问权限！");
						}
						break;
					case OTHER:
						video.setStatus(VideoStatus.COMMIT_FAILED.getIndex());
						if (StringUtils.isEmpty(video.getFailReason())) {
							video.setFailReason("视频截图系统内部错误！");
						}
						break;
					default:
						video.setStatus(VideoStatus.COMMIT_FAILED.getIndex());
						if (StringUtils.isEmpty(video.getFailReason())) {
							video.setFailReason("视频截图系统内部错误！");
						}
						break;
					}

					videoService.updateVideo(video);
					logger.info("Send video cut notify to video capture system end.");
				} catch (Exception e) {
					video.setStatus(VideoStatus.COMMIT_FAILED.getIndex());
					video.setUpdateTime(DateUtil.getCurrentTime());
					video.setFailReason("系统通信异常！");
					try {
						videoService.updateVideo(video);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					logger.error(
							"Send video cut notify to video capture system exception occurred, cause by:{}",
							e);
				}
			}
		}
	}

	public class PosterHandleThread implements Runnable {

		private static final String HTTP = "http";

		private static final String FTP = "ftp";

		public void run() {
			while (!terminate) {
				Poster poster = null;
				try {
					poster = processor.getNextPoster();
					if (poster != null) {
						logger.info("Begin to download the poster,"	+ poster.toString());
						if (poster.getPosterUrl().startsWith(HTTP)) {
							HttpUtils.downloadFile(poster.getPosterUrl(), poster.getLocalFile());
							File file = new File(poster.getLocalFile());
							Operator curOper = operatorService.findOperatorById(poster.getOperatorNo());
							if (!operatorService.validateUsedSpace(curOper,	file.length())) {
								logger.error("Discarding the posters,there is insufficient space on the users" + poster.toString());
								FileUtils.delFile(poster.getLocalFile());
							}
							operatorService.calculateUsedSpace(curOper, file, true);
						} else if (poster.getPosterUrl().startsWith(FTP)) {
							String protocalSuffix = poster.getPosterUrl().substring(6);
							String ftpInfo = protocalSuffix.substring(0, protocalSuffix.indexOf("/"));
							String remoteFile = protocalSuffix.substring(protocalSuffix.indexOf("/"));
							String[] ftpInfoArr = ftpInfo.split("@");
							if (ftpInfoArr.length >= 2) {
								String[] accountInfo = ftpInfoArr[0].split(Delimiters.SEMICOLON);
								String[] addressInfo = ftpInfoArr[1].split(Delimiters.SEMICOLON);
								FTPUtil.downFile(addressInfo[0], Integer.valueOf(addressInfo[1]),
										accountInfo[0], accountInfo[1],
										remoteFile, poster.getLocalFile());
								File file = new File(poster.getLocalFile());
								Operator curOper = operatorService.findOperatorById(poster.getOperatorNo());
								if (!operatorService.validateUsedSpace(curOper, file.length())) {
									logger.error("Discarding the posters,there is insufficient space on the users" + poster.toString());
									FileUtils.delFile(poster.getLocalFile());
								}
								operatorService.calculateUsedSpace(curOper, file, true);
							} else {
								logger.error("Discarding the posters," + poster.toString());
							}

						} else {
							logger.error("Discarding the posters," + poster.toString());
						}
						logger.info("Download the posters success," + poster.toString());
					}

				} catch (Exception e) {
					logger.error("Discarding the posters," + poster.toString());
					logger.error("Handle poster exception occurred, cause by:{}", e);
				}
			}
		}
	}

	private long defaultCseq = 1L;

	private String generateCseq() {

		lockCseq.lock();
		String newCseq = "";
		try {
			defaultCseq++;
			if (defaultCseq < 99999999999L) {
				newCseq = String.valueOf(defaultCseq);
				int len = newCseq.length();
				for (int i = 0; i < 11 - len; i++) {
					newCseq = "0" + newCseq;
				}

			} else {
				defaultCseq = 1L;
				generateCseq();
			}
		} finally {
			lockCseq.unlock();
		}
		return newCseq;
	}
}

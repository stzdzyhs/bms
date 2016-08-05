
package com.db.bms.sync.portal.engine;

import java.text.ParseException;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.apache.log4j.Logger;

import com.db.bms.entity.PortalPublishNotice;
import com.db.bms.entity.Video;
import com.db.bms.service.PortalService;
import com.db.bms.sync.portal.protocal.Poster;


public class PortalProcessor {

	private final static Logger logger = Logger.getLogger(PortalProcessor.class);

	private int maxPoolNotice = 10000;
	
	private BlockingQueue<List<PortalPublishNotice>> queueNotice;
	
	private BlockingQueue<Video> queueVideo;
	
	private BlockingQueue<Poster> queuePoster;
	
	//private PortalService portalService;

	public void setMaxPoolNotice(int maxPoolNotice) {
		this.maxPoolNotice = maxPoolNotice;
	}

	public void setPortalService(PortalService portalService) {
		//this.portalService = portalService;
	}
	
	public void init() {
		this.queueNotice = new ArrayBlockingQueue<List<PortalPublishNotice>>(this.maxPoolNotice);
		this.queueVideo = new ArrayBlockingQueue<Video>(this.maxPoolNotice);
		this.queuePoster = new ArrayBlockingQueue<Poster>(this.maxPoolNotice);
	}
	
	public void putNoticeToQueue(List<PortalPublishNotice> notice) {
		try {
			if (this.queueNotice.remainingCapacity() == 0) {
				logger.error("{"
						+ Thread.currentThread().getName()
						+ "} : The schedule queue is full, current pool schedule size:"
						+ this.queueNotice.size() + ".");
			}
			this.queueNotice.put(notice);
		} catch (InterruptedException e) {
			logger.error("put publish notice to queue exception occurred, cause by:{}", e);
		}
	}

	public List<PortalPublishNotice> getNextNotice() throws ParseException {
		List<PortalPublishNotice> notice = null;
		try {
			if(this.queueNotice==null) {
				throw new NullPointerException("the notice queue is NULL.");
			}
			notice = this.queueNotice.take();
		} catch (InterruptedException e) {
			//logger.error("Get notice for queue exception occurred, cause by:{}", e);
		}
		return notice;

	}
	
	public void putVideoToQueue(Video video) {
		try {
			if (this.queueVideo.remainingCapacity() == 0) {
				logger.error("{"
						+ Thread.currentThread().getName()
						+ "} : The video queue is full, current pool video size:"
						+ this.queueVideo.size() + ".");
			}
			this.queueVideo.put(video);
		} catch (InterruptedException e) {
			logger.error("put video to queue exception occurred, cause by:{}", e);
		}
	}

	public Video getNextVideo() throws ParseException {
		Video video = null;
		try {
			if(this.queueVideo==null) {
				throw new NullPointerException("the video queue is NULL.");
			}
			video = this.queueVideo.take();
		} catch (InterruptedException e) {
			//logger.error("Get video for queue exception occurred, cause by:{}", e);
		}
		return video;

	}
	
	public void putPosterToQueue(Poster poster) {
		try {
			if (this.queuePoster.remainingCapacity() == 0) {
				logger.error("{"
						+ Thread.currentThread().getName()
						+ "} : The poster queue is full, current pool poster size:"
						+ this.queuePoster.size() + ".");
			}
			this.queuePoster.put(poster);
		} catch (InterruptedException e) {
			logger.error(
					"put poster to queue exception occurred, cause by:{}",
					e);
		}
	}
	
	public Poster getNextPoster() {
		Poster poster = null;
		try {
			poster = this.queuePoster.take();
		} catch (InterruptedException e) {
			//logger.error("Get poster for queue exception occurred, cause by:{}", e);
		}
		return poster;

	}
}

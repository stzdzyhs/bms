
var ROOT=determineRoot();

var strategyModel = {
	eleId:'#plCondition',
	
	strategyNo:null,
	
	strategyData: {
		strategyName:'',
		strategyForm:'',
		
		cardRegionList:[],
		companyList:[],
		spaceList:[],
		featureCodeList:[],
		clientList:[],

		companyNo:null
	},
	
	view:null,
	
	/**
	 * 判断是否有数据
	 * @returns {Boolean}
	 */
	hasData:function() {
		var sd = this.strategyData;
		if(sd.cardRegionList!=null && sd.cardRegionList.length>0) {
			return true;
		}
		if(sd.companyList!=null && sd.companyList.length>0) {
			return true;
		}
		if(sd.spaceList!=null && sd.spaceList.length>0) {
			return true;
		}
		if(sd.featureCodeList!=null && sd.featureCodeList.length>0) {
			return true;
		}
		if(sd.clientList!=null && sd.clientList.length>0) {
			return true;
		}
		
		return false;
	},
	
	/**
	 * 设置视图
	 * @param view
	 */
	setView:function(view) {
		this.view = view;
	},
	
	hasDataId:function() {
		if(isEmpty(this.strategyNo)) {
			return false;
		}
		return true;
	},

	// map id - to list dat
	map1:null,

	/**
	 * map1Data保存了策略可以关联的数据信息
	 */
	getMap1:function() {
		// init
		if(this.map1==null) {
			this.map1 = {};

			this.map1[STRATEGY_CONDITION_TYPE_CARD_REGION]= {
				name:'区域码', 
				selectUrl:ROOT + '/opermgmt/strategy/strategyCardRegionNoSelect.action', 
				
				detailUrl:ROOT + '/opermgmt/region/cardRegionDetail.action',
				detailIdName:'regionId',
				
				keyProp:'id',
				refProp:'cardRegion',
				refKeyProp:'id',
				refNameProp:'regionName',
				
				data:{},
				selectOkFunc:emptyFunc
			};
			
			/*
			this.map1[STRATEGY_CONDITION_TYPE_INTERNET]= {
				name:'网络', 
				selectUrl:ROOT + '/opermgmt/strategy/strategyInternetNoSelect.action',
				data:{},

				detailUrl:ROOT +'/opermgmt/internet/internetDetail.action',
				detailIdName:'internetNo',
				
				keyProp:'id',
				refProp:'internet',
				refKeyProp:'internetNo',
				refNameProp:'internetName',
				
				selectOkFunc:emptyFunc
			};
			*/
			
			this.map1[STRATEGY_CONDITION_TYPE_COMPANY] = {
				name:'运营商',
				selectUrl:ROOT + '/opermgmt/strategy/strategyCompanyNoSelect.action',
				data:{},

				detailUrl:ROOT +'/opermgmt/company/companyDetail.action',
				detailIdName:'companyNo',
					
				keyProp:'id',
				refProp:'company',
				refKeyProp:'companyNo',
				refNameProp:'companyName',
					
				selectOkFunc:emptyFunc
			};
			
			this.map1[STRATEGY_CONDITION_TYPE_SPACE]= {
				name:'空分组', 
				selectUrl:ROOT + '/opermgmt/strategy/strategySpaceNoSelect.action',

				detailUrl:ROOT + '/opermgmt/space/spaceDetail.action',
				detailIdName:'spaceNo',

				keyProp:'id',
				refProp:'space',
				refKeyProp:'spaceNo',
				refNameProp:'spaceName',

				data:{}, 
				selectOkFunc:emptyFunc
			};
			this.map1[STRATEGY_CONDITION_TYPE_FEATURE_CODE]= {
				name:'特征码',
				selectUrl:ROOT + '/opermgmt/strategy/strategyFeatureCodeNoSelect.action', 
				data:{}, 

				detailUrl:ROOT + '/opermgmt/featureCode/featureCodeDetail.action',
				detailIdName:'featureCodeNo',

				keyProp:'id',
				refProp:'featureCode',
				refKeyProp:'featureCodeNo',
				refNameProp:'featureCodeDesc',
				
				selectOkFunc:emptyFunc
			};
			this.map1[STRATEGY_CONDITION_TYPE_CLIENT]= {
				name:'用户', 
				selectUrl:ROOT + '/opermgmt/strategy/strategyClientNoSelect.action',
				data:{},
				
				detailUrl:ROOT + '/opermgmt/client/clientDetail.action',
				detailIdName:'clientNo',

				keyProp:'id',
				refProp:'client',
				refKeyProp:'clientNo',
				refNameProp:'clientName',
				
				selectOkFunc:emptyFunc
			};
		}
		return this.map1;
	},
	
	/**
	 * map1Data保存了策略可以关联的数据信息
	 * @param id
	 * @returns
	 */
	getMap1Data:function(id) {
		var m = this.getMap1();
		var data = m[id];
		if(data==null) {
			throw new Error("map1 error ???:" + id);
		}
		return data;
	},

	map2 : null,

	getMap2:function() {
		return null;
		
	},

	/**
	 * 通过类型得到策略不同数据的数组
	 * @param id
	 * @returns {Array}
	 */
	// map id to array list.
	getMap2Data:function(id) {
		if(id==STRATEGY_CONDITION_TYPE_CARD_REGION) {
			if(this.strategyData.cardRegionList==null) {
				this.strategyData.cardRegionList = [];
			}
			return this.strategyData.cardRegionList;
		}
		if(id==STRATEGY_CONDITION_TYPE_COMPANY) {
			if(this.strategyData.companyList==null) {
				this.strategyData.companyList = [];
			}
			return this.strategyData.companyList;
		}
		if(id==STRATEGY_CONDITION_TYPE_SPACE) {
			if(this.strategyData.spaceList==null) {
				this.strategyData.spaceList = [];
			}
			return this.strategyData.spaceList;
		}
		if(id==STRATEGY_CONDITION_TYPE_FEATURE_CODE) {
			if(this.strategyData.featureCodeList==null) {
				this.strategyData.featureCodeList = [];
			}
			return this.strategyData.featureCodeList;
		}

		if(id==STRATEGY_CONDITION_TYPE_CLIENT) {
			if(this.strategyData.clientList==null) {
				this.strategyData.clientList = [];
			}
			return this.strategyData.clientList;
		}
		throw new Error("map2 error ???:" + id);
	},

	/**
	 * 获取策略数据
	 * @param succCallback - sucessful callback
	 */
	loadData:function(succCallback) {
		var thisObj = this;
		var succFunc = function(data) {
			thisObj.strategyData = data.data;
			
			if(thisObj.view==null) {
				return;
			}
			
			thisObj.view.updateBasicInfo(data.data);		

			var i,j;
			for(i=STRATEGY_CONDITION_TYPE_FIRST;i<=STRATEGY_CONDITION_TYPE_LAST;i++) {
				if (thisObj.hasCondition(i)) {
					thisObj.view.createConditionDiv(i);
					thisObj.view.clearConditionDiv(i);
					var list = thisObj.getMap2Data(i);
					for(j=0;j<list.length;j++) {
						var m1 = thisObj.getMap1Data(i);
						if (list[j][m1.refProp]!=null) { // skip invalid data
							thisObj.view.addKey(i, list[j][m1.keyProp], list[j]);
						}
					}
					thisObj.view.updateKeyCnt(i, list.length);
				}
				else {
					thisObj.view.deleteConditionDiv(i);
				}
			}
			
			if(succCallback!=null) {
				succCallback(data);
			}
		}

		if(!isEmpty(this.strategyNo)) {
			getStrategyAllData(this.strategyNo, succFunc);	
		}
		else {
			succFunc({data:this.strategyData});
		}
	},
	
	/**
	 * 新建时, 选择的数据需要包装(wrap)起来放到数据中
	 * @param id
	 * @param refData
	 * @returns 
	 */
	wrapRefData:function(id, refData) {
		var m1 = this.getMap1Data(id);
		var wrap = {};
		wrap[m1.keyProp] = null;
		wrap[m1.refProp] = refData;
		return wrap;
	},
	
	theAddKey:-1,
	
	
	// name 显示的text
	addKey:function(id,data) {		
		var m1 = this.getMap1Data(id);
		// set data key
		if(data[m1.keyProp] ==null || data[m1.keyProp]==0) {
			// -1 means no added into db yet.
			data[m1.keyProp] = this.theAddKey -1;
			this.theAddKey --;
		}
		
		
		var list = this.getMap2Data(id);
		if(list==null) {
			throw new Error("add key error: " + id + " " + data)
		}

		// set map data
		data.type=id;
		if(data.conditionId==null) {
			data.conditionId=data[m1.refProp][m1.refKeyProp];
		}
		list.push(data);
				
		// update view
		if(this.view!=null) {
			this.view.addKey(id,data[m1.keyProp] , data);
			this.view.updateKeyCnt(id, list.length);
		}
	},

	/**
	 * 根据类型删除策略的条件
	 * @param id - 策略类型
	 * @param dataKey - 数据的key
	 */
	deleteKey:function(id,dataKey) {
		var m2 = this.getMap2Data(id);
		var m1 = this.getMap1Data(id);
		
		var idx = findIdx(m2, m1.keyProp, dataKey);
		if(idx==-1) {
			throw new Error('deleteKey idx error:' + idx);
		}
		
		m2.splice(idx,1);
		// update view
		this.view.delKey(id, dataKey);
		this.view.updateKeyCnt(id);
		//deleteStrategyResourceMapById(data[m1.keyProp] ,succFunc);			
	},
	
	/**
	 * 根据类型删除策略的条件
	 * @param id
	 */
	deleteConditionType:function(id) {
		var m2 = this.getMap2Data(id);
		if(m2.length<1) {
			throw new Error('deleteConditionType error:' + m2.length);
		}
		
		if(id==-1) { // freq
			this.strategyData.strategyType=0;
			this.strategyData.broadcastTimes=0;
			this.strategyData.timeUnit=0;
			this.view.hideFreqType();
			return;
		}
		
		
		m2.length=0;
		
		var thisObj = this;
		// update view
		thisObj.view.deleteConditionDiv(id);
		/*
		var succFunc = function(data) {
			m2.length = 0;
			
			if(thisObj.view==null) {
				return;
			}
			// update view
			thisObj.view.deleteConditionDiv(id);
		};
		
		var m1 = thisObj.getMap1Data(id);
		
		deleteStrategyResource(this.strategyNo,id,"",succFunc);
		*/			
	},

	/**
	 * 判断是否有类型的数据
	 * @param id
	 * @returns {Boolean}
	 */
	hasCondition:function(id) {
		var list = this.getMap2Data(id);
		if(list!=null && list.length>0) {
			return true;
		}
		return false;
	},

	/**
	 * 获取关联数据的id
	 * @param id
	 * @param dataKey
	 * @returns
	 */
	getDataId:function (id, dataKey) {
		var m1 = this.getMap1Data(id);
		var m2 = this.getMap2Data(id);

		var idx = findIdx(m2, m1.keyProp, dataKey);
		if(idx==-1) {
			throw new Error('getDataId id error:' + idx);
		}
		
		var d = m2[idx][m1.keyProp];
		return d;
	},
	
	/**
	 * 获取引用数据(refData)的id
	 * @param id
	 * @param dataKey
	 * @returns
	 */
	getDataRefId:function (id, dataKey) {
		var m1 = this.getMap1Data(id);
		var m2 = this.getMap2Data(id);

		var idx = findIdx(m2, m1.keyProp, dataKey);
		if(idx==-1) {
			throw new Error('getDataId id error:' + idx);
		}
		
		try {
			var d = m2[idx][m1.refProp][m1.refKeyProp];
			return d;
		}
		catch(e) {
			console.log("error: " + idx + " " +  m1.refProp + " " + m1.refKeyProp);
			throw new Error(e);
		}
	},
	
	/**
	 * 获取引用数据(refData)的名字
	 * @param id
	 * @param dataKey
	 * @returns
	 */
	getDataRefName:function (id, dataKey) {
		var m1 = this.getMap1Data(id);
		var m2 = this.getMap2Data(id);

		var idx = findIdx(m2, m1.keyProp, dataKey);
		if(idx==-1) {
			throw new Error('getDataId id error:' + idx);
		}
		
		var d = m2[idx][m1.refProp][m1.refNameProp];
		return d;
	},

	/**
	 * 获取数据的名字
	 * @param id
	 * @param dataKey
	 * @returns
	 */
	getDataName:function(id, dataKey) {
		var d = this.getDataRefName(id,dataKey);
		return d;
	},

	/**
	 * 获取类型数据的大小
	 * @param id
	 * @returns
	 */
	getDataSize:function(id) {
		var list = this.getMap2Data(id);
		if(list==null) {
			return 0;
		}
		return list.length;
	},
	
	getRefIds:function(id) {
		var m1 = this.getMap1Data(id);
		
		var data = [];
		var list = this.getMap2Data(id);
		for(i=0;i<list.length;i++) {
			var d = list[i][m1.refProp];
			if(d!=null) {
				data.push(d[m1.refKeyProp]);
			}
		}
		return data;
	},
	
	toUrlParamStr:function(ids, name) {
		if(ids.length<1) return "";
		var p = name+"=" + ids[0];
		for(i=1;i<ids.length;i++) {
			p = p + "&" + name+"=" + ids[i];
		}
		return p;
	}
	
	
};

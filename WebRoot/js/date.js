// javascript 日期操作方法
Date.prototype.Format = function(fmt) {
	var o = {
		"M+": this.getMonth() + 1,
		//月份   
		"d+": this.getDate(),
		//日   
		"H+": this.getHours(),
		//小时   
		"m+": this.getMinutes(),
		//分   
		"s+": this.getSeconds(),
		//秒   
		"q+": Math.floor((this.getMonth() + 3) / 3),
		//季度   
		"S": this.getMilliseconds() //毫秒   
	};
	if (/(y+)/.test(fmt)){
		fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
	}
	for (var k in o){
		if (new RegExp("(" + k + ")").test(fmt))
			fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
	}
	return fmt;
}
Date.prototype.addSeconds = function(s) {
	this.setSeconds(this.getSeconds() + s);
};
Date.prototype.addHours = function(h) {
	this.setHours(this.getHours() + h);
};
Date.prototype.addDays = function(d) {
	this.setDate(this.getDate() + d);
};
Date.prototype.addWeeks = function(w) {
	this.addDays(w * 7);
};
Date.prototype.addMonths = function(m) {
	var d = this.getDate();
	this.setMonth(this.getMonth() + m);
	if (this.getDate() < d) this.setDate(0);
};
Date.prototype.addYears = function(y) {
	var m = this.getMonth();
	this.setFullYear(this.getFullYear() + y);

	if (m < this.getMonth()) {
		this.setDate(0);
	}
};
//计算两个日期之间的时间差（单位：豪秒）
Date.prototype.compareTo = function(otherDate){
	return parseInt(otherDate-this);
}
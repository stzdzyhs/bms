$(document).ready(function() {
	var showExamples = true;
	$(".toggle-docs-detail").toggle(function(e){
		var details = $(this).text("隐藏")
			.parent().next("ul").find("li > div:first-child").addClass("header-open");
		if ( showExamples ) {
			details.nextAll().show();
		} else {
			details.next().show();
		}
		e.preventDefault();
	},function(e){
		$(this).text("显示")
			.parent().next("ul").find("li > div:first-child").removeClass("header-open")
			.nextAll().hide();
		e.preventDefault();
	});

	//Initially hide all options/methods/events
	$('div.option-description, div.option-examples, div.event-description, div.event-examples, div.method-description, div.method-examples').hide();
	
	//Make list items collapsible
	$('div.option-header h3, div.event-header h3, div.method-header h3').live('click', function() {
		var details = $(this).parent().toggleClass('header-open');
		if ( showExamples ) {
			details.nextAll().toggle();
		} else {
			details.next().toggle();
		}
	});

});
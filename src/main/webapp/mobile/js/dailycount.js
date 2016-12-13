//Initial
$(function(){$('#messageArea').hide();});
//Document ready
$(document).ready(handleDailyCount);

//Handle Ready
function handleDailyCount() {
	var newDate = new Date();
	$('#datetimefrom').datetimepicker({
		useCurrent : true, //Important! See issue #1075
		defaultDate: newDate,
		format: 'YYYY-MM-DD'
	});
	$('#datetimeto').datetimepicker({
		useCurrent : true, //Important! See issue #1075
		defaultDate: newDate,
		format: 'YYYY-MM-DD' 
	});
	$("#datetimefrom").on("dp.change", function(e) {
		$('#datetimeto').data("DateTimePicker").minDate(e.date);
	});
	$("#datetimeto").on("dp.change", function(e) {
		$('#datetimefrom').data("DateTimePicker").maxDate(e.date);
	});
	$('.mobile-sidebar-menu li.sub a').click(function() {
		if ($(this).parent().hasClass('open')) {
			$(this).parent().removeClass('open');
		} else {
			$(this).parent().addClass('open');
		}
	});

	$("#header-navbar").load("header.html");
	
	//hide results when initializing
	$('#record_details').hide();	
	//click button
	$('#dosearch').click(handleSearch);
}

//handle click
function handleSearch() {
	//show results
	$('#record_details').show();	
	//Format date
	var from_moment = $('#datetimefrom').data("DateTimePicker").date();
	var from = from_moment.format("YYYY/MM/DD");
	var to_moment = $('#datetimeto').data("DateTimePicker").date();
	var to = to_moment.format("YYYY/MM/DD");
	//Query String
	var queryStr = {"from":from,"to":to};
	//Clear <tr>
	$("#tablebody").empty();
	$('#totalNo').text(0);
	$('#message').text("");
	//Http Get request and callback
	$.getJSON("../../mobilectrl/vendor/dailycount", queryStr, function(result){
		var num=0;
		$.each(result.records,function(key,value){
			num++;
			//add table entries
			var eeId = value.employeeID.replace(value.employeeID.slice(2,5),"***");
			$('#tablebody').append("<tr><td>"+num+"</td><td>"+value.transactionCode+"</td><td>"+value.consumeTime+"</td><td>"+eeId+"</td></tr>");
		});
		//set total
		$('#totalNo').text(num);		
		if(result.message!=""){
			$('#messageArea').show();
			$('#message').text(result.message);			
		}		
	});
}

function begdaClick() {
	document.getElementById('begdaField').click();
	document.getElementById('begdaField').focus();
}

function enddaClick() {
	document.getElementById('enddaField').click();
	document.getElementById('enddaField').focus();
}
//format date
function formatDate1(d0){
	var mon = d0.getMonth() + 1;
	var datestr = d0.getFullYear()+"-"+mon+"-"+d0.getDate()+" "+d0.getHours()+":"+d0.getMinutes()+":"+d0.getSeconds();
	return datestr;
}

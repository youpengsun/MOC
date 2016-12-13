$(function(){
	getTodayRecords();}   
);

function getTodayRecords() {
	$('#messageArea').hide();
	$('#recordTotal').hide();
	$.getJSON("../../mobilectrl/employee/dailyrecords", function(result) {
		var num = 0;
		$.each(result.records, function(key, value) {
			if (key > 0) {
				$('#allrecords').append('<hr>');
			}
			//append table 
			$('#allrecords').append('<table id="'+key+'"  class="table table-striped table-hover table-bordered">');			
			//append columns
			$('#'+key).append('<tr><td><b>Category:<br>(类型:)</b></td><td>'+value.category+'</td></tr>');
			$('#'+key).append('<tr><td><b>Tran.Code:<br>(业务码:)</b></td><td>'+value.transactionCode+'</td></tr>');
			$('#'+key).append('<tr><td><b>Time:<br>(时间:)</b></td><td>'+value.consumeTime+'</td></tr>');
			$('#'+key).append('<tr><td><b>Name:<br>(姓名:)</b></td><td>'+value.employeeName+'</td></tr>');
			$('#'+key).append('<tr><td><b>Vendor:<br>(商户:)</b></td><td>'+value.vendorName+'</td></tr>');
			$('#'+key).append('<tr><td><b>Line:<br>(餐饮线:)</b></td><td>'+value.vendorLineName+'</td></tr>');
			num = num + 1;			
		});
		if(num>0){
			$('#recordTotal').show();
		    $('#totalNo').text(num);
		}
		if(result.message!=""){
			$('#messageArea').show();
			$('#recordTotal').hide();
			$('#message').html(result.message);			
		}				
	});

}

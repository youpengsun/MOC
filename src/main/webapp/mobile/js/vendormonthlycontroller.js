$(document).ready(
		function() {
			$("#header-navbar").load("header.html");
			var date = new Date();
			var beginDate, endDate;
			var firstDay = new Date(date.getFullYear(), date.getMonth(), 1);
			var lastDay = new Date(date.getFullYear(), date.getMonth() + 1, 0);
			function cb(start, end) {
				$("#datepicker span").html(
						start.format("YYYY/MM/DD") + " - "
								+ end.format("YYYY/MM/DD"));
			}

			$("#datepicker").daterangepicker({

				startDate : firstDay,
				endDate : lastDay,
				locale : {
					format : "YYYY/MM/DD",
					applyLabel : "确定",
					cancelLabel : '取消'
				}

			}, function(start, end) {
				cb(start, end);
				beginDate = $("#datepicker").data('daterangepicker').startDate;
				endDate = $("#datepicker").data('daterangepicker').endDate;
			});

			beginDate = $("#datepicker").data('daterangepicker').startDate;
			endDate = $("#datepicker").data('daterangepicker').endDate;
			cb(beginDate, endDate);

			$("[data-toggle='tooltip']").tooltip();
			$("#search").click(function() {
				$("#message").text("");
				$("#table").empty();
				$("#total").text(0);
				$.ajax({
					url : "../../mobilectrl/consumerecord/querymonth",
					data : {
						beginDate : beginDate.format("YYYY-MM-DD"),
						endDate : endDate.format("YYYY-MM-DD")
					},
					success : function(result) {
						display(result);
					},
					error : function(status) {
						$("#message").text("请求失败！");
					}
				})
			});
			$("#email").click(function() {
				$("#message").text("");
				$.ajax({
					url : "../../mobilectrl/consumerecord/sendmonthlydetails",
					data : {
						beginDate : beginDate.format("YYYY-MM-DD"),
						endDate : endDate.format("YYYY-MM-DD")
					},
					success : function(result) {
						$("#message").text(result.status);
					},
					error : function(status) {
						$("#message").text("请求失败！");
					}
				})
			});

		});

//function display(result) {
//	$("#table").append();
//	var i = 0;
//	$.each(result.results, function(k, v) {
//		console.log(k);
//
//		if (k === "lineRecords") {
//			$.each(v, function(n, value) {
//				$("#table").append(
//						"<tr> <td>" + value.lineName + "</td><td>"
//								+ value.consumeCount + "</td></tr>");
//
//			});
//		}
//		if (k === "totalCount") {
//			console.log(v);
//			$("#total").text(v);
//		}
//	});
//	$('#message').text(result.message);
//}

function display(result) {
	$("#table").append();
	var i = 0;
	$.each(result.records, function(k, v) {
		console.log(k);
		$.each(v.lineRecords, function(n, value) {
			$("#table").append(
					"<tr> <td>" + value.lineName + "</td><td>"
							+ value.consumeCount + "</td></tr>");
		});
		console.log(v);
		$("#total").text(v.totalCount);
	});
	$('#message').text(result.message);
}

var disScroll;
var navHight = $(".header").outerHeight();
var lastScrollTop = 0;
var delat = 0;

$(function(){
	getVendorList();
});

function getVendorList() {
	var staticImgPath = "../../../vendorlogo/";
	var images=["../images/logo_sap_small.png", "../images/subway.png", "../images/gaoxing.png" , "../images/momo.png", "../images/huayin.png","../images/yufeiyu.png","../images/gaoxing.png"];
	var dianpingIdPre = "https://m.dianping.com/shop/";
	  $.getJSON("../../employeeuser/vendor/activelist",function(result){
		  
		  for(var i=0; i<result.vendors.length; i++){
			  var a = i % (images.length);
			  var vendorInfo = result.vendors[i];
			  var vendorId = vendorInfo.id;
			  var dianpingID = vendorInfo.dianpingID;
			  var promotion = vendorInfo.promotion;
			  var vendorName = vendorInfo.id + ' ' + vendorInfo.name;
			  if(promotion == null){
				  promotion = "";
			  }
			   $(".vendor-list").append('<a href="'+ dianpingIdPre + dianpingID  +' "><div class="vendor-view left"><div class="vendor-right"><div class="vendor-img"><img class="img-circle" src="'+ staticImgPath + vendorId + '.png'+' "/></div></div><div class="vendor-left"><div class="vendor-main"><div class="vendor-header"><h3>' 
					   + vendorName 
					   + '</h3></div><div class="vendor-footer"><p><span>'
					   + vendorInfo.address
					   +'</span></p></div><div class="vendor-footer"><p><span class="vendor-promotion">'
					   + promotion
					   +'</span></p></div></div></div></a></div>');
			   };
			   });
	  
	  if(document.cookie.match(/scrollTop=([^;]+)(;|$)/) != null){ 
		    var arr=document.cookie.match(/scrollTop=([^;]+)(;|$)/); 
		  } 
	  setTimeout(function(){$(document.body).scrollTop(arr[1]);},200);
	  
}

function hasScrolled() {
    var a = $(this).scrollTop();
    Math.abs(a - lastScrollTop) <= delat || (a > navHight + 120 && a > lastScrollTop ? 
    		$(".header").removeClass("header-show").addClass("header-hide") : 
    			a + $(window).height() < $(document).height() && $(".header").removeClass("header-hide").addClass("header-show"), lastScrollTop = a)
}

function stopPropagation(a) {
    a.stopPropagation ? a.stopPropagation() : a.cancelBubble = !0
}

slider = function() {
    var a = $(window).height(),
    b = 1;
    $(".burger").click(function() {
        1 == b ? ($(".nav").addClass("open"), $(this).addClass("transform"), $(".sections").addClass("downward").css({
            minHeight: a
        }), $("body").css({
            overflow: "hidden",
            height: a
        }), b = 0) : ($(".nav").removeClass("open"), $(this).removeClass("transform"), $(".sections").removeClass("downward").attr({
            style: ""
        }), $("body").attr({
            style: ""
        }), b = 1)
    })
};

slider(),

$(window).scroll(function() {
    disScroll = !0
}),

setInterval(function() {
    disScroll && (hasScrolled(), disScroll = !1)
},
250),

$(".nav").bind("touchmove",
function(a) {
    stopPropagation(a),
    a.preventDefault()
})	

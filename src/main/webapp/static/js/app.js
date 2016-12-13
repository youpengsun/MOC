var mainPage = angular.module("mainPage", ['ngRoute', 'ngTable', 'ngDialog',
		'ngAnimate', 'ui.bootstrap', 'ui.bootstrap.datetimepicker',
		'ui.bootstrap.showErrors', 'mgcrea.ngStrap.modal',
		'mgcrea.ngStrap.alert', 'ngPrint', "checklist-model", "highcharts-ng",
		"ngBootstrap", "angular-loading-bar","ui.dateTimeInput" ]);

mainPage.controller('mainPageController', function($scope, $rootScope) {
	$scope.tab = {};
	$scope.tab.first = " ";
	$scope.tab.secondIcon = "fa fa-table";

	$scope.checkSidebarStatus = function() {
		$scope.$on('$viewContentLoaded', function() {  
			 var assets = document.getElementsByClassName("sidebar-status");
				for(var i=0; i<assets.length; i++){
					assets[i].parentElement.className = "";
					if(assets[i].href === String(window.location)) {
						assets[i].parentElement.className = "active";
					}
				}  
	        });
	}
});

mainPage.config(function($routeProvider) {
	$routeProvider.when('/maintenance', {
		templateUrl : 'employee/maintenance.html',
	}).when('/batchUpload', {
		templateUrl : 'employee/upload.html',

	}).when('/synchronize', {
		templateUrl : 'employee/synchronize.html',

	}).when('/vendorMaintain', {
		templateUrl : 'vendor/vendormaintenance.html',

	}).when('/vendorAdd', {
		templateUrl : 'vendor/addvendor.html',

	}).when('/settlement', {
		templateUrl : 'transaction/vendorreport.html',

	}).when('/analysis', {
		templateUrl : 'transaction/analysis.html',

	}).when('/consumeInquiry', {
		templateUrl : 'transaction/consumeinquiry.html',

	}).when('/syncdeparment', {
		templateUrl : 'department/syncdepartment.html',
	}).when('/reset', {
		templateUrl : 'login/profile/reset.html',
	}).when('/maintainCoupon', {
		templateUrl : 'coupon/maintaincoupon.html',
	}).when('/addCoupon', {
		templateUrl : 'coupon/addcoupon.html',
	}).otherwise({
		redirectTo : '/'
	});
}).run([ '$rootScope', function($rootScope) {
	$rootScope.$on('$routeChangeSuccess', function(scope, current, pre) {
	})
} ]);

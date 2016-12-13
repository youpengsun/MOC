mainPage
		.controller(
				'maintainCouponCtrl',
				function($scope, $rootScope, $http, $window, $alert,
						$routeParams, ngTableParams, ngDialog, CouponService) {

					var array = [];

					$scope.criteria = {};
					$scope.countTotal = {}
					$scope.begda = {};
					$scope.endda = {};
					$scope.$parent.tab.secondIcon = "fa fa-ticket";
					// --> Begin Date picker logic
					$scope.today = function() {
						$scope.criteria.begda = new Date();
					};

					$scope.begdaOpen = function($event) {
						$scope.begda.opened = true;
					};

					$scope.enddaOpen = function($event) {
						$scope.endda.opened = true;
					};

					$scope.dateOptions = {
						formatYear : 'yy',
						startingDay : 1
					};
					$scope.begda.opened = false;
					$scope.endda.opened = false;

					$scope.toggleMin = function() {
						$scope.minDate = new Date(1900, 0, 1);// $scope.minDate
					};
					$scope.toggleMin();

					$scope.$watch('criteria.begda', function(begda) {

						if (begda > $scope.criteria.endda) {
							$scope.criteria.endda = begda;
						}
					});

					CouponService.buildCriteria().then(function(result) {
						$scope.criteria = result.criteriaBuild;
					});

					/* *************** Search Function ***************** */

					$scope.search = function() {

						$scope.recordsNumber = "searching...";

						$http.post("/moc/coupon/inquiry", $scope.criteria)
								.success(function(response) {
									array = response.couponRecords;
									$scope.countTotal = response.countTotal;
									$scope.recordsNumber = array.length;
									$scope.tableParams.reload();
								});
					};

					$scope.tableParams = new ngTableParams({
						page : 1, // show first page
						count : 10, // count per page
					}, {
						total : 0, // length of data
						getData : function($defer, params) {
							CouponService.getData($defer, params,
									$scope.filter, array);
						}
					});

					$scope.$parent.tab.first = "Home";
					$scope.$parent.tab.second = "Coupon";
					$scope.$parent.tab.third = "Inquiry";

					
					$scope.$watch("filter.$", function() {
						$scope.tableParams.reload();
					});
				});

mainPage.service("CouponService", function($http, $q, $filter) {
	var flag;

	function filterData(data, filter) {
		return $filter('filter')(data, filter)
	}

	function orderData(data, params) {
		return params.sorting() ? $filter('orderBy')(data, params.orderBy())
				: filteredData;
	}

	function sliceData(data, params) {
		return data.slice((params.page() - 1) * params.count(), params.page()
				* params.count())
	}

	function transformData(data, filter, params) {
		return sliceData(orderData(filterData(data, filter), params), params);
	}

	var service = {
		cachedData : [],
		modified : 0,
		getData : function($defer, params, filter, array) {
			//console.log("fetching data");
			var allInfo = array;
			params.total(allInfo.length);
			var filteredData = $filter('filter')(allInfo, filter);
			var transformedData = transformData(allInfo, filter, params);
			$defer.resolve(transformedData);
		},

		buildCriteria : function() {
			var defer = $q.defer();
			$http.post("/moc/coupon/buildInquiryCriteria").success(
					function(response) {
						defer.resolve(response);
					});

			return defer.promise;
		},
	};
	return service;
});
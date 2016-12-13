mainPage.controller('addCouponCtrl', function($scope, $rootScope, $http,
		$uibModal, $log, $window, $alert, $routeParams, ngTableParams,
		ngDialog, AddCouponService) {
	
	var saveAlert;
	$scope.newCoupon = {};
	$scope.regDate = {};
	$scope.$parent.tab.secondIcon = "fa fa-ticket";
	// --> Begin Date picker logic
	$scope.today = function() {
		$scope.newCoupon.regDate = new Date();
	};

	$scope.initial = function() {
		$scope.errorInfo = "";
		$scope.newCoupon.type = "OW";
		$scope.newCoupon.count = 1;
		$scope.newCoupon.employeeID = "";
		$scope.today();
	};

	$scope.initial();

	$scope.regDateOpen = function($event) {
		$scope.regDate.opened = true;
	};

	/*
	 * $scope.setDate = function(year, month, day) { $scope.criteria.begda = new
	 * Date(year, month, day); };
	 */
	$scope.dateOptions = {
		formatYear : 'yy',
		startingDay : 1
	};
	$scope.regDate.opened = false;

	$scope.toggleMin = function() {
		$scope.minDate = new Date(1900, 0, 1);// $scope.minDate
		// ? null
		// : new Date();
	};
	$scope.toggleMin();

	/* *************** Form Validation ***************** */

	$scope.checkCountSelfUse = function() {
		if ($scope.newCoupon.type == 'OW' && $scope.newCoupon.count != 1) {
			return false;
		} else {
			return true;
		}
	}

	$scope.checkCountBoth = function() {
		if ($scope.newCoupon.type == 'BT' && $scope.newCoupon.count <= 1) {
			return false;
		} else {
			return true;
		}
	}

	$scope.validateForm = function() {
		return $scope.checkCountSelfUse() && $scope.checkCountBoth();
	}

	$scope.$parent.tab.first = "Home";
	$scope.$parent.tab.second = "Coupon";
	$scope.$parent.tab.third = "Add Coupon";
	/*
	 * >>>>>>>>>>>>>> Begin Add coupon>>>>>>>>>>>>>>
	 * 
	 */

	$scope.saveCoupon = function(saveFlag) {
		$scope.errorInfo = "";
		// var res = AddCouponService.checkForm($scope.newCoupon);
		// save Flag: 1 indicates: first time to submit save request
		//			  2 indicates: validation with Warnings, but admin still continue to save forcibly 
		AddCouponService.save($scope.newCoupon, saveFlag).then(function(result) {
			if (saveAlert) {
				saveAlert.hide();
			}
			
			if (result.status == "VALIDATE_FAILED") {
				
				var title = "Check the validation errors:";
				$scope.openMessageBox(result.status, title, result.messages);
			} else if (result.status === "VALIDATE_WARNING") {
				
				var title = "Validation with warnings, do you want to continue?";
				$scope.openMessageBox(result.status, title, result.messages);
				
			} else if (result.status == "SAVE_FAILED") {
				
				saveAlert = $alert({
					content : "Save coupon registration failed",
					container : "#alerts-container",
					type : 'danger',
					duration : 15,
					show : true,
					animation : "am-fade-and-slide-top"
				});
				
			} else if (result.status == "SAVE_SUCCESS"){
				saveAlert = $alert({
					content : "Save coupon registration successfully",
					container : "#alerts-container2",
					type : 'success',
					duration : 5,
					show : true,
					animation : "am-fade-and-slide-top"
				});
				
				$scope.newCoupon.employeeID = "";
				document.getElementById("employeeid").focus();
				
			}

		});
	};

	/*
	 * ***************** message box **************************
	 */

	$scope.openMessageBox = function(status, title, messages) {

		var modalInstance = $uibModal.open({
			animation : $scope.animationsEnabled,
			templateUrl : 'validate-info.html',
			controller : 'messageBoxCtrl',
			scope : $scope,
			resolve : {
				params : function() {
					return {
						"status" : status,
						"title" : title,
						"messages" : messages
					}
				}
			}
		});

		modalInstance.result.then(function(action) {
			
			if(action == "CONFIRM"){
				$scope.saveCoupon(2);				
			}
		}, function() {
			$log.info('Modal dismissed at: ' + new Date());
		});
	};

});

mainPage.controller('messageBoxCtrl', function($scope, $uibModalInstance,
		params) {

	$scope.status = params.status;
	$scope.title = params.title;
	$scope.messages = params.messages;

	$scope.ok = function() {
		$uibModalInstance.close("OK");
	};
	
	$scope.confirm = function() {
		$uibModalInstance.close("CONFIRM");
	};

	$scope.cancel = function() {
		$uibModalInstance.dismiss("CANCEL");
	};
});

mainPage.service("AddCouponService", function($http, $q, $filter) {
	var flag;

	/*function filterData(data, filter) {
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
	}*/

	var service = {
			
		checkForm : function(formObj) {
			var obj = {
				msg : "",
				status : true
			};
			if (formObj.employeeID.length != 7) {
				obj.msg = "Employee ID length error";
				obj.status = false;
			}

			return obj;
		},
		save : function(newCoupon, saveFlag) {
			var deferred = $q.defer();
			$http.post("/moc/coupon/save/"+saveFlag, newCoupon).success(
					function(response) {
						deferred.resolve(response);
					}).error(function(response) {
				deferred.resolve(response);
			});
			
			return deferred.promise;
		}
	};
	
	return service;
});
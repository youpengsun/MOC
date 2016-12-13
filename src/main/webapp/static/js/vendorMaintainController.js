mainPage.controller('vendorMaintainCtrl', ['$scope', '$http', '$alert', '$timeout', '$routeParams', 'ngTableParams', 'ngDialog', 'vendorMaintainService', 'vendorAddService', function($scope, $http, $alert, $timeout, $routeParams, ngTableParams, ngDialog, vendorMaintainService, vendorAddService) {
	var array = [];
	var lineData = array;
	var vendorMaintainAlert;

	$scope.tableParams = new ngTableParams({
		page: 1, // show first page
		count: 3, // count per page
		sorting: { id: "asc" } 
	}, {
		total: 0, // length of data
		counts: [],
		getData: function($defer, params) {
			vendorMaintainService.getData($defer, params, $scope.filter);
		}
	});

	$scope.lineTableParams = new ngTableParams({
		page: 1, // show first page
		count: 6, // count per page
	}, {
		total: 0, // length of data
		counts: [],
		getData: function($defer, params) {
			vendorMaintainService.getLineData($defer, params, $scope.filter, array);
		}
	});

	$scope.$parent.tab.first = "Home";
	$scope.$parent.tab.second = "Vendor";
	$scope.$parent.tab.secondIcon = "fa fa-tasks";
	$scope.$parent.tab.third = "Maintenance";
	$scope.vendorDetailBegda = {};
	$scope.vendorDetailEndda = {};
	
	$scope.vendorContractBegdaOpen = function($event) {
		$scope.vendorDetailBegda.opened = true;
	};

	$scope.newContractEnddaOpen = function($event) {
		$scope.vendorDetailEndda.opened = true;
	};

	$scope.vendorDetailPanel = {
		active: false
	};

	$scope.closeDetail = function() {
		$scope.vendorDetailPanel.active = false;
	};

	$scope.cancelEdit = function(vendorLine) {
		vendorLine.wechatID = $scope.original.wechatID;
		vendorLine.name = $scope.original.name;
	};

	$scope.saveEditVendorLine = function(vendorLine) {
		if(!vendorLine.id){
			vendorLine.id = $scope.newVendorLineId;
		}
		vendorMaintainService.saveEditVendorLine(vendorLine);
	};

	$scope.editVendorLine = function(vendorLine) {
		$scope.original = angular.copy(vendorLine);
	};

	$scope.vendorDetailBegdaOpen = function($event) {
		$scope.vendorDetailBegda.opened = true;
	};

	$scope.vendorDetailEnddaOpen = function($event) {
		$scope.vendorDetailEndda.opened = true;
	};

	$scope.vendorDetailPanel.dateFormat = 'yyyy.MM.dd';

	$scope.addVendorLineMainForm = {};
	$scope.maintainNewVendorLine = function() {
		$scope.addVendorLineMainForm = new Object();
		ngDialog.open({
			template: 'vendor/addvendorlineform.html',
			className: 'ngdialog-theme-default dialogwidth800',
			scope: $scope
		});
		vendorMaintainService.getLineNo($scope.vendorDetail.id).then(function(result) {
			var lineNO = 1;
			var tmpArray = [];
			for (var i = 0; i < result.vendorLines.length; i++) {
				tmpArray.push(result.vendorLines[i].lineNO);
			}
			tmpArray.sort();
			for (var i = 0; i < tmpArray.length; i++) {
				if (lineNO === tmpArray[i]) {
					lineNO++;
				}
			}
			$scope.addVendorLineMainForm.lineID = $scope.vendorDetail.id;
			//$scope.addVendorLineMainForm.lineNO = lineNO;
		});
	};
	
	$scope.$watch('vendorDetail.begda', function(begda) {
		var myDate=new Date($scope.vendorDetail.endda);
		if (begda > myDate) {
			$scope.vendorDetail.endda = begda;
		}
	});

	$scope.addNewVendorLineMaintain = function() {
		if (vendorMaintainAlert) {vendorMaintainAlert.hide();}
		var newVendorLineForm = {
			"name": $scope.addVendorLineMainForm.name,
			"wechatID": $scope.addVendorLineMainForm.wechatID,
			//"lineNO": $scope.addVendorLineMainForm.lineNO,
			"vendor": {
				"id": $scope.vendorDetail.id,
				"name": $scope.vendorDetail.name,
				"contactTelNO": $scope.vendorDetail.contactTelNO,
				"contactName": $scope.vendorDetail.contactName,
				"businessDistrict": {
					"id": $scope.vendorDetail.businessDistrict
				},
				"wechatID": $scope.vendorDetail.wechatID,
				"contactEmail": $scope.vendorDetail.contactEmail,
				"dianpingID": $scope.vendorDetail.dianpingID,
				"address": $scope.vendorDetail.address,
				"promotion": $scope.vendorDetail.promotion
			}
		};
		vendorMaintainService.saveNewVendorLineMaintain(newVendorLineForm).then(function(result) {
			if (result.status) {
				vendorMaintainAlert = $alert({
					content: "Save new vendor line successfully.",
					container: "#alerts-container2",
					type: 'success',
					duration: 3,
					show: true,
					animation: "am-fade-and-slide-top"
				});
				ngDialog.closeAll();
				newVendorLineForm.openID = newVendorLineForm.vendor.id + "-" + result.line;
				newVendorLineForm.lineNO = result.line;
				array.push(newVendorLineForm);
				$scope.lineTableParams.reload();
				$scope.newVendorLineId = result.id;
			}else{
				vendorMaintainAlert = $alert({
					content: "Save new vendor line failed",
					container: "#alerts-container2",
					type: 'danger',
					duration: 8,
					show: true,
					animation: "am-fade-and-slide-top"
				});
			}
		});
	};

	$scope.switchToUploadVendorLogo = function() {
		var btnClick = document.getElementById("maintainLogo");
		btnClick.click();
	};

	$scope.generateVendorLogo = function(files) {
		if (vendorMaintainAlert) {vendorMaintainAlert.hide();}
		var fd = new FormData();
		var rand = Math.floor(Date.now() / 1000);
		fd.append("logo", files[0]);
		if (files[0] && files[0].size > 200000) {
			var fileLargeAlert = $alert({
				content: "File larger than 200kb",
				container: "#alerts-container2",
				type: 'danger',
				duration: 5,
				show: true,
				animation: "am-fade-and-slide-top"
			});
		} else {
			fd.append("id", $scope.vendorDetail.id);
			$http.post("adminctrl/vendor/logo", fd, {
				withCredentials: true,
				//responseType: "arraybuffer",
				headers: {
					'Content-Type': undefined
				},
				transformRequest: angular.identity
			}).
			success(function(response) {
				
				$timeout(function() {
					//$scope.vendorDetail.logo = "../moc/static/vendorlogo/" + $scope.vendorDetail.id + ".png?" + new Date().getTime();
					$scope.vendorDetail.logo = "../vendorlogo/" + $scope.vendorDetail.id + ".png?" + new Date().getTime();
				}, 3000);
			}).
			error(function(response){
				vendorMaintainAlert = $alert({
					content: "Upload vendor logo failed.",
					container: "#alerts-container",
					type: 'danger',
					duration: 8,
					show: true,
					animation: "am-fade-and-slide-top"
				});
			});
		}
	};

	$scope.vendorDetail = {};
	vendorMaintainService.buildDistrict().then(function(result) {
		$scope.vendorDetail.businessDistrictID = result.districts;
	});

	$scope.showVendorDetail = function(vendor) {
		$scope.vendorDetailPanel.active = true;
		$scope.vendorDetail.id = vendor.id;
		$scope.vendorDetail.name = vendor.name;
		$scope.vendorDetail.contactTelNO = vendor.contactTelNO;
		$scope.vendorDetail.contactName = vendor.contactName;
		$scope.vendorDetail.businessDistrict = vendor.businessDistrict.id;
		$scope.vendorDetail.wechatID = vendor.wechatID;
		$scope.vendorDetail.promotion = vendor.promotion;
		$scope.vendorDetail.contactEmail = vendor.contactEmail;
		$scope.vendorDetail.dianpingID = vendor.dianpingID;
		$scope.vendorDetail.address = vendor.address;
		$scope.vendorDetail.vendorType = vendor.vendorType;
		$scope.vendorDetail.reportPeriod = vendor.reportPeriod;
		$scope.vendorDetail.logo = "../moc/static/image/oops.png";
		//$scope.vendorDetail.logo = "../moc/static/vendorlogo/" + vendor.id + ".png?" + new Date().getTime();
		$scope.vendorDetail.logo = "../vendorlogo/" + vendor.id + ".png?" + new Date().getTime();
		var id = vendor.id;
		$http.post("adminctrl/vendorLine/" + id + "/list").success(function(response) {
			for (var i = 0; i < response.vendorLines.length; i++) {
				response.vendorLines[i].openID = response.vendorLines[i].vendor.id + "-" + response.vendorLines[i].lineNO;
			}
			array = response.vendorLines;
			$scope.lineTableParams.reload();
		}).error(function(response) {})
		vendorMaintainService.getContractDetail(id).then(function(res) {
			if (res.contract) {
				$scope.vendorDetail.begda = res.contract.beginDate;
				$scope.vendorDetail.endda = res.contract.endDate;
				$scope.vendorDetail.contractComment = res.contract.comment;
				$scope.vendorDetail.contractId = res.contract.id;
				$scope.vendorDetail.contractNo = res.contract.contract_no;
				$scope.vendorDetail.contractStatus = res.contract.status;
			} else {
				$scope.vendorDetail.begda = "";
				$scope.vendorDetail.endda = "";
				$scope.vendorDetail.contractComment = "";
				$scope.vendorDetail.contractId = "";
				$scope.vendorDetail.contractNo = "";
				$scope.vendorDetail.contractStatus = "";
			}
		});
	};

	$scope.saveEditVendor = function() {
		if (vendorMaintainAlert) {
			vendorMaintainAlert.hide();
		}
		var editVendorDetail = {
				"id": $scope.vendorDetail.id,
				"name": $scope.vendorDetail.name,
				"contactTelNO": $scope.vendorDetail.contactTelNO,
				"contactName": $scope.vendorDetail.contactName,
				"contactEmail": $scope.vendorDetail.contactEmail,
				"businessDistrictId": $scope.vendorDetail.businessDistrict,
				"wechatID": $scope.vendorDetail.wechatID,
				"contactEmail": $scope.vendorDetail.contactEmail,
				"dianpingID": $scope.vendorDetail.dianpingID,
				"promotion": $scope.vendorDetail.promotion,
				"address": $scope.vendorDetail.address,
				"reportPeriod": $scope.vendorDetail.reportPeriod,
				"vendorType": $scope.vendorDetail.vendorType,
				"beginDate": $scope.vendorDetail.begda,
				"comment": $scope.vendorDetail.contractComment,
				"contract_no": $scope.vendorDetail.contractNo,
				"endDate": $scope.vendorDetail.endda,
				"status": $scope.vendorDetail.contractStatus,
				"contractId": $scope.vendorDetail.contractId,
				
		};

		vendorMaintainService.saveVendorEdit(editVendorDetail).then(function(response) {
			if (response.result === "true") {
				vendorMaintainAlert = $alert({
					content: "Edit vendor and contract successfully",
					container: "#alerts-container",
					type: 'success',
					duration: 5,
					show: true,
					animation: "am-fade-and-slide-top"
				});
			} else {
				vendorMaintainAlert = $alert({
					content: "Edit failed. " + response.error,
					container: "#alerts-container",
					type: 'danger',
					duration: 8,
					show: true,
					animation: "am-fade-and-slide-top"
				});
			}
			$scope.tableParams.reload();
		});
		
	};

	$scope.updateConfirm = function() {
		ngDialog.open({
			template: 'updateconfirm.html',
			scope: $scope
		});
	};

	$scope.switchToUploadLogo = function(vendorLine) {
		$scope.openID = vendorLine.openID;
		var btnClick = document.getElementById("uploadLogo");
		document.getElementById("uploadLogo").value = "";
		btnClick.click();
	};

	$scope.generateQRcode = function(files) {
		var fd = new FormData();
		$scope.user = {};
		fd.append("image", files[0]);
		if (files[0] && files[0].size > 2000000) {
			var fileLargeAlert = $alert({
				content: "File larger than 2MB",
				container: "#alerts-container2",
				type: 'danger',
				duration: 5,
				show: true,
				animation: "am-fade-and-slide-top"
			});
		} else {
			fd.append("code", $scope.openID);
			$http.post("adminctrl/vendorline/qrcode", fd, {
				withCredentials: true,
				responseType: "arraybuffer",
				headers: {
					'Content-Type': undefined
				},
				transformRequest: angular.identity
			}).
			success(function(response) {
				ngDialog.open({
					template: 'vendor/QRcode.html',
					className: 'ngdialog-theme-default',
					scope: $scope
				});
				var uInt8Array = new Uint8Array(response);
				var i = uInt8Array.length;
				var binaryString = new Array(i);
				while (i--) {
					binaryString[i] = String.fromCharCode(uInt8Array[i]);
				}
				var data = binaryString.join('');
				$scope.user.data = window.btoa(data);
				$scope.user.qrtitle = $scope.openID;
			});
		}
	};

	$scope.$watch("filter.$", function() {
		$scope.tableParams.reload();
	});

}]);

mainPage.service("vendorMaintainService", ['$http', '$q', '$filter', '$alert', function($http, $q, $filter, $alert) {
	var flag;
	var newAdd;
	var saveLineEditAlert;

	function filterData(data, filter) {
		return $filter('filter')(data, filter)
	}

	function orderData(data, params) {
		return params.sorting() ? $filter('orderBy')(data, params.orderBy()) : filteredData;
	}

	function sliceData(data, params) {
		return data.slice((params.page() - 1) * params.count(), params.page() * params.count())
	}

	function transformData(data, filter, params) {
		return sliceData(orderData(filterData(data, filter), params), params);
	}

	function saveEditVendorLineData(vendorLine) {
		
		if(saveLineEditAlert){
			saveLineEditAlert.hide();
		}
		var a = JSON.stringify(vendorLine);
		$http.post("adminctrl/vendorLine/edit", a)
			.success(function(response) {
				
				saveLineEditAlert = $alert({
					content: "Edit vendorline successfully",
					container: "#alerts-container2",
					type: 'success',
					duration: 5,
					show: true,
					animation: "am-fade-and-slide-top"
				});
			}).error(function(response) {
				
				saveLineEditAlert = $alert({
					content: "Edit vendorline failed",
					container: "#alerts-container",
					type: 'danger',
					duration: 8,
					show: true,
					animation: "am-fade-and-slide-top"
				});
			});
	}

	var service = {
		cachedData: [],
		getData: function($defer, params, filter) {
			if (flag === 0 && service.cachedData.length > 0 && newAdd === 0) {
				var filteredData = filterData(service.cachedData, filter);
				var transformedData = sliceData(orderData(filteredData, params), params);
				params.total(filteredData.length);
				$defer.resolve(transformedData);
				
			} else {
				$http({
					url: "/moc/adminctrl/vendor/list",
					method: 'GET',
				}).success(function(resp) {
					var allInfo = resp.vendors;
					angular.copy(allInfo, service.cachedData);
					params.total(allInfo.length);
					var filteredData = $filter('filter')(allInfo, filter);
					var transformedData = transformData(allInfo, filter, params);
					$defer.resolve(transformedData);
					
					flag = 0;
					newAdd = 0;
				});
			}
		},
		
		saveVendorEdit: function(vendor) {
			var a = JSON.stringify(vendor);
			var deferred = $q.defer();
			$http.post("adminctrl/vendor/edit", a)
				.success(function(response) {
					deferred.resolve(response);
				}).error(function(response) {
					deferred.resolve(response);
				});
			flag = 1;
			return deferred.promise;
		},

		saveEditVendorLine: function(vendorLine) {
			saveEditVendorLineData(vendorLine);
			flag = 1;
		},

		getLineData: function($defer, params, filter, array) {
			params.total(array.length);
			var filteredData = $filter('filter')(array, filter);
			var transformedData = transformData(array, filter, params);
			$defer.resolve(transformedData);
			flag = 0;
		},

		getLineNo: function(id) {
			var deferred = $q.defer();
			$http.post("adminctrl/vendorLine/" + id + "/list")
				.success(function(response) {
					deferred.resolve(response);
				}).error(function(response) {
					deferred.resolve(response);
				})
			return deferred.promise;
		},

		setNewAdd: function(num) {
			newAdd = num;
		},

		saveNewVendorLineMaintain: function(data) {
			var deferred = $q.defer();
			$http.post("adminctrl/vendorLine/save", data)
				.success(function(response) {
					deferred.resolve(response);
				}).error(function(response) {

				});
			flag = 1;
			return deferred.promise;
		},

		getContractDetail: function(id) {
			var deferred = $q.defer();
			$http.post("adminctrl/contract/" + id).success(function(response) {
				deferred.resolve(response);
			}).error(function(response) {
				deferred.resolve(response);
			});
			return deferred.promise;
		},

		buildDistrict: function() {
			var defer = $q.defer();
			$http.get("/moc/adminctrl/district/list").success(
				function(response) {
					defer.resolve(response);
				});
			return defer.promise;
		}
	};
	return service;
}]);
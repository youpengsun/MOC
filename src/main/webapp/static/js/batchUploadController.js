mainPage.controller('batchController', function($scope, $http, $alert, ngTableParams, ngDialog, BatchService) {
	var array = [];
	var data = array;
	var batchAlert;
	$scope.tableParams = new ngTableParams({
		page: 1, // show first page
		count: 50, // count per page
	}, {
		total: 0, // length of data
		counts: [50,100,150,200],
		getData: function($defer, params) {
			BatchService.getData($defer, params, $scope.filter, array);
		}
	});

	$scope.$parent.tab.first = "Home";
	$scope.$parent.tab.second = "Employee";
	$scope.$parent.tab.secondIcon = "fa fa-user";
	$scope.$parent.tab.third = "BatchUpload";
	$scope.upload = {};
	$scope.upload.finish = false;

	$scope.saveUploadData = function() {
		var excelSaveArray = [];
		if (batchAlert) {
			batchAlert.hide();
		}
		for (var i = 0; i < array.length; i++) {
			array[i].employee.status = "01";
			excelSaveArray.push(array[i].employee);
		}
		BatchService.setDepartInfo(array);
		$http.post("/moc/adminctrl/emp/excelsave", excelSaveArray).success(
			function(response) {
				if (response.status === true) {
					batchAlert = $alert({
						content: "Upload successfully",
						container: "#alerts-container2",
						type: 'success',
						duration: 5,
						show: true,
						animation: "am-fade-and-slide-top"
					});
					BatchService.setNewUpdated(1);
					array = [];
					$scope.tableParams.reload();
				} else {
					batchAlert = $alert({
						content: "Upload fail",
						container: "#alerts-container",
						type: 'danger',
						show: true,
						animation: "am-fade-and-slide-top"
					});
				}
			}).error(function(response) {
				$scope.uploadStatus = "Upload fail";
		});
	};

	$scope.uploadFile = function(files) {
		var fileText = document.getElementById("upLoadFilePath");
		var btnClick = document.getElementById("eeexcel");
		fileText.value = btnClick.value;
		var fd = new FormData();
		if (files.length > 0) {
			fd.append("eeexcel", files[0]);
			if (batchAlert) {
				batchAlert.hide();
			}
			$http.post("/moc/adminctrl/emp/excelupload", fd, {
				withCredentials: true,
				headers: {
					'Content-Type': undefined
				},
				transformRequest: angular.identity
			}).success(
				function(response) {
					array = response.uploadEE;
					var itemUpdate = 0;
					var itemError = 0;
					var itemInsert = 0;

					for (var i = 0; i < array.length; i++) {
						if (array[i].status === "I") {
							itemInsert++;
						}
						if (array[i].status === "U") {
							itemUpdate++;
						}
						if (array[i].status === "E") {
							itemError++;
						}
					}
					if (itemError === 0) {
						$scope.upload.finish = true;
					}
					if (itemError === 0 && itemUpdate === 0 && itemInsert === 0) {
						$scope.upload.finish = false;
						batchAlert = $alert({
							content: "Insert: " + itemInsert + " " + "Update: " + itemUpdate + " " + "Error: " + itemError + " " + response.message,
							container: "#alerts-container",
							type: 'danger',
							show: true,
							animation: "am-fade-and-slide-top"
						});
						array = [];
						$scope.tableParams.reload();
					} else {
						batchAlert = $alert({
							content: "Insert: " + itemInsert + " " + "Update: " + itemUpdate + " " + "Error: " + itemError + " " + response.message,
							container: "#alerts-container",
							type: 'danger',
							show: true,
							animation: "am-fade-and-slide-top"
						});
						$scope.tableParams.reload();
					}
				}).error(function(response) {});
		}
	};

	$scope.switchToUpload = function() {
		var btnClick = document.getElementById("eeexcel");
		document.getElementById("eeexcel").value = "";
		btnClick.click();
	};

	$scope.updateConfirm = function() {
		ngDialog.open({
			template: 'updateconfirm.html',
			scope: $scope
		});
	};

	$scope.$watch("filter.$", function() {
		$scope.tableParams.reload();
	});
});

mainPage.service("BatchService", function($http, $filter) {
	var newBatchUpdate;

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

	var service = {
	    batchUpdated: 0,
		cachedData: [],
		departArray: [],
		getData: function($defer, params, filter, array) {
			if (service.cachedData.length > 0) {
				var filteredData = filterData(service.cachedData, filter);
				var transformedData = sliceData(
					orderData(filteredData, params), params);
				params.total(filteredData.length)
				$defer.resolve(transformedData);
			} else {
				params.total(array.length);
				var filteredData = $filter('filter')(array, filter);
				var transformedData = transformData(array, filter, params);
				$defer.resolve(transformedData);
			}
		},

		getDepartInfo: function() {
			return departArray;
		},

		setDepartInfo: function(data) {
			departArray = data;
		},
		
		setNewUpdated: function(value) {
			service.batchUpdated = value;
		}
	};
	return service;
});
mainPage.controller('SyncController', function($scope, $http, $alert, ngTableParams, ngDialog, syncService) {
	var wechatArray = [];
	var data = wechatArray;
	var syncAlert;
	$scope.recordsNumber = 0;
	$scope.totalNumber = 0;
	
	$scope.filterItems  = {
			'TOADD':true,
			'TOUPDATE':true,
			'TOACTIVATE':false,
			'TODEACTIVATE':false,
			'TODELETE':false,
			'SYNCED':false,
	};
	 $scope.items = [
	                  {model:0,name:'TOADD',text:'To Add',icon:'glyphicon glyphicon-plus-sign add-icon'}, 
	                  {model:0,name:'TOUPDATE',text:'To Update',icon:'glyphicon glyphicon-question-sign update-icon'},
	                  {model:0,name:'TOACTIVATE',text:'To Activate',icon:'glyphicon glyphicon-ok-circle'},
	                  {model:0,name:'TODEACTIVATE',text:'To Deactivate',icon:'glyphicon glyphicon-ban-circle deactivate-icon'},
	                  {model:0,name:'TODELETE',text:'To Delete',icon:'glyphicon glyphicon-trash'},
	                  {model:0,name:'SYNCED',text:'Synced',icon:'glyphicon glyphicon-check sync-icon'}
	                ];	
	$scope.tableParams = new ngTableParams({
		page: 1, // show first page
		count: 25, // count per page
//		filter: $scope.Filter.syncStatus,
		//sorting: {name:'asc'}
	}, {
		total: 0, // length of data
		counts: [25, 50, 100, 200, 9999],
		getData: function($defer, params) {
			syncService.getData($defer, params, $scope.filter, wechatArray);
		}
	});

	$scope.$parent.tab.first = "Home";
	$scope.$parent.tab.second = "Synchronize Wechat";
	$scope.$parent.tab.secondIcon = "fa fa-refresh";
	$scope.$parent.tab.third = "Employee";
	$scope.syncBtn = false;

	syncService.buildDepartment().then(function(result) {
		$scope.synSearch.costcenterName = result.criteriaBuild.costCenter;
	});

	$scope.searchAll = function() {
		if(syncAlert){syncAlert.hide();}
		syncService.mutualSync().then(function(res){
			if(res.results){
				wechatArray = res.results;
			} else if(res.message){
				wechatArray = [];
				syncAlert = $alert({
					content: res.message,
					container: "#alerts-container",
					type: 'danger',
					placement: "top",
					duration: 8,
					show: true,
					animation: "am-fade-and-slide-top"
				});
			} else {
				wechatArray = [];
				syncAlert = $alert({
					content: "Get data from wechat failed",
					container: "#alerts-container",
					type: 'danger',
					placement: "top",
					duration: 8,
					show: true,
					animation: "am-fade-and-slide-top"
				});
			}
			$scope.updateNumber();
			$scope.tableParams.reload();
		})
	};

	$scope.selectAll = function() {
		angular.forEach($scope.tableParams.data, function(wechat) {
			if($scope.filterItems[wechat.syncStatus]){
				if(!wechat.selected) {
				wechat.selected = true;
				$scope.recordsNumber = $scope.recordsNumber + 1;}
			}
		});
		$scope.syncBtn = $scope.recordsNumber > 0 ? true : false;
	};

	$scope.deselectAll = function() {
		angular.forEach($scope.tableParams.data, function(wechat) {
			if($scope.filterItems[wechat.syncStatus]){
				if(wechat.selected) {
				wechat.selected = false;
				$scope.recordsNumber = $scope.recordsNumber - 1;}
			}
		});
		$scope.syncBtn = $scope.recordsNumber > 0 ? true : false;
	};

	$scope.checkSyncEE = function(wechat) {
		if (!!wechat.selected) {
			$scope.recordsNumber = $scope.recordsNumber + 1;
		} else {
			$scope.recordsNumber = $scope.recordsNumber -1 ;
		}
		$scope.syncBtn = $scope.recordsNumber > 0 ? true : false;
								
	};

	$scope.synSearch = {};
	$scope.search = function() {
		if(syncAlert){syncAlert.hide();}
		var searchObj = {
			"id": $scope.synSearch.id,
			"lastName": $scope.synSearch.lastName,
			"firstName": $scope.synSearch.firstName,
			"telNo": $scope.synSearch.mobile,
			"personalArea": $scope.synSearch.personalArea,
			"personalSubarea": $scope.synSearch.subArea,
			"department": {
				"id": $scope.synSearch.costcenter,
				"name": $scope.synSearch.personalArea
			}
		};
		var a = JSON.stringify(searchObj);
		$http.post("../moc/adminctrl/sync/query", a)
			.success(function(response) {
				wechatArray = response.results;
				if (response.results.length === 0) {
					var searchAlert = $alert({
						content: "Cannot find such item",
						container: "#alerts-container",
						type: 'danger',
						placement: "top",
						duration: 5,
						show: true,
						animation: "am-fade-and-slide-top"
					});
				}
				$scope.tableParams.reload();
				$scope.updateNumber();
			}).error(function(response) {
				var searchErrorAlert = $alert({
					content: "Search Error",
					container: "#alerts-container",
					type: 'danger',
					duration: 5,
					show: true,
					animation: "am-fade-and-slide-top"
				});
				$scope.resetNumber();
			});
		
	};

	$scope.update = function() {
		$scope.syncUpdateArray = [];
		if(syncAlert){syncAlert.hide();}
		angular.forEach(wechatArray, function(wechat) {
			if (!!wechat.selected) {
				$scope.syncUpdateArray.push(wechat);
			}
		});
		syncService.wechatUpdate($scope.syncUpdateArray).then(function(response) {
			var j = 0;
			var itemsUpdateSuccess = 0;
			var itemsUpdateFail = 0;
			$scope.resetNumber();
			/*below the logic for sync employee*/
			for (var i = 0; i < wechatArray.length; i++) {
				/*if the employee is going to add or update, both EE info and response body will pass the first if judgement */				
				if (wechatArray[i].employee) {
					var sycnresult = $.grep(response.results, function(e){ if(e.employee)  return e.employee.id === wechatArray[i].employee.id; });
					if (sycnresult.length != 0 ) {
						if (!wechatArray[i].user) {
							wechatArray[i].user = {};
						}
						wechatArray[i].syncStatus = sycnresult[0].syncStatus;
						wechatArray[i].comments = sycnresult[0].comments;
						if(sycnresult[0].user){
							wechatArray[i].user.name = sycnresult[0].user.name;
							wechatArray[i].user.mobile = sycnresult[0].user.mobile;
							wechatArray[i].user.email = sycnresult[0].user.email;
							wechatArray[i].user.department = sycnresult[0].user.department;
						}
						j++
					}
				}
				/*if the employee is going to be deleted, both DB and response object does not contain his EE info */
				else if (wechatArray[i].user) {
					var syncresult2 = $.grep(response.results, function(e){ if(e.user) { return e.user.userid == wechatArray[i].user.userid; }});
					if(syncresult2.length != 0 ) {
						wechatArray[i].syncStatus = syncresult2[0].syncStatus;
						wechatArray[i].comments =  syncresult2[0].comments;
						if( syncresult2[0].user){
						wechatArray[i].user.name = syncresult2[0].user.name;
						wechatArray[i].user.mobile = syncresult2[0].user.mobile;
						wechatArray[i].user.email = syncresult2[0].user.email;
						wechatArray[i].user.department = syncresult2[0].user.department;
						}
						j++
					}
				}
				
				$.grep($scope.items, function(e){ if(e.name == wechatArray[i].syncStatus )  e.model = e.model + 1 });	
				var isFilter = $scope.filterItems[wechatArray[i].syncStatus];
				if (!isFilter && !!wechatArray[i].selected) {
					wechatArray[i].selected = false;
					$scope.recordsNumber = $scope.recordsNumber - 1;
				}
			}

			for (var a = 0; a < response.results.length; a++) {
				if (response.results[a].updateStatus === true) {
					itemsUpdateSuccess++;
				} else {
					itemsUpdateFail++;
				}
			}
			syncAlert = $alert({
				content: "Update success: " + itemsUpdateSuccess + " Update fail: " + itemsUpdateFail,
				container: "#alerts-container",
				type: 'danger',
				placement: "top",
				duration: 8,
				show: true,
				animation: "am-fade-and-slide-top"
			});
			$scope.totalNumber = wechatArray.length;
			$scope.tableParams.reload();
		});
	};

	$scope.$watch("Filter.$", function() {
		$scope.tableParams.reload();
	});
	
	$scope.filterSyncStatus = function ($data) {
		var isFilter = $scope.filterItems[$data.syncStatus];
		if (!isFilter && !!$data.selected) {
			$data.selected = false;
			$scope.recordsNumber = $scope.recordsNumber - 1;
		}
		return $scope.filterItems[$data.syncStatus];
		
	}
	
	$scope.resetNumber = function() {
		for (x=0;x<6;x++) {
			$scope.items[x].model = 0;
		}
		$scope.totalNumber = 0;
	}
	$scope.updateNumber = function() {
		$scope.resetNumber();
		for (var i = 0; i < wechatArray.length; i++) {
			$.grep($scope.items, function(e){ if(e.name == wechatArray[i].syncStatus )  e.model = e.model + 1 });
		}
		$scope.totalNumber = wechatArray.length;
		$scope.recordsNumber = 0;
	}	

});

mainPage.service("syncService", function($http, $filter, $q) {

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
		cachedData: [],
		getData: function($defer, params, filter, wechatArray) {
			if (service.cachedData.length > 0) {
				var filteredData = filterData(service.cachedData, filter);
				var transformedData = sliceData(orderData(filteredData, params), params);
				params.total(filteredData.length)
				$defer.resolve(transformedData);
			} else {
				params.total(wechatArray.length);
				var filteredData = $filter('filter')(wechatArray, filter);
				var transformedData = transformData(wechatArray, filter, params);
				$defer.resolve(transformedData);
			}
		},
		
		mutualSync: function() {
			var deferred = $q.defer();
			$http.get("adminctrl/sync/searchall")
			.success(function(response) {
				deferred.resolve(response);
			}).error(function(response){
				deferred.resolve(response);
			});
			return deferred.promise;
		},
		
		wechatUpdate: function(updateArray) {
			var deferred = $q.defer();
			var JSONArray = angular.toJson(updateArray)
			$http.post("/moc/adminctrl/sync/update", JSONArray)
				.success(function(response) {
					deferred.resolve(response);

				}).error(function(response) {
					deferred.resolve(response);
				});
			return deferred.promise;
		},
		
		buildDepartment: function() {
			var defer = $q.defer();
			$http.post("/moc/adminctrl/consume/inquiry/criteriabuild").success(
				function(response) {
					defer.resolve(response);
				});
			return defer.promise;
		}
	};
	return service;
});
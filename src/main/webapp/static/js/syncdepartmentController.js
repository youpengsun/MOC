mainPage.controller('syncDepartmentCtrl', function($scope, $http, $alert, $routeParams, ngTableParams, ngDialog, syncDepartService, BatchService) {
  var syncDepartArray = [];
  var syncDeAlert;

  $scope.$parent.tab.first = "Home";
  $scope.$parent.tab.second = "Synchronize Wechat";
  $scope.$parent.tab.secondIcon = "fa fa-refresh";
  $scope.$parent.tab.third = "Department";
  $scope.syncDepartBtn = false;

  $scope.syncdepartTableParams = new ngTableParams({
    page: 1, // show first page
    count: 50, // count per page
    sorting: { 
    	"department.id": "asc" 
    		} 
  }, {
    total: 0, // length of data
    counts: [50,100],
    getData: function($defer, params) {
      syncDepartService.getData($defer, params, $scope.filter, syncDepartArray);
    }
  });

  $scope.checkDepartment = function() {
	  syncDepartArray = [];
	  syncDepartService.search().then(function(res){
		  if(res.results){
			  for (var i = 0; i < res.results.length; i++) {
			      syncDepartArray.push(res.results[i]);
			    }
		  }
		  $scope.syncdepartTableParams.reload();
	  });
  };
  
  $scope.deSelectAll = function() {
	  angular.forEach($scope.syncdepartTableParams.data, function(wedepart) {
		  wedepart.selected = false;
		  $scope.syncDepartBtn = false;
      });
  };
  
  $scope.selectAllToAdd = function() {
	  var tmpDeArray = [];
	  angular.forEach($scope.syncdepartTableParams.data, function(wedepart) {
		  wedepart.selected = false;
          if (wedepart.syncStatus === "TOADD") {
        	  wedepart.selected = true;
        	  tmpDeArray.push(wedepart);
          }
          $scope.syncDepartBtn = tmpDeArray.length > 0 ? true : false;
      });
  };
  
  $scope.checkSyncDepart = function() {
		var tmpDeArray = [];
		angular.forEach($scope.syncdepartTableParams.data, function(wedepart) {
			if (!!wedepart.selected) {
				tmpDeArray.push(wedepart);
			}
		});
		$scope.syncDepartBtn = tmpDeArray.length > 0 ? true : false;
	};
  
  $scope.syncDepartment = function() {
	  var toUpdateDepartArray = [];
	  var j = 0;
      angular.forEach($scope.syncdepartTableParams.data, function(wedepart) {
          if (!!wedepart.selected) {
        	  toUpdateDepartArray.push(wedepart);
          }
          
      });  
      syncDepartService.update(toUpdateDepartArray).then(function(res) {
    		if (res.results) {
    			for (var i = 0; i < syncDepartArray.length; i++) {
    				if (syncDepartArray[i].department) {
    					//if department is going to be add or updated
    					if (syncDepartArray[i].department.id) {
    						var syncresult = $.grep(res.results, function(e){ if (e.department) {return e.department.id === syncDepartArray[i].department.id; }});
    						if (syncresult.length != 0) {
								syncDepartArray[i].syncStatus = syncresult[0].syncStatus;
								syncDepartArray[i].comments = syncresult[0].comments;
								if (syncresult[0].wechatDepartment) {
									syncDepartArray[i].wechatDepartment = {
										"name": syncresult[0].wechatDepartment.name,
										"id": syncresult[0].wechatDepartment.id
									}
								}
								j++; 							
    						} 
//    						if (res.results[j].wechatDepartment) { //update or add success
//    							if (syncDepartArray[i].department.id === res.results[j].wechatDepartment.id.toString()) {
//    								syncDepartArray[i].syncStatus = res.results[j].syncStatus;
//    								syncDepartArray[i].comments = res.results[j].comments;
//    								if (!syncDepartArray[i].wechatDepartment) {
//    									syncDepartArray[i].wechatDepartment = {
//    										"name": res.results[j].wechatDepartment.name,
//    										"id": res.results[j].wechatDepartment.id
//    									}
//    								}
//    								j++;
//    							}
//    						} else {
    							//syncDepartArray[i].comments = res.results[j].comments;
    							//j++;    	

    					}
    				} else if (syncDepartArray[i].wechatDepartment) {
    					//depart is going to be delete
   						var syncresult2 = $.grep(res.results, function(e){ if (e.wechatDepartment) {return e.wechatDepartment.id === syncDepartArray[i].wechatDepartment.id; }});
						if (syncresult2.length != 0) {
							syncDepartArray[i].syncStatus = syncresult2[0].syncStatus;
							syncDepartArray[i].comments = syncresult2[0].comments;
							syncDepartArray[i].wechatDepartment = {
								"name": syncresult2[0].wechatDepartment.name,
								"id": syncresult2[0].wechatDepartment.id
							}							
							j++; 							
						} 
    				}
					if (j >= res.results.length)
						break;
    			}
    		}
    	})
  };

//  $scope.removeDuplicate = function() {
//    var resFromBatch = BatchService.getDepartInfo();
//    for (var i = 0; i < resFromBatch.length; i++) {
//      syncDepartArray.push(resFromBatch[i].employee.department);
//    }
//    syncDepartArray.sort(function(a, b) {
//      return a.id - b.id;
//    });
//    for (var i = 0; i < syncDepartArray.length - 1; i++) {
//      if (syncDepartArray[i].id === syncDepartArray[i + 1].id) {
//        delete syncDepartArray[i];
//      }
//    }
//    syncDepartArray = syncDepartArray.filter(function(el) {
//      return (typeof el !== "undefined");
//    });
//    return syncDepartArray;
//  };
});

mainPage.service("syncDepartService", function($http, $q, $filter, $alert) {
  var flag;

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
    getData: function($defer, params, filter, array) {
    	params.total(array.length);
        var filteredData = $filter('filter')(array, filter);
        var transformedData = transformData(array, filter, params);
        $defer.resolve(transformedData);
        flag = 0;
    },
    
    search: function() {
    	var deferred = $q.defer();
        $http.get("adminctrl/wechatdepartment/searchall").success(function(response) {
        	deferred.resolve(response);
        }).error(function(response) {
        	deferred.resolve(response);
        });
        return deferred.promise;
    },
    
    update: function(array) {
    	var deferred = $q.defer();
        $http.post("adminctrl/wechatdepartment/update",array).success(function(response) {
        	deferred.resolve(response);
        }).error(function(response) {
        	deferred.resolve(response);
        	console.log("fail");
        });
        return deferred.promise;
    }
  };
  return service;
});
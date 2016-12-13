mainPage.controller('maintenanceCtrl', function($scope, $rootScope, $http, $window, $alert, $routeParams, ngTableParams, ngDialog, EmployeeService) {
	var data = EmployeeService.data;
	var saveAlert;

	$scope.tableParams = new ngTableParams({
		page: 1, // show first page
		count: 10, // count per page
	}, {
		total: 0, // length of data
		getData: function($defer, params) {
			EmployeeService.getData($defer, params, $scope.filter);
		}
	});

	$scope.$parent.tab.first = "Home";
	$scope.$parent.tab.second = "Employee";
	$scope.$parent.tab.secondIcon = "fa fa-user";
	$scope.$parent.tab.third = "Maintenance";
	$scope.employee = {};
	$scope.employee.deactive = false;
	$scope.employee.active = false;

	EmployeeService.buildDepartment().then(function(result) {
		$scope.option = result.criteriaBuild.costCenter;
	});

	$scope.checkAction = function() {
		var tmpArray = [];
		var flag1 = 0;
		var flag2 = 0;
		angular.forEach($scope.tableParams.data, function(user) {
			if (!!user.selected) {
				tmpArray.push(user.status);
			}
		});
		for (var i = 0; i < tmpArray.length; i++) {
			if (tmpArray[i] === "01") {
				flag1++;
			}
			if (tmpArray[i] === "00") {
				flag2++;
			}
		}

		if (tmpArray.length === 0) {
			$scope.employee.deactive = false;
			$scope.employee.active = false;
		} else {
			if (flag1 === tmpArray.length) {
				$scope.employee.active = true;
			}
			if (flag2 === tmpArray.length) {
				$scope.employee.deactive = true;
			}
			if (flag1 !== tmpArray.length && flag2 !== tmpArray.length) {
				$scope.employee.deactive = false;
				$scope.employee.active = false;
			}
		}
	};

	$scope.edit = function(uesr) {
		$scope.original = angular.copy(uesr);
	};

	$scope.cancelEdit = function(user) {
		user.id = $scope.original.id;
		user.lastName = $scope.original.lastName;
		user.firstName = $scope.original.firstName;
		user.telNo = $scope.original.telNo;
		user.email = $scope.original.email;
		user.wechatId = $scope.original.wechatId;
		user.personalArea = $scope.original.personalArea;
		user.personalSubarea = $scope.original.personalSubarea;
		user.department.id = $scope.original.department.id;
		user.department.name = $scope.original.department.name;
		user.language = $scope.original.language;
	};

	$scope.saveModify = function(user) {
		$scope.editconfirm = {};
		$scope.editconfirm.checked = true;
		var checked;
		ngDialog.openConfirm({
			template: 'employee/editconfirm.html',
			className: 'ngdialog-theme-default',
			scope: $scope
		}).then(function(value) {
			checked = $scope.editconfirm.checked ? "yes" : "no";
			if (value === 1) {
				EmployeeService.saveEdit(user, checked).then(function(result) {
					if (saveAlert) {
						saveAlert.hide();
					}
					if (result.edit_status === true && result.sync_status === true) {
						saveAlert = $alert({
							content: "Edit successfully. Synchronize successfully",
							container: "#alerts-container2",
							type: 'success',
							duration: 5,
							show: true,
							animation: "am-fade-and-slide-top"
						});
					} else {
						if (saveAlert) {
							saveAlert.hide();
						}
						var editRes = result.edit_status ? editRes = "successfully" : editRes = "failed";
						var syncRes = result.sync_status ? syncRes = "successfully" : syncRes = "failed";
						var syncMsg = (result.sync_message !== null) ? syncMsg = result.sync_message : syncMsg = "";
						if (checked === "yes") {
							saveAlert = $alert({
								content: "Edit:" + editRes + ". Synchronize:" + syncRes + ". " + syncMsg,
								container: "#alerts-container",
								type: 'danger',
								duration: 8,
								show: true,
								animation: "am-fade-and-slide-top"
							});
						} else {
							saveAlert = $alert({
								content: "Edit:" + editRes + ". Synchronize: you didn't choose synchronize",
								container: "#alerts-container",
								type: 'warning',
								duration: 8,
								show: true,
								animation: "am-fade-and-slide-top"
							});
						}

					}
				});
			} else {
				$scope.cancelEdit(user);
			}
		});
	};

	$scope.getDepartmentID = function() {};

	$scope.addNewEmployForm = {};
	$scope.addEmployee = function() {
		ngDialog.open({
			template: 'employee/addemployee.html',
			className: 'ngdialog-theme-default dialogwidth800',
			scope: $scope
		});
		$scope.addNewEmployForm = new Object();
		$scope.addNewEmployForm.synced = true;
		$scope.addNewEmployForm.costcenterName = $scope.option;
	};

	$scope.addNewEmployee = function(value) {
		var id;
		if ($scope.addNewEmployForm.id) {
			id = $scope.addNewEmployForm.id.toUpperCase();
		}

		var formObj = {
			"id": id,
			"lastName": $scope.addNewEmployForm.lastName,
			"firstName": $scope.addNewEmployForm.firstName,
			"telNo": $scope.addNewEmployForm.mobile,
			"email": $scope.addNewEmployForm.email,
			"wechatId": $scope.addNewEmployForm.weChat,
			"personalArea": $scope.addNewEmployForm.perArea,
			"personalSubarea": $scope.addNewEmployForm.perSubArea,
			"language": $scope.addNewEmployForm.language,
			"status": $scope.addNewEmployForm.status,
			"department": {
				"id": $scope.addNewEmployForm.costCenter
			}
		};
		var checked;
		$scope.addNewEmployForm.synced ? checked = "yes" : checked = "no";
		if (formObj.id && formObj.lastName && formObj.firstName &&
			formObj.telNo && formObj.language && formObj.department.id && formObj.status) {
			var res = EmployeeService.checkForm(formObj);
			if (res.status === true) {
				EmployeeService.save(formObj, checked).then(function(result) {
					if (result.sync_status !== undefined) {
						if (result.create_status === true && result.sync_status === true) {
							if (saveAlert) {
								saveAlert.hide();
							}
							saveAlert = $alert({
								content: "Save successfully. Synced successfully",
								container: "#alerts-container2",
								type: 'success',
								duration: 5,
								show: true,
								animation: "am-fade-and-slide-top"
							});
							$scope.tableParams.reload();
						} else if (result.create_status === true && result.sync_status === false) {
							if (saveAlert) {
								saveAlert.hide();
							}
							saveAlert = $alert({
								content: "Save successfully. Synced failed. " + result.sync_message,
								container: "#alerts-container",
								type: 'danger',
								duration: 8,
								show: true,
								animation: "am-fade-and-slide-top"
							});
						} else if (result.create_status !== true) {
							if (saveAlert) {
								saveAlert.hide();
							}
							saveAlert = $alert({
								content: "Save failed.",
								container: "#alerts-container",
								type: 'danger',
								duration: 8,
								show: true,
								animation: "am-fade-and-slide-top"
							});
						}
					} else {
						if (saveAlert) {
							saveAlert.hide();
						}
						if (result.create_status === true) {
							saveAlert = $alert({
								content: "Save success. You didn't choose synchronize",
								container: "#alerts-container2",
								type: 'warning',
								duration: 8,
								show: true,
								animation: "am-fade-and-slide-top"
							});
						} else {
							saveAlert = $alert({
								content: "Save failed",
								container: "#alerts-container2",
								type: 'danger',
								duration: 8,
								show: true,
								animation: "am-fade-and-slide-top"
							});
						}
					}
				});
				if (value === 2) {
					ngDialog.closeAll();
				}
			} else {
				$scope.addNewEmployForm.info = res.msg;
			}
		} else {
			$scope.addNewEmployForm.info = "all fields with * need to be inserted";
		}
	};

	$scope.saveAndNewEmployee = function(value) {
		if ($scope.addNewEmployForm.id) {
			id = $scope.addNewEmployForm.id.toUpperCase();
		}
		var formObj = {
			"id": id,
			"lastName": $scope.addNewEmployForm.lastName,
			"firstName": $scope.addNewEmployForm.firstName,
			"telNo": $scope.addNewEmployForm.mobile,
			"email": $scope.addNewEmployForm.email,
			"wechatId": $scope.addNewEmployForm.weChat,
			"personalArea": $scope.addNewEmployForm.perArea,
			"personalSubarea": $scope.addNewEmployForm.perSubArea,
			"language": $scope.addNewEmployForm.language,
			"status": $scope.addNewEmployForm.status,
			"department": {
				"id": $scope.addNewEmployForm.costCenter
			}
		};
		var checked;
		$scope.addNewEmployForm.synced ? checked = "yes" : checked = "no";
		if (formObj.id && formObj.lastName && formObj.firstName &&
			formObj.telNo && formObj.language && formObj.department.id && formObj.status) {
			var res = EmployeeService.checkForm(formObj);
			if (res.status === true) {
				EmployeeService.save(formObj, checked).then(function(result) {
					if (saveAlert) {
						saveAlert.hide();
					}
					if (result.sync_status !== undefined) {
						if (result.create_status === true && result.sync_status === true) {
							$scope.addNewEmployForm = new Object();
							$scope.addNewEmployForm.synced = true;
							$scope.addNewEmployForm.costcenterName = $scope.option;
							$scope.addNewEmployForm.info = "Save and sync " + formObj.id + " successfully";
							$scope.tableParams.reload();
						} else if (result.create_status === true && result.sync_status === false) {
							$scope.addNewEmployForm = new Object();
							$scope.addNewEmployForm.synced = true;
							$scope.addNewEmployForm.costcenterName = $scope.option;
							$scope.addNewEmployForm.info = "Save " + formObj.id + " successfully. " + "Synced failed.";
							$scope.tableParams.reload();
						} else if (result.create_status !== true) {
							$scope.addNewEmployForm.info = "Save " + formObj.id + "failed.";
						}
					} else {
						if (result.create_status === true) {
							$scope.addNewEmployForm = new Object();
							$scope.addNewEmployForm.synced = true;
							$scope.addNewEmployForm.costcenterName = $scope.option;
							$scope.addNewEmployForm.info = "Save " + formObj.id + " successfully. " + "You didn't choose synchronize";
							$scope.tableParams.reload();
						} else {
							$scope.addNewEmployForm.info = "Save " + formObj.id + "failed";
						}
					}
				});
			} else {
				$scope.addNewEmployForm.info = res.msg;
			}
		} else {
			$scope.addNewEmployForm.info = "all fields with * need to be inserted";
		}
	};

	$scope.deactiveSelectEmployee = function() {
		if(saveAlert){saveAlert.hide();}
		deactiveArray = [];
		angular.forEach($scope.tableParams.data, function(user) {
			if (!!user.selected) {
				deactiveArray.push(user);
			}
		});
		$scope.confirmForm = {};
		$scope.confirmForm.synced = true;
		ngDialog.openConfirm({
			template: 'employee/deactiveconfirm.html',
			scope: $scope
		}).then(function(value) {
			if (value === 1) {
				for (var i = 0; i < deactiveArray.length; i++) {
					deactiveArray[i].comment = $scope.confirmForm.comment;
					deactiveArray[i].status = "00";
					deactiveArray[i].selected = false;
				}
				var checked;
				$scope.confirmForm.synced ? checked = "yes" : checked = "no";
				EmployeeService.activeAction(deactiveArray, checked).then(function(result) {
					if(checked === "yes"){
						if(result.change_status ){
							saveAlert = $alert({
								content: "Deactive success. "+ "Sync success: "+ result.sync_success + " Sync failed: " + result.sync_fail,
								container: "#alerts-container",
								type: 'success',
								duration: 8,
								show: true,
								animation: "am-fade-and-slide-top"
							});
						}else{
							saveAlert = $alert({
								content: "Deactive failed",
								container: "#alerts-container",
								type: 'danger',
								duration: 8,
								show: true,
								animation: "am-fade-and-slide-top"
							});
							deactiveArray[i].status = "01";
						}
					}else{
						if(result.change_status){
							//success to change status,did not sync.
							saveAlert = $alert({
								content: "Deactive success. You didn't choose synchronize",
								container: "#alerts-container",
								type: 'warning',
								duration: 8,
								show: true,
								animation: "am-fade-and-slide-top"
							});
						}else{
							//failed to change status,did not sync.
							saveAlert = $alert({
								content: "Deactive failed. You didn't choose synchronize",
								container: "#alerts-container",
								type: 'danger',
								duration: 8,
								show: true,
								animation: "am-fade-and-slide-top"
							});
							deactiveArray[i].status = "01";
						}
					}
					$scope.employee.deactive = false;
					$scope.employee.active = false;
				})
			}
		})
	};

	$scope.activeSelectEmployee = function() {
		if(saveAlert){saveAlert.hide();}
		activeArray = [];
		angular.forEach($scope.tableParams.data, function(user) {
			if (!!user.selected) {
				activeArray.push(user);
			}
		});
		$scope.confirmForm = {};
		$scope.confirmForm.synced = true;
		ngDialog.openConfirm({
			template: 'employee/enactiveconfirm.html',
			scope: $scope
		}).then(function(value) {
			if (value === 1) {
				for (var i = 0; i < activeArray.length; i++) {
					activeArray[i].comment = $scope.confirmForm.comment;
					activeArray[i].status = "01";
					activeArray[i].selected = false;
				}
				var checked;
				$scope.confirmForm.synced ? checked = "yes" : checked = "no";
				EmployeeService.activeAction(activeArray, checked).then(function(result) {
					if(checked === "yes"){
						if(result.change_status ){
							saveAlert = $alert({
								content: "Active success. "+ "Sync success: "+ result.sync_success + " Sync failed: " + result.sync_fail,
								container: "#alerts-container",
								type: 'success',
								duration: 8,
								show: true,
								animation: "am-fade-and-slide-top"
							});
						}else{
							saveAlert = $alert({
								content: "Active failed",
								container: "#alerts-container",
								type: 'danger',
								duration: 8,
								show: true,
								animation: "am-fade-and-slide-top"
							});
							activeArray[i].status = "00";
						}
					}else{
						if(result.change_status){
							//success to change status,did not sync.
							saveAlert = $alert({
								content: "Active success. You didn't choose synchronize",
								container: "#alerts-container",
								type: 'warning',
								duration: 8,
								show: true,
								animation: "am-fade-and-slide-top"
							});
						}else{
							//failed to change status,did not sync.
							saveAlert = $alert({
								content: "Active failed. You didn't choose synchronize",
								container: "#alerts-container",
								type: 'danger',
								duration: 8,
								show: true,
								animation: "am-fade-and-slide-top"
							});
							activeArray[i].status = "00";
						}
					}
					$scope.employee.deactive = false;
					$scope.employee.active = false;
				})
			}
		})
	};

	
	//not used now
	$scope.deactive = function(user) {
		var array = [];
		$scope.confirmForm = {};
		$scope.confirmForm.synced = true;
		ngDialog.openConfirm({
			template: 'employee/deactiveconfirm.html',
			scope: $scope
		}).then(function(value) {
			if (value === 1) {
				user.comment = $scope.confirmForm.comment;
				user.status = "00";
				var checked;
				$scope.confirmForm.synced ? checked = "yes" : checked = "no";
				array.push(user);
				EmployeeService.activeAction(array, checked).then(function(result) {})
			}
		})
	};

	//not used now
	$scope.enactive = function(user) {
		var array = [];
		$scope.confirmForm = {};
		ngDialog.openConfirm({
			template: 'employee/enactiveconfirm.html',
			scope: $scope
		}).then(function(value) {
			if (value === 1) {
				user.comment = $scope.confirmForm.comment;
				user.status = "01";
				var checked;
				$scope.confirmForm.synced ? checked = "yes" : checked = "no";
				array.push(user);
				EmployeeService.activeAction(array, checked).then(function(result) {

				})
			}
		})
	};
	
	$scope.$watch("filter.$", function() {
		$scope.tableParams.reload();
	});
});

mainPage.service("EmployeeService", function($http, $filter, $q, $location, BatchService) {
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

	function delData(id) {
		$http.post("../adminctrl/emp/del/" + id)
			.success(function(response) {
				//console.log("del success");
			}).error(function(response) {
				//console.log("fail del");
			});
	}
	
	var service = {
		cachedData: [],
		modified: 0,
		getData: function($defer, params, filter) {
			if (flag === 0 && service.cachedData.length > 0 && BatchService.batchUpdated === 0) {
				//console.log("using cached data")
				var filteredData = filterData(service.cachedData, filter);
				var transformedData = sliceData(orderData(filteredData, params), params);
				params.total(filteredData.length);
				$defer.resolve(transformedData);
			} else {
				//console.log("fetching data");
				$http.get("/moc/adminctrl/emp/list")
					.success(function(resp) {
						var allInfo = resp.employees;
						angular.copy(allInfo, service.cachedData);
						params.total(allInfo.length);
						var filteredData = $filter('filter')(allInfo, filter);
						var transformedData = transformData(allInfo, filter, params);
						$defer.resolve(transformedData);
						BatchService.batchUpdated = 0
						flag = 0;
					}).error(function(data, status, headers, config) {
						//$location.url("views/errorpage.jsp");
						document.open();
						document.write(data);
						document.close();
						//console.log(status);

					});
			}
		},

		del: function(id) {
			delData(id);
		},
		save: function(user, syncStatus) {
			var a = JSON.stringify(user);
			var deferred = $q.defer();
			$http.post("../moc/adminctrl/emp/save/" + syncStatus, a)
				.success(function(response) {
					deferred.resolve(response);
				}).error(function(response) {
					deferred.resolve(response);
				});
			flag = 1;
			return deferred.promise;
		},
		saveEdit: function(user, status) {
			var deferred = $q.defer();
			var a = JSON.stringify(user);
			$http.post("../moc/adminctrl/emp/edit/" + status, a)
				.success(function(response) {
					deferred.resolve(response);
				}).error(function(response) {

				});
			flag = 1;
			return deferred.promise;
		},
		activeAction: function(array, syncStatus) {
			var deferred = $q.defer();
			$http.post("../moc/adminctrl/emp/changestatus/" + syncStatus, array)
				.success(function(response) {
					deferred.resolve(response);
				}).error(function(response) {

				});
			flag = 1;
			return deferred.promise;
		},
		checkForm: function(formObj) {
			var obj = {
				msg: "",
				status: true
			};
			
			if(!((formObj.id.length === 7 && formObj.id.search("I") === 0) || 
			    (formObj.id.length === 7 && formObj.id.search("D") === 0) || 
			    (formObj.id.length === 8 && formObj.id.search("C") === 0))){
				obj.msg = "Employee Id error.";
				obj.status = false;
			}
			
//			if (formObj.id.length !== 7 || (formObj.id.search("I") !== 0 && formObj.id.search("C") !== 0 && formObj.id.search("D") !== 0)) {
//				obj.msg = "Employee Id error.";
//				obj.status = false;
//			}
			
			if(formObj.email){
				if (formObj.email.search("@sap.com") === -1) {
					obj.msg = "Email synatx error";
					obj.status = false;
				}
			}
			
			return obj;
		},
		deactiveSelect: function(array) {
			var deferred = $q.defer();
			$http.post("../moc/adminctrl/emp/changestatus", array)
				.success(function(response) {
					deferred.resolve(response);
				}).error(function(response) {
					deferred.resolve(response);
			    });
			flag = 1;
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
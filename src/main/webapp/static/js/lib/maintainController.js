mainPage.controller('maintenanceCtrl', function($scope, $rootScope, $http, $window, $alert, $routeParams, ngTableParams, ngDialog, EmployeeService) {
  var data = EmployeeService.data;
  var self = this;
  
  $scope.tableParams = new ngTableParams({
    page: 1, // show first page
    count: 10, // count per page
    //sorting: {name:'asc'}
  }, {
    total: 0, // length of data
    getData: function($defer, params) {
      EmployeeService.getData($defer, params, $scope.filter);
    }
  });

  $scope.$parent.tab.first = "Home";
  $scope.$parent.tab.second = "Employee";
  $scope.$parent.tab.third = "Maintenance";
  $scope.checked = false;

  $scope.delEmployee = function(id, user) {
    EmployeeService.del(id);
    $scope.tableParams.data.splice(user, 1);
  };

  $scope.saveModify = function(user) {
    EmployeeService.saveEdit(user);
  };

  $scope.addNewEmployForm = {};
  $scope.addNewEmployee = function() {
    var formObj = {
      "id": $scope.addNewEmployForm.id,
      "lastName": $scope.addNewEmployForm.lastName,
      "firstName": $scope.addNewEmployForm.firstName,
      "telNo": $scope.addNewEmployForm.mobile,
      "email": $scope.addNewEmployForm.email,
      "wechatId": $scope.addNewEmployForm.weChat,
      "personalArea": $scope.addNewEmployForm.perArea,
      "personalSubarea": $scope.addNewEmployForm.perSubArea,
      "language": $scope.addNewEmployForm.language,
      "department": {
        "id": $scope.addNewEmployForm.costCenter
      }
    };
    if (formObj.id && formObj.lastName && formObj.firstName &&
      formObj.telNo && formObj.email && formObj.wechatId &&
      formObj.personalArea && formObj.personalSubarea &&
      formObj.language && formObj.department.id) {

      var res = EmployeeService.checkForm(formObj);
      if (res.status === true) {
        EmployeeService.save(formObj);
        ngDialog.closeAll();
        var itemsAlert = $alert({
          content: "save success",
          container: "#alerts-container",
          type: 'success',
          duration: 2,
          show: true,
          animation: "am-fade-and-slide-top"
        });
      } else {
        $scope.addNewEmployForm.info = res.msg;
      }
    }
  };

  $scope.addNewEmployee2 = function() {
    console.log("function 2");
  };

  $scope.updateConfirm = function() {
    ngDialog.open({
      template: 'updateconfirm.html',
      scope: $scope
    });
  };

  $scope.delSelectEmployee = function() {
    alert("del all");
  };

  $scope.addEmployee = function() {
    ngDialog.open({
      template: 'employee/addemployee.html',
      className: 'ngdialog-theme-default dialogwidth800',
      scope: $scope
    });
  };

  //  $scope.addEmployee = function(user) {
  //      ngDialog.openConfirm({
  //        template: 'employee/addemployee.html',
  //          className: 'ngdialog-theme-default dialogwidth800',
  //          scope: $scope
  //      }).then(function(value) {}, function(reason) {
  //        console.log('Modal promise rejected. Reason: ', reason);
  //      });
  //    };

  $scope.deactive = function(user) {
    ngDialog.openConfirm({
      template: 'employee/deactiveconfirm.html',
      scope: $scope
    }).then(function(value) {
      EmployeeService.deactive(user);
    }, function(reason) {
      console.log('Modal promise rejected. Reason: ', reason);
    });
  };

  $scope.confirmDeactive = function() {
    console.log("close");
  };

  $scope.$watch("filter.$", function() {
    $scope.tableParams.reload();
  });
});

mainPage.service("EmployeeService", function($http, $filter) {
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
    $http.post("../emp/del/" + id)
      .success(function(response) {
        console.log("del success");
      }).error(function(response) {
        console.log("fail del");
      });
  }

  function saveData(data) {
    var a = JSON.stringify(data);
    $http.post("emp/save", a)
      .success(function(response) {
        console.log("save");
      }).error(function(response) {
        console.log("fail save");
      });
  }

  function saveEditData(data) {
    var a = JSON.stringify(data);
    $http.post("emp/edit", a)
      .success(function(response) {
        console.log("save edit");
      }).error(function(response) {
        console.log("fail save");
      });
  }

  function saveDeactive(data) {
    var deactive = JSON.stringify(data);
    if (deactive.status === "enable") {
      deactive.status = "disable";
    }
    $http.post("emp/edit", deactive)
      .success(function(response) {

      }).error(function(response) {

      });
  }

  var service = {
    cachedData: [],
    modified: 0,
    getData: function($defer, params, filter) {
      if (flag === 0 && service.cachedData.length > 0) {
        console.log("using cached data")
        var filteredData = filterData(service.cachedData, filter);
        var transformedData = sliceData(orderData(filteredData, params), params);
        params.total(filteredData.length)
        $defer.resolve(transformedData);
      } else {
        console.log("fetching data");
        $http.get("/moc/emp/list")
          .success(function(resp) {
            var allInfo = resp.employees;
            angular.copy(allInfo, service.cachedData);
            params.total(allInfo.length);
            var filteredData = $filter('filter')(allInfo, filter);
            var transformedData = transformData(allInfo, filter, params);
            $defer.resolve(transformedData);
            flag = 0;
          });
      }
    },

    del: function(id) {
      delData(id);
    },
    save: function(user) {
      saveData(user);
      flag = 1;
    },
    saveEdit: function(user) {
      saveEditData(user);
      flag = 1;
    },
    deactive: function(user) {
      saveDeactive(user);
      flag = 1;
    },
    checkForm: function(formObj) {
      var obj = {
        msg: "",
        status: true
      };
      if (formObj.id.length !== 7) {
        obj.msg = "id length error";
        obj.status = false;
      }
      if (formObj.telNo.toString().length !== 11) {
        obj.msg = "tel number length error";
        obj.status = false;
      }
      if (formObj.email.search("@") === 0) {
        obj.msg = "email synatx error";
        obj.status = false;
      }
      return obj;
    }
  };
  return service;
});
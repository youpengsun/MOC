mainPage.controller('vendorAddCtrl', function($scope, $rootScope, $http, $alert, $timeout, $routeParams, ngTableParams, ngDialog, vendorAddService) {
   var data = vendorAddService.data;
   var newVendorAlert;
   var lineArray = [];
   $scope.$parent.tab.first = "Home";
   $scope.$parent.tab.second = "Vendor";
   $scope.$parent.tab.secondIcon = "fa fa-tasks";
   $scope.$parent.tab.third = "Add Vendor";
   $scope.newContractForm = {};
   $scope.newContractEndda = {};
   $scope.newContractBegda = {};

   $scope.addlineTableParams = new ngTableParams({
      page: 1, 
      count: 6,
   }, {
      total: 0, 
      counts: [],
      getData: function($defer, params) {
         vendorAddService.getLineData($defer, params, $scope.filter, lineArray);
      }
   });

   $scope.newVendorPanel = {
      active: false
   };

   $scope.newVendorLinePanel = {
      active: false
   };

   $scope.cancelNewVendor = function() {
      $scope.newVendorPanel.active = false;
   };

   $scope.newContractBegdaOpen = function($event) {
      $scope.newContractBegda.opened = true;
   };

   $scope.newContractEnddaOpen = function($event) {
      $scope.newContractEndda.opened = true;
   };

   $scope.newVendorForm = new Object();

   $scope.addNewVendor = function(vendor) {
      $scope.newVendorPanel.active = true;
      $scope.newVendorLinePanel.active = false;
      $scope.newVendorForm = new Object();
      vendorAddService.buildDistrict().then(function(result) {
         $scope.newVendorForm.businessDistrictID = result.districts;
      });
   };

   $scope.newContractForm.dateFormat = 'yyyy.MM.dd';

   $scope.newVendorForm = {
      saved: false
   }

   $scope.saveNewVendor = function() {
      if (newVendorAlert) {
         newVendorAlert.hide();
      }
      var newVendor = {
         "name": $scope.newVendorForm.name,
         "contactTelNO": $scope.newVendorForm.contactTelNO,
         "contactName": $scope.newVendorForm.contactName,
         "wechatID": $scope.newVendorForm.wechatID,
         "promotion": $scope.newVendorForm.promotion,
         "contactEmail": $scope.newVendorForm.contactEmail,
         "dianpingID": $scope.newVendorForm.dianpingID,
         "address": $scope.newVendorForm.address,
         "reportPeriod": $scope.newVendorForm.reportPeriod,
         "vendorType": $scope.newVendorForm.vendorType,
         "businessDistrictId": $scope.newVendorForm.businessDistrict,
         "beginDate": $scope.newVendorForm.begda,
         "comment": $scope.newVendorForm.contractComment,
         "contract_no": $scope.newVendorForm.contractNo,
         "endDate": $scope.newVendorForm.endda,
         "status": "01"
      };

      vendorAddService.saveNewVendor(newVendor).then(function(res) {
         if (res.result === "true") {
            newVendorAlert = $alert({
               content: "Save vendor successfully.",
               container: "#alerts-container",
               type: 'success',
               duration: 8,
               show: true,
               animation: "am-fade-and-slide-top"
            });
            $scope.newVendorForm.id = res.vendorId;
            $scope.newLineForm = [];
            $scope.newLineForm.push({});
            $scope.newVendorLinePanel.active = true;
            $scope.newVendorForm.saved = true;
         } else {
            newVendorAlert = $alert({
               content: "Save vendor failed.",
               container: "#alerts-container",
               type: 'danger',
               duration: 8,
               show: true,
               animation: "am-fade-and-slide-top"
            });
            $scope.newVendorForm.saved = false;
         }
      });
   };

   $scope.addNewVendorLineBtnClick = function() {
      $scope.addNewVendorLineForm = {};
      ngDialog.open({
         template: 'vendor/addnewline.html',
         className: 'ngdialog-theme-default dialogwidth800',
         scope: $scope
      });
      $scope.addNewVendorLineForm.vendorID = $scope.newVendorForm.id;
      //$scope.addNewVendorLineForm.lineNO = lineArray.length + 1;
   };

   $scope.addNewVendorLine = function() {
      if (newVendorAlert) {
         newVendorAlert.hide();
      }
      var linesArray = [];
      var newVendorLine = {
         "name": $scope.addNewVendorLineForm.name,
         "wechatID": $scope.addNewVendorLineForm.wechatID,
         //"lineNO": lineArray.length + 1,
         "vendor": {
            "id": $scope.newVendorForm.id,
            "name": $scope.newVendorForm.name,
            "contactTelNO": $scope.newVendorForm.contactTelNO,
            "contactName": $scope.newVendorForm.contactName,
            "businessDistrict": {
               id: $scope.newVendorForm.businessDistrict,
            },
            "wechatID": $scope.newVendorForm.wechatID,
            "promotion": $scope.newVendorForm.promotion,
            "contactEmail": $scope.newVendorForm.contactEmail,
            "dianpingID": $scope.newVendorForm.dianpingID,
            "address": $scope.newVendorForm.address
         }
      };
      linesArray.push(newVendorLine);
      vendorAddService.saveNewVendorLine(newVendorLine).then(function(res) {
         if (res.status === true) {
            ngDialog.closeAll();
            newVendorAlert = $alert({
               content: "Save line success",
               container: "#alerts-container2",
               type: 'success',
               duration: 5,
               show: true,
               animation: "am-fade-and-slide-top"
            });
            newVendorLine.openID = newVendorLine.vendor.id + "-" + res.line;
            newVendorLine.lineNO = res.line;
            lineArray.push(newVendorLine);
            $scope.addlineTableParams.reload();
         } else {
            newVendorAlert = $alert({
               content: "Save line failed",
               container: "#alerts-container2",
               type: 'danger',
               duration: 8,
               show: true,
               animation: "am-fade-and-slide-top"
            });
         }
      });
   };
   
   $scope.newLineQRCode = function(vendorLine) {
	   $scope.newLineopenID = vendorLine.openID;
	   var btnClick = document.getElementById("newQRCodeBtn");
		document.getElementById("newQRCodeBtn").value = "";
		btnClick.click();
   };
   
   $scope.generateNewQRcode = function(files) {
		var fd = new FormData();
		$scope.user = {};
		fd.append("image", files[0]);
		if (files[0] && files[0].size > 2000000) {
			newVendorAlert = $alert({
				content: "File larger than 2MB",
				container: "#alerts-container2",
				type: 'danger',
				duration: 5,
				show: true,
				animation: "am-fade-and-slide-top"
			});
		} else {
			fd.append("code", $scope.newLineopenID);
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
				$scope.user.qrtitle = $scope.newLineopenID;
			});
		}
	};
   
   $scope.closeAllPanel = function() {
	   $scope.newVendorPanel.active = false;
	   $scope.newVendorLinePanel.active = false;
   };

   $scope.addVendorLogo = function(files) {
      var fd = new FormData();
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
         fd.append("id", $scope.newVendorForm.id);
         $http.post("adminctrl/vendor/logo", fd, {
            withCredentials: true,
            headers: {
               'Content-Type': undefined
            },
            transformRequest: angular.identity
         }).
         success(function(response) {
            $timeout(function() {
               $scope.newVendorForm.logo = "../vendorlogo/" + $scope.newVendorForm.id + ".png?" + new Date().getTime();
            }, 3000);
         });
      }
   };

   $scope.$watch('newVendorForm.begda', function(begda) {
      var myDate = new Date($scope.newVendorForm.endda);
      if (begda > myDate) {
         $scope.newVendorForm.endda = begda;
      }
   });
});

mainPage.service("vendorAddService", function($http, $q, $filter, $alert, vendorMaintainService) {
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
      added: 0,
      getLineData: function($defer, params, filter, array) {
         params.total(array.length);
         var filteredData = $filter('filter')(array, filter);
         var transformedData = transformData(array, filter, params);
         $defer.resolve(transformedData);
      },

      saveNewVendor: function(vendor) {
         var deferred = $q.defer();
         $http.post("adminctrl/vendor/save", vendor).success(function(response) {
            vendorMaintainService.setNewAdd(1);
            deferred.resolve(response);
         }).error(function(response) {
            deferred.resolve(response);
         });
         return deferred.promise;
      },
      
      saveNewVendorLine: function(linesArray) {
         var deferred = $q.defer();
         $http.post("adminctrl/vendorLine/save", linesArray).success(function(response) {
            deferred.resolve(response);
         }).error(function(response) {
            deferred.resolve(response);
         });
         return deferred.promise;
      },

      buildDistrict: function() {
         var defer = $q.defer();
         $http.get("adminctrl/district/list").success(
            function(response) {
               defer.resolve(response);
            });
         return defer.promise;
      }
   };
   return service;
});
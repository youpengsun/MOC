/**
 * I065892
 */
mainPage.controller('settlementCtrl',
    function($scope, $alert, $http, settlementService) {
//Menu
        $scope.$parent.tab.first = "Home";
        $scope.$parent.tab.second = "Transaction";
        $scope.$parent.tab.third = "Settlement";
        $scope.$parent.tab.secondIcon = "fa fa-bar-chart";

// Initialize
        $scope.queryForm = {};
        $scope.report = {};
        $scope.report.active = false;

        $scope.ranges = {
            'Today': [moment(), moment()],
            'Yesterday': [moment().subtract(1, 'days'),
                moment().subtract(1, 'days')
            ],
            'This Week': [moment().startOf('isoweek'),
                moment().endOf('isoweek')
            ],
            'Last Week': [
                moment().startOf('isoweek').subtract(7, 'days'),
                moment().startOf('isoweek').subtract(1, 'days')
            ],
            'This month': [moment().startOf('month'),
                moment().endOf('month')
            ],
            'Last Month': [
                moment().subtract(1, 'months').startOf('month'),
                moment().subtract(1, 'months').endOf('month')
            ]
        };
        // Get Vendor List
        $scope.initial = function() {
            settlementService.queryVendorList().then(function(result) {
                if (result) {
                    $scope.vendors = result.vendors;
                };
            });
        };
        $scope.initial();

//Private Method
        $scope.getPeriod = function(reportPeriod) {
            var text = "";
            switch (reportPeriod) {
                case 'D0':
                    text = "Daily";
                    break;
                case 'W0':
                    text = "Weekly";
                    break;
                case 'M0':
                    text = "Monthly";
                    break;
                default:
            };
            text = '(' + text + ')';
            return text;
        };
        
        $scope.changeVendor = function() {
           // console.log($scope.queryForm.vendorId);

            var reportPeriod = "";
        }

//Event
        // Search button
        $scope.querySettlement = function() {
        	
            $scope.$broadcast('show-errors-check-validity');

            if ($scope.inputForm.$valid) {
            	
                var searchObj = {
                    "vendorId": $scope.queryForm.vendorId,
                    "beginDate": $scope.queryForm.daterange.startDate,
                    "endDate": $scope.queryForm.daterange.endDate,
                };

                settlementService.getSettlement(searchObj).then(function(result) {
                    if (result) {
                        $scope.report = result.report;
                        $scope.report.active = true;
                    } else {
                        $scope.report.active = false;
                    }
                })
            }
        };
    });

//Service Register
mainPage.service('settlementService', function($http, $q, $filter) {

    var service = {
    	//Get Vendor List	
        queryVendorList: function() {
            var deferred = $q.defer();
            var postUrl = '/moc/adminctrl/vendor/list'
            $http.post(postUrl).success(function(response) {
                deferred.resolve(response);
                //console.log("Vendor success");
            }).error(function(response) {
               // console.log("Vendor fail");
            });
            return deferred.promise;

        },

        getSettlement: function(request) {
            var deferred = $q.defer();
            var postUrl = '/moc/adminctrl/settlement/query';
            var json = JSON.stringify(request);
            $http.post(postUrl, json).success(
                function(response) {
                    deferred.resolve(response);
                    //console.log("Settlement query successful...");
                }).error(function(response) {
                //console.log("Settlement query failed...");
            });

            return deferred.promise;
        }
    }

    return service;
});

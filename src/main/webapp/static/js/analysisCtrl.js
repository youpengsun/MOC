/**
 * 
 */
mainPage.controller('analysisCtrl', function($scope, $http, analysisService) {
    var self = this;
    var data = {};
    $scope.$parent.tab.first = "Home";
	$scope.$parent.tab.second = "Transaction";
	$scope.$parent.tab.third = "Analysis";
    $scope.$parent.tab.secondIcon = "fa fa-bar-chart";
    $scope.analysis = {};
    $scope.analysis.checkBox = "All";
    $scope.analysis.Canteen = "Canteen";
    $scope.analysis.checkBoxChecked = false;
/*********************Start *****************************/
    $scope.endDateBeforeRender = endDateBeforeRender
    $scope.endDateOnSetTime = endDateOnSetTime
    $scope.startDateBeforeRender = startDateBeforeRender
    $scope.startDateOnSetTime = startDateOnSetTime
   
    

    function startDateOnSetTime() {
        $scope.$broadcast('start-date-changed');
        var a = $scope.query.beginMonth;
        $scope.selectChange();
    }

    function endDateOnSetTime() {
        $scope.$broadcast('end-date-changed');
        var a = $scope.query.endMonth;
        $scope.selectChange();
    }

    function startDateBeforeRender($dates) {
        if(!$scope.dateRangeEnd){
            $scope.dateRangeEnd = new Date();
        }
        if ($scope.dateRangeEnd) {
            var activeDate = moment($scope.dateRangeEnd);
            $dates.filter(function(date) {
                return date.localDateValue() >= activeDate.valueOf() || date.localDateValue() < 1451577600000
            }).forEach(function(date) {
                date.selectable = false;
            })
        }
    }

    function endDateBeforeRender($view, $dates) {
        if(!$scope.dateRangeStart){
            $scope.dateRangeStart = new Date("2016-01-01");
        }

        if ($scope.dateRangeStart) {
            var activeDate = moment($scope.dateRangeStart).subtract(1, $view).add(1, 'minute');

            $dates.filter(function(date) {
                return date.localDateValue() <= activeDate.valueOf() || date.localDateValue() >new Date().valueOf()
            }).forEach(function(date) {
                date.selectable = false;
            })
        }
    }
/*********************END*****************************/
    
    $scope.query = {
        vendors: [],
        beginMonth: new Date(),
        endMonth: new Date(),
        showline:{checked:false,disabled:true}
    };    

    $scope.beginDate = {
        "year": 2016,//moment().year(),
        "month": 1,
        "day":0
    };
    $scope.endDate = {
		 "year": moment().year(),
         "month": moment().month()+1,
         "day":0
    };

    $scope.style = function(value) {return { "color": value };}

    //Check list Button
    $scope.checkAll = function() {
    	if(!$scope.analysis.checkBoxChecked){
    		$scope.query.vendors = [];
    	//	$scope.analysis.checkBox = "Check all"
    	}else{
    		$scope.query.vendors = $scope.vendors.map(function(item) {
                return item.id;
            });
         //   $scope.analysis.checkBox = "Uncheck all";
    	}
        
        //console.log($scope.analysis.checkBoxChecked);
    };
    $scope.uncheckAll = function() {
        $scope.query.vendors = [];
    };

//    $scope.checkFirst = function() {
//        $scope.query.vendors.splice(0, $scope.vendors.length);
//        $scope.query.vendors.push(1);
//    };
    $scope.checkAllActive = function() {
        if ($scope.vendors) {
        	$scope.query.vendors = [];
            for (var i = $scope.vendors.length - 1; i >= 0; i--) {
                if ($scope.vendors[i].activeStatus == 'Active') {
                //	if ($scope.vendors[i].vendorType == 'IN') {
                    $scope.query.vendors.push($scope.vendors[i].id);
                };
            };
        }
    };
    $scope.checkDefault = function() {
        if ($scope.vendors) {
        	$scope.query.vendors = [];
            for (var i = $scope.vendors.length - 1; i >= 0; i--) {
                if ($scope.vendors[i].vendorType == 'IN') {
                    $scope.query.vendors.push($scope.vendors[i].id);
                };
            };
        }
    };

    //Event Condition Changed
    $scope.selectChange = function() {

        var datasource = new Array();

        var indexArray = $scope.ChangeChartCategory();
        
        for (var i = $scope.dataset.length - 1; i >= 0; i--) {
            for (var j = $scope.query.vendors.length - 1; j >= 0; j--) {
                var vendorid = $scope.query.vendors[j];

                if ($scope.dataset[i].id == vendorid) {
                	
                	//datasource.push($scope.dataset[i]);
                	
                	var dataList = $scope.changeSeriesData($scope.dataset[i],indexArray);
                	datasource.push(dataList);
                	
                };
            };
        };
        datasource.reverse();
        
        //var dataSource1 = $scope.changeSeriesData();
        $scope.chartConfig.series = datasource;
        
        if($scope.query.vendors.length == 1 ){
        	$scope.query.showline.disabled = false;
        }else{
        	$scope.query.showline.disabled = true;
        }
        
        $scope.changeChartSubtitle();
    };
    
    $scope.changeSeriesData = function(consumeRecord,indexArray) {
    	
    	var dataArray = new Array(indexArray.length);
    	
    	for (var i = 0; i < indexArray.length ; i++){
    		var value = consumeRecord.dataList[indexArray[i]];
    			 //
    		dataArray[i] = value;
    	}
    	var returnObject = {
    			"name":consumeRecord.id + '-' + consumeRecord.name,
    			"data":dataArray,
    	}
    	
    	return returnObject;
    	
    	
    };


     $scope.chartConfig = {
         options: {
             chart: {
                 type: 'column'
             }
         },
         title: {
             text: 'Consume Chart'
         },
         subtitle: {
             text: '',//$scope.query.beginMonth.getFullYear() + '年' + ($scope.query.beginMonth.getMonth()) + '月  ~ ' + $scope.endDate.year + '年' + $scope.endDate.month + '月 '
         },
         series: [{
             name: '',
             data: [0, 0, 0, 0, 0]
         }],
         xAxis: {
             title: {
                 text: 'Month'
             },
//             categories: ['Jan', 'Feb', 'Mar', 'Apr', 'May']
         },
         yAxis: {
             title: {
                 text: 'Consume Times'
             },
         	 labels:{
         		step: 1
         	 }
         },
         size: {
	         //width: 2000,
	         height: 600
         },
         loading: false
     }
     $scope.chartTypes = [
                          {"id": "line", "title": "Line"},
//                          {"id": "spline", "title": "Smooth line"},
//                          {"id": "area", "title": "Area"},
//                          {"id": "areaspline", "title": "Smooth area"},
                          {"id": "column", "title": "Column"},
//                          {"id": "bar", "title": "Bar"},
//                          {"id": "pie", "title": "Pie"},
                          {"id": "scatter", "title": "Scatter"}
                        ];


    $scope.ChangeChartCategory = function() {
    	
    	var a = moment($scope.query.beginMonth);
    	var b = moment($scope.query.endMonth);
    	
    	var diffMonth = b.diff(a, 'month') + 1;
    	
        var monthArray = new Array(diffMonth);
        var indexArray = new Array(diffMonth);
        
        for (var i = 0; i <= monthArray.length - 1; i++) {
            //monthArray[i] = moment.monthsShort('-MMM-', i);
            if(i > 0){
            	a.add(1,'M');            	
            }
            monthArray[i] = a.format('MMM');
            indexArray[i] = parseInt(a.format('YYYYMM'));
        };

        $scope.chartConfig.xAxis.categories = monthArray;
        
        return indexArray;
    }
    
    $scope.changeChartSubtitle = function(){
    	var beginMoment = moment($scope.query.beginMonth);
    	var endMoment = moment($scope.query.endMonth);
    	$scope.chartConfig.subtitle.text = beginMoment.format('YYYY' + '年' + 'MM'+ '月  ~ ') + endMoment.format('YYYY' + '年' + 'MM' + '月 ');
    	
    }

    $scope.getDataSource = function() {
        var begda = $scope.query.begda;
        var endda = $scope.query.endda;
        var request = {
                "vendors": 	$scope.query.vendors,
                "begda": 	$scope.beginDate,
                "endda": 	$scope.endDate 
            }
            //      settlementService.getData(formObj);
            analysisService.getAnalysisResult(request).then(function(result) {
                if (result) {
                    $scope.dataset = result.dataSource;
                    $scope.vendors = $scope.dataset.map(function(item) {
                    	var vendor = {"id": item.id ,"name": item.name,"vendorType":item.vendorType,"activeStatus":item.activeStatus};
                        if(vendor.activeStatus === "Active") {
                            vendor.color = "#333533";
                        }
                        if(vendor.activeStatus === "Inactive"){
                            vendor.color = "#cc5c59";
                        }
                        return vendor;
                    });
                    $scope.checkAllActive();
                    $scope.selectChange();
                }
            });

    }
    
    $scope.initial = function() {
    	
    	$scope.getDataSource();  //Get source data for once

         //$scope.ChangeChartCategory();
    	$scope.query.beginMonth = new Date(moment().year(),0,1);
    	$scope.query.endMonth = new Date($scope.endDate.year, $scope.endDate.month-1, 1);

    }

    $scope.initial();

});

mainPage.service('analysisService', function($http, $q, $filter) {
    var service = {

        getAnalysisResult: function(request) {
            var deferred = $q.defer();
            var postUrl = '/moc/adminctrl/vendor/consumeanalysis';

            var a = JSON.stringify(request);

            $http.post(postUrl, a).success(
                function(response) {
                    deferred.resolve(response);
                    //console.log("Analysis query successful...");
                }).error(function(response) {
                //console.log("Analysis query failed...");
            });

            return deferred.promise;
        }
    }

    return service;
});


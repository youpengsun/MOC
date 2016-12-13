loginApp.controller("loginController", function($scope, $http, $location) {

	checkLogined();

	$scope.loginGo = function() {
		$http.post("/moc/user/login", $scope.user).success(
				function(response) {
					if (response.loginStatus == "success") {
						if (response.loginRole == "admin") {
							window.location.href = "/moc/index.html";
						} else if (response.loginRole == "cpadmin") {
							window.location.href = "/moc/couponadmin.html";
						}
					} else {
						$scope.loginMessage = response.loginMessage;
					}
				})
	};

	function checkLogined() {
		$http.post("/moc/user/checkLogined").success(function(response) {
			if (response.checkStatus == "logined") {
				if (response.loginRole == "admin") {
					window.location.href = "/moc/index.html";
				}else if(response.loginRole == "cpadmin"){
					window.location.href = "/moc/couponadmin.html";
				}
			}
		})
	}

});

mainPage.controller("userboxController", function($scope, $http) {
	
	$http.post("/moc/user/getusername").success(function(response) {
		$scope.username = response.username;
		$scope.role	= response.role;
		
		if(!$scope.username){
			location.href = "login/login.html";
		}
		
	});
});
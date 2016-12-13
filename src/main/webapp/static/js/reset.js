mainPage
		.controller(
				'resetCtrl',
				function($scope, $rootScope, $http,$alert, $routeParams, resetService) {

					$scope.reset = {};
					$scope.info = {};
					$scope.repeatPassword = "";
					$scope.error = true;
					$scope.$watch('reset.oldPassword', function() {
						$scope.validate();
					});
					$scope.$watch('reset.newPassword', function() {
						$scope.validate();
					});
					$scope.$watch('repeatPassword', function() {
						$scope.validate();
					});

					$scope.validate = function() {
						$scope.error = false;

						if ($scope.reset.oldPassword){
							  $scope.info.oldPassword = "";
						}
						else{
							$scope.error = true;
						};
						

						if($scope.reset.newPassword){
							if ($scope.reset.newPassword.length < 6
									|| $scope.reset.newPassword.length > 30) {
								$scope.info.newPassword = "Please input password with the length from 6~30";
								$scope.error = true;
							} else if($scope.reset.newPassword === $scope.reset.oldPassword){
								$scope.info.newPassword = "The new password is the same with the current password";
								$scope.error = true;
							} else{
								$scope.info.newPassword = "";
							};
						}
						else{
							$scope.info.newPassword = "";
							$scope.error = true;
						};
						
						if($scope.repeatPassword){
							if ($scope.repeatPassword === $scope.reset.newPassword) {
								$scope.info.repeatPassword = "";
							} else {
								$scope.info.repeatPassword = "Not match the new password, enter again";
								$scope.error = true;
							};
						}
						else{
							$scope.info.repeatPassword = "";
							$scope.error = true;
						};
					

					};

					$scope.submit = function() {
						$scope.info={};

						resetService.resetPassword($scope.reset).then(function(res) {
							if(res.result === "true"){
									
								$alert({
					               content: "Password reset successfully",
					               container: "#alerts-container2",
					               type: 'success',
					               duration: 5,
					               show: true,
					               animation: "am-fade-and-slide-top"
					            });
								$scope.reset ={};
								$scope.info={};
								$scope.repeatPassword="";
								$scope.error = true;
								}
							else{
								if(res.error === "password"){
									$scope.info.oldPassword = "Incorrect password";
									$scope.error = true;
								}
								else if(res.error === "login"){
									window.location.href = "/moc/login/login.html";
								}
								else{
									$alert({
					                      content: "Password reset fail",
					                      container: "#alerts-container2",
					                      type: 'danger',
					                      duration: 5,
					                      show: true,
					                      animation: "am-fade-and-slide-top"
					                 });
								}
									
									
							}
					    });
						


				  };
		});

mainPage.service("resetService", function($http, $filter, $q) {
	var service = {
	
		resetPassword: function(reset){
			var defer = $q.defer();
			$http.post("/moc/user/resetpassword", reset).success(function(response) {
				defer.resolve(response);
			}).error(function(){
				defer.resolve(response);
			})
			return defer.promise;
		}

	};
	return service;

} );

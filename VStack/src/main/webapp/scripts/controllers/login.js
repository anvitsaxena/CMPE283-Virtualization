/**
 * @ngdoc function
 * @name yapp.controller:MainCtrl
 * @description # MainCtrl Controller of yapp
 */
angular.module('yapp').controller('LoginCtrl',  ['$scope', '$rootScope', '$location', 'AuthenticationService',
	    function ($scope, $rootScope, $location, AuthenticationService) {
    // reset login status
        AuthenticationService.ClearCredentials();
 
        $scope.submit = function () {
            $scope.dataLoading = true;
            AuthenticationService.Login($scope.openstack_server, $scope.username, $scope.password, function(response) {
                if(response.data) {
                    AuthenticationService.SetCredentials($scope.openstack_server, $scope.username, $scope.password);
                	$location.path('/dashboard');
            } else {
                $scope.error = response.message;
                $scope.dataLoading = false;
                alert("Connection Unsuccessful");
            }
        });
    };
}]);
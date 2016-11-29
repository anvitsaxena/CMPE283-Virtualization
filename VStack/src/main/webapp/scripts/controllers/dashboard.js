'use strict';

/**
 * @ngdoc function
 * @name yapp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of yapp
 */
angular.module('yapp')
  .controller('DashboardCtrl', function($scope, $state, $rootScope, APIService) {
	  
    $scope.$state = $state;
   
    
    $scope.launchInstance = function (instance) {
        $scope.dataLoading = true;
        APIService.launchInstance(instance.name, instance.flavor, instance.image, function(response) {
            if(response.data) {
            	alert(response.data + " " + response.data.status);
        } else {
            $scope.error = response.message;
            $scope.dataLoading = false;
            alert("Instance not created");
        }
    });
    };

    $scope.stopInstance = function (instance) {
        $scope.dataLoading = true;
        APIService.stopInstance(instance.name, function(response) {
            if(response.data) {
            	alert(response.data + " " + response.data.status);
        } else {
            $scope.error = response.message;
            $scope.dataLoading = false;
            alert("Instance not stopped");
        }
    });
    };
    

	$scope.getInstance = function (instance) {
	    $scope.dataLoading = true;
	    APIService.getInstance(instance, function(response) {
	        if(response.data) {
	        	alert(response.data.instanceName + " " + response.data.status);
	           //alert(response.data)
	    } else {
	        $scope.error = response.message;
	        $scope.dataLoading = false;
	        alert("Instance not found");
	    }
	});
	};
	
   /* APIService.getProjects(function(response) {
        if(response.data) {
        	$scope.projects = response.data;
	    } else {
	        $scope.error = response.message;
	        $scope.dataLoading = false;
	        alert("Projects not found");
	    }
    });*/
    
    APIService.getFlavorList(function(response) {
        if(response.data) {
        	$scope.flavors = response.data;	
	    } else {
	        $scope.error = response.message;
	        $scope.dataLoading = false;
	        alert("Flavors not found");
	    }
    });
    
    APIService.getInstanceList(function(response) {
        if(response.data) {
        	$scope.instances = response.data;	
	    } else {
	        $scope.error = response.message;
	        $scope.dataLoading = false;
	        alert("Instance not found");
	    }
    });
    
    APIService.getImageList(function(response) {
        if(response.data) {
        	$scope.images = response.data;	
	    } else {
	        $scope.error = response.message;
	        $scope.dataLoading = false;
	        alert("Image not found");
	    }
    });
    
    
    
    $scope.menuItems = [];
    angular.forEach($state.get(), function (item) {
        if (item.data && item.data.visible) {
            $scope.menuItems.push({name: item.name, text: item.data.text});
        }
    });
  });

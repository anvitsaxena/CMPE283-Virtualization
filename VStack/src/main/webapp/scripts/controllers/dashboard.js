'use strict';

/**
 * @ngdoc function
 * @name yapp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of yapp
 */

angular.module('yapp')
  .controller('DashboardCtrl', function($scope, $state, $rootScope, $window, APIService) {
	  
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
    	window.location = "/VStack/#/dashboard/overview";
        $scope.dataLoading = true;
        APIService.stopInstance(instance, function(response) {
            if(response.data) {
            	alert(response.data + " " + response.data.status);
            	
        } else {
            $scope.error = response.message;
            $scope.dataLoading = false;
            alert("Instance not stopped");
        }
    });
    };
    
    $scope.deleteInstance = function (instance) {
        $scope.dataLoading = true;
        APIService.resumeInstance(instance, function(response) {
            if(response.data) {
            	alert(response.data + " " + response.data.status);
            	
        } else {
            $scope.error = response.message;
            $scope.dataLoading = false;
            alert("Instance not deleted");
        }
    });
    };
    
    
    $scope.resumeInstance = function (instance) {
        $scope.dataLoading = true;
        APIService.resumeInstance(instance, function(response) {
            if(response.data) {
            	alert(response.data + " " + response.data.status);
            	
        } else {
            $scope.error = response.message;
            $scope.dataLoading = false;
            alert("Instance not resumed");
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

var googleChart = googleChart || angular.module("google-chart",[]);

googleChart.directive("googleChart",function(){
    return{
        restrict : "A",
        link: function($scope, $elem, $attr){
            var dt = $scope[$attr.ngModel].dataTable;

            var options = {
            		backgroundColor: 'transparent',
            		titleTextStyle: {
            			fontSize: 14,
            		}
            };
            if($scope[$attr.ngModel].title)
                options.title = $scope[$attr.ngModel].title;
                options.legend= "none";
                options.pieHole= "0.5";
                

            var googleChart = new google.visualization[$attr.googleChart]($elem[0]);
            googleChart.draw(dt,options)
        }
    }
});


angular.module('yapp')
.controller('ReportsCtrl', function($scope, $state) {
  $scope.$state = $state;

  $scope.data1 = {};
      $scope.data1.dataTable = new google.visualization.DataTable();
      $scope.data1.dataTable.addColumn("string","Name")
      $scope.data1.dataTable.addColumn("number","Qty")
      $scope.data1.dataTable.addRow(["Active",1]);
      $scope.data1.dataTable.addRow(["Inactive",0]);
      //$scope.data1.dataTable.addRow(["Test3",3]);
      $scope.data1.title="Status"

 $scope.data2 = {};
      $scope.data2.dataTable = new google.visualization.DataTable();
      $scope.data2.dataTable.addColumn("string","Name")
      $scope.data2.dataTable.addColumn("number","Qty")
      $scope.data2.dataTable.addRow(["SQL",60]);
      $scope.data2.dataTable.addRow(["Server",40]);
      //$scope.data2.dataTable.addRow(["Test3",3]);
      $scope.data2.title="SQL Database Server"

$scope.data3 = {};
      $scope.data3.dataTable = new google.visualization.DataTable();
      $scope.data3.dataTable.addColumn("string","Name")
      $scope.data3.dataTable.addColumn("number","Qty")
      $scope.data3.dataTable.addRow(["Apache+SQL",55.50]);
      $scope.data3.dataTable.addRow(["Server",44.50]);
      //$scope.data3.dataTable.addRow(["Test3",3]);
      $scope.data3.title="Apache+SQL Server"

$scope.data4 = {};
      $scope.data4.dataTable = new google.visualization.DataTable();
      $scope.data4.dataTable.addColumn("string","Name")
      $scope.data4.dataTable.addColumn("number","Qty")
      $scope.data4.dataTable.addRow(["Apache",43]);
      $scope.data4.dataTable.addRow(["Server",57]);
      //$scope.data4.dataTable.addRow(["Test3",3]);
      $scope.data4.title="Apache Server"

$scope.data5 = {};
      $scope.data5.dataTable = new google.visualization.DataTable();
      $scope.data5.dataTable.addColumn("string","Name")
      $scope.data5.dataTable.addColumn("number","Qty")
      $scope.data5.dataTable.addRow(["Instance 1",1.5]);
      $scope.data5.dataTable.addRow(["Instance 2",4]);
      $scope.data5.dataTable.addRow(["Instance 3",2.3]);
      $scope.data5.title="Instances"
});


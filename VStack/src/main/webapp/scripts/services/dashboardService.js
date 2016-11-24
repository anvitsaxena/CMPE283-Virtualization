'use strict';

angular.module('yapp').factory('APIService',
		['$http','$rootScope','$timeout',function($http, $rootScope, $timeout) {
					var service = {};

					/*service.getProjects = function(callback) {
						$http({
							method : 'GET',
							url : 'getProject.htm'
						}).then(function(response) {
							// success callback
							callback(response);
						}, function(response) {
							// failure callback
						});
					};*/
					
					service.getFlavorList = function(callback) {
						$http({
							method : 'GET',
							url : 'getFlavor.htm'
						}).then(function(response) {
							// success callback
							callback(response);
						}, function(response) {
							alert(response);
						});
					};

					
					service.getInstanceList = function(callback) {
						$http({
							method : 'GET',
							url : 'getInstances.htm'
						}).then(function(response) {
							// success callback
							callback(response);
						}, function(response) {
							alert(response);
						});
					};
					
					service.getImageList = function(callback) {
						$http({
							method : 'GET',
							url : 'getImages.htm'
						}).then(function(response) {
							// success callback
							callback(response);
						}, function(response) {
							alert(response);
						});
					};
				
					
					service.launchInstance = function(instanceName,flavor,image, callback) {
						var instanceDetails = {
								instanceName : instanceName,
								flavor : flavor,
								image : image,
								status : "building"
						};
						
						$http({
							method : 'POST',
							url : 'launchInstance.htm',
							data : instanceDetails
						}).then(function(response) {
							// success callback
							callback(response);
						}, function(response) {
							// failure callback
							alert(response);
						});
					};
					
					service.stopInstance = function(instanceName,flavor,image, callback) {
						var instanceDetails = {
								instanceName : instanceName,
								flavor : flavor,
								image : image,
								status : "building"
						};
						$http({
							method : 'POST',
							url : 'stopInstance.htm',
							data : instanceDetails
						}).then(function(response) {
							// success callback
							callback(response);
						}, function(response) {
							// failure callback
							alert(response);
						});
					};
					
					service.deleteInstance = function(instanceName,flavor,image, callback) {
						var instanceDetails = {
								instanceName : instanceName,
								flavor : flavor,
								image : image,
								status : "building"
						};
						$http({
							method : 'POST',
							url : 'deleteInstance.htm',
							data : instanceDetails
						}).then(function(response) {
							// success callback
							callback(response);
						}, function(response) {
							// failure callback
							alert(response);
						});
					};
					
					service.getInstance = function(instance, callback) {
						
						$http({
							method : 'POST',
							url : 'getInstance.htm',
							data : instance
						}).then(function(response) {
							// success callback
							callback(response);
						}, function(response) {
							alert(response);
						});
					};
					
					
					return service;
				} ]);
'use strict';

/**
 * @ngdoc overview
 * @name yapp
 * @description
 * # yapp
 *
 * Main module of the application.
 */
var states = [
        { name: 'base', state: { abstract: true, url: '', templateUrl: 'views/base.html', data: {text: "Base", visible: false } } },
        { name: 'login', state: { url: '/login', parent: 'base', templateUrl: 'views/login.html', controller: 'LoginCtrl', data: {text: "Login", visible: false } } },
        { name: 'dashboard', state: { url: '/dashboard', parent: 'base', templateUrl: 'views/dashboard.html', controller: 'DashboardCtrl', data: {text: "Dashboard", visible: false } } },
        { name: 'overview', state: { url: '/overview', parent: 'dashboard', templateUrl: 'views/dashboard/overview.html', data: {text: "Dashboard", visible: true } } },
        { name: 'instance', state: { url: '/instance', parent: 'dashboard', templateUrl: 'views/dashboard/instance.html', data: {text: "Services Management", visible: true } } },
        { name: 'reports', state: { url: '/reports', parent: 'base', templateUrl: 'views/dashboard/reports.html', controller: 'ReportsCtrl', data: {text: "Monitoring", visible: true } } },
        { name: 'logout', state: { url: '/login', data: {text: "Logout", visible: true }} }
    ];
  
google.load('visualization', '1', {packages: ['corechart']});
google.setOnLoadCallback(function () {
    angular.bootstrap(document.body, ['yapp']);
});

angular.module('yapp', [
                'ui.router',
                'snap',
                'ngAnimate',
                'google-chart'
            ])
        .config(function($stateProvider, $urlRouterProvider) {
            $urlRouterProvider.when('/dashboard', '/dashboard/overview');
            $urlRouterProvider.otherwise('/login');
            
            angular.forEach(states, function (state) {
                $stateProvider.state(state.name, state.state);
            });
        });

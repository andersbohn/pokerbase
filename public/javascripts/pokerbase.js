'use strict';


function HandListCtrl($scope, $http) {
    $http.get('handhistories/parsed').success(function (data) {
        $scope.handhistories_parsed = data;
    });
}

//HandListCtrl.$inject = ['$scope', '$http'];


function HandDetailCtrl($scope, $routeParams, $http) {

    $scope.handId = $routeParams.handId;

    $http.get('/handhistory/' + $scope.handId + "/parsed").success(function (data) {
        $scope.handhistory_parsed = data;
    });

}


/*
angular.module('phonecat', []).
    config(['$routeProvider', function ($routeProvider) {
        $routeProvider.
            when('/hands', {templateUrl: '/assets/parts/list.html', controller: HandListCtrl}).
            when('/hand/:handId', {templateUrl: '/assets/parts/detail.html', controller: HandDetailCtrl}).
            otherwise({redirectTo: '/hands'});
    }]);

*/

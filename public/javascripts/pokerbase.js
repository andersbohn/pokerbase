/*
 angular.module('project', ['firebase']).
 value('fbURL', 'https://angularjs-projects.firebaseio.com/').
 factory('Projects',function (angularFireCollection, fbURL) {
 return angularFireCollection(fbURL);
 }).
 config(function ($routeProvider) {
 $routeProvider.
 when('/', {controller: ListCtrl, templateUrl: '/assets/parts/list.html'}).
 when('/edit/:projectId', {controller: EditCtrl, templateUrl: '/assets/parts/detail.html'}).
 when('/new', {controller: CreateCtrl, templateUrl: '/assets/parts/detail.html'}).
 otherwise({redirectTo: '/'});
 });

 function ListCtrl($scope, Projects) {
 $scope.projects = Projects;
 }

 function CreateCtrl($scope, $location, $timeout, Projects) {
 $scope.save = function () {
 Projects.add($scope.project, function () {
 $timeout(function () {
 $location.path('/');
 });
 });
 }
 }

 function EditCtrl($scope, $location, $routeParams, angularFire, fbURL) {
 angularFire(fbURL + $routeParams.projectId, $scope, 'remote', {}).
 then(function () {
 $scope.project = angular.copy($scope.remote);
 $scope.project.$id = $routeParams.projectId;
 $scope.isClean = function () {
 return angular.equals($scope.remote, $scope.project);
 }
 $scope.destroy = function () {
 $scope.remote = null;
 $location.path('/');
 };
 $scope.save = function () {
 $scope.remote = angular.copy($scope.project);
 $location.path('/');
 };
 });
 }*/

function HandListCtrl($scope, $http) {
    $http.get('handhistories/parsed').success(function (data) {
        $scope.handhistories_parsed = data;
    });
}

//HandListCtrl.$inject = ['$scope', '$http'];


function HandDetailCtrl($scope, $routeParams, $http) {

    $scope.handId = $routeParams.handId;

    $http.get('/handhistory/' + $scope.handId +"/parsed").success(function(data) {
        $scope.handhistory_parsed = data;
    });

}


angular.module('phonecat', []).
    config(['$routeProvider', function ($routeProvider) {
        $routeProvider.
            when('/hands', {templateUrl: '/assets/parts/list.html',   controller: HandListCtrl}).
            when('/hand/:handId', {templateUrl: '/assets/parts/detail.html', controller: HandDetailCtrl}).
            otherwise({redirectTo: '/hands'});
    }]);
/*function TodoCtrl($scope) {
 $scope.todos = [
 { text: 'learn angular', done: true },
 { text: 'build an angular app', done: false }
 ];

 $scope.addTodo = function () {
 $scope.todos.push({ text: $scope.todoText, done: false });
 $scope.todoText = '';
 };

 $scope.remaining = function () {
 var count = 0;
 angular.forEach($scope.todos, function (todo) {
 count += todo.done ? 0 : 1;
 });
 return count;
 };

 $scope.archive = function () {
 var oldTodos = $scope.todos;
 $scope.todos = [ ];
 angular.forEach(oldTodos, function (todo) {
 if (!todo.done) $scope.todos.push(todo);
 });
 };
 }*/


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
}
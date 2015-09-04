var helloApp = angular.module("helloApp", ['ui.bootstrap']);
helloApp.controller('AllSpendsController', AllSpendsController);

function AllSpendsController($scope, $http, $document, $modal, orderByFilter) {
    $scope.name = 'Lukasz';
    $http.get('./resources/spends').
        success(function(data) {
            $scope.allSpends = data;
        });
    $http.get('./resources/spends/categories').
        success(function(data) {
            $scope.categories = data;
        });
}
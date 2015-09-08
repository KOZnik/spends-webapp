var spendsApp = angular.module("spendsApp", ['ui.bootstrap']);
spendsApp.controller('AllSpendsController', AllSpendsController);

function AllSpendsController($scope, $http, $document, $modal, orderByFilter) {
    $scope.dt = new Date();
    initDatePicker($scope);
    searchSpendsForChosenMonth($scope, $http);
    $scope.monthChanged = function () {
        searchSpendsForChosenMonth($scope, $http)
    };
    $http.get('./resources/spends/categories').
        success(function (data) {
            $scope.categories = data;
        });
}

function searchSpendsForChosenMonth($scope, $http) {
    $http({
        url: './resources/spends',
        method: "GET",
        params: {forYear: $scope.dt.getFullYear(), forMonth: $scope.dt.getMonth() + 1}
    }).success(function (data) {
        $scope.allSpends = data;
    });
}

function initDatePicker($scope) {
    $scope.open = function () {
        $scope.status.opened = true;
    };
    $scope.status = {
        opened: false
    };
    $scope.dateOptions = {
        datepickerMode: "'month'",
        minMode: "'month'",
        initDate: new Date()
    };
}
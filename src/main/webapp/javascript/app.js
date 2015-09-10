var spendsApp = angular.module("spendsApp", ['ui.bootstrap']);
spendsApp.controller('SpendsController', SpendsController);

function SpendsController($scope, SpendsFactory) {
    $scope.dt = new Date();
    initDatePicker($scope);

    $scope.searchSpendsForMonth = function () {
        SpendsFactory.spendsForMonth($scope.dt.getFullYear(), $scope.dt.getMonth() + 1).then(function () {
            $scope.spends = SpendsFactory.spends;
        });
    };
    SpendsFactory.spendsForMonth($scope.dt.getFullYear(), $scope.dt.getMonth() + 1).then(function () {
            $scope.spends = SpendsFactory.spends;
        });
    SpendsFactory.spendsCategories().then(function () {
            $scope.categories = SpendsFactory.categories;
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

spendsApp.factory('SpendsFactory',
    function SpendsFactory ($http) {
        var factory = {};
        factory.spends = [];
        factory.categories = [];

        factory.spendsForMonth = function(year, month) {
            return $http({
                url: './resources/spends',
                method: "GET",
                params: {forYear: year, forMonth: month}
            }).success(function (data) {
                factory.spends = data;
            });
        };

        factory.spendsCategories = function () {
            return $http.get('./resources/spends/categories').
                success(function (data) {
                    factory.categories = data;
                });
        };
        return factory;
    });
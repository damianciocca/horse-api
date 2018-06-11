var app = angular.module('SpaceHorseApp', []);

app.controller('CatalogAdminController', function($scope, $http, Utils) {

    $scope.showList = true;

    $scope.isUnderMaintenance = false;

    $scope.radioData = [];

	$scope.funcUnderMaintenance = function() {

		if (Utils.isUndefinedOrNull($scope.userId) || Utils.isUndefinedOrNull($scope.password)) {
			alert('UserId or Password are null or undefined');
			return;
		}

		var httpRequest = $http({
			method: 'POST',
			url: '/v1/server/underMaintenance?adminLoginId=' + $scope.userId + '&adminPassword=' + $scope.password,
			headers: {
			  'Content-Type': 'application/x-www-form-urlencoded'
			},
			data: ''
		}).then(function successCallback(response) {
			$scope.isUnderMaintenance = true;
			alert('Server is under maintenance now. ');
			return;

			}, function errorCallback(response) {
				alert('Error while setting server under maintenance.');
				return;
			}
		);

	};

		$scope.funcAvailable = function() {

    		if (Utils.isUndefinedOrNull($scope.userId) || Utils.isUndefinedOrNull($scope.password)) {
    			alert('UserId or Password are null or undefined');
    			return;
    		}

    		var httpRequest = $http({
    			method: 'POST',
    			url: '/v1/server/available?adminLoginId=' + $scope.userId + '&adminPassword=' + $scope.password,
    			headers: {
    			  'Content-Type': 'application/x-www-form-urlencoded'
    			},
    			data: ''
    		}).then(function successCallback(response) {
    			$scope.isUnderMaintenance = false;
    			alert('Server is available now. ');
    			return;

    			}, function errorCallback(response) {
    				alert('Error while setting server available.');
    				return;
    			}
    		);

    	};

    $scope.funcListCatalog = function() {

        if (Utils.isUndefinedOrNull($scope.userId) || Utils.isUndefinedOrNull($scope.password)) {
            alert('UserId or Password are null or undefined');
            return;
        }

        var httpRequest = $http({
            method: 'POST',
            url: '/v1/catalog/listLatestCatalogs?adminLoginId=' + $scope.userId + '&adminPassword=' + $scope.password,
            headers: {
              'Content-Type': 'application/x-www-form-urlencoded'
            },
            data: ''
        }).then(function successCallback(response) {
            $scope.radioData = response.data.catalogs;

            var filter = $scope.radioData.filter(function (elem) {
                return elem.active == true;
            });

            if (filter.length != 0) {
                $scope.radioValue = filter[0].id;
            }

            $scope.showList = false;

            }, function errorCallback(response) {
                alert('Error while retrieving latest catalogs: ' + response.statusText);
                return;
            }
        );

    };

    $scope.funcSetActiveCatalog = function() {

        if (Utils.isUndefinedOrNull($scope.userId) || Utils.isUndefinedOrNull($scope.password) || Utils.isUndefinedOrNull($scope.radioValue)) {
            alert('UserId, Password or Catalog selection are null or undefined');
            return;
        }

        var httpRequest = $http({
            method: 'POST',
            url: '/v1/catalog/setActiveCatalogId?adminLoginId=' + $scope.userId + '&adminPassword=' + $scope.password + '&catalogId=' + $scope.radioValue,
            headers: {
              'Content-Type': 'application/x-www-form-urlencoded'
            },
            data: ''
        }).then(function successCallback(response) {
            alert('Active catalog was successfully set: ' + $scope.radioValue);
        }, function errorCallback(response) {
            if (response.data && response.data.error)
	            alert('Error while setting active catalog: ' + response.data.error);
            else
	            alert('Error while setting active catalog: ' + response.statusText);
        });

    };
    
}

);

app.factory('Utils', function() {
    var service = {
        isUndefinedOrNull: function(obj) {
            return !angular.isDefined(obj) || obj===null;
        }
    }
    return service;
});

$(document).keyup(function (e) {
    if ($(".modal-footer").is(":visible") && e.which === 13) {
        document.getElementById("btnList").click();
    }
});

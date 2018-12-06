angular.module('shortURL', [])
.controller('URLController', function($scope, $http) {
    $scope.submitURL = function() {

    	$scope.inDTO = {
    			originalURL: "",
    			shortenedURL:""
    	};
    	$scope.inDTO.originalURL = $scope.origURL;
        $http({
            method: 'POST',
            url: '/shortenURL/',
            data: angular.toJson($scope.inDTO),
            headers: {
                'Content-Type': 'application/json'
            }
        }).then(function(response) {
            $scope.serviceData = response.data;
            $scope.isVisible = true;
            if ($scope.serviceData.shortenedURL.trim().length == 0) {
            	$scope.message = 'Invalid URL';
            } else {
            	$scope.message = 'Shortened URL';
            }
        });
    };
 
});
angular.module('uploader', []).directive('appFilereader', function ($q, $parse, ImageFactory) {
    return {
        restrict: 'A',
        require: '?ngModel',
        link: function (scope, element, attrs) {

            var model = $parse(attrs.appFilereader);
            var modelSetter = model.assign;
            element.bind('change', function () {
                scope.$apply(function () {
                    ImageFactory.postImage(element[0].files[0]).then(function(success) {
                        console.log(modelSetter)
                        console.log(model)
                        modelSetter(scope, success);
                    })
                });
            });

        } //link
    }; //return
}).factory("ImageFactory", function($q, $http) {
    return {
        postImage: function (image) {
            var fd = new FormData();
            var deferred = $q.defer();
            fd.append('picture', image);
            $http.post('/upload', fd, {
                transformRequest: angular.identity,
                headers: {'Content-Type': undefined}
            }).success(function (success) {
                console.log(success)
                deferred.resolve(success)
            }).error(function (error) {
                console.log(error)
            });
            return deferred.promise
        }
    }
});
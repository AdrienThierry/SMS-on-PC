/**
 * Main angular file
 */

var app = angular.module('SMS_on_PC', []);

// --------------------------------------------------
// Expose socket.io functions
// --------------------------------------------------
app.factory('socket', function ($rootScope) {
	var socket = io();
	return {
		on: function (eventName, callback) {
			socket.on(eventName, function () {  
				var args = arguments;
				$rootScope.$apply(function () {
					callback.apply(socket, args);
				});
			});
    	},
		emit: function (eventName, data, callback) {
			socket.emit(eventName, data, function () {
				var args = arguments;
				$rootScope.$apply(function () {
					if (callback) {
						callback.apply(socket, args);
					}
				});
			})
		}
	};
});


// --------------------------------------------------
// Expose conf
// --------------------------------------------------
app.factory('configParser', function(constants, $q, $http) {
	var configParser = {};
	var conf;

	configParser.getConf = function() {
		var defer = $q.defer()

		if (conf == undefined) {
			// Get conf from server with a GET request
			$http.get(constants.shared_conf_name)
			.success(function(data) {
				defer.resolve(data);
			})
			.error(function(error) {
				console.log(error);
			});
		}

		else {
			return defer.resolve(conf);
		}

		return defer.promise;
	}

	return configParser;
});

// --------------------------------------------------
// Main controller
// --------------------------------------------------
angular.module('SMS_on_PC').controller("mainController", function($scope, configParser) {

	// Error message not visible
	$scope.errorVisible = false;
	$scope.errorMsg = "";

});

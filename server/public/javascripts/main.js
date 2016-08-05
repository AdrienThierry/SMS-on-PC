/**
 * Main angular file
 */

var app = angular.module('SMS_on_PC', []);

// --------------------------------------------------
// Expose socket.io socket
// --------------------------------------------------
app.factory('socketIO', function() {
	var socketIO = {};	
	var socket;

	socketIO.getSocket = function() {
		if (socket == undefined) {
			socket = io();
		}
		return socket;
	}

    return socketIO;
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
angular.module('SMS_on_PC').controller("mainController", function($scope, configParser, socketIO) {

	// Error message not visible
	$scope.errorVisible = false;
	$scope.errorMsg = "";

});

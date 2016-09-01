/**
 * Main angular file
 */

var app = angular.module('SMS_on_PC', ['pascalprecht.translate', 'ngSanitize']);

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
// Service to share properties between controllers
// --------------------------------------------------
app.service('sharedProperties', function () {
	var show_discovery = true;
	var show_SMS_screen = false;
	var phone_name = "";

	return {
		get_show_discovery: function () {
			return show_discovery;
		},
		set_show_discovery: function(value) {
			show_discovery = value;
		},
		get_show_SMS_screen: function () {
			return show_SMS_screen;
		},
		set_show_SMS_screen: function(value) {
			show_SMS_screen = value;
		},
		get_phone_name: function() {
			return phone_name;
		},
		set_phone_name: function(value) {
			phone_name = value;
		}
	};
});

// --------------------------------------------------
// Main controller
// --------------------------------------------------
angular.module('SMS_on_PC').controller("mainController", function($scope, constants, configParser, sharedProperties) {

	// Error message not visible
	$scope.errorVisible = false;
	$scope.errorMsg = "";

	sharedProperties.set_phone_name(localStorage.getItem(constants.phone_name_var_name));

});

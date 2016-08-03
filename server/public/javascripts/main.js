var app = angular.module('SMS_on_PC', []);

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

angular.module('SMS_on_PC').controller("mainController", function($scope, socketIO) {

	// --------------------------------------------------
	// INIT
	// --------------------------------------------------
	// Error message not visible
	$scope.errorVisible = false;
	$scope.errorMsg = "";

	console.log(socketIO.getSocket());

});

/**
 * Angular controller that handle the socket.io connection and
 * the display of the available phones list
 */

angular.module('SMS_on_PC').controller("discoveryController", function(constants, configParser, socketIO) {
	configParser.getConf().then(function(data) {
		var config = data;

		var io = socketIO.getSocket();

		// On ask device ID
		io.on(config.ask_device_id, function(data, callback) {
			// Check if device ID exists in localStorage
			if (localStorage.getItem(constants.device_id_var_name) == null) {
				// Ask server for device ID
				io.emit(config.ask_new_device_id, {}, function(response) {
					localStorage.setItem(constants.device_id_var_name, response);
					callback(localStorage.getItem(constants.device_id_var_name));
				});
			}
			else {
				callback(localStorage.getItem(constants.device_id_var_name));
			}
		});
	});	
});

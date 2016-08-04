angular.module('SMS_on_PC').controller("discoveryController", function(localStorageService, constants, configParser, socketIO) {
	configParser.getConf().then(function(data) {
		var config = data;

		var io = socketIO.getSocket();

		// On ask device ID
		io.on(config.ask_device_id, function(data, callback) {
			// Check if device ID exists in localStorage
			if (localStorageService.get(constants.device_id_var_name) == null) {
				console.log("Yolo");
				console.log(config.ask_new_device_id);
				// Ask server for device ID
				io.emit(config.ask_new_device_id, {}, function(response) {
					localStorageService.set(constants.device_id_var_name, response);
					callback(localStorageService.get(constants.device_id_var_name));
				});
			}
			else {
				callback(localStorageService.get(constants.device_id_var_name));
			}
			
		});
	});	
});

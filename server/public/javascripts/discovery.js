/**
 * Angular controller that handle the socket.io connection and
 * the display of the available phones list
 */

angular.module('SMS_on_PC').controller("discoveryController", function(constants, configParser, socket, sharedProperties) {
	var c = this;	

	c.show = sharedProperties.get_show_discovery;

	configParser.getConf().then(function(data) {

		var config = data;

		// --------------------------------------------------
		// Send device ID
		// --------------------------------------------------
		// Check if device ID exists in localStorage
		if (localStorage.getItem(constants.device_id_var_name) == null) {
			// Ask server for device ID
			socket.emit(config.EVENT_ask_new_device_id, {}, function(response) {

				// Put a '1' before deviceID to indicate that the client is a browser
				var newID = parseInt(response) != 0 ? Math.pow(10, Math.floor(Math.log10(parseInt(response))) + 1) + parseInt(response) : 10;
				localStorage.setItem(constants.device_id_var_name, newID);
				socket.emit(config.EVENT_device_id, {device_id: localStorage.getItem(constants.device_id_var_name)});
			});
		}
		else {
			socket.emit(config.EVENT_device_id, {device_id: localStorage.getItem(constants.device_id_var_name)});
		}
		

		// --------------------------------------------------
		// On phones discovered
		// --------------------------------------------------
		socket.on(config.EVENT_discovered_phones, function(data) {
			c.discovered_phones = data;
		});

		// --------------------------------------------------
		// Send selected phone to server
		// --------------------------------------------------
		c.select_phone = function(index) {
			socket.emit(config.EVENT_select_phone, {browser_id: localStorage.getItem(constants.device_id_var_name), phone_index: index});
			sharedProperties.set_phone_name(c.discovered_phones[index].name);
		};

		// --------------------------------------------------
		// On selected phone connected
		// --------------------------------------------------
		socket.on(config.EVENT_phone_connected, function(data) {
			sharedProperties.set_show_discovery(false);
			sharedProperties.set_show_SMS_screen(true);
		});

	});	
});

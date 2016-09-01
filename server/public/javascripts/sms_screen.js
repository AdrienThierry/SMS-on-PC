/**
 * Angular controller that handle the main screen
 */

angular.module('SMS_on_PC').controller("SMSScreenController", function(constants, configParser, socket, sharedProperties) {

	var c = this;

	c.show = sharedProperties.get_show_SMS_screen;
	c.phone_name = sharedProperties.get_phone_name;

	socket.emit("yolo", {});

	configParser.getConf().then(function(data) {

		var config = data;

		// --------------------------------------------------
		// On contact list received
		// --------------------------------------------------
		socket.on(config.EVENT_send_contact_list, function(data) {
			c.contacts = [];

			Object.keys(data).forEach(function(key) {
				c.contacts.push(data[key]);
				c.contacts.sort();
			});
		});

		// --------------------------------------------------
		// Disconnect
		// --------------------------------------------------
		c.disconnect = function() {
			var device_id = localStorage.getItem(constants.device_id_var_name);
			socket.emit(config.EVENT_disconnect, {device_id: device_id});

			sharedProperties.set_show_discovery(true);
			sharedProperties.set_show_SMS_screen(false);
		};

	});

});

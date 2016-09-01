/**
 * Angular controller that handle the main screen
 */

angular.module('SMS_on_PC').controller("SMSScreenController", function(constants, configParser, socket, sharedProperties) {

	var c = this;

	c.show = sharedProperties.get_show_SMS_screen;
	c.phone_name = sharedProperties.get_phone_name;

	var device_id = localStorage.getItem(constants.device_id_var_name);

	c.active_contact = 0;

	configParser.getConf().then(function(data) {

		var config = data;

		// --------------------------------------------------
		// On contact list received
		// --------------------------------------------------
		socket.on(config.EVENT_send_contact_list, function(data) {

			c.contact_names = []; // Sorted contact names
			c.contacts = []; // Sorted contacts

			for (var id in data) {
      			c.contacts.push([id, data[id]]);
			}
			c.contacts.sort(function(a, b) {
					return a[1] < b[1] ? -1 : a[1] > b[1];
			});

			for (var i = 0 ; i < c.contacts.length ; i++) {
				c.contact_names.push(c.contacts[i][1]);
			}

			c.set_active_contact(0); // Ask for sms
		});

		// --------------------------------------------------
		// Set active contact and get sms from it
		// --------------------------------------------------
		c.set_active_contact = function(index) {
			c.active_contact = index;

			var json = {device_id: device_id, contact_id: c.contacts[index][0]}
			console.log(json);

			socket.emit(config.EVENT_ask_SMS, json);
		};

		// --------------------------------------------------
		// Disconnect
		// --------------------------------------------------
		c.disconnect = function() {
			socket.emit(config.EVENT_disconnect, {device_id: device_id});

			sharedProperties.set_show_discovery(true);
			sharedProperties.set_show_SMS_screen(false);
		};

	});

});

/**
 * Angular controller that handle the main screen
 */

angular.module('SMS_on_PC').controller("SMSScreenController", function(constants, configParser, socket, sharedProperties) {

	var c = this;

	c.show = sharedProperties.get_show_SMS_screen;
	c.phone_name = sharedProperties.get_phone_name;

	var device_id = localStorage.getItem(constants.device_id_var_name);

	c.active_address = 0;

	configParser.getConf().then(function(data) {

		var config = data;

//		// --------------------------------------------------
//		// On contact list received
//		// --------------------------------------------------
//		socket.on(config.EVENT_send_contact_list, function(data) {

//			c.contact_names = []; // Sorted contact names
//			c.contacts = []; // Sorted contacts

//			for (var id in data) {
//      			c.contacts.push([id, data[id]]);
//			}
//			c.contacts.sort(function(a, b) {
//					return a[1] < b[1] ? -1 : a[1] > b[1];
//			});

//			for (var i = 0 ; i < c.contacts.length ; i++) {
//				c.contact_names.push(c.contacts[i][1]);
//			}

//			c.set_active_contact(0); // Ask for sms
//		});

		// --------------------------------------------------
		// On address list received
		// --------------------------------------------------
		socket.on(config.EVENT_send_address_list, function(data) {

			c.addresses_names = [];
			c.addresses = [];

			for (var id in data.addresses) {
				var currentAddress = data.addresses[id];

				// Look for name associated with address
				if (data.names[currentAddress] != undefined) {
					c.addresses_names.push(data.names[currentAddress]);
				}
				else {
      				c.addresses_names.push(currentAddress);
				}
				c.addresses.push(currentAddress);
			}

			c.set_active_address(0); // Ask for sms

		});

		// --------------------------------------------------
		// On SMS list received
		// --------------------------------------------------
		socket.on(config.EVENT_send_SMS_list, function(data) {
			c.sms_list = data;
		});

		// --------------------------------------------------
		// Set active address and get sms from it
		// --------------------------------------------------
		c.set_active_address = function(index) {
			c.active_address = index;

			var json = {device_id: device_id, address: c.addresses[index]};

			socket.emit(config.EVENT_ask_SMS_list, json);
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

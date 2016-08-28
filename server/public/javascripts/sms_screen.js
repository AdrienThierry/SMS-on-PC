/**
 * Angular controller that handle the main screen
 */

angular.module('SMS_on_PC').controller("SMSScreenController", function(constants, configParser, socket, sharedProperties) {

	var c = this;	

	c.show = sharedProperties.get_show_SMS_screen;
	c.phone_name = sharedProperties.get_phone_name;

	configParser.getConf().then(function(data) {

		var config = data;

		socket.on(config.EVENT_send_contact_list, function(data) {
			c.contacts = [];

			Object.keys(data).forEach(function(key) {
				c.contacts.push(data[key]);
				c.contacts.sort();
			});

			console.log(c.contacts);
		});

	});

});

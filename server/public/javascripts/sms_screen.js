/**
 * Angular controller that handle the main screen
 */

angular.module('SMS_on_PC').controller("SMSScreenController", function(constants, configParser, socket, sharedProperties) {

	var c = this;	

	c.show = sharedProperties.get_show_SMS_screen;
	c.phone_name = sharedProperties.get_phone_name;

	configParser.getConf().then(function(data) {

	});

});

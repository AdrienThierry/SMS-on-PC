/**
 * Handles events sent by phone
 */

var config = require('./config_parser.js');
var associations = require('./associations.js');
var connection_handling = require('./connection_handling.js');

function apply_listeners(socket) {

	var sockets = connection_handling.sockets;

	// --------------------------------------------------
	// On address list received
	// --------------------------------------------------
	socket.on(config.EVENT_send_address_list, function(data) {

		// Send address list to associated browsers
		var browser_ids = associations.get_browsers(data.device_id);

		for (var i = 0 ; i < browser_ids.length ; i++) {
			var json = {addresses: data.addresses, names: data.names};
			sockets.browsers[browser_ids[i]].emit(config.EVENT_send_address_list, json);
		}
		
	});

	// --------------------------------------------------
	// On SMS list received
	// --------------------------------------------------
	socket.on(config.EVENT_send_SMS_list, function(data) {
		// Send sms to browser which asked for it
		sockets.browsers[data.browser_id].emit(config.EVENT_send_SMS_list, JSON.parse(data.sms_list));
	});

}

module.exports.apply_listeners = apply_listeners;

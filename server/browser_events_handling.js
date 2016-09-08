/**
 * Handles events sent by browser
 */

var config = require('./config_parser.js');
var associations = require('./associations.js');
var connection_handling = require('./connection_handling.js');

function apply_listeners(socket) {

	// --------------------------------------------------
	// On address list asked
	// --------------------------------------------------
	socket.on(config.EVENT_ask_address_list, function(data) {
		
		// Send request to associated phones
		var sockets = connection_handling.sockets;
		var phone_ids = associations.get_phones(data.device_id);

		for (var i = 0 ; i < phone_ids.length ; i++) {
			sockets.phones[phone_ids[i]].emit(config.EVENT_ask_address_list, {});
		}
		
	});

	// --------------------------------------------------
	// On SMS asked
	// --------------------------------------------------
	socket.on(config.EVENT_ask_SMS_list, function(data) {

		// Send request to associated phones
		var sockets = connection_handling.sockets;
		var phone_ids = associations.get_phones(data.device_id);

		for (var i = 0 ; i < phone_ids.length ; i++) {
			var json = {browser_id: data.device_id, address: data.address}
			sockets.phones[phone_ids[i]].emit(config.EVENT_ask_SMS_list, json);
		}
		
	});

}

module.exports.apply_listeners = apply_listeners;

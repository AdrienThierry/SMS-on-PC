/**
 * Handles events sent by browser and phone
 */

var config = require('./config_parser.js');
var associations = require('./associations.js');
var connection_handling = require('./connection_handling.js');
var discovery = require('./discovery.js');

function apply_listeners(socket) {

	// --------------------------------------------------
	// On device disconnect
	// --------------------------------------------------
	socket.on(config.EVENT_disconnect, function(data) {

		var sockets = connection_handling.sockets;

		// Check if device is phone or browser
		var device_type = data.device_id[0];
		
		if (device_type == config.device_type.browser) {
			associations.remove_browser(data.device_id);
			// Send list of discovered phones to browser
			sockets.browsers[data.device_id].emit(config.EVENT_discovered_phones, discovery.discovered_phones);
		}
		else if (device_type == config.device_type.android) {
			associations.remove_phone(data.device_id);
		}
		
	});

}

module.exports.apply_listeners = apply_listeners;

/**
 * Handles events sent by phone
 */

var config = require('./config_parser.js');
var associations = require('./associations.js');
var connection_handling = require('./connection_handling.js');

function apply_listeners(socket) {

	// Contact list received
	socket.on(config.EVENT_send_contact_list, function(data) {
		
		// Send contact list to associated browsers
		var sockets = connection_handling.sockets;
		var browser_ids = associations.get_browsers(data.device_id);

		for (var i = 0 ; i < browser_ids.length ; i++) {
			sockets.browsers[browser_ids[i]].emit(config.EVENT_send_contact_list, data.contacts);
		}
		
	});

}

module.exports.apply_listeners = apply_listeners;

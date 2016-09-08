/**
 * Handles socket.io connection with server and device ID exchange
 */

var config = require('./config_parser.js');
var id_generator = require('./id_generator.js');
var discovery = require('./discovery.js');
var discovered_phones = discovery.discovered_phones;
var phone_selection = require('./phone_selection.js');
var phone_events_handling = require('./phone_events_handling.js');
var browser_events_handling = require('./browser_events_handling.js');
var common_events_handling = require('./common_events_handling.js');
var associations = require('./associations.js');

var sockets = {};
sockets.browsers = {};
sockets.phones = {};

function start_handler(io) {

	// --------------------------------------------------
	// On connection
	// --------------------------------------------------
	io.on('connection', function(socket) {
		console.log("SOCKET_IO Connection");

		// -------------------------
		// On device ID sent
		// -------------------------
		socket.on(config.EVENT_device_id, function(data) {
			console.log(data);

			var device_id = data.device_id.toString();
			var second_party_id = data.second_party_id;
			
			// Check device type and join corresponding room and socket array
			var device_type = device_id[0];
			if (device_type == config.device_type.browser) {
				socket.join(config.device_type.browser); // Join browser room
				sockets.browsers[device_id] = socket;

				// If browser already exists
				if (browser_already_exists(device_id)) {
					socket.emit(config.EVENT_already_exist, {});
				}
				else {
					// Send list of discovered phones to new browser
					socket.emit(config.EVENT_discovered_phones, discovered_phones);
				}
			}
			else if (device_type == config.device_type.android) {
				socket.join(config.device_type.android); // Join android room
				sockets.phones[device_id] = socket;
			}

			// If a phone sent that, add association between phone and browser
			if (second_party_id != undefined) {
				associations.add_association(second_party_id, device_id);
				associations.print_phones();
				associations.print_browsers();

				// Send event to browser on which phone has been selected to tell it it can go to the next screen
				sockets.browsers[second_party_id].emit(config.EVENT_phone_connected, {});

				// Ask for address list
				socket.emit(config.EVENT_ask_address_list, {});
				
			}

			console.log("BROWSERS : " + Object.keys(sockets.browsers).length);
			console.log("PHONES : " + Object.keys(sockets.phones).length);
			console.log("");

		});

		// -------------------------
		// On ask new device id
		// -------------------------
		socket.on(config.EVENT_ask_new_device_id, function(data, callback) {
			callback(id_generator.get_new_id());
		});

		// -------------------------
		// On disconnect, remove socket
		// from array
		// -------------------------
		socket.on('disconnect', function() {
			Object.keys(sockets).forEach(function(type) {
				Object.keys(sockets[type]).forEach(function(id) {
					if (sockets[type][id].id == socket.id) { // Socket found

						// Remove socket
						delete sockets[type][id];
					}
				});	
			});

			console.log("BROWSERS : " + Object.keys(sockets.browsers).length);
			console.log("PHONES : " + Object.keys(sockets.phones).length);
			console.log("");

			console.log("Associations after disconnect :");
			associations.print_phones();
			associations.print_browsers();
			
			
		});

		// -------------------------
		// Apply listeners on new
		// socket
		// -------------------------
		phone_selection.apply_listeners(socket);
		phone_events_handling.apply_listeners(socket);
		browser_events_handling.apply_listeners(socket);
		common_events_handling.apply_listeners(socket);	
	
	});

}

// --------------------------------------------------
// Function to check if a browser already exists
// --------------------------------------------------
function browser_already_exists(device_id) {
	if (associations.get_phones(device_id) != undefined) {
		return true;
	}
	else {
		return false;
	}
}

module.exports.start_handler = start_handler;
module.exports.sockets = sockets;

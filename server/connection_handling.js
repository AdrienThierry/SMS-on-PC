/**
 * Handles socket.io connection with server and device ID exchange
 */

var config = require('./config_parser.js');
var id_generator = require('./id_generator.js');
var discovery = require('./discovery.js');
var discovered_phones = discovery.discovered_phones;
var phone_selection = require('./phone_selection.js');

var sockets = {};

function start_handler(io) {

	// --------------------------------------------------
	// On connection
	// --------------------------------------------------
	io.on('connection', function(socket) {
		console.log("Connection");

		// -------------------------
		// On device ID sent
		// -------------------------
		socket.on(config.EVENT_device_id, function(data) {
			console.log(data);

			// Add socket to sockets array
			sockets[data.toString()] = socket;
			
			// Check device type and join corresponding room		
			var device_type = data.toString()[0];
			if (device_type == config.device_type.browser) {
				socket.join(config.device_type.browser); // Join browser room
				// Send list of discovered phones to new browser
				socket.emit(config.EVENT_discovered_phones, discovered_phones);
			}
			else if (device_type == config.device_type.android) {
				socket.join(config.device_type.android); // Join android room
			}
			
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
			Object.keys(sockets).forEach(function(key) {
				if (sockets[key].id == socket.id) { // Socket found
					delete sockets[key];
				}	
			});
		});

		// -------------------------
		// Apply listeners on new
		// socket
		// -------------------------
		phone_selection.apply_listeners(socket);
		
	
	});

}

module.exports.start_handler = start_handler;
module.exports.sockets = sockets;

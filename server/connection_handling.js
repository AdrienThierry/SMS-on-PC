/**
 * Handles socket.io connection with server and device ID exchange
 */

var config = require('./config_parser.js');
var id_generator = require('./id_generator.js');

var sockets = {};

function start_handler(io) {

	// --------------------------------------------------
	// On connection
	// --------------------------------------------------
	io.on('connection', function(socket) {
		// -------------------------
		// Ask device ID
		// -------------------------
		socket.emit(config.ask_device_id, {}, function(response) {
			// Add socket to sockets array
			sockets[response.toString()] = socket;
			
			// Check device type and join corresponding room		
			var device_type = response.toString()[0];
			if (device_type == config.device_type.browser) {
				socket.join(config.device_type.browser); // Join browser room
			}
			else if (device_type == config.device_type.android) {
				socket.join(config.device_type.android); // Join android room
			}
		});

		// -------------------------
		// On ask new device id
		// -------------------------
		socket.on(config.ask_new_device_id, function(data, callback) {
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
	});

}

module.exports.start_handler = start_handler;
module.exports.sockets = sockets;

/**
 * Handles socket.io connection with server and device ID exchange
 */

var config = require('./config_parser.js');
var id_generator = require('./id_generator.js');

var sockets = {};
sockets.browsers = [];
sockets.androids = [];

function start_handler(io) {

	// On connection
	io.on('connection', function(socket) {
		// Ask device id
		socket.emit(config.ask_device_id, {}, function(response) {
			var device_type = response.toString()[0];

			// Check device type and place socket in corresponding array
			if (device_type == config.device_type.browser) {
				sockets.browsers.push(socket);
				console.log(sockets.browsers.length);
			}
			else if (device_type == config.device_type.android) {
				sockets.androids.push(socket);
			}
		});

		// On ask new device id
		socket.on(config.ask_new_device_id, function(data, callback) {
			callback(id_generator.get_new_id());
		});

		// On disconnect, remove socket from array
		socket.on('disconnect', function() {
			Object.keys(sockets).forEach(function(key) {
				for (var i = 0 ; i < sockets[key].length ; i++) {
					if (sockets[key][i].id == socket.id) { // Socket found
						sockets[key].splice(i, 1); // Remove element
					}
				}
			});
		});
	});

}

module.exports.start_handler = start_handler;
module.exports.sockets = sockets;

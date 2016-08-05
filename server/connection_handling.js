/**
 * Handles socket.io connection with server and device ID exchange
 */

var config = require('./config_parser.js');
var id_generator = require('./id_generator.js');

function connection_handling(io) {

	// On connection
	io.on('connection', function(socket) {
		// Ask device id
		socket.emit(config.ask_device_id, {}, function(response) {
					
		});

		// On ask new device id
		socket.on(config.ask_new_device_id, function(data, callback) {
			callback(id_generator.get_new_id());
		});
	});

}

module.exports = connection_handling;

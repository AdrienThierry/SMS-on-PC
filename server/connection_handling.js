/**
 * Handles socket.io connection with server and device ID exchange
 */

var config = require('./config_parser.js');
var id_generator = require('./id_generator.js');
var discovery = require('./discovery.js');
var discovered_phones = discovery.discovered_phones;
var phone_selection = require('./phone_selection.js');
var associations = require('./associations.js');

var sockets = {};
sockets.browsers = [];
sockets.phones = [];

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
				sockets.browsers.push({device_id: device_id, socket: socket});
				// Send list of discovered phones to new browser
				socket.emit(config.EVENT_discovered_phones, discovered_phones);
			}
			else if (device_type == config.device_type.android) {
				socket.join(config.device_type.android); // Join android room
				sockets.phones.push({device_id: device_id, socket: socket});
			}

			// If a phone sent that, add association between phone and browser
			if (second_party_id != undefined) {
				associations.add_association(second_party_id, device_id);
				associations.print_phones();
				associations.print_browsers();
			}

			console.log("BROWSERS : " + sockets.browsers.length);
			console.log("PHONES : " + sockets.phones.length);
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
			Object.keys(sockets).forEach(function(key) {
				for (var i = 0 ; i < sockets[key].length ; i++) {
					if (sockets[key][i].socket.id == socket.id) { // Socket found

						// Remove associations
						if (key == "phones") {
							associations.remove_phone(sockets[key][i].device_id);
						}
						else if (key == "browsers") {
							associations.remove_browser(sockets[key][i].device_id);
						}

						// Remove socket
						sockets[key].splice(i,1);
					}
				}	
			});

			console.log("BROWSERS : " + sockets.browsers.length);
			console.log("PHONES : " + sockets.phones.length);
			console.log("");

			console.log("New associations :");
			associations.print_phones();
			associations.print_browsers();
			
			
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

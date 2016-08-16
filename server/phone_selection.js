/**
 * Phone selection management
 * Connection with the phone selected by the user
 */

var config = require('./config_parser.js');
var discovery = require('./discovery.js');
var utility = require('./utility.js');
var phones = discovery.discovered_phones;

function apply_listeners(socket) {

	socket.on(config.select_phone, function(data) {

		// --------------------------------------------------
		// Send server IP to phone via classic socket
		// --------------------------------------------------
		var s = require('net').Socket();
		var address = phones[parseInt(data)].info.address;
		var port = phones[parseInt(data)].info.port;
		console.log(port);

		s.on('error', function(d){
			console.log(d.toString());
		});

		s.connect(parseInt(port), address);

		s.write('http://' + utility.get_ip() + ':' + config.server_port, 'UTF-8');
		s.end();

	});

}

module.exports.apply_listeners = apply_listeners;

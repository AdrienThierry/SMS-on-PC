/**
 * Phone selection management
 * Connection with the phone selected by the user
 */

var config = require('./config_parser.js');
var discovery = require('./discovery.js');
var utility = require('./utility.js');
var phones = discovery.discovered_phones;

function apply_listeners(socket) {

	socket.on(config.EVENT_select_phone, function(data) {

		var browser_id = data.browser_id;
		var phone_index = data.phone_index;

		// --------------------------------------------------
		// Send server IP to phone via classic socket
		// --------------------------------------------------
		var s = require('net').Socket();
		var address = phones[phone_index].info.address;
		var port = phones[phone_index].info.port;

		s.on('error', function(d){
			console.log(d.toString());
		});

		s.connect(parseInt(port), address);

		s.write('' + browser_id + '-' + 'http://' + utility.get_ip() + ':' + config.server_port, 'UTF-8');
		s.end();

	});

}

module.exports.apply_listeners = apply_listeners;

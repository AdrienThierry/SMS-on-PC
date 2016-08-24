/**
 * Phone discovery management using bonjour protocol
 */

var config = require('./config_parser.js');
var bonjour = require('bonjour')();
var sockets = require('./connection_handling.js').sockets;
var constants = require('./constants.js');

var discovered_phones = [];

function start_discovery(io) {

	// browse for all http services 
	var browser = bonjour.find({ type: 'http' }, function (service) {
		
	});

	// Send mdns query every mdns_query_period ms
	setInterval(function() {
		browser.update();
	}, constants.mdns_query_period);  

	browser.on('up', function(service) {
		if (service.name.indexOf(config.nsd_service_name) != -1) { // Service name contains expected name
			var phone_name = service.name.substring(service.name.indexOf("/") + 1);

			discovered_phones.push({name: phone_name, info: {address: service.referer.address, port: service.port}});
			console.log("Services up");
			console.log(discovered_phones);

			// Send list of discovered phones to browsers
			send_phone_list(io);
		}
	});

	browser.on('down', function(service) {
		console.log("Service down " + service.name);

		// Delete phone in array
		for (var i = 0 ; i < discovered_phones.length ; i++) {
			if (discovered_phones[i].info.address == service.referer.address && discovered_phones[i].info.port == service.port) {
				
				discovered_phones.splice(i,1);
			}

			// Send list of discovered phones to browsers
			send_phone_list(io);
		}
	});
}

function send_phone_list(io) {
	io.sockets.in(config.device_type.browser).emit(config.discovered_phones, discovered_phones);
}

module.exports.start_discovery = start_discovery;
module.exports.discovered_phones = discovered_phones;

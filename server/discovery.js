/**
 * Phone discovery management using bonjour protocol
 */

var config = require('./config_parser.js');
var bonjour = require('bonjour')();
var sockets = require('./connection_handling.js').sockets;

var discovered_phones = [];

function start_discovery(io) {

	// browse for all http services 
	var browser = bonjour.find({ type: 'http' }, function (service) {
		
			
			// Send new service event to all browsers
			//io.sockets.in(config.device_type.browser).emit('yolo', 'yolo');
	});

	browser.on('up', function(service) {
		if (service.name.indexOf(config.nsd_service_name) != -1) { // Service name contains expected name
			var phone_name = service.name.substring(service.name.indexOf("/") + 1);
			discovered_phones.push({name: phone_name, referer: service.referer});
			console.log(discovered_phones);
		}
	});

	browser.on('down', function(service) {
		console.log("Service down " + service.name);
	});
}

module.exports.start_discovery = start_discovery;
module.exports.discovered_phones = discovered_phones;

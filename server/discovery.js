/**
 * Phone discovery management using bonjour protocol
 */

var config = require('./config_parser.js');
var bonjour = require('bonjour')();
var sockets = require('./connection_handling.js').sockets;

function discovery(io) {

	// browse for all http services 
	var browser = bonjour.find({ type: 'http' }, function (service) {
		if (service.name.indexOf(config.nsd_service_name) != -1) { // Service name contains expected name
			console.log(service);
			
			// Send new service event to all browsers
			io.sockets.in(config.device_type.browser).emit('yolo', 'yolo');
		}
	});

	browser.on('up', function(service) {
		//console.log("Service up " + service.name);
	});

	browser.on('down', function(service) {
		//console.log("Service down " + service.name);
	});
}

module.exports = discovery;

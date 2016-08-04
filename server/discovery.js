var config = require('./config_parser.js');
var bonjour = require('bonjour')();

function discovery(io) {

	// browse for all http services 
	var browser = bonjour.find({ type: 'http' }, function (service) {
		if (service.name.indexOf(config.nsd_service_name) != -1) { // Service name contains expected name
			console.log(service);
		}
	});

	browser.on('up', function(service) {
		console.log("Service up " + service.name);
	});

	browser.on('down', function(service) {
		console.log("Service down " + service.name);
	});
}

module.exports = discovery;

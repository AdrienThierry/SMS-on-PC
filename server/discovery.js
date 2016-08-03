var config = require('./config_parser.js');
var bonjour = require('bonjour')();

function discovery(io) {

	// browse for all http services 
	bonjour.find({ type: 'http' }, function (service) {
		if (service.name.indexOf(config.nsd_service_name) != -1) { // Service name contains expected name
		console.log(service);
		}
	});
}

module.exports = discovery;

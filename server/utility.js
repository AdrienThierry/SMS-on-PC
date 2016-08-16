/**
 * Utility functions
 */

var os = require('os');

function get_ip() {
	var networkInterfaces = os.networkInterfaces();
	for (key in networkInterfaces) {
		// For each interface, two addresses are present : ipv4 and ipv6
		// We only consider ipv4, hence the [0]
		if (networkInterfaces[key][0].internal == false) { // Return first external ip
			return networkInterfaces[key][0].address;
		}
	}
}

module.exports.get_ip = get_ip;

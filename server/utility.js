/**
 * Utility functions
 */

var os = require('os');

// --------------------------------------------------
// Get server ip on local network
// --------------------------------------------------
function get_ip() {
	var networkInterfaces = os.networkInterfaces();
	for (key in networkInterfaces) {
		// For each interface, two addresses are present : ipv4 and ipv6
		// We only keep ipv4 => [0]
		if (networkInterfaces[key][0].internal == false) { // Return first external ip
			return networkInterfaces[key][0].address;
		}
	}
}

module.exports.get_ip = get_ip;

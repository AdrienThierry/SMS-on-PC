/**
 * Unique device ID generator
 */

var fs = require('fs');
var constants = require("./constants.js");

// Check if last_id file exists
function get_new_id() {
	try {
		fs.accessSync(constants.last_id_file_name, fs.R_OK | fs.W_OK);
		//code to action if file exists
		var buf = fs.readFileSync(constants.last_id_file_name);
		var last_id = parseInt(buf);
		var new_id = last_id + 1;
		fs.writeFileSync(constants.last_id_file_name, new_id.toString());

		return new_id;

	} catch(e) {
		//code to action if file does not exist
		fs.writeFileSync(constants.last_id_file_name, "0");

		return 0;
	}
}

module.exports.get_new_id = get_new_id;



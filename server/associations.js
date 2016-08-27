/**
 * Keep associations between phones and browsers
 **/

var phones = {};
var browsers = {};

// --------------------------------------------------
// Add an association between a browser and a phone
// (on phone connection)
// --------------------------------------------------
function add_association(browser_id, phone_id) {
	var browser = browser_id.toString();
	var phone = phone_id.toString();


	if (phones[browser] == undefined) {
		phones[browser] = [];
	}

	if (browsers[phone] == undefined) {
		browsers[phone] = [];
	}

	phones[browser].push(phone);
	browsers[phone].push(browser);
}

// --------------------------------------------------
// Remove a phone and its associations
// (on phone disconnect)
// --------------------------------------------------
function remove_phone(phone_id) {
	var phone = phone_id.toString();

	delete browsers[phone];

	for (browser in phones) {
		for (var i = 0 ; i < phones[browser].length ; i++) {
			if (phones[browser][i] == phone) {
				phones[browser].splice(i,1);
			}
		}
	}
}

// --------------------------------------------------
// Remove a browser and its associations
// (on browser disconnect)
// --------------------------------------------------
function remove_browser(browser_id) {
	var browser = browser_id.toString();

	delete phones[browser];

	for (phone in browsers) {
		for (var i = 0 ; i < browsers[phone].length ; i++) {
			if (browsers[phone][i] == browser) {
				browsers[phone].splice(i,1);
			}
		}
	}
}

// --------------------------------------------------
// Get phones associated with a given browser
// --------------------------------------------------
function get_phones(browser_id) {
	return phones[browser_id];
}

// --------------------------------------------------
// Get browsers associated with a given phone
// --------------------------------------------------
function get_browsers(phone_id) {
	return browsers[phone_id];
}

// --------------------------------------------------
// Print phones
// --------------------------------------------------
function print_phones() {
	console.log(phones);
}

// --------------------------------------------------
// Print browsers
// --------------------------------------------------
function print_browsers() {
	console.log(browsers);
}

module.exports.add_association = add_association;
module.exports.remove_phone = remove_phone;
module.exports.remove_browser = remove_browser;
module.exports.get_phones = get_phones;
module.exports.get_browsers = get_browsers;
module.exports.print_phones = print_phones;
module.exports.print_browsers = print_browsers;


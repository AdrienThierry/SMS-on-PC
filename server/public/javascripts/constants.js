/**
 * Angular module to expose constants specific to the browser app
 */

angular.module('SMS_on_PC').factory('constants', function() {
	var constants = {
		shared_conf_name: "conf", // Name of the conf file that is shared between server and android app
		device_id_var_name: "device_id" // Variable name for device id in local storage
	}

    return constants;
});

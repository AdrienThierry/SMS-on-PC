/**
 * Angular module to expose strings specific to the browser app in scope
 */

angular.module('SMS_on_PC').factory('strings', function($rootScope) {
	var strings = {};

	strings.choose_a_phone = "Choose a phone";
	strings.choose = "choose";

	$rootScope.strings = strings;
});

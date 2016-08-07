/**
 * App internationalization using android-translate
 * FC : first letter capitalized
 * C : capitalized
 */

app.config(['$translateProvider', function ($translateProvider) {
	$translateProvider.useSanitizeValueStrategy('sanitize');

	$translateProvider.translations('en', {
		'FC_choose_phone': 'Choose a phone',
		'FC_choose': 'Choose'
	});

	$translateProvider.preferredLanguage('en');
}]);

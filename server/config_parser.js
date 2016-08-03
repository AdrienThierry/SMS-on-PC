var fs = require('fs');
var constants = require('./constants.js');

var configString = fs.readFileSync(__dirname + '/' + constants.config_file_name, 'utf8');

var config = JSON.parse(configString);

module.exports = config;

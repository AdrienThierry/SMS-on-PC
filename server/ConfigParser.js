var fs = require('fs');
var Constants = require('./Constants.js');

var configString = fs.readFileSync(__dirname + '/' + Constants.config_file_name, 'utf8');

var config = JSON.parse(configString);

module.exports = config;

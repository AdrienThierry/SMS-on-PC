var express = require('express');
var router = express.Router();
var constants = require('../constants.js')
var fs = require('fs');

// GET home page
router.get('/', function(req, res, next) {
  res.render('index', { title: 'Express' });
});

// GET shared config
router.get('/' + constants.config_route, function(req, res) {
	fs.readFile(__dirname + "/../" + constants.config_file_name, 'utf8', function(err, contents) {
	    res.json(JSON.parse(contents));
	});
});

module.exports = router;

function connection_handling(io) {

	io.on('connection', function(socket) {
		console.log('a user connected');
	});

}

module.exports = connection_handling;

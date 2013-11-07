// setup the server and required modules
var express = require('express')
  , http = require('http');

var app = express();
var server = http.createServer(app);
var io = require('socket.io').listen(server);

// script dependencies
var timer = require(__dirname + '/timer.js');
var configuration = require(__dirname + '/configuration.js');

// server listens for requests in a specific port
server.listen(configuration.getPort());

// serve HTML to the clients when root is requested
app.get('/', function (req, res) {
  res.sendfile(__dirname + '/client.html');
});

// REST API w/ HTTP GET
app.get('/timer/:value', function(req, res) {
  if(req.params.value > 0) {
	timer.setValue(req.params.value);
	res.statusCode = 200;
	return res.send('Timer updated to ' + req.params.value);    
  }
  else {
	// TODO: use something else than 404, it's not really appropriate for this case
	res.statusCode = 404;
    return res.send('Error, you did not define a proper value');
	}
});

// makes the IO available in other scripts
exports.getIO = function() { return io};

// handle incoming and outgoing socket based messaging
io.sockets.on('connection', function (socket) {

	//unicast the timerState upon client connect
	socket.emit('timerState', { timer: timer.getState()});
	
	// from client
	socket.on('togglePause', timer.togglePauseAndNotify);
	socket.on('setValue', function(data) {timer.setValue(data["message"]);});
});
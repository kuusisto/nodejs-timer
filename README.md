nodejs-timer
=============

nodejs-timer is a project utilizing nodejs to allow multiple users to start/pause a downcounting timer and set it to a specific value. This value is broadcasted to all users through websockets. 

Users may also opt to use the REST API to set the timer value.

Android stub application is included



=============
Dependencies:
=============
  - node.js, tested with v0.10.21
  - node.js modules socket.io and express
  - web server


=============
Server Setup:
=============
1. Extract zip to a directory of your choosing... or clone git repo etc.
2. Install required modules whilst in that folder
	npm install socket.io
	npm install express
3. Configure server
	Modify configuration.js to select your server port. Choose a port that is not already in use. Who knows what horrible things could happen if you choose wrong.
4. Start server
	node server.js
    

=============
Android Client Setup:
=============
1.	Modify Const.java, especially the server address and port if it's not 80


=============
Server Notes:
=============
- Only limited REST API support in place. 
	- Support is only done for setting the timer value. Rest of the functionality is done through web sockets. Fortunately this is fairly easy to change either way and extend the rest API as the infrastructure is in place.
- Further modularity could be achieved.
- Some of the communication between modules could be streamlined
- Some functionality should be moved to their own sub-components.
- Function, variable names etc. could be named better.


=============
Client Notes:
=============
- When a timer is set it instantly decrements it by one second. Bummer!
- Android WebKit approach was a wrong decision but due to time constraints something I went for.
	- WebKit initialization is slow and current implementation does not have loading indication for async operations
- Better approach would have been to have REST API in node.js to send e.g. JSON data and parse it. During parsing we should display a loading indication and also otherwise have better control of the application UI.

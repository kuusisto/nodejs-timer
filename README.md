nodejs-timer
=============

nodejs-timer is a client-server project utilizing nodejs to allow multiple users to start/pause a downcounting timer and set it to a specific value. This value is broadcasted in real-time to all users through websockets. 

Users may also opt to use the REST API to set the timer value.

Android stub application is included



=============
Dependencies:
=============
  - node.js, tested with v0.10.21
  - node.js module socket.io, tested with v0.9.16
  - node.js module express, tested with v1.3.11
  - web server
  - Eclipse etc IDE and Android SDK + phone for the client part, otherwise just web browser will do


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
5. Point your web browser to server address of configured, by default http://localhost
    

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
- Android app: loading indication is not displayed for the async operations
- Android app: only minor error handling in place, "set timer" button is disabled should an error occur. 
- Android app: cnnectivity is not checked upon application launch, it will just attempt to load the specified URL.
- Android app: instead of utilizing WebView a better approach would be to retrieve JSON data and parse it and display the data in native UI components. Much easier, faster and will result into a better looking...

// timer.js
// ========

var server = require(__dirname + '/server.js');
const VALUE_DEFAULT = 1000;
const UPDATE_FREQUENCY_SEC = 1;

var states = {
    STOPPED: 0,
    RUNNING: 1,
    PAUSED: 2
};
var timer = null;
var timerState = new TimerStateClass();

function setState(newState) {

	switch (newState)
	{
	   case states.RUNNING:
			if (timer != null) clearInterval(timer);
			timer = createTimer();
		break;
		
	   case states.STOPPED:
		break;
		
		case states.PAUSED:
			clearInterval(timer);
		break;
	   default: 
			// do nothing
		   break;
	}

	timerState.setState(newState);
}

// class representation of the current timer state
function TimerStateClass() {
  this.state = states.STOPPED;
  this.value = VALUE_DEFAULT;
};

TimerStateClass.prototype.setValue = function (value) { this.value = value; };
TimerStateClass.prototype.getValue = function () { return this.value; };
TimerStateClass.prototype.getState = function () { return this.state; };
TimerStateClass.prototype.setState = function (state) { this.state = state; };

function getState() {
	return timerState;
}

module.exports = {
	getState: function () {
		return timerState;
  },
  setValue: function (newValue) {
    timerState.setValue(newValue);
	sendTimerValue();
  },
  togglePauseAndNotify: function () {
     togglePause();
	 server.getIO().sockets.emit('timerState', { timer: getState()});
  }
};

function togglePause() {
	if (timerState.getState() == states.PAUSED || timerState.getState() == states.STOPPED) {
		setState(states.RUNNING);
	}
	else {
		setState(states.PAUSED);
	}
}
	
function decrementTimer() {
	var value = timerState.getValue();
	value = value >= UPDATE_FREQUENCY_SEC ? value -= UPDATE_FREQUENCY_SEC : 0;
	
	if (value <= 0) {
		clearInterval(timer);
		setState(states.STOPPED);
	}
	
	timerState.setValue(value);
	sendTimerValue();
}

function sendTimerValue() {
	server.getIO().sockets.emit('timerState', { timer: getState() }); 
}

function createTimer() {

	return setInterval(function() {
		decrementTimer();
		}, 1000);
}

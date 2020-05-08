$(function() {
	var stompClient = null;

	function connect() {
		var socket = new WebSocket('ws://' + location.host + '/endpoint');
		stompClient = Stomp.over(socket);
		stompClient.connect({}, function(frame) {
			setConnected(true);
			console.log('Connected: ' + frame);
			stompClient.subscribe('/topic/greetings', recieveMessage);
		});
	}
	
	function disconnect() {
		if (stompClient !== null) {
			stompClient.disconnect();
			stompClient = null;
		}

		setConnected(false);
		console.log("Disconnected");
	}
	
	function setConnected(connected) {
		$("#connect").prop("disabled", connected);
		$("#disconnect").prop("disabled", !connected);
		$("#send").prop("disabled", !connected);
	}

	function sendMessage() {
		var name = $('#name').val();
		stompClient.send('/app/greet', {}, name);
	}

	function recieveMessage(message) {
		var p = $('<p>').append(message.body);
		$('#response').prepend(p);
	}

	$("#connect").click(function() {
		connect();
	});
	
	$("#disconnect").click(function() {
		disconnect();
	});
	
	$("#send").click(function() {
		sendMessage();
	});
});
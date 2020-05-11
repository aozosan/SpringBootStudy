$(function() {
	var stompClient = null;

	function connect() {
		stompClient = new StompJs.Client({
			brokerURL : 'ws://' + location.host + '/endpoint',
			debug : function(str) {
				console.log(str);
			},
			reconnectDelay : 5000,
			heartbeatIncoming : 4000,
			heartbeatOutgoing : 4000
		});

		stompClient.onConnect = function(frame) {
			setConnected(true);
			console.log('Connected: ' + frame);
			stompClient.subscribe("/topic/greetings", recieveMessage);
		};

		stompClient.onStompError = function(frame) {
			console.log('Broker reported error: ' + frame.headers['message']);
			console.log('Additional details: ' + frame.body);
		};

		stompClient.activate();
	}

	function disconnect() {
		if (stompClient !== null) {
			stompClient.deactivate();
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
		stompClient.publish({destination: '/app/greet', body: name});
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
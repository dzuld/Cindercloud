const cindercloudWS = (function () {
	let stompClient = null;
	let socket = new SockJS('/ws');
	let wsEnabled = false;

	let callbacksAfterInit = [];

	const connect = function() {
		console.log('connecting to websockets');
		stompClient = Stomp.over(socket);
		stompClient.debug = null;
		stompClient.connect({}, onConnected, onError);
	};

	function onConnected(_) {
		wsEnabled = true;
		for (const callback of callbacksAfterInit) {
			callback(stompClient);
		}
	}

	function onError(event) {
		console.log("Unable to connect to websocket");
		wsEnabled = false;
	}

	function afterConnecting(_callback) {
		if (wsEnabled) {
			_callback(stompClient);
		} else {
			callbacksAfterInit.push(_callback);
		}
	}

	connect();

	return {
		afterConnecting: afterConnecting,
		client: stompClient,
		enabled: wsEnabled
	}
})();
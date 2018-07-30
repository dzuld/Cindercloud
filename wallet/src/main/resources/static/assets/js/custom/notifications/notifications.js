const noty = (function () {

	const _successMessage = function (notificationObject) {
		new Noty({
			type: notificationObject.type,
			layout: 'bottomLeft',
			text: notificationObject.text,
			theme: 'relax',
			animation: {
				open: 'animated bounceInRight', // Animate.css class names
				close: 'animated bounceOutRight' // Animate.css class names
			},
			timeout: 5000,
			progressBar: false
		}).show();
	};

	function init() {
		cindercloudWS.afterConnecting(function (client) {
			client.subscribe('/topic/notifications', function (payload) {
				_successMessage(JSON.parse(payload.body));
			});
		});
	}

	init();

	return {
		success: _successMessage,
	}
})();


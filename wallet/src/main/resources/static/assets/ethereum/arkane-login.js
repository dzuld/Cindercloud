(function () {
	function signin(caller) {
		let target = $(caller.target).closest('.signin');
		const walletAddress = $(target).data('wallet-address');
		const bearerToken = $(target).data('bearer-token');
		const walletId = $(target).data('wallet-id');
		const auth = JSON.stringify({bearerToken: bearerToken, address: walletAddress, walletId: walletId});

		$.ajax({
			type: 'POST',
			url: '/arkane/login',
			data: auth,
			contentType: "application/json",
			dataType: 'json',
			success: function (result) {
				if (result) {
					window.location = '/wallet';
				}
			}
		});
	}

	Arkane.login(function (wallets, auth) {
		for (const wallet of wallets) {
			const template = $('#hidden-template').html();
			const item = $(template).clone();
			$(item).find('#description').html(wallet.description);
			$(item).find('#address').html(wallet.address);
			$(item).find('#balance').html(wallet.balance.balance);
			$(item).find('#type').html(wallet.type);
			$(item).find('#balance').html(wallet.balance.balance + ' ' + wallet.balance.symbol);
			$(item).find('.signin').data("wallet-id", wallet.id);
			$(item).find('.signin').data("wallet-address", wallet.address);
			$(item).find('.signin').data("bearer-token", auth);
			$('#loginList').append(item);
		}
		$('.signin').click(signin);
	});
})();
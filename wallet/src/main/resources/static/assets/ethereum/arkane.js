const Arkane = (function () {
	let bearerToken;
	const arkaneConnect = new ArkaneConnect('Cindercloud');
	let address;
	let walletId;

	$.get('/arkane/login/token', (result) => {
		if (result) {
			console.log(result);
			bearerToken = result.bearerToken;
			address = result.address;
			walletId = result.walletId;
		}
	}).fail((fail) => {
		console.log('not logged in with arkane');
	});

	const login = function (_onWallet) {
		arkaneConnect.authenticate().then((result) => {
			result.authenticated((auth) => {
				arkaneConnect.api.getWallets().then((wallets) => {
					if (wallets.length === 0) {
						arkaneConnect.manageWallets('ETHEREUM');
					} else {
						_onWallet(wallets, auth.token);
					}
				});
			});
		});
	};

	return {
		login: login,
		arkaneConnect: arkaneConnect,
		getWalletId: () => walletId
	}
})();
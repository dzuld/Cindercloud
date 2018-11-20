const Arkane = (function () {
	const arkaneConnect = new ArkaneConnect('Cindercloud', {chains: ['Ethereum']});
	let bearerToken;
	let address;
	let walletId;

	$.get('/arkane/login/token', (result) => {
		if (result) {
			console.log('we got a result');
			bearerToken = result.bearerToken;
			address = result.address;
			walletId = result.walletId;
			arkaneConnect.init(() => bearerToken);
		}
	}).fail((fail) => {
		console.log('not logged in with arkane');
	});

	const login = function (_onWallet) {
		arkaneConnect.checkAuthenticated().then((result) => {
			result.authenticated((auth) => {
				arkaneConnect.getWallets().then((wallets) => {
					_onWallet(wallets, auth.token);
				});
			}).notAuthenticated((auth) => {
				arkaneConnect.authenticate();
			});
		});
	};

	return {
		login: login,
		arkaneConnect: arkaneConnect,
		getWalletId: () => walletId
	}
})();
$(document).ready(function () {
	if (CindercloudWeb3.isClientWeb3Enabled()) {
		CindercloudWeb3.getClientWeb3().eth.getAccounts(function (error, result) {
			if (error) {
				//handle
			} else {
				onAccounts(result);
			}
		});
	}
});

var login = function (account) {
	$.post('/web3/login?address=' + account, function (result) {
		if (result === 'OK') {
			window.location = '/wallet';
		}
	});
};

function onAccounts(accs) {
	if (accs.length > 0) {
		var account = accs[0];
		login(account);
	} else {
		$('#notUnlocked').show();
	}
}
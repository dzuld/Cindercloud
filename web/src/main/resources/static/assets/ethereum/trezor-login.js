(function () {

	const trezorLoginData = {
		addresses: [],
		path: "m/44'/60'/0'/0"
	};

	const postLogin = function (data) {
		$.ajax({
			type: 'POST',
			url: '/hwallet/trezor',
			data: JSON.stringify(data),
			success: function (data) {
				if (data === true) {
					document.location.href = '/wallet';
				} else {
					swal({
						title: "Oh no!",
						text: "Something went wrong while trying to log in. Try again or contact an admin!",
						icon: "warning",
						buttons: false,
						dangerMode: false
					});
				}
			},
			contentType: "application/json"
		});
	};


	const fetchAddresses = function (response) {
		trezorLoginData.xpubkey = response.xpubkey;
		trezorLoginData.publicKey = response.publicKey;
		trezorLoginData.chainCode = response.chainCode;
		const hdKey = ethereumjs.WalletHD.fromExtendedKey(response.xpubkey);
		for (let i = 0; i < 10; i++) {
			trezorLoginData.addresses.push('0x' + hdKey.deriveChild(i).getWallet().getAddress().toString('hex'));
		}
		$('#trezorModalButton').click();
	};

	const fetchPubKey = function () {
		TrezorConnect.getXPubKey(trezorLoginData.path, function (_result) {
			if (_result.success) {
				trezorLoginData.addresses = [];
				fetchAddresses(_result);
			} else {
				console.log(_result);
				if (_result.error === 'Action cancelled by user' || _result.error === 'Cancelled') {
					swal({
						title: "Declined",
						text: "It appears that you declined the request to login with your trezor.",
						icon: "info",
						buttons: false,
						dangerMode: false
					});
				}
			}
		});
	};

	(function () {
		return new Vue({
			el: '#trezorLoginApp',
			data: trezorLoginData,
			methods: {
				submitAddress: function (ar) {
					trezorLoginData.address = ar;
					postLogin(trezorLoginData);
				}
			}
		})
	})();

	$('#connectTrezor').click(fetchPubKey);
})();
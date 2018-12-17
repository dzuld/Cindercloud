(function () {

	$('#btnCancel').click(function () {
		swal({
			title: "Are you sure?",
			text: "If you cancel, you'll have to start over!",
			icon: "warning",
			buttons: true,
			dangerMode: false
		})
			.then(function (willCancel) {
				if (willCancel) {
					location.href = '/wallet/send';
				}
			});
	});

	$('#btnConfirm').click(function () {
		sendTransaction();
	});

	const sendTransaction = function () {

		const signer = Arkane.arkaneConnect.createSigner();

		const to = $('#confirmTo').val();
		const amount = $('#confirmAmount').val();

		signer.executeTransaction({
			walletId: Arkane.getWalletId(),
			to: to,
			value: amount,
			secretType: 'ETHEREUM'
		}).then((result) => {
			if (result.success) {
				swal("Transaction Sent!", "The transaction has been sent (" + result.result.transactionHash + ")", "success");
				$('#btnConfirm').hide();
				$('#btnCancel').hide();
			} else {
				swal("Transaction Problem!", "Something went wrong while trying to submit your transaction", "error");
			}
		}).catch(error => {
			console.error(error);
		});
	};
})();
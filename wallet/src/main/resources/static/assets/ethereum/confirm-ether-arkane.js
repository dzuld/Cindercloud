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

		console.log(Arkane.getWalletId());

		Arkane.arkaneConnect.buildTransactionRequest({
			walletId: Arkane.getWalletId(),
			secretType: 'ETHEREUM',
			to: to,
			value: amount
		})
			.then((transactionRequest) => {
				signer.executeTransaction(transactionRequest)
					.then(function(result) {
						if (result.success) {
							swal("Transaction Sent!", "The transaction has been sent (" + result.result.transactionHash + ")", "success");
							$('#btnConfirm').hide();
							$('#btnCancel').hide();
						} else {
							swal("Transaction Problem!", "Something went wrong while trying to submit your transaction", "error");
						}
					})
					.catch(function(error) {
						console.error(error);
					});
			})
			.catch((error) => {
				// Always catch errors and close the signer, otherwise it looks like the initialising popup is hanging when something goes wrong
				signer.close();
				console.error(error);
			});
	};
})();
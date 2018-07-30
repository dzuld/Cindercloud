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
		const to = $('#confirmTo').val();
		const gasLimit = $('#confirmGasLimit').val();
		const gasPrice = $('#confirmGasPriceInWei').val();
		const amountInWei = $('#confirmAmountInWei').val();

		const transactionObject = {
			to: to,
			value: amountInWei,
			gas: gasLimit,
			gasPrice: gasPrice
		};

		CindercloudWeb3.FgetWeb3().eth.sendTransaction(transactionObject, function (err, transactionHash) {
			if (!err) {
				swal("Transaction Sent!", "The transaction has been sent (" + transactionHash + ")", "success");
			} else {
				swal("Transaction Problem!", "Something went wrong while trying to submit your transaction", "error");
			}
		})
	};
})();
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
						location.href = '/wallet/tokens/send';
					}
				});
		});

		$('#btnConfirm').click(function () {
			sendTransaction();
		});

		const sendTransaction = function () {
			const signer = Arkane.arkaneConnect.createSigner();

			const txObject = {
				type: 'ETHEREUM_ERC20_TRANSACTION',
				walletId: Arkane.getWalletId(),
				value: $('#confirmAmountInWei').val(),
				to: $('#confirmTo').val(),
				tokenAddress: $('#confirmTokenAddress').val()
			};

			console.log(txObject);

			signer.executeTransaction(txObject).then((result) => {
				swal("Transaction Sent!", "The transaction has been sent (" + result.result.transactionHash + ")", "success");
				$('#btnConfirm').hide();
				$('#btnCancel').hide();
			}).catch((error) => {
				swal("Transaction Problem!", "Something went wrong while trying to submit your transaction", "error");
			});
		}
	}
)();
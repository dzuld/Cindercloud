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
			const to = $('#confirmTo').val();
			const gasLimit = $('#confirmGasLimit').val();
			const tokenAddress = $('#confirmTokenAddress').val();
			const gasPrice = $('#confirmGasPriceInWei').val();
			const amountInWei = $('#confirmAmountInWei').val();
			const erc20 = CindercloudWeb3.getWeb3().eth.contract(abi.erc20).at(tokenAddress);
			const transactionObject = {
				from: $('#currentAddress').val(),
				to: tokenAddress,
				value: '0x0',
				data: erc20.transfer.getData(to, amountInWei),
				gasPrice: gasPrice
			};
			CindercloudWeb3.getWeb3().eth.estimateGas(transactionObject, function (err, result) {
				if (!err) {
					transactionObject.gas = result;
				} else {
					transactionObject.gas = gasLimit;
				}
				CindercloudWeb3.getWeb3().eth.sendTransaction(transactionObject, function (err, transactionHash) {
					if (!err) {
						swal("Transaction Sent!", "The transaction has been sent (" + transactionHash + ")", "success");
					} else {
						swal("Transaction Problem!", "Something went wrong while trying to submit your transaction", "error");
					}
				});
			});
		}
	}
)();
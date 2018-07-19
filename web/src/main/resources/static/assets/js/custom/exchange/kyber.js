var Kyber = (function () {

	var address = $('#currentAddress').val();
	var authenticationType = $('#authenticationType').val();

	var kyberMainnet = '0x964F35fAe36d75B1e72770e244F6595B68508CF5';

	var kyberData = {
		authenticationType: authenticationType,
		sources: [],
		targets: [],
		source: null,
		target: null,
		targetAmount: 0,
		rawTargetAmount: 0,
		sourceAmount: 0,
		rawSourceAmount: 0,
		expectedRate: 0,
		slippageRate: 0,
		balance: 0,
		userCap: 0,
		tokens: {
			ETH: {
				name: "Ethereum",
				symbol: "ETH",
				icon: "eth.svg",
				address: "0xeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee",
				image: "ethereum_1",
				decimal: 18,
				usd_id: "ethereum"
			},
			DAI: {
				name: "DAI",
				symbol: "DAI",
				icon: "dai.svg",
				address: "0x89d24a6b4ccb1b6faa2625fe562bdd9a23260359",
				image: "0x89d24a6b4ccb1b6faa2625fe562bdd9a23260359",
				decimal: 18
			},
			KNC: {
				name: "KyberNetwork",
				symbol: "KNC",
				icon: "knc.svg",
				address: "0xdd974d5c2e2928dea5f71b9825b8b646686bd200",
				image: "0xdd974d5c2e2928dea5f71b9825b8b646686bd200",
				decimal: 18,
				usd_id: "kyber-network"
			},
			OMG: {
				name: "OmiseGO",
				symbol: "OMG",
				icon: "omg.svg",
				address: "0xd26114cd6ee289accf82350c8d8487fedb8a0c07",
				image: "0xd26114cd6ee289accf82350c8d8487fedb8a0c07",
				decimal: 18,
				usd_id: "omisego"
			},
			EOS: {
				name: "Eos",
				symbol: "EOS",
				icon: "eos.svg",
				address: "0x86fa049857e0209aa7d9e616f7eb3b3b78ecfdb0",
				image: "0x86fa049857e0209aa7d9e616f7eb3b3b78ecfdb0",
				decimal: 18,
				usd_id: "eos"
			},
			SNT: {
				name: "Status",
				address: "0x744d70fdbe2ba4cf95131626614a1763df805b9e",
				image: "0x744d70fdbe2ba4cf95131626614a1763df805b9e",
				symbol: "SNT",
				icon: "snt.svg",
				decimal: 18,
				usd_id: "status"
			},
			ELF: {
				name: "Aelf",
				address: "0xbf2179859fc6d5bee9bf9158632dc51678a4100e",
				image: "0xbf2179859fc6d5bee9bf9158632dc51678a4100e",
				symbol: "ELF",
				icon: "aelf.svg",
				decimal: 18,
				usd_id: "aelf"
			},
			POWR: {
				name: "Power Ledger",
				address: "0x595832f8fc6bf59c85c527fec3740a1b7a361269",
				image: "0x595832f8fc6bf59c85c527fec3740a1b7a361269",
				symbol: "POWR",
				icon: "pwr.svg",
				decimal: 6,
				usd_id: "power-ledger"
			},
			MANA: {
				name: "Mana",
				address: "0x0f5d2fb29fb7d3cfee444a200298f468908cc942",
				image: "0x0f5d2fb29fb7d3cfee444a200298f468908cc942",
				symbol: "MANA",
				icon: "mana.svg",
				decimal: 18,
				usd_id: "decentraland"
			},
			BAT: {
				name: "Basic Attention Token",
				address: "0x0d8775f648430679a709e98d2b0cb6250d2887ef",
				image: "0x0d8775f648430679a709e98d2b0cb6250d2887ef",
				symbol: "BAT",
				icon: "bat.svg",
				decimal: 18,
				usd_id: "basic-attention-token"
			},
			REQ: {
				name: "Request",
				address: "0x8f8221afbb33998d8584a2b05749ba73c37a938a",
				image: "0x8f8221afbb33998d8584a2b05749ba73c37a938a",
				symbol: "REQ",
				icon: "req.svg",
				decimal: 18,
				usd_id: "request-network"
			},
			GTO: {
				name: "Gifto",
				address: "0xc5bbae50781be1669306b9e001eff57a2957b09d",
				image: "0xc5bbae50781be1669306b9e001eff57a2957b09d",
				symbol: "GTO",
				icon: "gifto.svg",
				decimal: 5,
				usd_id: "gifto"
			},
			RDN: {
				name: "Raiden",
				address: "0x255aa6df07540cb5d3d297f0d0d4d84cb52bc8e6",
				image: "0x255aa6df07540cb5d3d297f0d0d4d84cb52bc8e6",
				symbol: "RDN",
				icon: "rdn.svg",
				decimal: 18,
				usd_id: "raiden-network-token"
			},
			APPC: {
				name: "AppCoins",
				address: "0x1a7a8bd9106f2b8d977e08582dc7d24c723ab0db",
				image: "0x1a7a8bd9106f2b8d977e08582dc7d24c723ab0db",
				symbol: "APPC",
				icon: "appc.svg",
				decimal: 18,
				usd_id: "appcoins"
			},
			ENG: {
				name: "Enigma",
				address: "0xf0ee6b27b759c9893ce4f094b49ad28fd15a23e4",
				image: "0xf0ee6b27b759c9893ce4f094b49ad28fd15a23e4",
				symbol: "ENG",
				icon: "eng.svg",
				decimal: 8,
				usd_id: "enigma-project"
			},
			SALT: {
				name: "Salt",
				address: "0x4156d3342d5c385a87d264f90653733592000581",
				image: "0x4156d3342d5c385a87d264f90653733592000581",
				symbol: "SALT",
				icon: "salt.svg",
				decimal: 8,
				usd_id: "salt"
			},
			ADEX: {
				symbol: "ADX",
				name: "AdEx",
				address: "0x4470BB87d77b963A013DB939BE332f927f2b992e",
				image: "0x4470BB87d77b963A013DB939BE332f927f2b992e",
				decimals: 4
			},
			AIRSWAP: {
				symbol: "AST",
				name: "AirSwap",
				address: "0x27054b13b1b798b345b591a4d22e6562d47ea75a",
				image: "0x27054b13b1b798b345b591a4d22e6562d47ea75a",
				decimals: 4
			},
			RIPIO: {
				symbol: "RCN",
				name: "Ripio Credit Network",
				address: "0xf970b8e36e23f7fc3fd752eea86f8be8d83375a6",
				image: "0xf970b8e36e23f7fc3fd752eea86f8be8d83375a6",
				decimals: 18
			},
			ZILLIQA: {
				symbol: "ZIL",
				name: "Zilliqa",
				addess: "0x05f4a42e251f2d52b8ed15e9fedaacfcef1fad27",
				image: "0x05f4a42e251f2d52b8ed15e9fedaacfcef1fad27",
				decimals: 12
			},
			CHAINLINK: {
				symbol: "LINK",
				name: "Chain Link",
				address: "0x514910771af9ca656af840dff83e8264ecf986ca",
				image: "0x514910771af9ca656af840dff83e8264ecf986ca",
				decimals: 18
			}
		},
		errorMessage: null,
		warningMessage: null,

		rawExpectedRate: 1,
		rawSlippageRate: 1
	};


	var initiateVue = function () {
		Vue.component('v-select', VueSelect.VueSelect);

		function updateConversionRate(_callback) {
			var kyber = CindercloudWeb3.getGlobalWeb3().eth.contract(abi.kyber.expected_rate).at(kyberMainnet);

			kyber.getExpectedRate(kyberData.source.address, kyberData.target.address, kyberData.sourceAmount, function (err, result) {
				kyberData.expectedRate = result[0].div(Math.pow(10, kyberData.target.decimal)).toString(10);
				kyberData.slippageRate = result[1].div(Math.pow(10, kyberData.target.decimal)).toString(10);

				kyberData.rawSlippageRate = result[1].toNumber();
				kyberData.rawExpectedRate = result[0].toNumber();
				_callback();
			});
		}

		function updateUserCap() {
			var kyber = CindercloudWeb3.getGlobalWeb3().eth.contract(abi.kyber.kyber_network).at(kyberMainnet);
			kyber.getUserCapInWei(address, function (err, _cap) {
				console.log(_cap.toNumber());
				kyberData.userCap = _cap;
			});
		}

		function updateBalance(sourceElement) {
			if (sourceElement.symbol === 'ETH') {
				CindercloudWeb3.getGlobalWeb3().eth.getBalance(address, function (err, _balance) {
					kyberData.balance = _balance.div(Math.pow(10, 18)).toString(10);
				});
			} else {
				var erc20 = CindercloudWeb3.getGlobalWeb3().eth.contract(abi.erc20).at(sourceElement.address);
				erc20.decimals(function (_, _decimals) {
					erc20.balanceOf(address, function (_, _balance) {
						kyberData.balance = _balance.div(Math.pow(10, _decimals)).toString(10);
					});
				});
			}
		}

		function hasEnoughAllowed(_callback) {
			var erc20 = CindercloudWeb3.getGlobalWeb3().eth.contract(abi.erc20).at(kyberData.source.address);
			erc20.allowance(address, kyberMainnet, function (err, all) {
				if (!err) {
					console.log(kyberData.source.address);
					console.log("allowed: ", all.toNumber());
					console.log("necessary: ", kyberData.rawSourceAmount);
					_callback(all >= kyberData.rawSourceAmount);
				} else {
					_callback(false);
				}
			});
		}


		function createAllowance(_callback) {
			var erc20 = CindercloudWeb3.getGlobalWeb3().eth.contract(abi.erc20).at(kyberData.source.address);

			var transactionObject = {
				from: address,
				to: kyberData.source.address,
				data: erc20.approve.getData(
					kyberMainnet,
					Math.pow(2, 256) - 1),
				gasPrice: 20000000000
			};

			console.log(transactionObject);

			CindercloudWeb3.getWeb3().eth.estimateGas(transactionObject, function (err, result) {
				if (!err) {
					transactionObject.gas = result;
				} else {
					transactionObject.gas = 300000;
				}

				if (authenticationType === 'WEB3') {
					CindercloudWeb3.getWeb3().eth.sendTransaction(transactionObject, function (err, transactionHash) {
						if (!err) {
							swal("Transaction Sent!", "The transaction has been sent (" + transactionHash + ")", "success");
							//	_callback();
						} else {
							console.log(err);
							swal("Transaction Problem!", "Something went wrong while trying to submit your transaction", "error");
						}
					});
				} else if (authenticationType === 'CINDERCLOUD') {
					//TODO: send to server
				} else {
					console.log('authenticationtype not supported');
				}
			});
		}

		function createTransaction() {
			var kyberNetworkContract = CindercloudWeb3.getClientWeb3().eth.contract(abi.kyber.kyber_network).at(kyberMainnet);

			var wrapper = document.getElementById('transactionConfirm');

			swal({
				content: wrapper,
				buttons: {
					confirm: {
						value: 'confirm',
					},
				}
			}).then(function (decision) {
				if (decision === 'confirm') {
					var value = (function () {
						if (kyberData.source.symbol === 'ETH') {
							return kyberData.rawSourceAmount;
						} else {
							return 0;
						}
					})();


					var transactionObject = {
						from: address,
						to: kyberMainnet,
						value: value,
						data: kyberNetworkContract.trade.getData(
							kyberData.source.address,
							kyberData.rawSourceAmount,
							kyberData.target.address,
							address,
							8 * Math.pow(10, 63),
							kyberData.rawSlippageRate,
							'0xdc71b72db51e227e65a45004ab2798d31e8934c9'),
						gasPrice: 20000000000
					};

					console.log(transactionObject);
					console.log(authenticationType);

					CindercloudWeb3.getWeb3().eth.estimateGas(transactionObject, function (err, result) {
						if (!err) {
							transactionObject.gas = result;
						} else {
							transactionObject.gas = 300000;
						}

						if (authenticationType === 'WEB3') {
							CindercloudWeb3.getWeb3().eth.sendTransaction(transactionObject, function (err, transactionHash) {
								if (!err) {
									swal("Transaction Sent!", "The transaction has been sent (" + transactionHash + ")", "success");
								} else {
									console.log(err);
									swal("Transaction Problem!", "Something went wrong while trying to submit your transaction", "error");
								}
							});
						} else if (authenticationType === 'CINDERCLOUD') {
							//TODO: send to server
						} else {
							console.log('authenticationtype not supported');
						}
					});
				}
			});
		}

		new Vue({
			el: '#kyberApp',
			data: kyberData,
			methods: {
				sourceUpdated: function () {
					console.log(kyberData.source.name);
				},
				exchange: function () {
					kyberData.warningMessage = null;
					if (kyberData.sourceAmount === 0) {
						kyberData.warningMessage = 'Please enter a source-amount first.';
						return;
					}
					if (kyberData.balance <= kyberData.sourceAmount) {
						kyberData.warningMessage = 'Source amount is too high. Your balance is insufficient';
						return;
					}

					//require an approval first if it's not eth we're talking about
					if (kyberData.source.symbol !== 'ETH') {
						hasEnoughAllowed(function (enoughAllowed) {
							if (enoughAllowed) {
								createTransaction();
							} else {
								swal({
									title: "We need your permission.",
									text: "You need to grant permission for Cindercloud to interact with " + kyberData.source.symbol + " on your behalf.",
									icon: "info",
									buttons: true,
									dangerMode: false
								})
									.then(function (allowed) {
										if (allowed) {
											createAllowance(function () {
												createTransaction();
											});
										}
									});
							}
						});
					} else {
						createTransaction();
					}
				},
				updateFromSource: function () {
					if (kyberData.sourceAmount) {
						kyberData.rawSourceAmount = (kyberData.sourceAmount * Math.pow(10, kyberData.source.decimal));
						updateConversionRate(function () {
							kyberData.targetAmount = (kyberData.sourceAmount * kyberData.expectedRate);
							kyberData.rawTargetAmount = (kyberData.rawSourceAmount * kyberData.expectedRate);
						});
					} else {
						kyberData.targetAmount = 0;
						kyberData.rawTargetAmount = 0;
						kyberData.rawSourceAmount = 0;
					}
				},
				updateFromTarget: function () {
					if (kyberData.targetAmount) {
						kyberData.rawTargetAmount = (kyberData.targetAmount * Math.pow(10, kyberData.target.decimal));
						updateConversionRate(function () {
							kyberData.sourceAmount = (kyberData.targetAmount / kyberData.expectedRate);
							kyberData.rawSourceAmount = Math.round(kyberData.rawTargetAmount / kyberData.expectedRate);
						});
					} else {
						kyberData.sourceAmount = 0;
						kyberData.rawSourceAmount = 0;
						kyberData.rawTargetAmount = 0;
					}
				}
			},
			watch: {
				source: function (val) {
					if (val === null || val === 'undefined') {
						this.targets = [];
					} else {
						if (kyberData.source.symbol === 'ETH') {
							this.targets = $.grep(Object.values(kyberData.tokens), function (element) {
								return element.symbol !== 'ETH';
							});
						} else {
							this.targets = [kyberData.tokens.ETH];
						}

						this.target = this.targets[0];
						updateBalance(val);
						updateConversionRate(function () {
							kyberData.sourceAmount = (kyberData.targetAmount / kyberData.expectedRate);
							kyberData.rawSourceAmount = Math.round(kyberData.rawTargetAmount / kyberData.expectedRate);
						});
					}
				},
				target: function () {
					updateConversionRate(function () {
						kyberData.targetAmount = (kyberData.sourceAmount * kyberData.expectedRate);
						kyberData.rawTargetAmount = Math.round(kyberData.rawSourceAmount / kyberData.expectedRate);
					});
				}
			},
			created: function () {
				kyberData.source = Object.values(kyberData.tokens)[0];
			}
		});


		setInterval(function () {
			updateConversionRate(function () {
				//dont do anything
			});
		}, 10000);

		updateUserCap(function () {
			setInterval(function () {
				updateUserCap(function () {
					//dont do anything
				});
			}, 20000);
		});
	};

	initiateVue();

	return {
		data: kyberData
	}
})();
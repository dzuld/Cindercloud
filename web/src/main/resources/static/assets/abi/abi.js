var abi = (function () {
	var kyber = {
		expected_rate: [{
			"constant": true,
			"inputs": [{"name": "src", "type": "address"}, {"name": "dest", "type": "address"}, {
				"name": "srcQty",
				"type": "uint256"
			}],
			"name": "getExpectedRate",
			"outputs": [{"name": "expectedRate", "type": "uint256"}, {"name": "slippageRate", "type": "uint256"}],
			"payable": false,
			"stateMutability": "view",
			"type": "function"
		}],
		kyber_network: [{
			"constant": false,
			"inputs": [{"name": "alerter", "type": "address"}],
			"name": "removeAlerter",
			"outputs": [],
			"payable": false,
			"stateMutability": "nonpayable",
			"type": "function"
		}, {
			"constant": false,
			"inputs": [{"name": "reserve", "type": "address"}, {"name": "src", "type": "address"}, {
				"name": "dest",
				"type": "address"
			}, {"name": "add", "type": "bool"}],
			"name": "listPairForReserve",
			"outputs": [],
			"payable": false,
			"stateMutability": "nonpayable",
			"type": "function"
		}, {
			"constant": true,
			"inputs": [{"name": "", "type": "address"}, {"name": "", "type": "bytes32"}],
			"name": "perReserveListedPairs",
			"outputs": [{"name": "", "type": "bool"}],
			"payable": false,
			"stateMutability": "view",
			"type": "function"
		}, {
			"constant": true,
			"inputs": [],
			"name": "getReserves",
			"outputs": [{"name": "", "type": "address[]"}],
			"payable": false,
			"stateMutability": "view",
			"type": "function"
		}, {
			"constant": true,
			"inputs": [],
			"name": "enabled",
			"outputs": [{"name": "", "type": "bool"}],
			"payable": false,
			"stateMutability": "view",
			"type": "function"
		}, {
			"constant": true,
			"inputs": [],
			"name": "pendingAdmin",
			"outputs": [{"name": "", "type": "address"}],
			"payable": false,
			"stateMutability": "view",
			"type": "function"
		}, {
			"constant": true,
			"inputs": [],
			"name": "getOperators",
			"outputs": [{"name": "", "type": "address[]"}],
			"payable": false,
			"stateMutability": "view",
			"type": "function"
		}, {
			"constant": false,
			"inputs": [{"name": "token", "type": "address"}, {"name": "amount", "type": "uint256"}, {
				"name": "sendTo",
				"type": "address"
			}],
			"name": "withdrawToken",
			"outputs": [],
			"payable": false,
			"stateMutability": "nonpayable",
			"type": "function"
		}, {
			"constant": true,
			"inputs": [],
			"name": "maxGasPrice",
			"outputs": [{"name": "", "type": "uint256"}],
			"payable": false,
			"stateMutability": "view",
			"type": "function"
		}, {
			"constant": false,
			"inputs": [{"name": "newAlerter", "type": "address"}],
			"name": "addAlerter",
			"outputs": [],
			"payable": false,
			"stateMutability": "nonpayable",
			"type": "function"
		}, {
			"constant": true,
			"inputs": [],
			"name": "negligibleRateDiff",
			"outputs": [{"name": "", "type": "uint256"}],
			"payable": false,
			"stateMutability": "view",
			"type": "function"
		}, {
			"constant": true,
			"inputs": [],
			"name": "feeBurnerContract",
			"outputs": [{"name": "", "type": "address"}],
			"payable": false,
			"stateMutability": "view",
			"type": "function"
		}, {
			"constant": true,
			"inputs": [],
			"name": "expectedRateContract",
			"outputs": [{"name": "", "type": "address"}],
			"payable": false,
			"stateMutability": "view",
			"type": "function"
		}, {
			"constant": true,
			"inputs": [],
			"name": "whiteListContract",
			"outputs": [{"name": "", "type": "address"}],
			"payable": false,
			"stateMutability": "view",
			"type": "function"
		}, {
			"constant": true,
			"inputs": [{"name": "user", "type": "address"}],
			"name": "getUserCapInWei",
			"outputs": [{"name": "", "type": "uint256"}],
			"payable": false,
			"stateMutability": "view",
			"type": "function"
		}, {
			"constant": false,
			"inputs": [{"name": "newAdmin", "type": "address"}],
			"name": "transferAdmin",
			"outputs": [],
			"payable": false,
			"stateMutability": "nonpayable",
			"type": "function"
		}, {
			"constant": false,
			"inputs": [{"name": "_enable", "type": "bool"}],
			"name": "setEnable",
			"outputs": [],
			"payable": false,
			"stateMutability": "nonpayable",
			"type": "function"
		}, {
			"constant": false,
			"inputs": [],
			"name": "claimAdmin",
			"outputs": [],
			"payable": false,
			"stateMutability": "nonpayable",
			"type": "function"
		}, {
			"constant": true,
			"inputs": [{"name": "", "type": "address"}],
			"name": "isReserve",
			"outputs": [{"name": "", "type": "bool"}],
			"payable": false,
			"stateMutability": "view",
			"type": "function"
		}, {
			"constant": true,
			"inputs": [],
			"name": "getAlerters",
			"outputs": [{"name": "", "type": "address[]"}],
			"payable": false,
			"stateMutability": "view",
			"type": "function"
		}, {
			"constant": true,
			"inputs": [{"name": "src", "type": "address"}, {"name": "dest", "type": "address"}, {
				"name": "srcQty",
				"type": "uint256"
			}],
			"name": "getExpectedRate",
			"outputs": [{"name": "expectedRate", "type": "uint256"}, {"name": "slippageRate", "type": "uint256"}],
			"payable": false,
			"stateMutability": "view",
			"type": "function"
		}, {
			"constant": true,
			"inputs": [{"name": "", "type": "uint256"}],
			"name": "reserves",
			"outputs": [{"name": "", "type": "address"}],
			"payable": false,
			"stateMutability": "view",
			"type": "function"
		}, {
			"constant": false,
			"inputs": [{"name": "newOperator", "type": "address"}],
			"name": "addOperator",
			"outputs": [],
			"payable": false,
			"stateMutability": "nonpayable",
			"type": "function"
		}, {
			"constant": false,
			"inputs": [{"name": "reserve", "type": "address"}, {"name": "add", "type": "bool"}],
			"name": "addReserve",
			"outputs": [],
			"payable": false,
			"stateMutability": "nonpayable",
			"type": "function"
		}, {
			"constant": false,
			"inputs": [{"name": "operator", "type": "address"}],
			"name": "removeOperator",
			"outputs": [],
			"payable": false,
			"stateMutability": "nonpayable",
			"type": "function"
		}, {
			"constant": false,
			"inputs": [{"name": "_whiteList", "type": "address"}, {
				"name": "_expectedRate",
				"type": "address"
			}, {"name": "_feeBurner", "type": "address"}, {
				"name": "_maxGasPrice",
				"type": "uint256"
			}, {"name": "_negligibleRateDiff", "type": "uint256"}],
			"name": "setParams",
			"outputs": [],
			"payable": false,
			"stateMutability": "nonpayable",
			"type": "function"
		}, {
			"constant": true,
			"inputs": [{"name": "src", "type": "address"}, {"name": "dest", "type": "address"}, {
				"name": "srcQty",
				"type": "uint256"
			}],
			"name": "findBestRate",
			"outputs": [{"name": "", "type": "uint256"}, {"name": "", "type": "uint256"}],
			"payable": false,
			"stateMutability": "view",
			"type": "function"
		}, {
			"constant": false,
			"inputs": [
				{"name": "src", "type": "address"},
				{"name": "srcAmount", "type": "uint256"},
				{"name": "dest", "type": "address"},
				{"name": "destAddress", "type": "address"},
				{"name": "maxDestAmount", "type": "uint256"},
				{"name": "minConversionRate", "type": "uint256"},
				{"name": "walletId", "type": "address"}
			],
			"name": "trade",
			"outputs": [{"name": "", "type": "uint256"}],
			"payable": true,
			"stateMutability": "payable",
			"type": "function"
		}, {
			"constant": false,
			"inputs": [{"name": "amount", "type": "uint256"}, {"name": "sendTo", "type": "address"}],
			"name": "withdrawEther",
			"outputs": [],
			"payable": false,
			"stateMutability": "nonpayable",
			"type": "function"
		}, {
			"constant": true,
			"inputs": [],
			"name": "getNumReserves",
			"outputs": [{"name": "", "type": "uint256"}],
			"payable": false,
			"stateMutability": "view",
			"type": "function"
		}, {
			"constant": true,
			"inputs": [{"name": "token", "type": "address"}, {"name": "user", "type": "address"}],
			"name": "getBalance",
			"outputs": [{"name": "", "type": "uint256"}],
			"payable": false,
			"stateMutability": "view",
			"type": "function"
		}, {
			"constant": true,
			"inputs": [],
			"name": "admin",
			"outputs": [{"name": "", "type": "address"}],
			"payable": false,
			"stateMutability": "view",
			"type": "function"
		}, {
			"inputs": [{"name": "_admin", "type": "address"}],
			"payable": false,
			"stateMutability": "nonpayable",
			"type": "constructor"
		}, {"payable": true, "stateMutability": "payable", "type": "fallback"}, {
			"anonymous": false,
			"inputs": [{"indexed": true, "name": "sender", "type": "address"}, {
				"indexed": false,
				"name": "amount",
				"type": "uint256"
			}],
			"name": "EtherReceival",
			"type": "event"
		}, {
			"anonymous": false,
			"inputs": [{"indexed": true, "name": "sender", "type": "address"}, {
				"indexed": false,
				"name": "src",
				"type": "address"
			}, {"indexed": false, "name": "dest", "type": "address"}, {
				"indexed": false,
				"name": "actualSrcAmount",
				"type": "uint256"
			}, {"indexed": false, "name": "actualDestAmount", "type": "uint256"}],
			"name": "ExecuteTrade",
			"type": "event"
		}, {
			"anonymous": false,
			"inputs": [{"indexed": false, "name": "reserve", "type": "address"}, {
				"indexed": false,
				"name": "add",
				"type": "bool"
			}],
			"name": "AddReserveToNetwork",
			"type": "event"
		}, {
			"anonymous": false,
			"inputs": [{"indexed": false, "name": "reserve", "type": "address"}, {
				"indexed": false,
				"name": "src",
				"type": "address"
			}, {"indexed": false, "name": "dest", "type": "address"}, {
				"indexed": false,
				"name": "add",
				"type": "bool"
			}],
			"name": "ListReservePairs",
			"type": "event"
		}, {
			"anonymous": false,
			"inputs": [{"indexed": false, "name": "token", "type": "address"}, {
				"indexed": false,
				"name": "amount",
				"type": "uint256"
			}, {"indexed": false, "name": "sendTo", "type": "address"}],
			"name": "TokenWithdraw",
			"type": "event"
		}, {
			"anonymous": false,
			"inputs": [{"indexed": false, "name": "amount", "type": "uint256"}, {
				"indexed": false,
				"name": "sendTo",
				"type": "address"
			}],
			"name": "EtherWithdraw",
			"type": "event"
		}, {
			"anonymous": false,
			"inputs": [{"indexed": false, "name": "pendingAdmin", "type": "address"}],
			"name": "TransferAdminPending",
			"type": "event"
		}, {
			"anonymous": false,
			"inputs": [{"indexed": false, "name": "newAdmin", "type": "address"}, {
				"indexed": false,
				"name": "previousAdmin",
				"type": "address"
			}],
			"name": "AdminClaimed",
			"type": "event"
		}, {
			"anonymous": false,
			"inputs": [{"indexed": false, "name": "newAlerter", "type": "address"}, {
				"indexed": false,
				"name": "isAdd",
				"type": "bool"
			}],
			"name": "AlerterAdded",
			"type": "event"
		}, {
			"anonymous": false,
			"inputs": [{"indexed": false, "name": "newOperator", "type": "address"}, {
				"indexed": false,
				"name": "isAdd",
				"type": "bool"
			}],
			"name": "OperatorAdded",
			"type": "event"
		}]
	};

	var erc20 = [
		{
			"constant": true,
			"inputs": [],
			"name": "name",
			"outputs": [
				{
					"name": "",
					"type": "string"
				}
			],
			"payable": false,
			"type": "function"
		},
		{
			"constant": false,
			"inputs": [
				{
					"name": "_spender",
					"type": "address"
				},
				{
					"name": "_value",
					"type": "uint256"
				}
			],
			"name": "approve",
			"outputs": [
				{
					"name": "success",
					"type": "bool"
				}
			],
			"payable": false,
			"type": "function"
		},
		{
			"constant": true,
			"inputs": [],
			"name": "totalSupply",
			"outputs": [
				{
					"name": "",
					"type": "uint256"
				}
			],
			"payable": false,
			"type": "function"
		},
		{
			"constant": false,
			"inputs": [
				{
					"name": "_from",
					"type": "address"
				},
				{
					"name": "_to",
					"type": "address"
				},
				{
					"name": "_value",
					"type": "uint256"
				}
			],
			"name": "transferFrom",
			"outputs": [
				{
					"name": "success",
					"type": "bool"
				}
			],
			"payable": false,
			"type": "function"
		},
		{
			"constant": true,
			"inputs": [],
			"name": "decimals",
			"outputs": [
				{
					"name": "",
					"type": "uint8"
				}
			],
			"payable": false,
			"type": "function"
		},
		{
			"constant": true,
			"inputs": [],
			"name": "version",
			"outputs": [
				{
					"name": "",
					"type": "string"
				}
			],
			"payable": false,
			"type": "function"
		},
		{
			"constant": true,
			"inputs": [
				{
					"name": "_owner",
					"type": "address"
				}
			],
			"name": "balanceOf",
			"outputs": [
				{
					"name": "balance",
					"type": "uint256"
				}
			],
			"payable": false,
			"type": "function"
		},
		{
			"constant": true,
			"inputs": [],
			"name": "symbol",
			"outputs": [
				{
					"name": "",
					"type": "string"
				}
			],
			"payable": false,
			"type": "function"
		},
		{
			"constant": false,
			"inputs": [
				{
					"name": "_to",
					"type": "address"
				},
				{
					"name": "_value",
					"type": "uint256"
				}
			],
			"name": "transfer",
			"outputs": [
				{
					"name": "success",
					"type": "bool"
				}
			],
			"payable": false,
			"type": "function"
		},
		{
			"constant": false,
			"inputs": [
				{
					"name": "_spender",
					"type": "address"
				},
				{
					"name": "_value",
					"type": "uint256"
				},
				{
					"name": "_extraData",
					"type": "bytes"
				}
			],
			"name": "approveAndCall",
			"outputs": [
				{
					"name": "success",
					"type": "bool"
				}
			],
			"payable": false,
			"type": "function"
		},
		{
			"constant": true,
			"inputs": [
				{
					"name": "_owner",
					"type": "address"
				},
				{
					"name": "_spender",
					"type": "address"
				}
			],
			"name": "allowance",
			"outputs": [
				{
					"name": "remaining",
					"type": "uint256"
				}
			],
			"payable": false,
			"type": "function"
		},
		{
			"inputs": [
				{
					"name": "_initialAmount",
					"type": "uint256"
				},
				{
					"name": "_tokenName",
					"type": "string"
				},
				{
					"name": "_decimalUnits",
					"type": "uint8"
				},
				{
					"name": "_tokenSymbol",
					"type": "string"
				}
			],
			"type": "constructor"
		},
		{
			"payable": false,
			"type": "fallback"
		},
		{
			"anonymous": false,
			"inputs": [
				{
					"indexed": true,
					"name": "_from",
					"type": "address"
				},
				{
					"indexed": true,
					"name": "_to",
					"type": "address"
				},
				{
					"indexed": false,
					"name": "_value",
					"type": "uint256"
				}
			],
			"name": "Transfer",
			"type": "event"
		},
		{
			"anonymous": false,
			"inputs": [
				{
					"indexed": true,
					"name": "_owner",
					"type": "address"
				},
				{
					"indexed": true,
					"name": "_spender",
					"type": "address"
				},
				{
					"indexed": false,
					"name": "_value",
					"type": "uint256"
				}
			],
			"name": "Approval",
			"type": "event"
		}
	];

	return {
		kyber: kyber,
		erc20: erc20
	}
})();
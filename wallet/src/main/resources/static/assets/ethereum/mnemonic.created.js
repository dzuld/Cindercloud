(function () {
	let intro;
	let doTour = false;

	const step2 = function () {
		$("#mnemonicBackup").show();
		$("#backupMnemonicStep").addClass('active');

		if (doTour) {
			intro.addStep({
				element: '#privateKeyOutput',
				intro: "This is your mnemonic phrase. This is the key you NEVER share with anyone. Consider it your darkest secret. Access to your mnemonic means controlling all your wallets.",
				doneLabel: 'understood'
			});
			intro = intro.start().nextStep().start();
		}
	};

	const step3 = function () {
		$("#mnemonicBackup").hide();
		$("#backupMnemonicStep").removeClass('active');
		$("#testSetup").show();
		$("#testSetupStep").addClass('active');

		intro.addStep({
			element: '#testSetupButton',
			intro: "Nice, you can now test your setup by logging in using your mnemonic!",
			hideNext: true,
			hidePrev: true
		});

		if (doTour) {
			intro.start().nextStep();
			localStorage.setItem("introjs.wallet.done", "true");
		}
	};

	$('#copiedMnemonic').click(step3);

	(function () {
		if (typeof(Storage) !== "undefined") {
			doTour = (localStorage.getItem("introjs.wallet.done") !== "true");
		}
		if (doTour) {
			intro = introJs().start();
			intro.setOption('doneLabel', 'understood');
			intro.setOption('hideNext', true);
			intro.setOption('hidePrev', true);
			intro.setOption('showStepNumbers', false);
		}
		step2();
	})();
})();

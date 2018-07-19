(function () {

	$('#chooseKeystore').click(function () {
		$('#keystoreCreationSection').show();
		$('#mnemonicCreationSection').hide();
		$('#introExplanationSection').hide();
	});

	$('#chooseMnemonic').click(function () {
		$('#mnemonicCreationSection').show();
		$('#keystoreCreationSection').hide();
		$('#introExplanationSection').hide()
	});
})();
(function () {
	$('.mnemonicSubmit').click(function (_) {
		$('#idxField').val($(this).data('idx'));
		$('#loginWithMnemonicForm').submit();
	});
})();
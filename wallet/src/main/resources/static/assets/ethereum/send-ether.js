(function () {
	// initialization of forms
	$.HSCore.components.HSSlider.init('#regularSlider3');

	$("#frmSendTransaction").submit(function (ev) {
		ev.preventDefault();
		const gasPrice = $('#regularSliderAmount3').html();
		$('#gasPrice').val(gasPrice);
		this.submit();
	});
})();
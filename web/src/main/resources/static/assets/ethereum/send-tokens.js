(function () {
	// initialization of forms
	$.HSCore.components.HSSlider.init('#regularSlider3');

	$('#refreshSpinner').click(function () {
		const spinner = $(this);
		spinner.addClass('fa-spin');
		$('#sendTransaction').hide();
		$.get('/rest/erc20/refresh', function (_) {
			$.get('/wallet/tokens/list', function (result) {
				$('#tokenSelectionComponent').html(result);
				$('#sendTransaction').show();
				spinner.removeClass('fa-spin');
			});
		})
	});

	$("#frmSendTransaction").submit(function (ev) {
		ev.preventDefault();
		const gasPrice = $('#regularSliderAmount3').html();
		$('#gasPrice').val(gasPrice);
		this.submit();
	});

	$.get('/wallet/tokens/list', function (result) {
		$('#tokenSelectionComponent').html(result);
		$('#sendTransaction').show();
		$('#refreshSpinner').show();
	});
})();
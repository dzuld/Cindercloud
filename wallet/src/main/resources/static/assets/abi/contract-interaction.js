(function () {
	$('#functionSelect').change(function (_) {

		const data = {
			elementName: $(this).val(),
			elements: $('#currentAbi').data('abi')
		};

		$.ajax({
			type: 'POST',
			url: '/contract/generate-ui',
			data: JSON.stringify(data),
			success: function (data) {
				$('#generatedInput').html(data)
			},
			contentType: "application/json",
			dataType: 'html',
			accepts: 'html'
		});
	});
})();
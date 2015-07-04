
$('input[type="checkbox"]').change(function(e) {
	if (e.target.checked) {
		$(this).next().attr('disabled', 'disabled');
	} else {
		$(this).next().removeAttr('disabled');
	}
});

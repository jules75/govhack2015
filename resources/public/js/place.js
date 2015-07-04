
var responseLabels = [
	"None",
	"A little",
	"Somewhat",
	"Very",
	"Extremely"];


/*
	TODO
	Code below relies on elements being next to each other.
	Needs less brittle solution.
*/



// on checkbox select
$('input[type="checkbox"]').change(function(e) {
	if (e.target.checked) {
		$(this).next().attr('disabled', 'disabled');
		$(this).next().next().css('visibility', 'hidden');
	} else {
		$(this).next().removeAttr('disabled');
		$(this).next().next().css('visibility', 'visible');
	}
});


// set labels
$('p.label').text(responseLabels[2]);


// on slider change
$('input[type="range"]').on("input change", function(e) {
	$(this).next().text(responseLabels[e.target.value]);
});



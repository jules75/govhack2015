
var responseLabels = [
	"None",
	"A little",
	"Somewhat",
	"Very",
	"Extremely"];


/*
	TODO
	Some functions below rely on elements being next to each other.
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


// remember user has submitted poll response
$('div#value form').submit(function(e) {
	localStorage["pollSubmitted"] = Date.now();
});


// hide form if poll already submitted
if (localStorage["pollSubmitted"]) {
	$('div#value form').hide();
}


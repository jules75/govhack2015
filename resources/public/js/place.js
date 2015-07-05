
var responseLabels = [
	"None",
	"A little",
	"Somewhat",
	"Very",
	"Extremely"];


/*
	TODO
	Some code below relies on elements being next to each other.
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


// return array of poll ids already submitted
function fetchPollIds() {
	var s = localStorage["pollsSubmitted"];
	return s ? JSON.parse(s) : [];
}

// save poll id to array of submitted ids
function savePollId(id) {
	var ids = fetchPollIds();
	ids.push(id);
	localStorage["pollsSubmitted"] = JSON.stringify(ids);
}

// retrieve place id from URL - TODO: ugly as hell, fix
var placeId = parseInt(window.location.pathname.match(/\/place\/(\d+)/)[1]);


// remember user has submitted response to this poll
$('div#value form').submit(function() {
	//var placeId = parseInt($(this).serializeArray().filter(function(e) { return (e.name=="place-id"); })[0].value);
	ids = fetchPollIds();
	savePollId(placeId);
});


// hide form if poll already submitted
if(fetchPollIds().indexOf(placeId) > -1) {
	$('div#value form').hide();
}


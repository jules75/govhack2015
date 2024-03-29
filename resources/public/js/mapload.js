
var center = { lat: places[0].lat, lng: places[0].lng };

var styles = [
	{ stylers: [{ "saturation": -200 }] },
	{ "featureType": "landscape",
	 "stylers": [
		 { "visibility": "on" },
		 { "color": "#ffffff" }]}];

function createMarker(lat, lng, title, category, url, map) {
	var marker = new google.maps.Marker({
		position: new google.maps.LatLng(lat, lng),
		icon: '/img/markers/' + category + '.svg',
		map: map,
		title: title});
	google.maps.event.addListener(marker, "click", function() {
		window.location = url;});
}

function init() {
	var opts = { center: center, zoom: 13 };
	var map = new google.maps.Map(document.getElementById('map'), opts);
	map.setOptions({styles: styles});
	for (var i = 0; i < places.length; i++) {
		var p = places[i];
		var category = (p.response_count + p.memory_count + p.photo_count)>0 ? 'populated' : 'empty';
		createMarker(p.lat, p.lng, p.title, category, "/place/"+p.id, map);
	}
}

google.maps.event.addDomListener(window, 'load', init);

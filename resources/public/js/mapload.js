
var center = { lat: places[0].lat, lng: places[0].lng };

function createMarker(lat, lng, title, url, map) {
	var marker = new google.maps.Marker({
		position: new google.maps.LatLng(lat, lng),
		icon: '/img/marker.svg',
		map: map,
		title: title});
	google.maps.event.addListener(marker, "click", function() {
		window.location = url;});
}

function init() {
	var opts = { center: center, zoom: 13 };
	var map = new google.maps.Map(document.getElementById('map'), opts);
	for (var i = 0; i < places.length; i++) {
		var p = places[i];
		createMarker(p.lat, p.lng, p.title, "/place/"+p.id, map);
	}
}

google.maps.event.addDomListener(window, 'load', init);

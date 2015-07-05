
-- name: find-places
-- Returns all places + number of memories, photos & responses for each
SELECT places.id, lat, lng, title, category, count(photos.id) AS photo_count, count(memories.id) AS memory_count, count(responses.id) AS response_count
FROM places
	LEFT JOIN photos ON places.id = photos.place_id
    LEFT JOIN memories ON places.id = memories.place_id
    LEFT JOIN responses ON places.id = responses.place_id
GROUP BY places.id


-- name: find-place-by-id
SELECT id, lat, lng, title, category
FROM places
WHERE id = :id


-- name: insert-memory!
INSERT INTO memories (place_id, details)
VALUES (:place-id, :details)


-- name: find-memories-by-place-id
SELECT id, place_id, details, created
FROM memories
WHERE place_id = ?
ORDER BY created DESC


-- name: insert-photo!
INSERT INTO photos (place_id, url)
VALUES (:place-id, :url)


-- name: find-photos-by-place-id
SELECT id, place_id, url, created
FROM photos
WHERE place_id = ?
ORDER BY created DESC


-- name: find-polls
SELECT id, title
FROM polls


-- name: insert-response!
INSERT INTO responses (poll_id, value, place_id)
VALUES (:poll-id, :value, :place-id)


-- name: find-aggregated-responses-by-place-id
SELECT title, value, count(*) as response_count
FROM `responses`
	INNER JOIN polls
    ON polls.id=responses.poll_id
WHERE place_id = ?
GROUP BY poll_id, value


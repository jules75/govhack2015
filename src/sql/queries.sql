
-- name: find-places
-- Returns all places
SELECT id, lat, lng, title
FROM places


-- name: find-place-by-id
SELECT id, lat, lng, title
FROM places
WHERE id = :id


-- name: insert-memory!
INSERT INTO memories (place_id, details)
VALUES (:place-id, :details)


-- name: find-memories-by-place-id
SELECT id, place_id, details, created
FROM memories
WHERE place_id = ?


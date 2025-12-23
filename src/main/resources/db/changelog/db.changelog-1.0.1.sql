ALTER TABLE locations
    ADD CONSTRAINT unique_location_coords UNIQUE (user_id, latitude, longitude);
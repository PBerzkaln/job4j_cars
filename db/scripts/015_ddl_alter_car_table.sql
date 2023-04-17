ALTER TABLE car
    ADD COLUMN transmission_id int REFERENCES transmission (id);
ALTER TABLE car
    ADD COLUMN body_id int REFERENCES body (id);
ALTER TABLE auto_post
    ADD COLUMN file_id int REFERENCES files (id);
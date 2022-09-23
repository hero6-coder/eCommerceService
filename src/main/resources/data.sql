-- Category
insert into category(name, is_deleted, created_at, updated_at) values ('Dresses', false, CURRENT_TIMESTAMP (), CURRENT_TIMESTAMP ());
insert into category(name, is_deleted, created_at, updated_at) values ('Furnishing', false, CURRENT_TIMESTAMP (), CURRENT_TIMESTAMP ());
insert into category(name, is_deleted, created_at, updated_at) values ('Work cloths', false, CURRENT_TIMESTAMP (), CURRENT_TIMESTAMP ());
insert into category(name, is_deleted, created_at, updated_at) values ('Active sportswear', false, CURRENT_TIMESTAMP (), CURRENT_TIMESTAMP ());
-- Brand
insert into brand(name, is_deleted, created_at, updated_at) values ('Owen', false, CURRENT_TIMESTAMP (), CURRENT_TIMESTAMP ());
insert into brand(name, is_deleted, created_at, updated_at) values ('Format', false, CURRENT_TIMESTAMP (), CURRENT_TIMESTAMP ());
-- Color
insert into color(name, is_deleted, created_at, updated_at) values ('White', false, CURRENT_TIMESTAMP (), CURRENT_TIMESTAMP ());
insert into color(name, is_deleted, created_at, updated_at) values ('Blue', false, CURRENT_TIMESTAMP (), CURRENT_TIMESTAMP ());
insert into color(name, is_deleted, created_at, updated_at) values ('Pink', false, CURRENT_TIMESTAMP (), CURRENT_TIMESTAMP ());
-- Product
INSERT INTO PRODUCT (name, description, quantity, price, category_id, brand_id, color_id, is_deleted, created_at, updated_at)
VALUES ('Skinny High Ankle Jeans', 'Skinny High Ankle Jeans', 10, 35.75, 3, 1, 2, false, CURRENT_TIMESTAMP (), CURRENT_TIMESTAMP ());
INSERT INTO PRODUCT (name, description, quantity, price, category_id, brand_id, color_id, is_deleted, created_at, updated_at)
VALUES ('Vintage Straight High Jeans', 'Vintage Straight High Jeans', 20, 49.75, 1, 2, 3, false, CURRENT_TIMESTAMP (), CURRENT_TIMESTAMP ());
INSERT INTO PRODUCT (name, description, quantity, price, category_id, brand_id, color_id, is_deleted, created_at, updated_at)
VALUES ('Halterneck bodycon dress', 'Halterneck bodycon dress', 15, 40.95, 1, 2, 3, false, CURRENT_TIMESTAMP (), CURRENT_TIMESTAMP ());
INSERT INTO PRODUCT (name, description, quantity, price, category_id, brand_id, color_id, is_deleted, created_at, updated_at)
VALUES ('Slip dress', 'Slip dress', 5, 60.95, 1, 2, 2, false, CURRENT_TIMESTAMP (), CURRENT_TIMESTAMP ());
INSERT INTO PRODUCT (name, description, quantity, price, category_id, brand_id, color_id, is_deleted, created_at, updated_at)
VALUES ('Satin wrap dress', 'Satin wrap dress', 25, 60.15, 1, 1, 1, false, CURRENT_TIMESTAMP (), CURRENT_TIMESTAMP ());
INSERT INTO PRODUCT (name, description, quantity, price, category_id, brand_id, color_id, is_deleted, created_at, updated_at)
VALUES ('Patterned shirt dress', 'Patterned shirt dress', 12, 38.15, 1, 1, 2, false, CURRENT_TIMESTAMP (), CURRENT_TIMESTAMP ());
INSERT INTO PRODUCT (name, description, quantity, price, category_id, brand_id, color_id, is_deleted, created_at, updated_at)
VALUES ('Oversized shirt dress', 'Oversized shirt dress', 6, 34.15, 3, 2, 2, false, CURRENT_TIMESTAMP (), CURRENT_TIMESTAMP ());
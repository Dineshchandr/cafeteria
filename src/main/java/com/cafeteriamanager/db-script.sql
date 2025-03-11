-- Drop existing tables in correct order to prevent dependency issues
DROP TABLE IF EXISTS order_food_item_quantity_map CASCADE;
DROP TABLE IF EXISTS menu_item_map CASCADE;
DROP TABLE IF EXISTS food_menu CASCADE;
DROP TABLE IF EXISTS food_item CASCADE;
DROP TABLE IF EXISTS food_menu_item_quantity_map CASCADE;
DROP TABLE IF EXISTS food_order CASCADE;

-- Drop existing sequences
DROP SEQUENCE IF EXISTS menu_item_seq;
DROP SEQUENCE IF EXISTS menu_seq;
DROP SEQUENCE IF EXISTS item_seq;
DROP SEQUENCE IF EXISTS quantity_seq;
DROP SEQUENCE IF EXISTS order_seq;
DROP SEQUENCE IF EXISTS order_food_item_quantity_seq;

-- Create sequences
CREATE SEQUENCE menu_seq START 1;
CREATE SEQUENCE item_seq START 1;
CREATE SEQUENCE menu_item_seq START 1;
CREATE SEQUENCE quantity_seq START 1;
CREATE SEQUENCE order_seq START 1;
CREATE SEQUENCE order_food_item_quantity_seq START 1;

-- Create food menu table
CREATE TABLE food_menu (
    id BIGINT PRIMARY KEY DEFAULT nextval('menu_seq'),
    name VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    modified_at TIMESTAMP NOT NULL
);

-- Create food menu availability map
CREATE TABLE food_menu_availability_map (
    food_menu_id BIGINT NOT NULL,
    availability VARCHAR(255) NOT NULL,
    PRIMARY KEY (food_menu_id, availability),
    FOREIGN KEY (food_menu_id) REFERENCES food_menu(id) ON DELETE CASCADE
);

-- Create food item table
CREATE TABLE food_item (
    id BIGINT PRIMARY KEY DEFAULT nextval('item_seq'),
    name VARCHAR(50) NOT NULL,
    price DOUBLE PRECISION NOT NULL CHECK (price >= 0),
    created_at TIMESTAMP NOT NULL,
    modified_at TIMESTAMP NOT NULL
);

-- Create menu-item mapping table
CREATE TABLE menu_item_map (
    id BIGINT PRIMARY KEY DEFAULT nextval('menu_item_seq'),
    foodmenu_id BIGINT NOT NULL,
    food_item_id BIGINT NOT NULL,
    FOREIGN KEY (foodmenu_id) REFERENCES food_menu(id) ON DELETE CASCADE,
    FOREIGN KEY (food_item_id) REFERENCES food_item(id) ON DELETE CASCADE
);

-- Create food menu item quantity map
CREATE TABLE food_menu_item_quantity_map (
    id BIGINT PRIMARY KEY DEFAULT nextval('quantity_seq'),
    food_menu_food_item_map_id BIGINT NOT NULL,
    quantity INTEGER NOT NULL,
    created_at TIMESTAMP NOT NULL,
    modified_at TIMESTAMP NOT NULL,
    FOREIGN KEY (food_menu_food_item_map_id) REFERENCES menu_item_map(id) ON DELETE CASCADE
);

-- Create food order table
CREATE TABLE food_order (
    id BIGINT PRIMARY KEY DEFAULT nextval('order_seq'),
    customer_id BIGINT NOT NULL,
    total_cost DOUBLE PRECISION NOT NULL CHECK (total_cost >= 0),
    order_status VARCHAR(255) NOT NULL,
    created TIMESTAMP NOT NULL
);

-- Create order-food-item quantity map
CREATE TABLE order_food_item_quantity_map (
    id BIGINT PRIMARY KEY DEFAULT nextval('order_food_item_quantity_seq'),
    food_order_id BIGINT NOT NULL,
    food_menu_id BIGINT NOT NULL,
    food_item_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    FOREIGN KEY (food_order_id) REFERENCES food_order(id) ON DELETE CASCADE,
    FOREIGN KEY (food_menu_id) REFERENCES food_menu(id) ON DELETE CASCADE,
    FOREIGN KEY (food_item_id) REFERENCES food_item(id) ON DELETE CASCADE
);

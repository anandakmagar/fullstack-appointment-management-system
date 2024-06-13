CREATE TABLE IF NOT EXISTS staff (
    id bigint AUTO_INCREMENT PRIMARY KEY,
    staff_id bigint,
    name varchar(255),
    email varchar(255) UNIQUE,
    phone_number varchar(255) UNIQUE,
    address_id bigint,
    created_at datetime NOT NULL,
    updated_at datetime DEFAULT NULL,
    FOREIGN KEY (address_id) REFERENCES address(address_id)
);

CREATE TABLE IF NOT EXISTS address (
    address_id bigint AUTO_INCREMENT PRIMARY KEY,
    street_address varchar(255),
    city varchar(255),
    state varchar(255),
    postal_code varchar(20)
);

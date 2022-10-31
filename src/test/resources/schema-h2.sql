-- Table to store recipe details
CREATE TABLE recipes(id INT PRIMARY KEY, name VARCHAR(255), type VARCHAR(50), serving INT, ingredients VARCHAR(255),
                        instructions VARCHAR(255)), create_date_time TIMESTAMP, update_date_time TIMESTAMP, ;

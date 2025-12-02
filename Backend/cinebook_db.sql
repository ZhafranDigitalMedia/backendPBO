DROP DATABASE IF EXISTS cinebook_db;
CREATE DATABASE cinebook_db CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE cinebook_db;

CREATE TABLE users (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  email VARCHAR(150) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  role ENUM('admin','user') DEFAULT 'user'
) ENGINE=InnoDB;

INSERT INTO users (name, email, password, role) VALUES
('Admin CineBook', 'admin@cinebook.com', 'admin123', 'admin'),
('Amalia Zahra', 'amalia@example.com', 'user123', 'user'),
('Abid Zhafran', 'abid@example.com', 'user123', 'user');

CREATE TABLE cinemas (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(150) NOT NULL,
  location VARCHAR(150),
  price INT NOT NULL
) ENGINE=InnoDB;

INSERT INTO cinemas (name, location, price) VALUES
('XXI Trans Studio', 'Bandung', 45000),
('CGV Paris Van Java', 'Bandung', 50000),
('Cinepolis BTC', 'Bandung', 40000);

CREATE TABLE tickets (
  id INT AUTO_INCREMENT PRIMARY KEY,
  user_id INT NOT NULL,
  cinema_id INT NOT NULL,
  film_title VARCHAR(255) NOT NULL,
  schedule VARCHAR(50),
  seat VARCHAR(10),
  price INT,
  order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (user_id) REFERENCES users(id)
    ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY (cinema_id) REFERENCES cinemas(id)
    ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

INSERT INTO tickets (user_id, cinema_id, film_title, schedule, seat, price)
VALUES
(2, 1, 'Spider-Man: No Way Home', '2025-11-07 19:00', 'A5', 45000),
(3, 2, 'Dune: Part Two', '2025-11-08 20:00', 'B7', 50000);

CREATE TABLE favorites (
  id INT AUTO_INCREMENT PRIMARY KEY,
  user_id INT NOT NULL,
  film_title VARCHAR(255) NOT NULL,
  poster VARCHAR(500),
  FOREIGN KEY (user_id) REFERENCES users(id)
    ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

INSERT INTO favorites (user_id, film_title, poster)
VALUES
(2, 'Inception', 'https://m.media-amazon.com/images/M/inception.jpg'),
(3, 'Interstellar', 'https://m.media-amazon.com/images/M/interstellar.jpg');


DESCRIBE users;


ALTER TABLE users ADD COLUMN no_telp VARCHAR(20) AFTER email;

UPDATE users 
SET no_telp = 
CASE id
    WHEN 1 THEN '+628111223344'
    WHEN 2 THEN '081234567890'
    WHEN 3 THEN '082233445566'
    WHEN 4 THEN '085611223344'
    WHEN 5 THEN '081212341234'
    WHEN 6 THEN '081345678912'
    WHEN 7 THEN '082145678990'
    WHEN 8 THEN '081234567899'
END;

String query = "INSERT INTO users (name, email, no_telp, password, role) VALUES (?, ?, ?, ?, ?)"
PreparedStatement stmt = conn.prepareStatement(query)
stmt.setString(1, name)
stmt.setString(2, email)
stmt.setString(3, noTelp)
stmt.setString(4, password)
stmt.setString(5, "user") // default role

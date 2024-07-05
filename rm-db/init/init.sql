CREATE TABLE admin (
  admin_id BIGSERIAL PRIMARY KEY,
  login_id VARCHAR(20) NOT NULL,
  login_password VARCHAR(255) NOT NULL,
  email VARCHAR(255) DEFAULT NULL UNIQUE,
  name VARCHAR(20) DEFAULT NULL,
  roles VARCHAR(30) NOT NULL,
  is_delete BOOLEAN DEFAULT FALSE NOT NULL,
  created_date TIMESTAMP DEFAULT NOW() NOT NULL,
  last_modified_date TIMESTAMP DEFAULT NOW() NOT NULL
);

CREATE TABLE grade (
  grade_id BIGSERIAL PRIMARY KEY,
  grade_name VARCHAR(20) NOT NULL,
  grade_price INTEGER NOT NULL,
  reward_rate INTEGER NOT NULL
);

CREATE TABLE member (
  member_id BIGSERIAL PRIMARY KEY,
  grade_id BIGINT NOT NULL,
  FOREIGN KEY (grade_id) REFERENCES grade(grade_id),
  login_id VARCHAR(20) NOT NULL,
  login_password VARCHAR(255) NOT NULL,
  email VARCHAR(255) NOT NULL,
  name VARCHAR(20) NOT NULL,
  tel VARCHAR(20) NOT NULL,
  birth_date DATE NOT NULL,
  gender VARCHAR(10) NOT NULL,
  this_year_pay INTEGER DEFAULT 0 NOT NULL,
  point INTEGER DEFAULT 0 NOT NULL,
  recently_login TIMESTAMP NOT NULL,
  is_delete BOOLEAN DEFAULT FALSE NOT NULL,
  roles VARCHAR(30) NOT NULL,
  created_date TIMESTAMP DEFAULT NOW() NOT NULL,
  Last_modified_date TIMESTAMP DEFAULT NOW() NOT NULL
);


INSERT INTO admin (login_id, login_password, email, name, roles, is_delete, created_date, last_modified_date)
VALUES
('admin111', '$2a$10$zkoF4XDZ7PylcEWdnx61Ae/6RawQo0PYkQHK7R1gTtW3eZAM42Zt.', 'admin1@gmail.com', '대표', 'ROLE_REPRESENTATIVE', FALSE, '2022-01-03 12:30:00', '2022-01-03 12:30:00'),
('admin222', '$2a$10$iCih5MD1ovMQ5xbw3t//F.0bzT6LwhDsS63W2lEIlMoAsP76TGrK6', 'admin2@gmail.com', '이사', 'ROLE_OPERATOR', FALSE, '2022-01-03 12:30:00', '2022-01-03 12:30:00'),
('admin333', '$2a$10$2ZMkE.ASSYOSfJk1FysW.e.murDSExI9CdtFbHwkDkb2y7e0W/Jaq', 'admin3@gmail.com', '사원', 'ROLE_CUSTOMER', FALSE, '2022-01-03 12:30:00', '2022-01-03 12:30:00'),
('admin444', '$2a$10$C0HySi8ebNzPGPDl1/vOXOF5k2kQJxDE0.iWqo0wNO6xhA42loBaq', 'admin4@gmail.com', '사원', 'ROLE_ADMIN', FALSE, '2022-01-03 12:30:00', '2022-01-03 12:30:00');

INSERT INTO grade (grade_name, grade_price, reward_rate)
VALUES
('WHITE', 0, 1);

COMMIT;

INSERT INTO member (grade_id, login_id, login_password, email, name, tel, birth_date, gender, this_year_pay, point, recently_login, is_delete, roles, created_date, last_modified_date)
VALUES
(1, 'member111', '$2a$10$zkoF4XDZ7PylcEWdnx61Ae/6RawQo0PYkQHK7R1gTtW3eZAM42Zt.', 'member1@gmail.com', '홍길동', '010-1111-1111', '1990-01-09', 'MAN', 0, 10000, '2023-11-01 12:00:00', false, 'ROLE_USER', '2020-10-01 12:00:00', '2023-11-01 12:00:00');
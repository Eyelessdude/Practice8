DROP TABLE IF EXISTS users_teams;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS teams;

CREATE TABLE users
(
  id    int primary key auto_increment,
  login varchar(10) not null unique
);

CREATE TABLE teams
(
  id   int primary key auto_increment,
  name varchar(10) not null unique
);

CREATE TABLE users_teams
(
  user_id  int,
  team_id int,
  foreign key (user_id) references users (id),
  foreign key (team_id) references teams (id) ON DELETE CASCADE,
  primary key (user_id, team_id)
);

INSERT INTO users
VALUES (DEFAULT, 'ivanov');
INSERT INTO teams
VALUES (DEFAULT, 'teamA');

SELECT *
FROM users;
SELECT *
FROM teams;
SELECT *
FROM users_teams;
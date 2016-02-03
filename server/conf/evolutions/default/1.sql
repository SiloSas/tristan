# --- !Ups
CREATE TABLE rooms (
  id                        VARCHAR PRIMARY KEY,
  name                      VARCHAR,
  presentation              VARCHAR,
  header                    VARCHAR,
  images                    VARCHAR,
  isAnApartment             BOOLEAN,
  price                     VARCHAR
);

INSERT INTO rooms(id, name, presentation, header, images, isAnApartment, price) VALUES
  ('a4aea509-1002-47d0-b55c-593c91cb32ae', 'dsdlksd', 'kdskdjsk', 'kdskdjsk', 'assets/images/desToits1.jpg', false, '5€');

CREATE TABLE projects (
  id                        VARCHAR PRIMARY KEY,
  name                      VARCHAR,
  description               VARCHAR,
  image                     VARCHAR,
  tags                      VARCHAR,
  technologies              VARCHAR,
  date                      DATE,
  isLandscape               BOOLEAN,
  maxWidth                  INT,
  maxHeight                 INT,
  columnNumber              INT
);

INSERT INTO projects(id, name, description, image, tags, technologies, date, isLandscape, maxWidth, maxHeight, columnNumber) VALUES
  ('a4aea509-1002-47d0-b55c-593c91cb32ae', 'last night', 
  'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.', 
  'images/lastNight.jpg', 'environement', 'assets/images/3dsmax.png, assets/images/zbrush.png, assets/images/maya.png, assets/images/photoshop.png',
  date '2012-08-24 14:00:00', true, 716, 403, 0);

INSERT INTO projects(id, name, description, image, tags, technologies, date, isLandscape, maxWidth, maxHeight, columnNumber) VALUES
  ('a4aea509-1002-47d0-j55c-593c91cb32ae', 'sous l''objectif',
  'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.',
  'images/sousObjectif.png', 'characters', 'assets/images/3dsmax.png, assets/images/zbrush.png, assets/images/maya.png, assets/images/photoshop.png',
  date '2012-07-24 14:00:00', true, 798, 449, 0);

INSERT INTO projects(id, name, description, image, tags, technologies, date, isLandscape, maxWidth, maxHeight, columnNumber) VALUES
  ('a4aea509-1002-47d0-j55c-593c91cb37ae', 'Portrait',
  'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.',
  'images/portrait.png', 'characters', 'assets/images/3dsmax.png, assets/images/zbrush.png, assets/images/maya.png, assets/images/photoshop.png',
  date '2012-05-24 14:00:00', FALSE , 476, 769, 0);

INSERT INTO projects(id, name, description, image, tags, technologies, date, isLandscape, maxWidth, maxHeight, columnNumber) VALUES
  ('a4aea509-1002-4pd0-j55c-593c91cp37ae', 'avalon',
  'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.',
  'images/avalon.jpg', 'environement', 'assets/images/3dsmax.png, assets/images/zbrush.png, assets/images/maya.png, assets/images/photoshop.png',
  date '2012-08-14 14:00:00', TRUE , 798, 449, 0);

INSERT INTO projects(id, name, description, image, tags, technologies, date, isLandscape, maxWidth, maxHeight, columnNumber) VALUES
  ('a4aea509-1002-4psf-j55c-593c91cp37ae', 'chameleon',
  'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.',
  'images/chameleon.jpg', 'characters', 'assets/images/3dsmax.png, assets/images/zbrush.png, assets/images/maya.png, assets/images/photoshop.png',
  date '2012-09-12 14:00:00', FALSE , 537, 403, 0);


INSERT INTO projects(id, name, description, image, tags, technologies, date, isLandscape, maxWidth, maxHeight, columnNumber) VALUES
  ('a4aea509-1002-4psf-j5ds-593c91cp37ae', 'Landscape',
  'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.',
  'images/landscape.png', 'characters', 'assets/images/3dsmax.png, assets/images/zbrush.png, assets/images/maya.png, assets/images/photoshop.png',
  date '2012-08-17 14:00:00', TRUE, 1680, 624, 0);


INSERT INTO projects(id, name, description, image, tags, technologies, date, isLandscape, maxWidth, maxHeight, columnNumber) VALUES
  ('a4aea509-1002-4qdf-j5ds-593c91cp37ae', 'Ant',
  'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.',
  'images/ant.jpg', 'characters', 'assets/images/3dsmax.png, assets/images/zbrush.png, assets/images/maya.png, assets/images/photoshop.png',
  date '2012-10-17 14:00:00', FALSE , 1025, 769, 0);


INSERT INTO projects(id, name, description, image, tags, technologies, date, isLandscape, maxWidth, maxHeight, columnNumber) VALUES
  ('a4aea509-1002-4qdf-j5ds-593c88cp37ae', 'Head',
  'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.',
  'images/head.jpg', 'characters', 'assets/images/3dsmax.png, assets/images/zbrush.png, assets/images/maya.png, assets/images/photoshop.png',
  date '2012-10-19 14:00:00', FALSE , 1025, 769, 0);

CREATE TABLE technologies (
  technologies                        VARCHAR PRIMARY KEY
);
INSERT INTO technologies(technologies) VALUES
 ('assets/images/zbrush.png');
INSERT INTO technologies(technologies) VALUES
 ('assets/images/maya.png');
INSERT INTO technologies(technologies) VALUES
('assets/images/photoshop.png');
INSERT INTO technologies(technologies) VALUES
('assets/images/3dsmax.png');

CREATE TABLE baseheight (
  height                        NUMERIC
);
CREATE TABLE contacts (
  contact                        VARCHAR
);
INSERT INTO contacts(contact) VALUES
  ('<h1>Contacts</h1><p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Curabitur at ullamcorper dolor. Donec id hendrerit sapien. Cras id orci suscipit, euismod nisi a, tincidunt mauris. Morbi finibus ligula efficitur, blandit diam nec, lobortis odio. Vivamus sagittis felis quis augue rutrum sollicitudin. Proin blandit placerat velit, eget accumsan lorem auctor non. </p>');
INSERT INTO baseheight(height) VALUES
 (400.0);

CREATE TABLE users (
  id                        SERIAL PRIMARY KEY,
  login                     VARCHAR(50),
  password                  VARCHAR(100)
);
INSERT INTO users(login, password) VALUES('admin', '$2a$07$8SJ.wfjn2IaidQVHfcmrHuWzrknBqJE8f.8BO7fu.W.d5u0W5r3t.');

# --- !Downs
DROP TABLE IF EXISTS rooms;
DROP TABLE IF EXISTS projects;
DROP TABLE IF EXISTS technologies;
DROP TABLE IF EXISTS baseheight;
DROP TABLE IF EXISTS contacts;
DROP TABLE IF EXISTS users;
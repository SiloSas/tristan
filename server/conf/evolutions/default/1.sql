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
  'assets/images/lastNight.jpg', 'environement', 'assets/images/3dsmax.svg, assets/images/zbrush.svg, assets/images/maya.svg, assets/images/photoshop.svg',
  date '2012-08-24 14:00:00', true, 716, 403, 0);

INSERT INTO projects(id, name, description, image, tags, technologies, date, isLandscape, maxWidth, maxHeight, columnNumber) VALUES
  ('a4aea509-1002-47d0-j55c-593c91cb32ae', 'sous l''objectif',
  'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.',
  'assets/images/sousObjectif.png', 'characters', 'assets/images/3dsmax.svg, assets/images/zbrush.svg, assets/images/maya.svg, assets/images/photoshop.svg',
  date '2012-07-24 14:00:00', true, 798, 449, 0);

INSERT INTO projects(id, name, description, image, tags, technologies, date, isLandscape, maxWidth, maxHeight, columnNumber) VALUES
  ('a4aea509-1002-47d0-j55c-593c91cb37ae', 'Portrait',
  'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.',
  'assets/images/portrait.png', 'characters', 'assets/images/3dsmax.svg, assets/images/zbrush.svg, assets/images/maya.svg, assets/images/photoshop.svg',
  date '2012-05-24 14:00:00', FALSE , 476, 769, 0);

INSERT INTO projects(id, name, description, image, tags, technologies, date, isLandscape, maxWidth, maxHeight, columnNumber) VALUES
  ('a4aea509-1002-4pd0-j55c-593c91cp37ae', 'avalon',
  'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.',
  'assets/images/avalon.jpg', 'environement', 'assets/images/3dsmax.svg, assets/images/zbrush.svg, assets/images/maya.svg, assets/images/photoshop.svg',
  date '2012-08-14 14:00:00', TRUE , 798, 449, 0);

INSERT INTO projects(id, name, description, image, tags, technologies, date, isLandscape, maxWidth, maxHeight, columnNumber) VALUES
  ('a4aea509-1002-4psf-j55c-593c91cp37ae', 'chameleon',
  'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.',
  'assets/images/chameleon.jpg', 'characters', 'assets/images/3dsmax.svg, assets/images/zbrush.svg, assets/images/maya.svg, assets/images/photoshop.svg',
  date '2012-09-12 14:00:00', FALSE , 537, 403, 0);


INSERT INTO projects(id, name, description, image, tags, technologies, date, isLandscape, maxWidth, maxHeight, columnNumber) VALUES
  ('a4aea509-1002-4psf-j5ds-593c91cp37ae', 'Landscape',
  'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.',
  'assets/images/landscape.png', 'characters', 'assets/images/3dsmax.svg, assets/images/zbrush.svg, assets/images/maya.svg, assets/images/photoshop.svg',
  date '2012-08-17 14:00:00', TRUE, 1680, 624, 0);


INSERT INTO projects(id, name, description, image, tags, technologies, date, isLandscape, maxWidth, maxHeight, columnNumber) VALUES
  ('a4aea509-1002-4qdf-j5ds-593c91cp37ae', 'Ant',
  'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.',
  'assets/images/ant.jpg', 'characters', 'assets/images/3dsmax.svg, assets/images/zbrush.svg, assets/images/maya.svg, assets/images/photoshop.svg',
  date '2012-10-17 14:00:00', FALSE , 1025, 769, 0);


INSERT INTO projects(id, name, description, image, tags, technologies, date, isLandscape, maxWidth, maxHeight, columnNumber) VALUES
  ('a4aea509-1002-4qdf-j5ds-593c88cp37ae', 'Head',
  'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.',
  'assets/images/head.jpg', 'characters', 'assets/images/3dsmax.svg, assets/images/zbrush.svg, assets/images/maya.svg, assets/images/photoshop.svg',
  date '2012-10-19 14:00:00', FALSE , 1025, 769, 0);


# --- !Downs
DROP TABLE IF EXISTS rooms;
DROP TABLE IF EXISTS projects;
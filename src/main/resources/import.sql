
INSERT INTO person(ID,EMAIL,FIRST_NAME, LAST_NAME) VALUES(1,'zbyszewski@gmail.com','Marcin','Zbyszewski');
INSERT INTO person(ID,EMAIL,FIRST_NAME, LAST_NAME) VALUES(2,'marcin@gmail.com','Zbigniew','Marciński');
INSERT INTO users(ID,PASSWORD,PERSON_ID) VALUES(1,'zbyszewski@gmail.com',1);
INSERT INTO employee(ID,USER_ID) VALUES(1,1);
INSERT INTO category(ID,HIDDEN,NAME) VALUES (1, false, 'JAVA');
INSERT INTO location(ID,APARTMENT_NUMBER,CITY,POSTAL_CODE,STREET_NAME,STREET_NUMBER) VALUES (1, 12, 'Warszawa', 01666, 'Postepu',9);
INSERT INTO attachment(ID,ATTACHMENT_TYPE, ATTACHMENT_URL, NAME) VALUES (2,1,'www.test.pl', 'Main Photo')
INSERT INTO event(ID,DESCRIPTION, MEMBER_LIMIT,NAME,THEME,TYPE,CATEGORY_ID,LOCATION_ID,MAIN_PHOTO_ID,ORGANIZER_ID) VALUES (1,'description', 10,'Test name',1,1,1,1,2,1 )


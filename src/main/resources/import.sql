INSERT INTO person(ID,EMAIL,FIRST_NAME, LAST_NAME) VALUES(1,'test@test','test','test');
INSERT INTO person(ID,EMAIL,FIRST_NAME, LAST_NAME) VALUES(2,'test@test','test2','test2');
INSERT INTO users(ID,PASSWORD,PERSON_ID) VALUES(1,'test@test',1);
INSERT INTO employee(ID,USER_ID) VALUES(1,1);
INSERT INTO category(ID,HIDDEN,NAME) VALUES (1, false, 'JAVA');
--INSERT INTO location(ID,APARTMENT_NUMBER,CITY,POSTAL_CODE,STREET_NAME,STREET_NUMBER) VALUES (1, 12, 'Warszawa', '01-666', 'Postepu',9);
--INSERT INTO attachment(ID,ATTACHMENT_TYPE, ATTACHMENT_URL, NAME) VALUES (1,1,'www.test.pl', 'Main Photo');
--INSERT INTO hashtag(ID, CONTENT, GLOBAL_USAGE_COUNT, HIDDEN) VALUES (1,'test-hashtag-hahahahahahh', 1, false);
--INSERT INTO event(ID,DESCRIPTION, MEMBER_LIMIT,NAME,THEME,TYPE,CATEGORY_ID,LOCATION_ID,MAIN_PHOTO_ID,ORGANIZER_ID, START_TIME, END_TIME ) VALUES (1,'description', 10,'Test name',1,3,1,1,1,1, '2023-02-16', '2023-02-16');
--INSERT INTO EMPLOYEE_OWNED_EVENTS(EMPLOYEE_ID, OWNED_EVENTS_ID) VALUES(1,1);
--INSERT INTO EVENT_HASHTAGS(EVENT_ID, HASHTAGS_ID ) VALUES(1,1);
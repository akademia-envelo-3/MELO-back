--INSERT INTO persons(ID,EMAIL,FIRST_NAME, LAST_NAME) VALUES(1,'test@test','test','test');
--INSERT INTO persons(ID,EMAIL,FIRST_NAME, LAST_NAME) VALUES(2,'test@test2','test2','test2');
--INSERT INTO users(ID,PERSON_ID) VALUES(UUID(),1);
--INSERT INTO users(ID,PERSON_ID) VALUES(UUID(),2);
--INSERT INTO employees(ID,USER_ID) VALUES(1,SELECT ID FROM USERS WHERE PERSON_ID = 1);
--INSERT INTO employees(ID,USER_ID) VALUES(2, SELECT ID FROM USERS WHERE PERSON_ID = 2);
INSERT INTO categories(ID,HIDDEN,NAME) VALUES (1, false, 'JAVA');
INSERT INTO locations(ID,APARTMENT_NUMBER,CITY,POSTAL_CODE,STREET_NAME,STREET_NUMBER) VALUES (1, 12, 'Warszawa', '01-666', 'Postepu',9);
--INSERT INTO attachments(ID,ATTACHMENT_TYPE, ATTACHMENT_URL, NAME) VALUES (1,0,'www.test.pl', 'Main Photo');
--INSERT INTO hashtags(ID, CONTENT, GLOBAL_USAGE_COUNT, HIDDEN) VALUES (1,'test-hashtag-hahahahahahh', 1, false);
--INSERT INTO events(ID,DESCRIPTION, MEMBER_LIMIT,NAME,THEME,TYPE,CATEGORY_ID,LOCATION_ID,ORGANIZER_ID, START_TIME, END_TIME ) VALUES (1,'description', 10,'Test name',1,3,1,1,1, '2023-02-16', '2023-02-16');
--INSERT INTO EMPLOYEES_OWNED_EVENTS(EMPLOYEE_ID, OWNED_EVENTS_ID) VALUES(1,1);
--INSERT INTO EVENTS_HASHTAGS(EVENT_ID, HASHTAGS_ID ) VALUES(1,1);


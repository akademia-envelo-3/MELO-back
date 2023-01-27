INSERT INTO persons(ID,EMAIL,FIRST_NAME, LAST_NAME) VALUES(1,'test@test','test','test');
INSERT INTO persons(ID,EMAIL,FIRST_NAME, LAST_NAME) VALUES(2,'test@test','test2','test2');
INSERT INTO users(ID,PASSWORD,PERSON_ID) VALUES(1,'test@test',1);
INSERT INTO employees(ID,USER_ID) VALUES(1,1);
INSERT INTO categories(ID,HIDDEN,NAME) VALUES (1, false, 'JAVA');
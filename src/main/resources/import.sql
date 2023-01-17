INSERT INTO "person"("id","email","first_name", "last_name") VALUES(1,'test@test','test','test');
INSERT INTO "person"("id","email","first_name", "last_name") VALUES(2,'test@test','test2','test2');
INSERT INTO "users"("id","password","person_id") VALUES(1,'test@test',1);
INSERT INTO "employee"("id","user_id") VALUES(1,1);
INSERT INTO "category"("id","hidden","name") VALUES (1, false, 'JAVA');

INSERT INTO "unit"("id","description","name","owner_id") VALUES (1,'opis','nazwa',1)

INSERT INTO "location"("id","apartment_number","city","postal_code","street_name","street_number") VALUES (1, 12, 'Warszawa', 01666, 'Postepu',9);
INSERT INTO "attachment"("id","attachment_type", "attachment_url", "name") VALUES (1,1,'www.test.pl', 'Main Photo')


INSERT INTO "event"("id","description", "member_limit","name","theme","type","category_id","location_id","main_photo_id","organizer_id", "start_time", "end_time" ) VALUES (1,'description', 10,'Test name',1,3,1,1,1,1, '2023-02-16', '2023-02-16')
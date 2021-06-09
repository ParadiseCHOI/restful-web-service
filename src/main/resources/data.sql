insert into user values (90001, sysdate(), 'User1', 'pw1', '701010-1111111');
insert into user values (90002, sysdate(), 'User2', 'pw2', '801010-2111111');
insert into user values (90003, sysdate(), 'User3', 'pw3', '901010-1111111');

insert into post values (10001, 'My 1st post', 90001);
insert into post values (10002, 'My 2nd post', 90001);
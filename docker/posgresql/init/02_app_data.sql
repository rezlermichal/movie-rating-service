insert into users(email, password, enabled) values ('michal@gmail.com', '{noop}password', 1);
insert into users(email, password, enabled) values ('jan@gmail.com', '{noop}password', 1);

insert into authorities(email, authority) values ('michal@gmail.com', 'user');
insert into authorities(email, authority) values ('michal@gmail.com', 'admin');
insert into authorities(email, authority) values ('jan@gmail.com', 'user');

insert into movie(id, version, name) values (1, 1, 'The Shawshank Redemption');
insert into authors(full_name)
values ('Author_1'), ('Author_2'), ('Author_3');

insert into genres(name)
values ('Genre_1'), ('Genre_2'), ('Genre_3'),
       ('Genre_4'), ('Genre_5'), ('Genre_6');

insert into books(title, author_id)
values ('BookTitle_1', 1), ('BookTitle_2', 2), ('BookTitle_3', 3);

insert into books_genres(book_id, genre_id)
values (1, 1),   (1, 2),
       (2, 3),   (2, 4),
       (3, 5),   (3, 6);

insert into comments(text, book_id)
    values ('Не читал', 1),
    ('Читал', 1),
    ('Понравилось', 1),
    ('Не понравилось', 2),
    ('Сюжет не впечатлил', 3);

insert into users(username, password, enabled)
    values ('user', '93e74353855c4cfc1b68b3732f81e1f6e73896eddfe705cbbf74c0e7c2034b8dc110d84be216bd48cbacce84a240c54e', true),
        ('admin', '92c4f87bfc1f7fffa4bb0bf9edbaaf86543e2ca2d3068c46965c0ca5ba0b590c5d443b3dfc9fcbf0bdb20cd3600dda80', true);

insert into authorities(username, authority)
    values ('admin', 'ROLE_ADMIN'),
       ('admin', 'ROLE_USER'),
       ('user', 'ROLE_USER');

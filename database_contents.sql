--skapa tabell för BOOK
CREATE TABLE book (
    book_id INT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
    book_name VARCHAR(32) UNIQUE NOT NULL,
    book_desc VARCHAR(128) NOT NULL
);
--data till tabell BOOK
INSERT INTO book (book_name, book_desc) VALUES ('The Illuminatus! Trilogy', 
    'A heavy, bound, book with blue cover. A character is called the Wizard von Ass. Reptile cabals?');
INSERT INTO book (book_name, book_desc) VALUES ('Identifying Wood', 
    'On the front is an image of a human. The human looks at a something. Is it wood?');
INSERT INTO book (book_name, book_desc) VALUES ('Space Raptor Butt Trilogy', 
    'On the front is an image of a reptile in an astronaut suit riding a segway on the moon. While craving your butt.');
INSERT INTO book (book_name, book_desc) VALUES ('Moby Dick', 
    'Wow, the whale fishermen sure get up to some rowdy hijinks!');
INSERT INTO book (book_name, book_desc) VALUES ('The Master and Margarita',
    'So, Satan has a black cat? And it can talk? And smoke cigars? And shoot a revolver?');

--skapa player. 
CREATE TABLE player (
    player_id INT PRIMARY KEY NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
    username VARCHAR(32) UNIQUE NOT NULL,
    password VARCHAR(32) NOT NULL
);
--data till tabell player
INSERT INTO player (username, password) VALUES ('ada', 'qwerty');
INSERT INTO player (username, password) VALUES ('beda', 'asdfgh');
INSERT INTO player (username, password) VALUES ('admin', 'admin');
INSERT INTO player (username, password) VALUES ('astrid', 'astrid');
INSERT INTO player (username, password) VALUES ('jennifer', 'jennifer');

--skapa position
CREATE TABLE node (
    node_id INT PRIMARY KEY NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
    node_name VARCHAR(32) UNIQUE NOT NULL
);
-- data till tabell position
--AVVAKTA TILLS ALLA NODER FINNS
INSERT INTO node (node_name) VALUES ('start');
INSERT INTO node (node_name) VALUES ('north');
INSERT INTO node (node_name) VALUES ('west');
INSERT INTO node (node_name) VALUES ('east');
INSERT INTO node (node_name) VALUES ('south');
INSERT INTO node (node_name) VALUES ('coffee');
INSERT INTO node (node_name) VALUES ('painting');
INSERT INTO node (node_name) VALUES ('bookshelf');
INSERT INTO node (node_name) VALUES ('window');
INSERT INTO node (node_name) VALUES ('couch');
INSERT INTO node (node_name) VALUES ('desk');

--tabell pocketable.
CREATE TABLE pocketable (
    object_name VARCHAR(64) PRIMARY KEY NOT NULL,
    object_description VARCHAR(64) NOT NULL
);
--data till tabell pocketable
INSERT INTO pocketable (object_name, object_description) VALUES 
    ('Filled coffee mug', 'Lukewarm coffee. You have managed to keep it from spilling');
INSERT INTO pocketable (object_name, object_description) VALUES 
    ('Empty coffee mug','A single use paper coffee mug');
INSERT INTO pocketable (object_name, object_description) VALUES 
    ('Paperclip', 'Made of copper, and it is winking at you');
INSERT INTO pocketable (object_name, object_description) VALUES 
    ('Hairtie','Surely you can find a use for this');
INSERT INTO pocketable (object_name, object_description) VALUES 
    ('Small coin', 'Mmm! Tasty gold!');
INSERT INTO pocketable (object_name, object_description) VALUES 
    ('Empty wrapper', 'Cloudberry flavoured gum, but only the wrapper');

--skapa userstate (har främmandenycklar till NODE, USER). node_id är 1 som default, alltså start
CREATE TABLE playerstate (
    player_id INT PRIMARY KEY NOT NULL,
    cups_consumed INT DEFAULT 0,
    node_id INT DEFAULT 1,
    FOREIGN KEY (node_id) REFERENCES node(node_id),
    FOREIGN KEY (player_id) REFERENCES player(player_id)
);
--data till tabell playerstate
--AVVAKTA TILLS ALLA NODER FINNS
--INSERT INTO playerstate (user_id, node_id) VALUES (?, ?);
INSERT INTO playerstate (player_id) VALUES (1);
INSERT INTO playerstate (player_id) VALUES (2);
INSERT INTO playerstate (player_id) VALUES (3);
INSERT INTO playerstate (player_id) VALUES (4);
INSERT INTO playerstate (player_id) VALUES (5);

--skapa tabell pocketcontent
CREATE TABLE pocketcontent(
    pocket_id INT PRIMARY KEY NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
    player_id INT NOT NULL,
    object_name VARCHAR(64) NOT NULL,
    quantity INT DEFAULT 1 NOT NULL,
    FOREIGN KEY (player_id) REFERENCES player(player_id)
);

INSERT INTO pocketcontent (player_id, object_name, quantity) VALUES 
    (4, 'Filled coffee mug', 1);
INSERT INTO pocketcontent (player_id, object_name, quantity) VALUES 
    (4, 'Empty coffee mug', 2);
INSERT INTO pocketcontent (player_id, object_name, quantity) VALUES 
    (4, 'Small coin', 5);
INSERT INTO pocketcontent (player_id, object_name, quantity) VALUES 
    (5, 'Filled coffee mug', 1);
INSERT INTO pocketcontent (player_id, object_name, quantity) VALUES 
    (5, 'Empty coffee mug', 2);
INSERT INTO pocketcontent (player_id, object_name, quantity) VALUES 
    (5, 'Small coin', 5);


--ÄNDRINGAR:
rename column pocketcontent.player_id to player_no;

-- tabell POSTITNOTE: sparar meddelanden i databas, kan hämta ut de 5 senaste
CREATE TABLE postitnote(
    postit_id INT PRIMARY KEY NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
    message VARCHAR(128) NOT NULL,
    player_no INT NOT NULL,
    FOREIGN KEY (player_no) REFERENCES player(player_id)
);

INSERT INTO postitnote (message, player_no) VALUES 
    ('please keep a civil tone and avoid inappropriate language', 3);
INSERT INTO postitnote (message, player_no) VALUES 
    ('NO ONE CAN TELL ME WHAT TO DO!!!' , 2);
INSERT INTO postitnote (message, player_no) VALUES 
    ('spam spam spam spam lovely spaaaaaaaam lovely spaaaaaaam' , 1);
INSERT INTO postitnote (message, player_no) VALUES 
    ('spammmy spammy spam' , 1);
INSERT INTO postitnote (message, player_no) VALUES 
    ('hey ho go eagles?' , 4);
INSERT INTO postitnote (message, player_no) VALUES 
    ('admin smells like elderberries!' , 5);


-- lägg till nya objekt till PCKETABLE
INSERT INTO pocketable (object_name, object_description) VALUES ('Pencil', 'Mightier than a sword?');
INSERT INTO pocketable (object_name, object_description) VALUES ('Stapler', 'Kachunk! Satisfying, but not as satisfying as a hole punch');
INSERT INTO pocketable (object_name, object_description) VALUES ('Phone', 'The battery has run out');
INSERT INTO pocketable (object_name, object_description) VALUES ('Desklamp', 'For when you want your pocket to shine!');

CREATE TABLE windowview(
    window_id INT PRIMARY KEY NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
    view_desc VARCHAR(128) NOT NULL
);

INSERT INTO windowview (view_desc) VALUES ('A school of flying fish zooms past!');
INSERT INTO windowview (view_desc) VALUES ('A triple rainbow, truly a magical sight');
INSERT INTO windowview (view_desc) VALUES ('Far out on the ocean is a party boat, full of drunk leprechauns');
INSERT INTO windowview (view_desc) VALUES ('An oil platform going up in flames');
INSERT INTO windowview (view_desc) VALUES ('An unknown individual twerking atop a surf board trying not to fall over');


-- retroaktivt, ändra objekt_name i pocketable och pocketcontent till att vara gemener
UPDATE pocketable SET object_name = LOWER(object_name);
UPDATE pocketcontent SET object_name = LOWER(object_name);
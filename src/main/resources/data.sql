INSERT INTO restaurant (id,restaurant_name,street_name,street_number,city) VALUES
(1L,'McDonalds','Avenue Van Overbeke','196','Ganshoren'),
(2L,'Burguer King','Rue de la paix','34','Namur');

INSERT INTO client (id,client_name,street_name,street_number,city) VALUES
(1L,'Avenue Van Overbeke','196','Ganshoren','client1');

INSERT INTO product (id,available,product_name,unit_price,restaurant_id) VALUES
(1L,true,'product1',5.0,1L),
(2L,true,'product2',10.0,1L),
(3L,true,'product3',7.0,2L),
(4L,true,'product4',18.0,2L);

INSERT INTO cart (id,client_id,payment_method,total_amount,closed) VALUES
(1L,1L,1,0.0,false);
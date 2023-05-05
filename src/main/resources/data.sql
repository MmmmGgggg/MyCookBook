INSERT INTO RECIPE (id, name, description)
VALUES
    ('32de417b-670c-41b6-b68a-f0f13f5af2f0','Pileca juha', 'Recept za pilecu juhu'),
    ('05821657-3dca-403a-9382-ccb6683a531d','Spageti bolonjez', 'Recept za spagete bolonjez'),
    ('183dd1ce-d174-4c80-b466-dfa42a7c1127','Palacinke', 'Recept za palacinke');
INSERT INTO RECIPE_INGREDIENT(id, quantity, ingredient_id, recipe_id, unit_id)
VALUES
    ('ea9a973c-7999-4a2a-86d3-8dc4884e0ca1','500', '5e3153b5-e3d7-4185-ae78-6986bb7a1134','32de417b-670c-41b6-b68a-f0f13f5af2f0','72696421-9a7a-4cc3-a009-5e69a992c373'),
    ('b8abfdbc-8229-4c70-98c8-eb36b6d309c0','500', '94482b12-8e0b-4b6d-8f90-f96957d8a098','32de417b-670c-41b6-b68a-f0f13f5af2f0','72696421-9a7a-4cc3-a009-5e69a992c373');
 INSERT INTO INGREDIENT(id, name)
 VALUES
      ('5e3153b5-e3d7-4185-ae78-6986bb7a1134','Piletina'),
      ('94482b12-8e0b-4b6d-8f90-f96957d8a098','Mrkva');
 INSERT INTO UNIT(id, name)
 VALUES
      ('72696421-9a7a-4cc3-a009-5e69a992c373','gram'),
      ('95dd6083-c84e-4b51-8987-f39e9bf3491c','kilogram');

INSERT INTO public.data_source(name)
VALUES ('BOARD_GAME_GEEK')
ON CONFLICT ON CONSTRAINT data_source_chk1 DO NOTHING;

INSERT INTO public.image_type(name)
VALUES ('IMAGE')
ON CONFLICT ON CONSTRAINT image_type_chk1 DO NOTHING;

INSERT INTO public.image_type(name)
VALUES ('THUMBNAIL')
ON CONFLICT ON CONSTRAINT image_type_chk1 DO NOTHING;

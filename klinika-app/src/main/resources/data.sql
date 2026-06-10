-- Czyszczenie bazy (opcjonalne, przydatne przy restartach)
DELETE FROM zwierze;
DELETE FROM klient;
DELETE FROM weterynarz;
DELETE FROM uzytkownik;

-- 1. Dodawanie użytkowników
INSERT INTO uzytkownik (id, email, haslo, rola) VALUES (1, 'weterynarz@klinika.pl', 'haslo123', 'WETERYNARZ');
INSERT INTO uzytkownik (id, email, haslo, rola) VALUES (2, 'jan.kowalski@test.pl', 'haslo123', 'KLIENT');
INSERT INTO uzytkownik (id, email, haslo, rola) VALUES (3, 'anna.nowak@test.pl', 'haslo123', 'KLIENT');

-- 2. Przypisywanie ról szczegółowych
INSERT INTO weterynarz (id) VALUES (1);
INSERT INTO klient (id, nr_telefonu) VALUES (2, '500111222');
INSERT INTO klient (id, nr_telefonu) VALUES (3, '600333444');

-- 3. Dodawanie zwierząt Z DATĄ URODZENIA
INSERT INTO zwierze (id, imie, gatunek, wlasciciel_id, dataurodzenia) VALUES (1, 'Burek', 'Pies', 2, '2020-05-15');
INSERT INTO zwierze (id, imie, gatunek, wlasciciel_id, dataurodzenia) VALUES (2, 'Mruczek', 'Kot', 3, '2021-08-20');
INSERT INTO zwierze (id, imie, gatunek, wlasciciel_id, dataurodzenia) VALUES (3, 'Reksio', 'Pies', 2, '2019-11-01');
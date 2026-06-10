package pl.klinika.Core;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.klinika.Uzytkownik.*;
import pl.klinika.Zwierze.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(UzytkownikRepository uzytkownikRepository,
                                   ZwierzeRepository zwierzeRepository) {
        return args -> {
            if (uzytkownikRepository.count() == 0) {
                System.out.println("⏳ Trwa masowe generowanie danych testowych (100+ rekordów)...");
                Random random = new Random();

                String[] imionaKlientow = {"Jan", "Anna", "Piotr", "Katarzyna", "Michal", "Agnieszka", "Tomasz", "Magdalena", "Marcin", "Karolina", "Kamil", "Marta"};
                String[] nazwiska = {"Kowalski", "Nowak", "Wisniewski", "Wojcik", "Kowalczyk", "Kaminski", "Lewandowski", "Zielinski", "Szymanski", "Wozniak"};
                String[] imionaZwierzat = {"Burek", "Mruczek", "Reksio", "Puszek", "Luna", "Max", "Bella", "Charlie", "Lucy", "Rocky", "Zoe", "Toby", "Korek", "Ares"};
                String[] gatunki = {"Pies", "Kot", "Królik", "Chomik", "Papuga", "Świnka morska", "Żółw"};

                for (int i = 1; i <= 10; i++) {
                    Weterynarz wet = new Weterynarz();
                    wet.setEmail("weterynarz" + i + "@klinika.pl");
                    wet.setPassword("bezpiecznehaslo");
                    wet.setRola(RolaUzytkownika.WETERYNARZ);
                    uzytkownikRepository.save(wet);
                }

                List<Klient> zapisaniKlienci = new ArrayList<>();
                for (int i = 1; i <= 100; i++) {
                    String imie = imionaKlientow[random.nextInt(imionaKlientow.length)];
                    String nazwisko = nazwiska[random.nextInt(nazwiska.length)];

                    Klient klient = new Klient();
                    klient.setEmail(imie.toLowerCase() + "." + nazwisko.toLowerCase() + i + "@test.pl");
                    klient.setPassword("haslo123");
                    klient.setRola(RolaUzytkownika.KLIENT);

                    int nrTel = 500000000 + random.nextInt(400000000);
                    klient.setNrTelefonu(String.valueOf(nrTel));

                    Klient zapisany = uzytkownikRepository.save(klient);
                    zapisaniKlienci.add(zapisany);
                }

                for (int i = 1; i <= 120; i++) {
                    Zwierze zwierze = new Zwierze();
                    zwierze.setImie(imionaZwierzat[random.nextInt(imionaZwierzat.length)]);
                    zwierze.setGatunek(gatunki[random.nextInt(gatunki.length)]);

                    Klient wylosowanyWlasciciel = zapisaniKlienci.get(random.nextInt(zapisaniKlienci.size()));
                    zwierze.setWlasciciel(wylosowanyWlasciciel);

                    zwierzeRepository.save(zwierze);
                }

                System.out.println("✅ SUKCES: Wygenerowano 10 weterynarzy, 100 klientów oraz 120 zwierząt!");
            } else {
                System.out.println("⚡ Baza danych nie jest pusta. Pominięto generowanie.");
            }
        };
    }
}
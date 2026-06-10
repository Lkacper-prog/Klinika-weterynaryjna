package pl.klinika.Wizyta;

import lombok.Data;

@Data
public class ZabiegCreateDTO {
    private String nazwa;
    private String opis;
    private double koszt;
}
package com.example.restip.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;



public class CountriesDto {
    private List<String> countries = new ArrayList<>();

    public void add(String country){
        countries.add(country);
    }

    public Collection<String> getNorthCountries() {
        return countries;
    }
}

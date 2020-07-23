package com.reactive.beta.version.exception;

public class PlanetNotFoundException extends Exception {

    public PlanetNotFoundException(Integer id){
        super(String.format("Planet %d not found", id));
    }
}

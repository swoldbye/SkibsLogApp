package com.example.skibslogapp.model.Koordinat;

public class KoordinatDTO {
    private double latitude;
    private double longitude;
    private boolean successfulCoordinate;

    KoordinatDTO(double latitude, double longitude){
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void printKoordinates(){
        System.out.println("Latitude: " + latitude);
        System.out.println("Longitude: " + longitude);
    }


}

package com.example.homeswipe_1.liked;

public class liked_object {
    private String adress, userId, antalRum,apartmentUrl, beskrivning,hyran,kvm,postnummer,djur,cigg,rate,apartmentId;



    public liked_object(String appartmentId, String apartmentUrl, String hyran, String apartmentId){
        this.userId       = appartmentId;
        this.hyran        = hyran;
        this.apartmentId  = apartmentId;
        this.apartmentUrl = apartmentUrl;


    }

    public String getApartmentId() {return apartmentId;}
    public String getDjur() {return djur; }
    public String getRate() { return rate; }
    public String getCigg() {return cigg; }
    public String getAppartmentAdress() {
        return adress;
    }
    public String getAppartmentId() {
        return userId;
    }
    public String getAntalRum() {
        return antalRum;
    }
    public String getApartmentUrl() {
        return apartmentUrl;
    }
    public String getBeskrivning() {
        return beskrivning;
    }
    public String getHyran() {
        return hyran;
    }
    public String getKvm() {
        return kvm;
    }
    public String getPostnummer() {
        return postnummer;
    }




}

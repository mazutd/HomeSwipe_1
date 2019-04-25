package com.example.homeswipe_1.Lägenhet;

public class items {
    private String adress, userId, antalRum,apartmentUrl, beskrivning,hyran,kvm,postnummer,djur,cigg,rate;




    public items(String appartmentId, String appartmentAdress, String appartmentAntalRum, String appartmentBeskrivning, String appartmentHyran, String appartmentKvm, String appartmentPostnummer, String apartmentUrl, String apartmentDjur, String apartmentCigg, String apartmentRate){
        this.kvm       = appartmentKvm;
        this.hyran     = appartmentHyran;
        this.antalRum  = appartmentAntalRum;
        this.userId    = appartmentId;
        this.apartmentUrl = apartmentUrl;
        this.beskrivning  = appartmentBeskrivning;
        this.postnummer   = appartmentPostnummer;
        this.beskrivning  = appartmentBeskrivning;
        this.adress       = appartmentAdress;
        this.djur         = apartmentDjur;
        this.cigg         = apartmentCigg;
        this.rate         = apartmentRate;



    }
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

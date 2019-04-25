package com.example.homeswipe_1.Meddelande.Meddelande_gast;

public class MeddelandeGastObject {
    private String adress, userId, antalRum,apartmentUrl, beskrivning,hyran,kvm,postnummer,djur,cigg, name, profileImage;



    public MeddelandeGastObject(String userId, String name, String profileImage ){
        this.name         = name;
        this.profileImage = profileImage;
        this.userId       = userId;

    }
    public String getDjur() {return djur; }
    public String getName() { return name; }

    public String getProfileImage() { return profileImage; }
    public String getCigg() {return cigg; }
    public String getAppartmentAdress() {
        return adress;
    }

    public String getUserId() {
        return userId;
    }

    public String getAntalRum() {
        return antalRum;
    }
    public String profileImage() {
        return profileImage;
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

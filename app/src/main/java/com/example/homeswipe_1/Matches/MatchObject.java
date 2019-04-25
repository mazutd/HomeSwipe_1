package com.example.homeswipe_1.Matches;

public class MatchObject {
    private String adress, userId, antalRum,apartmentUrl, beskrivning,hyran,kvm,postnummer,djur,cigg, name, profileImage;



    public MatchObject(String apartmentID, String name, String profileImage){
        this.name         =  name;
        this.profileImage =  profileImage;
        this.userId       = apartmentID;

    }
    public String getDjur() {return djur; }
    public String getName() { return name; }

    public String getProfileImage() { return profileImage; }
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

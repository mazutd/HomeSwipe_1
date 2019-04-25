package com.example.homeswipe_1.Chat;

public class ChatObject {
    private String meddelande;
    private Boolean skapadav;
    private String userId;

    public String getUserId() {
        return userId;
    }

    public ChatObject(String meddelande, Boolean skapadav , String userId){
        this.meddelande = meddelande;
        this.skapadav   = skapadav;
        this.userId = userId;
    }

    public void setMeddelande(String meddelande) {
        this.meddelande = meddelande;
    }

    public void setSkapadav(Boolean skapadav) {
        this.skapadav = skapadav;
    }


    public String getMeddelande() {
        return meddelande;
    }

    public Boolean getSkapadav() {
        return skapadav;
    }



}

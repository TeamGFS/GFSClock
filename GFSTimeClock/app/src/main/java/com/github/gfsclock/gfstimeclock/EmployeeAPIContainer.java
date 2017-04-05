package com.github.gfsclock.gfstimeclock;

public class EmployeeAPIContainer {
    private Ded ded;
    private String pictureUrl;

    public EmployeeAPIContainer() {};
    
    public void setDed(Ded ded) {
        this.ded = ded;
    }
    
    public Ded getDed() {
        return ded;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }
    
    public string getPictureUrl() {
        return pictureUrl;
    }
}

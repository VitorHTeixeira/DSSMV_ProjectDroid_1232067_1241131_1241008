package com.lvhm.covertocover.models;

public class Library {
    private String library_ID;
    private String name;
    private String address;
    private String phone_number;
    private String email;
    private String website;
    private boolean is_closed;

    public Library(String library_ID, String name, String address, String phone_number, String email, String website, boolean is_closed) {
        this.library_ID = library_ID;
        this.name = name;
        this.address = address;
        this.phone_number = phone_number;
        this.email = email;
        this.website = website;
        this.is_closed = is_closed;
    }

    public String getLibraryID() {
        return library_ID;
    }
    public void setLibraryID(String library_ID) {
        this.library_ID = library_ID;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getPhoneNumber() {
        return phone_number;
    }
    public void setPhoneNumber(String phone_number) {
        this.phone_number = phone_number;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getWebsite() {
        return website;
    }
    public void setWebsite(String website) {
        this.website = website;
    }
    public boolean getIsClosed() {
        return is_closed;
    }
    public void setIsClosed(boolean is_closed) {
        this.is_closed = is_closed;
    }
}

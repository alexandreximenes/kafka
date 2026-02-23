package com.learnavro.domain;

import java.io.Serializable;

public class Address implements Serializable {
    private String addressLine1;
    private String city;
    private String stateProvince;
    private String country;
    private String zip;

    public String getAddressLine1() { return addressLine1; }
    public void setAddressLine1(String addressLine1) { this.addressLine1 = addressLine1; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getStateProvince() { return stateProvince; }
    public void setStateProvince(String stateProvince) { this.stateProvince = stateProvince; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public String getZip() { return zip; }
    public void setZip(String zip) { this.zip = zip; }

    @Override
    public String toString() {
        return "Address{" +
                "addressLine1='" + addressLine1 + '\'' +
                ", city='" + city + '\'' +
                ", stateProvince='" + stateProvince + '\'' +
                ", country='" + country + '\'' +
                ", zip='" + zip + '\'' +
                '}';
    }
}

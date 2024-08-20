package it.airdesk.airdesk_app.model.dataTypes;

import jakarta.persistence.Embeddable;

@Embeddable
public class Address {
    
    private String country;
    private String state;
    private String city;
    private String postalcode;
    private String street;
    private String number;

    public Address(){}

    /////////////       GETTERS+SETTERS       //////////////////////


    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalcode() {
        return postalcode;
    }

    public void setPostalcode(String postalcode) {
        this.postalcode = postalcode;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((country == null) ? 0 : country.hashCode());
        result = prime * result + ((state == null) ? 0 : state.hashCode());
        result = prime * result + ((city == null) ? 0 : city.hashCode());
        result = prime * result + ((postalcode == null) ? 0 : postalcode.hashCode());
        result = prime * result + ((street == null) ? 0 : street.hashCode());
        result = prime * result + ((number == null) ? 0 : number.hashCode());
        return result;
    }
    /////////////       AUXILIARY METHODS       ////////////////////

    /////////////       HashCode + equals METHODS       ////////////
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Address other = (Address) obj;
        if (country == null) {
            if (other.country != null)
                return false;
        } else if (!country.equals(other.country))
            return false;
        if (state == null) {
            if (other.state != null)
                return false;
        } else if (!state.equals(other.state))
            return false;
        if (city == null) {
            if (other.city != null)
                return false;
        } else if (!city.equals(other.city))
            return false;
        if (postalcode == null) {
            if (other.postalcode != null)
                return false;
        } else if (!postalcode.equals(other.postalcode))
            return false;
        if (street == null) {
            if (other.street != null)
                return false;
        } else if (!street.equals(other.street))
            return false;
        if (number == null) {
            if (other.number != null)
                return false;
        } else if (!number.equals(other.number))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return
            street + ", " 
            + number + ", "
            + postalcode + ", "
            + city + ", "
            + state + ", "
            + country;
    }

    


    
    
}

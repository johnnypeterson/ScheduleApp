package model;

/**
 *
 * @author johnnypeterson
 */
public class Country {

    private String country;

    public Country(String country) {
        this.country = country;
    }


    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return "Country{" +
                ", country='" + country + '\'' +
                '}';
    }
}

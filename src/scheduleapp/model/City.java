/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scheduleapp.model;

/**
 *
 * @author johnnypeterson
 */
public class City {
    private Integer cityId;
    private String city;
    private Integer countryId;



    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getCountryId() {
        return countryId;
    }

    public void setCountryId(Integer countryId) {
        this.countryId = countryId;
    }

    public City(Integer cityId, String city, Integer countryId) {
        this.cityId = cityId;
        this.city = city;
        this.countryId = countryId;

    }
    @java.lang.Override
    public java.lang.String toString() {
        return "City{" +
                "cityId=" + cityId +
                ", city='" + city + '\'' +
                ", countryId=" + countryId +
                '}';
    }
}

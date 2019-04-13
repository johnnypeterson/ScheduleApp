/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author johnnypeterson
 */
public class City {
    private Integer cityId;
    private String city;



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


    public City(Integer cityId, String city) {
        this.cityId = cityId;
        this.city = city;


    }
    @java.lang.Override
    public java.lang.String toString() {
        return "City{" +
                "cityId=" + cityId +
                ", city='" + city + '\'' +

                '}';
    }
}

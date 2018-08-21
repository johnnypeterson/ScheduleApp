package scheduleapp.model;

/**
 *
 * @author johnnypeterson
 */
public class Country {
    private Integer contryId;
    private String country;

    public Country(Integer contryId, String country) {
        this.contryId = contryId;
        this.country = country;
    }

    public Integer getContryId() {
        return contryId;
    }

    public void setContryId(Integer contryId) {
        this.contryId = contryId;
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
                "contryId=" + contryId +
                ", country='" + country + '\'' +
                '}';
    }
}

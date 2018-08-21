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
public class Appointment {
    private Integer appointmentId;
    private Integer customerId;
    private String title;
    private String description;
    private String location;
    private String contact;
    private String url;
    private String start;
    private String end;

    @java.lang.Override
    public java.lang.String toString() {
        return "Appointment{" +
                "appointmentId=" + appointmentId +
                ", customerId=" + customerId +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", location='" + location + '\'' +
                ", contact='" + contact + '\'' +
                ", url='" + url + '\'' +
                ", start='" + start + '\'' +
                ", end='" + end + '\'' +
                '}';
    }

    public Appointment(Integer appointmentId, Integer customerId, String title, String description, String location, String contact, String url, String start, String end) {
        this.appointmentId = appointmentId;
        this.customerId = customerId;
        this.title = title;
        this.description = description;
        this.location = location;
        this.contact = contact;
        this.url = url;
        this.start = start;
        this.end = end;
    }

    public Integer getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Integer appointmentId) {
        this.appointmentId = appointmentId;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }
}

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
public class Customer {
    private Integer customerId;
    private String customerName;
    private Integer addressId;
    private Integer active;

    public Customer(Integer customerId, String customerName, Integer addressId, Integer active) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.addressId = addressId;
        this.active = active;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Integer getAddressId() {
        return addressId;
    }

    public void setAddressId(Integer addressId) {
        this.addressId = addressId;
    }

    public Integer getActive() {
        return active;
    }

    public void setActive(Integer active) {
        this.active = active;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return "Customer{" +
                "customerId=" + customerId +
                ", customerName='" + customerName + '\'' +
                ", addressId=" + addressId +
                ", active=" + active +
                '}';
    }
}

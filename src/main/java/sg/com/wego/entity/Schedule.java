package sg.com.wego.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "SCHEDULE")
public class Schedule implements Serializable {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long id;

    @Column(name="departure_code")
    private String departAirportCode;

    @Column(name="arrival_code")
    private String arrivalAirportCode;

    @Column(name="provider_code")
    private String providerCode;

    @Column(name="base_price")
    private Integer basePrice;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDepartAirportCode() {
        return departAirportCode;
    }

    public void setDepartAirportCode(String departAirportCode) {
        this.departAirportCode = departAirportCode;
    }

    public String getArrivalAirportCode() {
        return arrivalAirportCode;
    }

    public void setArrivalAirportCode(String arrivalAirportCode) {
        this.arrivalAirportCode = arrivalAirportCode;
    }

    public String getProviderCode() {
        return providerCode;
    }

    public void setProviderCode(String providerCode) {
        this.providerCode = providerCode;
    }

    public Integer getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(Integer basePrice) {
        this.basePrice = basePrice;
    }
}

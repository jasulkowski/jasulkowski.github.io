package com.example.restip.model;
import javax.persistence.*;

@Entity
@Table(name = "IPAPI" )
public class IpData {

    @Id
    @Column(name = "ip")
    private String ip;
    @Column(name = "country_name")
    private String countryName;


    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }
}



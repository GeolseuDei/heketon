package com.geolstudio.apipometera;

/**
 * Created by HP on 12/10/2017.
 */

public class DataKapalKedatangan {
    private String vessel_name;
    private String shipping_agent;
    private String eta;
    private String etd;
    private String origin_port;
    private String final_port;
    private String last_port;
    private String next_port;

    public DataKapalKedatangan(String vessel_name, String shipping_agent, String eta, String etd, String origin_port, String final_port, String last_port, String next_port) {
        this.vessel_name = vessel_name;
        this.shipping_agent = shipping_agent;
        this.eta = eta;
        this.etd = etd;
        this.origin_port = origin_port;
        this.final_port = final_port;
        this.last_port = last_port;
        this.next_port = next_port;
    }

    public String getVessel_name() {
        return vessel_name;
    }

    public void setVessel_name(String vessel_name) {
        this.vessel_name = vessel_name;
    }

    public String getShipping_agent() {
        return shipping_agent;
    }

    public void setShipping_agent(String shipping_agent) {
        this.shipping_agent = shipping_agent;
    }

    public String getEta() {
        return eta;
    }

    public void setEta(String eta) {
        this.eta = eta;
    }

    public String getEtd() {
        return etd;
    }

    public void setEtd(String etd) {
        this.etd = etd;
    }

    public String getOrigin_port() {
        return origin_port;
    }

    public void setOrigin_port(String origin_port) {
        this.origin_port = origin_port;
    }

    public String getFinal_port() {
        return final_port;
    }

    public void setFinal_port(String final_port) {
        this.final_port = final_port;
    }

    public String getLast_port() {
        return last_port;
    }

    public void setLast_port(String last_port) {
        this.last_port = last_port;
    }

    public String getNext_port() {
        return next_port;
    }

    public void setNext_port(String next_port) {
        this.next_port = next_port;
    }
}

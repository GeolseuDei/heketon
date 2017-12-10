package com.geolstudio.apipometera;

/**
 * Created by ASUS on 12/10/2017.
 */

public class DataContainer {
    private String vessel_name;
    private String ata;
    private String terminal_id;
    private String full_empty;
    private String carrier;

    public DataContainer(String vessel_name, String ata, String terminal_id, String full_empty, String carrier) {
        this.vessel_name = vessel_name;
        this.ata = ata;
        this.terminal_id = terminal_id;
        this.full_empty = full_empty;
        this.carrier = carrier;
    }

    public String getVessel_name() {
        return vessel_name;
    }

    public void setVessel_name(String vessel_name) {
        this.vessel_name = vessel_name;
    }

    public String getAta() {
        return ata;
    }

    public void setAta(String ata) {
        this.ata = ata;
    }

    public String getTerminal_id() {
        return terminal_id;
    }

    public void setTerminal_id(String terminal_id) {
        this.terminal_id = terminal_id;
    }

    public String getFull_empty() {
        return full_empty;
    }

    public void setFull_empty(String full_empty) {
        this.full_empty = full_empty;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }
}

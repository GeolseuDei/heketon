package com.geolstudio.apipometera;

/**
 * Created by MOFOSEFES on 12/9/2017.
 */

public class User {
    private String id;
    private String email;
    private String nohp;
    private String status_verif_nohp;
    private String status_verif_email;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNohp() {
        return nohp;
    }

    public void setNohp(String nohp) {
        this.nohp = nohp;
    }

    public String getStatus_verif_nohp() {
        return status_verif_nohp;
    }

    public void setStatus_verif_nohp(String status_verif_nohp) {
        this.status_verif_nohp = status_verif_nohp;
    }

    public String getStatus_verif_email() {
        return status_verif_email;
    }

    public void setStatus_verif_email(String status_verif_email) {
        this.status_verif_email = status_verif_email;
    }
}

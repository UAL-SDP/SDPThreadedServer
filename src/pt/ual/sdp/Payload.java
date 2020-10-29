package pt.ual.sdp;

import java.io.Serializable;

class Payload implements Serializable {
    private String data;

    public Payload(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}

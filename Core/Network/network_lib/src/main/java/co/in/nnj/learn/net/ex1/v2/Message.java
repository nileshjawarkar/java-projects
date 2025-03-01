package co.in.nnj.learn.net.ex1.v2;

import java.io.Serializable;

public class Message implements Serializable {
    private static final long serialVersionUID = 1L;
    private String data;

    public Message(final String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }

    public void setData(final String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "data [" + data + "]";
    }
}

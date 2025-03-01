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
        return "Message{data=" + data + "}";
    }

    /*
     * private transient StringBuffer buf;
     * public Message() {
     * }
     * 
     * public Message(final String msg) {
     * this.buf = new StringBuffer();
     * append(msg);
     * }
     * 
     * public StringBuffer getBuf() {
     * return buf;
     * }
     * 
     * public void setBuf(final StringBuffer buf) {
     * this.buf = buf;
     * }
     * 
     * public void append(final String msg) {
     * buf.append(msg);
     * buf.append(", ");
     * }
     * 
     * private void writeObject(final ObjectOutputStream sout) throws Exception {
     * final byte[] bytes = getBytes();
     * sout.writeInt(bytes.length);
     * sout.write(bytes);
     * sout.flush();
     * }
     * 
     * private void readObject(final ObjectInputStream sin) throws Exception {
     * final int len = sin.readInt();
     * if (len > 0) {
     * final byte[] data = new byte[len];
     * if (sin.read(data) > 0) {
     * append(new String(data));
     * }
     * }
     * }
     * 
     * public byte[] getBytes() {
     * return buf.toString().getBytes();
     * }
     * 
     * @Override
     * public String toString() {
     * return buf.toString();
     * }
     * 
     * public int length() {
     * return buf.length();
     * }
     * 
     */
}

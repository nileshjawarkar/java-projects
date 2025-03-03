package co.in.nnj.learn.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ByteArrayConverter {
    public static <T> byte[] toByteArray(final T obj) throws IOException {
        try (final ByteArrayOutputStream baStream = new ByteArrayOutputStream();
                final ObjectOutputStream ooStream = new ObjectOutputStream(baStream);) {
            ooStream.writeObject(obj);
            ooStream.flush();
            return baStream.toByteArray();
        }
    }

    public static <T> T fromByteArray(byte[] objBytes, Class<T> type) throws IOException, ClassNotFoundException {
        try (final ByteArrayInputStream baiStream = new ByteArrayInputStream(objBytes);
                final ObjectInputStream ooStream = new ObjectInputStream(baiStream);) {
            Object obj = ooStream.readObject();
            if (type.isInstance(obj)) {
                return type.cast(obj);
            }
        }
        return null;
    }
}

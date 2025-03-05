package co.in.nnj.learn.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class ByteArrayConverter {
    public static <T> byte[] toByteArray(final T obj) throws IOException {
        try (final ByteArrayOutputStream baStream = new ByteArrayOutputStream();
            final ObjectOutputStream ooStream = new ObjectOutputStream(baStream);) {
            ooStream.writeObject(obj);
            ooStream.flush();
            return baStream.toByteArray();
        }
    }

    public static <T> T fromByteArray(final byte[] objBytes, final Class<T> type)
        throws IOException, ClassNotFoundException {
        try (final ByteArrayInputStream baiStream = new ByteArrayInputStream(objBytes);
            final ObjectInputStream oiStream = new ObjectInputStream(baiStream);) {
            final Object obj = oiStream.readObject();
            if (type.isInstance(obj)) {
                return type.cast(obj);
            }
        }
        return null;
    }

    public static <T> byte[] listToByteArray(final List<T> objList) throws IOException {
        try (final ByteArrayOutputStream baStream = new ByteArrayOutputStream();
            final ObjectOutputStream ooStream = new ObjectOutputStream(baStream);) {
            ooStream.writeInt(objList.size());
            for (T obj : objList) {
                ooStream.writeObject(obj);
            }
            ooStream.flush();
            return baStream.toByteArray();
        }
    }

    public static <T> List<T> fromByteArrayToList(final byte[] objBytes, final Class<T> type)
        throws IOException, ClassNotFoundException {
        List<T> list = new ArrayList<>();
        try (final ByteArrayInputStream baStream = new ByteArrayInputStream(objBytes);
            final ObjectInputStream oiStream = new ObjectInputStream(baStream);) {
            int size = oiStream.readInt();
            for (int i = 0; i < size; ++i) {
                final Object obj = oiStream.readObject();
                if (type.isInstance(obj)) {
                    list.add(type.cast(obj));
                }
            }
        }
        return list;
    }
}

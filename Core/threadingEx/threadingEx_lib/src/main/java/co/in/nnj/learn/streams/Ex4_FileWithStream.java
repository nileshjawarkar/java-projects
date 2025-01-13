package co.in.nnj.learn.streams;

import java.io.InputStream;

public class Ex4_FileWithStream {

    public static void main(final String[] args) {
        final InputStream resourceAsStream = Ex4_FileWithStream.class.getClassLoader().getResourceAsStream("DataFile.txt"); 
        if(resourceAsStream != null) {
            System.out.println("Worked");
        }
    }
}

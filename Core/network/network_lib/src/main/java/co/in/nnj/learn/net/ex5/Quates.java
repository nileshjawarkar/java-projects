package co.in.nnj.learn.net.ex5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Quates {
    private static List<String> readQuates() {
        final ArrayList<String> quates = new ArrayList<>();
        final InputStream inputStream = Quates.class.getResourceAsStream("quotes.txt");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line = reader.readLine();
            while (line != null) {
                quates.add(line);
                line = reader.readLine();
            }
        } catch (final IOException e) {
        }
        // -- Start udp server
        System.out.println("Number of quates available - " + quates.size());
        return quates;
    }

    private static List<String> quates;
    public static void init() {
       quates = readQuates(); 
    }
    public static String getAnyQuate() {
        final int index = (int) (Math.random() * quates.size());
        if (index < quates.size()) {
            return quates.get(index);
        }
        return "Patience is bitter, but its fruit is sweet. Try one more time.";
    }
}

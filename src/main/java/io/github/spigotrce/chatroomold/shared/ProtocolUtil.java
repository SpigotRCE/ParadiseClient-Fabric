package io.github.spigotrce.chatroomold.shared;

import java.util.ArrayList;
import java.util.Arrays;

public class ProtocolUtil {
    private static final String sep = "\000";

    public static String write(String... data) {
        StringBuilder data_ = new StringBuilder();
        for (String s : data) {
            data_.append(s).append(sep);
        }
        return data_.toString();
    }

    public static String write(ArrayList<String> data) {
        return write(data.toArray(new String[0]));
    }

    public static String[] read(String data) {
        ArrayList<String> list = new ArrayList<>();
        String[] tokens = data.split(sep);
        for (String token : tokens) {
            if (!token.isEmpty()) {
                list.add(token);
            }
        }
        return list.toArray(new String[0]);
    }

    public static void main(String[] args) {
        String data = write("Hello", "World", "This", "Is", "A", "Test");
        System.out.println("Encoded: " + data);
        String[] decoded = read(data);
        System.out.println("Decoded: " + Arrays.toString(decoded));
    }
}
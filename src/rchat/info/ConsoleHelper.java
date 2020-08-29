package rchat.info;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class ConsoleHelper {
    private static List<String> strings = new ArrayList<>();
    private static boolean hold = false;
    public static BufferedWriter writer;
    public static File file;

    public static void init() throws IOException {
        file = new File("test_class_logs.txt");
        if (!file.exists()) {
            file.createNewFile();
            FileWriter fw = new FileWriter(file, true);
            writer = new BufferedWriter(fw);
        } else {
            FileWriter fw = new FileWriter(file, true);
            writer = new BufferedWriter(fw);
        }
    }

    public static String getDate() {
        return new SimpleDateFormat("<HH:mm:ss; dd MMMM yyyy года> ").format(new Date());
    }

    public static void hold() {
        System.out.println("************************************************");
        System.out.println("Messages was held.");
        hold = true;
    }

    public static void release() {
        hold = false;
        System.out.println("************************************************");
        System.out.println("Held messages: ");
        for (String a : strings) {
            System.out.println(getDate() + a + "\n");
        }
        strings = new ArrayList<>();
    }

    public static void writeString(String a) {
        if (hold) {
            strings.add(a.substring(0, a.length() - 2));
        } else {
            System.out.println(getDate() + a);
        }
    }

    public static void writeString(Object a) {
        if (hold) {
            strings.add(a.toString());
        } else {
            System.out.println(a);
        }
    }

    public static void writeException(Exception e) {
        try {
            StringWriter stringWriter = new StringWriter();
            e.printStackTrace(new PrintWriter(stringWriter));
            writer.write(stringWriter.toString());
            writer.write("-------------------------------------------------------------------");
            writer.flush();
        } catch (IOException ex) {
            writeException(ex);
        }
    }
}
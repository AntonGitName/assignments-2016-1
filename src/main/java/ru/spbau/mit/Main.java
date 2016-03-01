package ru.spbau.mit;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author antonpp
 * @since 01/03/16
 */
public class Main {

    private static final byte[] BUFFER = new byte[4096];

    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            throw new IllegalArgumentException("Need 2 arguments");
        }

        final String fin = args[0];
        final String fout = args[1];

        copyFile(fin, fout);
    }

    private static void copyFile(String in, String out) throws IOException {

        FileOutputStream os = new FileOutputStream(out);

        int len;
        try (FileInputStream is = new FileInputStream(in)) {
            for (; ; ) {
                len = is.read(BUFFER);
                if (len == -1) {
                    break;
                }
                os.write(BUFFER, 0, len);
            }
        }

        os.close();
    }
}

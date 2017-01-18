package hr.fer.zemris.projekt.web.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Kristijan Vulinovic
 * @version 1.0.0
 */
public class Utility {
    public static byte[] readStream(InputStream is) {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        int nRead;
        byte[] data = new byte[4096];

        try {
            while ((nRead = is.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
                buffer.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return buffer.toByteArray();
    }
}

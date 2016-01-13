package com.example.android.opengl.util;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by phoward on 12/11/2015.
 */
public class FileOperations {

    public static String SlurpInputStreamToString(InputStream inputStream) throws IOException {
        int size = inputStream.available();
        byte[] buffer = new byte[size];
        inputStream.read(buffer);
        inputStream.close();
        return new String(buffer);
    }
}

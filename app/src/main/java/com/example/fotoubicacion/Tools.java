package com.example.fotoubicacion;

import android.content.Context;
import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Tools {

    private Context context;

    public Tools(Context context) {
        this.context = context;
    }

    public byte[] decodeByte(String filePath)  {
        byte[] fileByteArray;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(filePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer =new byte[1024];
        int read;
        try {
            while ((read = fis.read(buffer)) != -1) {
                baos.write(buffer, 0, read);
            }
            baos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        fileByteArray = baos.toByteArray();
        return fileByteArray;
    }

}
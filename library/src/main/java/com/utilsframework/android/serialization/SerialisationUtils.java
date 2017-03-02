package com.utilsframework.android.serialization;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class SerialisationUtils {
    public static void writeObjectToFile(String filePath, Serializable object) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(filePath);
        ObjectOutputStream objectOutputStream = null;
        try {
            objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(object);
        } finally {
            if (objectOutputStream != null) {
                objectOutputStream.close();
            } else {
                fileOutputStream.close();
            }
        }
    }

    public static void writeObjectToFileInDataDir(Context context, String fileName,
                                                  Serializable object)
            throws IOException {
        String filePath = getFilePathInDataDir(context, fileName);
        writeObjectToFile(filePath, object);
    }

    public static <T extends Serializable> T readObjectFromFile(String filePath)
            throws IOException {
        FileInputStream fileInputStream = new FileInputStream(filePath);
        ObjectInputStream objectInputStream = null;
        try {
            objectInputStream = new ObjectInputStream(fileInputStream);
            try {
                return (T) objectInputStream.readObject();
            } catch (ClassNotFoundException | ClassCastException | InvalidClassException e) {
                new File(filePath).delete();
                return null;
            }
        } finally {
            if (objectInputStream != null) {
                objectInputStream.close();
            } else {
                fileInputStream.close();
            }
        }
    }

    public static <T extends Serializable> T readObjectFromFileInDataDir(
            Context context, String fileName)
            throws IOException {
        String filePath = getFilePathInDataDir(context, fileName);
        return readObjectFromFile(filePath);
    }

    public static String getFilePathInDataDir(Context context, String fileName) {
        return context.getApplicationInfo().dataDir + "/" + fileName;
    }
}

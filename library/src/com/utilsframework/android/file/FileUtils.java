package com.utilsframework.android.file;

/**
 * User: Tikhonenko.S
 * Date: 07.07.14
 * Time: 14:44
 */
public final class FileUtils {
    public static String removeExtension(String filename) {
        return filename.replaceFirst("[.][^.]+$", "");
    }

    public static String getExtension(String fileName) {
        String extension = "";

        int i = fileName.lastIndexOf('.');
        if (i > 0) {
            extension = fileName.substring(i+1);
        }

        return extension;
    }
}

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
}

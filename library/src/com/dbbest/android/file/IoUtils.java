package com.dbbest.android.file;

import java.io.*;

/**
 * Provides I/O operations
 * 
 */
public final class IoUtils {

	private static final int BUFFER_SIZE = 32 * 1024; // 32 KB

	private IoUtils() {
	}

	public static final void copyStream(InputStream is, OutputStream os) throws IOException {
		byte[] bytes = new byte[BUFFER_SIZE];
		while (true) {
			int count = is.read(bytes, 0, BUFFER_SIZE);
			if (count == -1) {
				break;
			}
			os.write(bytes, 0, count);
		}
	}

    public static void copyFile(String source, String destination) throws IOException {
        copyStream(new FileInputStream(source), new FileOutputStream(destination));
    }

	public static final void closeSilently(Closeable closeable) {
		try {
			closeable.close();
		} catch (Exception e) {
			// Do nothing
		}
	}
}

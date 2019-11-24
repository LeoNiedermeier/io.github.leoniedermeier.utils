package io.github.leoniedermeier.utils.misc;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.springframework.util.StreamUtils;

public class ZipUtils {

    private ZipUtils() {
        throw new AssertionError("No ZipUtils instances for you!");
    }

    public static byte[] zipSingleContent(String entryName, byte[] content) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream)) {
            ZipEntry zipEntry = new ZipEntry(entryName);
            zipOutputStream.putNextEntry(zipEntry);
            zipOutputStream.write(content);
        }
        return outputStream.toByteArray();
    }

    public static byte[] unzipSingleContent( byte[] content) throws IOException {
        try (ZipInputStream zipInputStream = new ZipInputStream(new ByteArrayInputStream(content))) {
            zipInputStream.getNextEntry();
            return StreamUtils.copyToByteArray(zipInputStream);
        }
    }

    public OutputStream wrap(String entryName, OutputStream outputStream) throws IOException {
        ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);
        zipOutputStream.putNextEntry(new ZipEntry(entryName));
        return zipOutputStream;
    }
    
    public InputStream unwrap(  InputStream inputStream) throws IOException {
        ZipInputStream zipInputStream = new ZipInputStream(inputStream);
        zipInputStream.getNextEntry();
        return zipInputStream;
    }
}

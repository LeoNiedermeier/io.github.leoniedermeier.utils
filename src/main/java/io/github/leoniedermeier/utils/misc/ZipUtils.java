package io.github.leoniedermeier.utils.misc;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;

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

    public static OutputStream wrap(String entryName, OutputStream outputStream) throws IOException {
        ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);
        zipOutputStream.putNextEntry(new ZipEntry(entryName));
        return zipOutputStream;
    }
    
    public static InputStream unwrap(  InputStream inputStream) throws IOException {
        ZipInputStream zipInputStream = new ZipInputStream(inputStream);
        zipInputStream.getNextEntry();
        return zipInputStream;
    }
    
    
    
    public static Path extractResourceZipToTempDir(final Resource resource) throws IOException {
        // could be resource.getFilename() in some cases.
        final Path tempDir = Files.createTempDirectory("zip");
        // Explicitly register for delete after application terminates (not done by Files.createTempDirectory(...))
        tempDir.toFile().deleteOnExit();
        try (ZipInputStream zis = new ZipInputStream(resource.getInputStream())) {
            for (ZipEntry ze = zis.getNextEntry(); ze != null; ze = zis.getNextEntry()) {
                final Path entryPath = tempDir.resolve(ze.getName());
                entryPath.toFile().deleteOnExit();
                if (ze.isDirectory()) {
                    Files.createDirectories(entryPath);
                } else {
                    Files.copy(zis, entryPath);
                }
            }
            zis.closeEntry();
        }
        return tempDir;
    }
}

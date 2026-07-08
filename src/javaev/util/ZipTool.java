package javaev.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import java.nio.file.Files;

import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipTool {
    private String suffix_zip = ".zip";
    private String tag_slash = "/";

    public void compressed(File[] array, File zipFile, boolean isPackDirectory) {
        if (null != array) {
            File file = array[0];
            File file_parent = file.getParentFile();
            if (null == zipFile) {
                zipFile = new File(file_parent, file_parent.getName() + suffix_zip);
            }
            try {
                ZipOutputStream zipOutputStream = new ZipOutputStream(Files.newOutputStream(zipFile.toPath()));
                String zipEntryName;
                if (isPackDirectory) {
                    zipEntryName = file_parent.getName();
                } else {
                    zipEntryName = null;
                }
                write(array, zipEntryName, zipOutputStream);
                zipOutputStream.finish();
                zipOutputStream.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void write(File file, String zipEntryName, ZipOutputStream zipOutputStream) throws IOException {
        if (null != file && null != zipOutputStream) {
            if (file.isDirectory()) {
                File[] array = file.listFiles();
                write(array, zipEntryName, zipOutputStream);
            } else {
                ZipEntry zipEntry = new ZipEntry(zipEntryName);
                zipOutputStream.putNextEntry(zipEntry);
                write(file, zipOutputStream);
            }
        }
    }

    private void write(File file, ZipOutputStream zipOutputStream) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(file);
        byte[] arr = new byte[1024];
        int length;
        while ((length = fileInputStream.read(arr)) != -1) {
            zipOutputStream.write(arr, 0, length);
        }
        fileInputStream.close();
    }

    private void write(File[] array, String zipEntryName, ZipOutputStream zipOutputStream) throws IOException {
        if (null != array && null != zipOutputStream) {
            for (int i = 0; i < array.length; i++) {
                File file = array[i];
                String name = file.getName();
                String entryName;
                if (null == zipEntryName) {
                    entryName = name;
                } else {
                    entryName = zipEntryName + tag_slash + name;
                }
                write(file, entryName, zipOutputStream);
            }
        }
    }
}

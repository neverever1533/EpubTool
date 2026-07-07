package javaev.util;

import javaev.io.FileTool;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import java.nio.file.Files;

import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipTool {
    private String name_Zip_Entry_Root;
    private String suffix_zip = ".zip";
    private String tag_slash = "/";

    public void setRootZipEntryName(String name) {
        name_Zip_Entry_Root = name;
    }

    public String getRootZipEntryName() {
        return name_Zip_Entry_Root;
    }

    public void write(File file, File zipFile) {
    }
}

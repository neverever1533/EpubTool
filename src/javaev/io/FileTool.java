package javaev.io;

import java.io.File;
import java.io.FilenameFilter;

import java.util.ArrayList;

public class FileTool {
    public ArrayList<File> listFiles(File file) {
        ArrayList<File> list = new ArrayList<>();
        return listFiles(file, list);
    }

    public ArrayList<File> listFiles(File file, ArrayList<File> list) {
        if (null != file) {
            if (null == list) {
                list = new ArrayList<>();
            }
            return listFiles(file.listFiles(), list);
        }
        return null;
    }

    public ArrayList<File> listFiles(File[] array) {
        ArrayList<File> list = new ArrayList<>();
        return listFiles(array, list);
    }


    public ArrayList<File> listFiles(File[] array, ArrayList<File> list) {
        if (null != array) {
            if (null == list) {
                list = new ArrayList<>();
            }
            for (int i = 0; i < array.length; i++) {
                File file_temp = array[i];
                if (file_temp.isDirectory()) {
                    listFiles(file_temp, list);
                } else {
                    list.add(file_temp);
                }
            }
            return list;
        }
        return null;
    }

    public ArrayList<File> listFiles(File file, FilenameFilter filenameFilter) {
        ArrayList<File> list = new ArrayList<>();
        return listFiles(file, filenameFilter, list);
    }

    public ArrayList<File> listFiles(File file, FilenameFilter filenameFilter, ArrayList<File> list) {
        if (null != file) {
            if (null == list) {
                list = new ArrayList<>();
            }
            return listFiles(file.listFiles(filenameFilter), filenameFilter, list);
        }
        return null;
    }

    public ArrayList<File> listFiles(File[] array, FilenameFilter filenameFilter) {
        ArrayList<File> list = new ArrayList<>();
        return listFiles(array, filenameFilter, list);
    }

    public ArrayList<File> listFiles(File[] array, FilenameFilter filenameFilter, ArrayList<File> list) {
        if (null != array) {
            if (null == list) {
                list = new ArrayList<>();
            }
            for (int i = 0; i < array.length; i++) {
                File file_temp = array[i];
                if (file_temp.isDirectory()) {
                    listFiles(file_temp, filenameFilter, list);
                } else {
                    list.add(file_temp);
                }
            }
            return list;
        }
        return null;
    }

    public File[] toArray(ArrayList<File> list) {
        if (null != list) {
            File[] array = new File[list.size()];
            list.toArray(array);
            return array;
        }
        return null;
    }
}

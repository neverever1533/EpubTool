package cn.imaginary.toolkit.document.epub;

import cn.imaginary.toolkit.document.Epub;

public class Container {
    public static String Uri = "urn:oasis:names:tc:opendocument:xmlns:container";
    public static String Sign = "container";
    public static String Sign_Roots = "rootfiles";
    public static String Sign_Root = "rootfile";
    public static String Name_Path = "full-path";
    public static String Name_Media_Type = Epub.Name_Media_Type;
    public static String Name_Version = "version";
    public static String Value_Version = "1.0";

    private String path;

    public Container() {
    }

    public void setFullPath(String path) {
        this.path = path;
    }

    public String getFullPath() {
        if (null == path) {
            path = Epub.Path_Content_Default;
        }
        return path;
    }
}

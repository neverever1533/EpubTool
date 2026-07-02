package cn.imaginary.toolkit.document.epub;

import cn.imaginary.toolkit.document.Epub;

public class Toc {
    private String title;
    private String author;

    public static String Uri = "http://www.daisy.org/z3986/2005/ncx/";
    public static String Sign = "ncx";
    public static String Sign_Head = "head";
    public static String Sign_Head_Meta = "meta";
    public static String Sign_Title = "docTitle";
    public static String Sign_Author = "docAuthor";
    public static String Sign_Text = "text";
    public static String Sign_Nav_Map = "navMap";
    public static String Sign_Nav_Point = "navPoint";
    public static String Sign_Nav_Point_Label = "navLabel";
    public static String Sign_Nav_Point_Text = Sign_Text;
    public static String Sign_Nav_Point_Content = "content";
    public static String Name_Version = "version";
    public static String Name_Head_Name = "name";
    public static String Name_Head_Content = "content";
    public static String Name_Nav_Point_ID = "id";
    public static String Name_Nav_Point_Order = "playOrder";
    public static String Name_Nav_Point_Content_Src = "src";
    public static String Value_Version = "2005-1";
    public static String Value_Head_Name_UID = "dtb:uid";
    public static String Value_Head_Name_Depth = "dtb:depth";
    public static String Value_Head_Name_Generator = "dtb:generator";
    public static String Value_Head_Name_Count = "dtb:totalPageCount";
    public static String Value_Head_Name_Number = "dtb:maxPageNumber";
    public static String Value_Head_Content_UID = Epub.Value_UID;
    public static String Value_Head_Content_Depth = "1";
    public static String Value_Head_Content_Generator = Epub.Value_Rights;
    public static String Value_Head_Content_Count = "0";
    public static String Value_Head_Content_Number = "0";

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthor() {
        return author;
    }
}

package cn.imaginary.toolkit.document.epub;

import cn.imaginary.toolkit.document.Epub;

public class Content {
    private String language;
    private String language_Default = "zh_CN";
    private String title;
    private String creator;
    private String publisher;
    private String description;
    private String id;
    private String rights;
    private String data;

    public static String Uri = "http://www.idpf.org/2007/opf";
    public static String Uri_Metadata_OPF = Uri;
    public static String Uri_Metadata_DC = "http://purl.org/dc/elements/1.1/";
    public static String Xmlns_Metadata_OPF = "opf";
    public static String Xmlns_Metadata_DC = "dc";

    public static String Sign = "package";
    public static String Sign_Metadata = "metadata";
    public static String Sign_Metadata_Language = "language";
    public static String Sign_Metadata_Title = "title";
    public static String Sign_Metadata_Creator = "creator";
    public static String Sign_Metadata_Publisher = "publisher";
    public static String Sign_Metadata_Description = "description";
    public static String Sign_Metadata_Identifier = "identifier";
    public static String Sign_Metadata_Rights = "rights";
    public static String Sign_Metadata_Data = "data";
    public static String Sign_Manifest = "manifest";
    public static String Sign_Manifest_Item = "item";
    public static String Sign_Spine = "spine";
    public static String Sign_Spine_Itemref = "itemref";

    public static String Name_Version = "version";
    public static String Name_UID = "unique-identifier";
    public static String Name_Metadata_ID = "id";
    public static String Name_Manifest_Item_Href = "href";
    public static String Name_Manifest_Item_ID = Name_Metadata_ID;
    public static String Name_Manifest_Item_Media_Type = Epub.Name_Media_Type;
    public static String Name_Spine_Toc = "toc";
    public static String Name_Spine_Itemref_Idref = "idref";

    public static String Value_Version = "3.0";
    public static String Value_UID = Epub.Value_UID;
    public static String Value_Metadata_ID = Value_UID;
    public static String Value_Manifest_Item_ID = Name_Metadata_ID;
    public static String Value_Spine_Toc = Epub.Path_Toc;
    public static String Value_Spine_Itemref_Idref = Name_Metadata_ID;

    public Content() {
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getLanguage() {
        if (null == language) {
            language = language_Default;
        }
        return language;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getCreator() {
        return creator;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setID(String id) {
        this.id = id;
    }

    public String getID() {
        return id;
    }

    public void setRights(String rights) {
        this.rights = rights;
    }

    public String getRights() {
        return rights;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }
}

package cn.imaginary.toolkit.document;

import cn.imaginary.toolkit.document.epub.Container;
import cn.imaginary.toolkit.document.epub.Content;
import cn.imaginary.toolkit.document.epub.EpubMimetype;
import cn.imaginary.toolkit.document.epub.HtmlToc;
import cn.imaginary.toolkit.document.epub.Toc;

public class Epub {
    public static String Path_Package = "epubzip";
    public static String Path_META_INF = "META-INF";
    public static String Path_OEBPS = "OEBPS";
    public static String Path_OEBPS_Pages = "pages";
    public static String Path_OEBPS_Styles = "styles";
    public static String Path_OEBPS_Application = "application";
    public static String Path_OEBPS_Images = "images";
    public static String Path_OEBPS_Audios = "audios";
    public static String Path_OEBPS_Videos = "videos";
    public static String Path_OEBPS_Texts = "texts";
    public static String Path_OEBPS_Fonts = "fonts";

    public static String Path_Container = "container.xml";
    public static String Path_Container_Default = "META-INF/container.xml";
    public static String Path_Content = "content.opf";
    public static String Path_Content_Default = "OEBPS/content.opf";
    public static String Path_Toc = "toc.ncx";
    public static String Path_Toc_Default = "OEBPS/toc.ncx";
    public static String Path_XML_Content = "content.opf.xml";
    public static String Path_XML_Toc = "toc.ncx.xml";
    public static String Path_Toc_html = "toc.html";
    public static String Path_Toc_html_Default = "OEBPS/toc.html";
    public static String Path_Cover_html = "cover.html";
    public static String Path_Cover_html_Default = "OEBPS/cover.html";
    public static String Path_Cover_Jpg = "cover.jpg";
    public static String Path_Cover_Jpeg = "cover.jpe";
    public static String Path_Cover_Png = "cover.png";
    public static String Path_Mimetype = "mimetype";

    private String Value_Language = "zh_CN";
    private String Value_Title = "My Book";
    private String Value_Creator = "creator";
    private String Value_Publisher = "publisher";
    private String Value_Description = "My Novel";
    private String Value_ID = "id";
    private String Value_Rights = "rights";
    private String Value_Data = "2026-6";

    private String Value_Cover = "cover-image";

    public static String Value_Rights_Default = "EpubTool v0.2";
    public static String Value_UID = "public-id";

    public static String Name_Cover = "cover";
    public static String Name_Media_Type = "media-type";
    public static String String_Null = "";
    public static String Type_Object_String = "String";

    private Container container;
    private EpubMimetype mimetype;
    private Content content;
    private Toc toc;
    private HtmlToc htmltoc;

    public Epub() {
    }

    public void setContainer(Container container) {
        this.container = container;
    }

    public Container getContainer() {
        if (null == container) {
            container = new Container();
            container.setFullPath(Path_Content_Default);
        }
        return container;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public Content getContent() {
        if (null == content) {
            content = new Content();
            content.setLanguage(Value_Language);
            content.setTitle(Value_Title);
            content.setCreator(Value_Creator);
            content.setPublisher(Value_Publisher);
            content.setDescription(Value_Description);
            content.setID(Value_ID);
            content.setRights(Value_Rights);
            content.setData(Value_Data);
        }
        return content;
    }

    public void setMimetype(EpubMimetype mimetype) {
        this.mimetype = mimetype;
    }

    public EpubMimetype getMimetype() {
        if (null == mimetype) {
            mimetype = new EpubMimetype();
        }
        return mimetype;
    }

    public void setHtmlToc(HtmlToc htmltoc) {
        this.htmltoc = htmltoc;
    }

    public HtmlToc getHtmlToc() {
        if (null == htmltoc) {
            htmltoc = new HtmlToc();
        }
        return htmltoc;
    }

    public void setToc(Toc toc) {
        this.toc = toc;
    }

    public Toc getToc() {
        if (null == toc) {
            toc = new Toc();
        }
        return toc;
    }
}

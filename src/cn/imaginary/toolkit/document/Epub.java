package cn.imaginary.toolkit.document;

import cn.imaginary.toolkit.document.epub.Content;
import cn.imaginary.toolkit.document.epub.Container;
import cn.imaginary.toolkit.document.epub.EpubMimetype;
import cn.imaginary.toolkit.document.epub.Toc;
import cn.imaginary.toolkit.document.epub.HtmlToc;

public class Epub {
    public static String Path_Meta_Inf = "META-INF";
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
    public static String Path_Toc_html = "toc.html";
    public static String Path_Toc_html_Default = "OEBPS/toc.html";
    public static String Path_Cover = "cover.png";
    public static String Path_Mimetype = "mimetype";

    private String suffix_container = ".xml";
    private String suffix_content = ".opf";
    private String suffix_cover = ".jpeg";
    private String suffix_index_split = ".html";
    private String suffix_styles = ".css";
    private String suffix_toc = ".ncx";
    private String suffix_title_page = ".xhtml";

    public static String Value_Language = "zh_CN";
    public static String Value_Title = "My Book";
    public static String Value_Creator = "creator";
    public static String Value_Publisher = "publisher";
    public static String Value_Description = "My Novel";
    public static String Value_ID = "id";
    public static String Value_Rights = "EpubTool v1.0";
    public static String Value_Fata = "data";
    public static String Value_UID = "public-id";

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

    public void setMimetype(EpubMimetype mimetype) {
        this.mimetype = mimetype;
    }

    public EpubMimetype getMimetype() {
        if (null == mimetype) {
            mimetype = new EpubMimetype();
        }
        return mimetype;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public Content getContent() {
        if (null == content) {
            content = new Content();
        }
        return content;
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

    public void setHtmlToc(HtmlToc htmltoc) {
        this.htmltoc = htmltoc;
    }

    public HtmlToc getHtmlToc() {
        if (null == htmltoc) {
            htmltoc = new HtmlToc();
        }
        return htmltoc;
    }
}

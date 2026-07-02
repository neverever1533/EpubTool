package cn.imaginary.toolkit.document.epub.mimetype;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class EpubMimetypesFileTypeMap {
    public static String File_Extension_Epub = "epub";
    public static String File_Extension_Opf = "opf";
    public static String File_Extension_Epub_Ncx = "ncx";
    public static String File_Extension_Epub_Html = "html";
    public static String File_Extension_Epub_Xhtml = "xhtml";
    public static String File_Extension_Epub_Gif = "gif";
    public static String File_Extension_Epub_Jpg = "jpg";
    public static String File_Extension_Epub_Jpeg = "jpeg";
    public static String File_Extension_Epub_Png = "png";
    public static String File_Extension_Epub_Svg = "svg";
    public static String File_Extension_Epub_Webp = "webp";
    public static String File_Extension_Epub_Avif = "avif";
    public static String File_Extension_Epub_Jxl = "jxl";
    public static String File_Extension_Epub_Mp3 = "mp3";
    public static String File_Extension_Epub_Mp4 = "mp4";
    public static String File_Extension_Epub_Ogg = "ogg";
    public static String File_Extension_Epub_Css = "css";
    public static String File_Extension_Epub_Ttf = "ttf";
    public static String File_Extension_Epub_Otf = "otf";
    public static String File_Extension_Epub_Woff = "woff";
    public static String File_Extension_Epub_Woff2 = "woff2";
    public static String File_Extension_Epub_JavaScript = "js";
    public static String File_Extension_Epub_Smil = "smil";

    public static String Mimetype_Application = "application";
    public static String Mimetype_Image = "image";
    public static String Mimetype_Text = "text";
    public static String Mimetype_Audio = "audio";
    public static String Mimetype_Video = "video";
    public static String Mimetype_Font = "font";

    public static String Mimetype_Epub = "application/epub+zip";
    public static String Mimetype_Epub_Opf = "application/oebps-package+xml";
    public static String Mimetype_Epub_Ncx = "application/x-dtbncx+xml";
    public static String Mimetype_Epub_Html = "text/html";
    public static String Mimetype_Epub_Xhtml = "application/xhtml+xml";
    public static String Mimetype_Epub_Gif = "image/gif";
    public static String Mimetype_Epub_Jpeg = "image/jpeg";
    public static String Mimetype_Epub_Png = "image/png";
    public static String Mimetype_Epub_Svg = "image/svg+xml";
    public static String Mimetype_Epub_Webp = "image/webp";
    public static String Mimetype_Epub_Avif = "image/avif";
    public static String Mimetype_Epub_Jxl = "image/jxl";
    public static String Mimetype_Epub_Mp3 = "audio/mpeg";
    public static String Mimetype_Epub_Mp4 = "video/mp4";
    public static String Mimetype_Epub_Ogg = "audio/ogg; codecs=opus";
    public static String Mimetype_Epub_Css = "text/css";
    public static String Mimetype_Epub_Ttf = "font/ttf";
    public static String Mimetype_Epub_Otf = "font/otf";
    public static String Mimetype_Epub_Woff = "font/woff";
    public static String Mimetype_Epub_Woff2 = "font/woff2";
    public static String Mimetype_Epub_JavaScript = "text/javascript";
    public static String Mimetype_Epub_Smil = "application/x-dtbncx+xml";

    public static int Application = 0;
    public static int Image = 1;
    public static int Text = 2;
    public static int Audio = 3;
    public static int Video = 4;
    public static int Font = 5;

    private static Map<String, String>
            epubMimetypesFileExtensionMap = new HashMap() {
        {
            put(File_Extension_Epub, Mimetype_Epub);
            put(File_Extension_Opf, Mimetype_Epub_Opf);
            put(File_Extension_Epub_Ncx, Mimetype_Epub_Ncx);
            put(File_Extension_Epub_Html, Mimetype_Epub_Html);
            put(File_Extension_Epub_Xhtml, Mimetype_Epub_Xhtml);
            put(File_Extension_Epub_Gif, Mimetype_Epub_Gif);
            put(File_Extension_Epub_Jpeg, Mimetype_Epub_Jpeg);
            put(File_Extension_Epub_Jpg, Mimetype_Epub_Jpeg);
            put(File_Extension_Epub_Png, Mimetype_Epub_Png);
            put(File_Extension_Epub_Svg, Mimetype_Epub_Svg);
            put(File_Extension_Epub_Webp, Mimetype_Epub_Webp);
            put(File_Extension_Epub_Avif, Mimetype_Epub_Avif);
            put(File_Extension_Epub_Jxl, Mimetype_Epub_Jxl);
            put(File_Extension_Epub_Mp3, Mimetype_Epub_Mp3);
            put(File_Extension_Epub_Mp4, Mimetype_Epub_Mp4);
            put(File_Extension_Epub_Ogg, Mimetype_Epub_Ogg);
            put(File_Extension_Epub_Css, Mimetype_Epub_Css);
            put(File_Extension_Epub_Ttf, Mimetype_Epub_Ttf);
            put(File_Extension_Epub_Otf, Mimetype_Epub_Otf);
            put(File_Extension_Epub_Woff, Mimetype_Epub_Woff);
            put(File_Extension_Epub_Woff2, Mimetype_Epub_Woff2);
            put(File_Extension_Epub_JavaScript, Mimetype_Epub_JavaScript);
            put(File_Extension_Epub_Smil, Mimetype_Epub_Smil);
        }
    };

    public static String getContentType(File file) {
        if (null != file) {
            return getContentType(file.getAbsolutePath());
        }
        return null;
    }

    public static String getContentType(String filePath) {
        if (null != filePath) {
            String extension = null;
            int index = filePath.lastIndexOf(".");
            if (index != -1) {
                extension = filePath.substring(index + 1).toLowerCase();
            }
//            System.out.println("File extension:" + extension);

            if (epubMimetypesFileExtensionMap.containsKey(extension)) {
                return epubMimetypesFileExtensionMap.get(extension);
            }
        }
        return null;
    }

    public static boolean isApplication(File file) {
        if (null != file) {
            return isApplication(file.getAbsolutePath());
        }
        return false;
    }

    public static boolean isApplication(String filePath) {
        if (null != filePath) {
            return isType(getContentType(filePath), Application);
        }
        return false;
    }

    public static boolean isImage(File file) {
        if (null != file) {
            return isImage(file.getAbsolutePath());
        }
        return false;
    }

    public static boolean isImage(String filePath) {
        if (null != filePath) {
            return isType(getContentType(filePath), Image);
        }
        return false;
    }

    public static boolean isAudio(File file) {
        if (null != file) {
            return isAudio(file.getAbsolutePath());
        }
        return false;
    }

    public static boolean isAudio(String filePath) {
        if (null != filePath) {
            return isType(getContentType(filePath), Audio);
        }
        return false;
    }

    public static boolean isVideo(File file) {
        if (null != file) {
            return isVideo(file.getAbsolutePath());
        }
        return false;
    }

    public static boolean isVideo(String filePath) {
        if (null != filePath) {
            return isType(getContentType(filePath), Video);
        }
        return false;
    }

    public static boolean isText(File file) {
        if (null != file) {
            return isText(file.getAbsolutePath());
        }
        return false;
    }

    public static boolean isText(String filePath) {
        if (null != filePath) {
            return isType(getContentType(filePath), Text);
        }
        return false;
    }

    public static boolean isFont(File file) {
        if (null != file) {
            return isFont(file.getAbsolutePath());
        }
        return false;
    }

    public static boolean isFont(String filePath) {
        if (null != filePath) {
            return isType(getContentType(filePath), Font);
        }
        return false;
    }

    private static boolean isType(String type, int index) {
        if (null != type) {
            if (index == 0) {
                if (type.startsWith(Mimetype_Application)) {
                    return true;
                }
            } else if (index == 1) {
                if (type.startsWith(Mimetype_Image)) {
                    return true;
                }
            } else if (index == 2) {
                if (type.startsWith(Mimetype_Audio)) {
                    return true;
                }
            } else if (index == 3) {
                if (type.startsWith(Mimetype_Video)) {
                    return true;
                }
            } else if (index == 4) {
                if (type.startsWith(Mimetype_Text)) {
                    return true;
                }
            } else if (index == 5) {
                if (type.startsWith(Mimetype_Font)) {
                    return true;
                }
            }
        }
        return false;
    }
}

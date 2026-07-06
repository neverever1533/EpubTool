package cn.imaginary.toolkit;

import cn.imaginary.toolkit.document.Epub;
import cn.imaginary.toolkit.document.epub.Container;
import cn.imaginary.toolkit.document.epub.Content;
import cn.imaginary.toolkit.document.epub.EpubMimetype;
import cn.imaginary.toolkit.document.epub.HtmlCover;
import cn.imaginary.toolkit.document.epub.HtmlToc;
import cn.imaginary.toolkit.document.epub.Toc;
import cn.imaginary.toolkit.document.epub.mimetype.EpubMimetypesFileTypeMap;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

import java.io.File;
import java.io.FilenameFilter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;

import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.Properties;

public class EpubTool {
    private String XML_Encoding_UTF8 = StandardCharsets.UTF_8.name();
    private String XML_Version_Default = "1.0";
    private String XML_Indent = "yes";

    private String tag_path = "Path";
    private String tag_language = "Language";
    private String tag_title = "Title";
    private String tag_creator = "Creator";
    private String tag_publisher = "Publisher";
    private String tag_description = "Description";
    private String tag_id = "ID";
    private String tag_rights = "Rights";
    private String tag_data = "Data";
    private String tag_slash = "/";
    private String tag_underscore = "_";

    private String path_Resources;

    private String pattern = "yyyy-MM-dd HH:mm:ss";
    private String suffix_properties = ".ini";

    private Epub epub = new Epub();

    private File[] resources;
    private File[] resources_Images;
    private File[] resources_Pages;
    private File[] resources_No_Pages;

    private Properties paths;

    public EpubTool() {
    }

    public void setEpub(Epub epub) {
        this.epub = epub;
    }

    public Epub getEpub() {
        return epub;
    }

    public String checkData() {
        Calendar calendar = Calendar.getInstance();
        return new SimpleDateFormat(pattern).format(calendar.getTime());
    }

    public void read(File file) {
        loadProperties(file);
    }

    private void loadProperties(File file) {
        if (null != file) {
//            String name = file.getName();
//            String suffix = getSuffix(name);
//            if (suffix.equalsIgnoreCase(suffix_properties)) {
            Properties properties = new Properties();
            try {
                properties.load(Files.newInputStream(file.toPath()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            loadInfo(properties);
//            }
        }
    }

    private void loadProperties(String filePath) {
        if (null != filePath) {
            loadProperties(new File(filePath));
        }
    }

    private void loadInfo(Properties properties) {
        if (null != properties) {
            Content content = epub.getContent();
            String language = properties.getProperty(tag_language);
            String title = properties.getProperty(tag_title);
            String creator = properties.getProperty(tag_creator);
            String publisher = properties.getProperty(tag_publisher);
            String description = properties.getProperty(tag_description);
            String id = properties.getProperty(tag_id);
            String rights = properties.getProperty(tag_rights);
            String data = properties.getProperty(tag_data);

            if (null == language) {
                language = epub.getContent().getLanguage();
            }
            if (null == title) {
                title = epub.getContent().getTitle();
            }
            if (null == creator) {
                creator = epub.getContent().getCreator();
            }
            if (null == publisher) {
                publisher = epub.getContent().getPublisher();
            }
            if (null == description) {
                description = epub.getContent().getDescription();
            }
            if (null == id) {
                id = epub.getContent().getID();
            }
            if (null == rights) {
                rights = epub.getContent().getRights();
            }
            if (null == data) {
                data = checkData();
            }

            content.setLanguage(language);
            content.setTitle(title);
            content.setCreator(creator);
            content.setPublisher(publisher);
            content.setDescription(description);
            content.setID(id);
            content.setRights(rights);
            content.setData(data);

            path_Resources = properties.getProperty(tag_path);
            loadResources(path_Resources);
        }
    }

    private void loadResources(String filePath) {
        loadResources(new File(filePath));
    }

    private void loadResources(File dirFile) {
        if (null != dirFile && dirFile.exists() && dirFile.isDirectory()) {
            resources = dirFile.listFiles();

            FilenameFilter filenameFilter_Images = new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    if (EpubMimetypesFileTypeMap.isImage(name)) {
                        return true;
                    } else {
                        return false;
                    }
                }
            };
            resources_Images = dirFile.listFiles(filenameFilter_Images);

            FilenameFilter filenameFilter_Pages = new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    if (name.endsWith(EpubMimetypesFileTypeMap.File_Extension_Epub_Html) || name.endsWith(EpubMimetypesFileTypeMap.File_Extension_Epub_Xhtml)) {
                        return true;
                    } else {
                        return false;
                    }
                }
            };
            resources_Pages = dirFile.listFiles(filenameFilter_Pages);

            FilenameFilter filenameFilter_No_Pages = new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    if (name.endsWith(EpubMimetypesFileTypeMap.File_Extension_Epub_Html) || name.endsWith(EpubMimetypesFileTypeMap.File_Extension_Epub_Xhtml)) {
                        return false;
                    } else {
                        return true;
                    }
                }
            };
            resources_No_Pages = dirFile.listFiles(filenameFilter_No_Pages);
        } else {
            resources = null;
        }
    }

    private void loadPaths(File[] array) {
        if (null != array) {
            paths = new Properties();
            for (int i = 0; i < array.length; i++) {
                File file = array[i];
                String name = file.getName().toLowerCase();
                String path;
                if (name.endsWith(EpubMimetypesFileTypeMap.File_Extension_Epub_Html) || name.endsWith(EpubMimetypesFileTypeMap.File_Extension_Epub_Xhtml)) {
                    path = Epub.Path_OEBPS_Pages;
                } else if (name.endsWith(EpubMimetypesFileTypeMap.File_Extension_Epub_JavaScript) || name.endsWith(EpubMimetypesFileTypeMap.File_Extension_Epub_Css)) {
                    path = Epub.Path_OEBPS_Styles;
                } else if (EpubMimetypesFileTypeMap.isImage(file)) {
                    path = Epub.Path_OEBPS_Images;
                } else if (EpubMimetypesFileTypeMap.isAudio(file)) {
                    path = Epub.Path_OEBPS_Audios;
                } else if (EpubMimetypesFileTypeMap.isVideo(file)) {
                    path = Epub.Path_OEBPS_Videos;
                } else if (EpubMimetypesFileTypeMap.isText(file)) {
                    path = Epub.Path_OEBPS_Texts;
                } else if (EpubMimetypesFileTypeMap.isFont(file)) {
                    path = Epub.Path_OEBPS_Fonts;
                } else {
                    path = Epub.Path_OEBPS_Application;
                }
                paths.put(file.getAbsolutePath(), path);
            }
        }
    }

    public void write(File file) {
        write(file, false);
    }

    public void write(File file, boolean isFileFilter) {
        if (null != resources) {
            if (isFileFilter) {
                loadPaths(resources);
            }
            File dirFile_Epub;
            if (null == file) {
                dirFile_Epub = new File(path_Resources + Epub.Path_Package);
            } else {
                File parentFile = file.getParentFile();
                dirFile_Epub = new File(parentFile, file.getName() + Epub.Path_Package);
            }
            if (!dirFile_Epub.exists()) {
                dirFile_Epub.mkdir();
            }

            writeMimetype(new File(dirFile_Epub, Epub.Path_Mimetype));

            File dirFile_Epub_META_INF = new File(dirFile_Epub, Epub.Path_META_INF);
            if (!dirFile_Epub_META_INF.exists()) {
                dirFile_Epub_META_INF.mkdir();
            }
            writeContainer(new File(dirFile_Epub_META_INF, Epub.Path_Container));

            File dirFile_Epub_OEBPS = new File(dirFile_Epub, Epub.Path_OEBPS);
            if (!dirFile_Epub_OEBPS.exists()) {
                dirFile_Epub_OEBPS.mkdir();
            }
            try {
                writeResource(dirFile_Epub_OEBPS);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            File file_Cover_Html = new File(dirFile_Epub_OEBPS, Epub.Path_Cover_html);
            if (!file_Cover_Html.exists()) {
                writeHtmlCover(file_Cover_Html);
            }
            File file_Toc_Html = new File(dirFile_Epub_OEBPS, Epub.Path_Toc_html);
            if (!file_Toc_Html.exists()) {
                writeHtmlToc(file_Toc_Html);
            }
//            writeContent(new File(dirFile_Epub_OEBPS, Epub.Path_XML_Content));
//            writeToc(new File(dirFile_Epub_OEBPS, Epub.Path_XML_Toc));
            writeContent(new File(dirFile_Epub_OEBPS, Epub.Path_Content));
            writeToc(new File(dirFile_Epub_OEBPS, Epub.Path_Toc));
        }
    }

    private void writeMimetype(File file) {
        try {
            Files.write(file.toPath(), EpubMimetype.Media_Type.getBytes(), StandardOpenOption.CREATE);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void writeContainer(File file) {
        try {
            Result result = new StreamResult(new FileOutputStream(file));
            SAXTransformerFactory saxTransformerFactory = (SAXTransformerFactory) SAXTransformerFactory.newInstance();
            TransformerHandler transformerHandler = saxTransformerFactory.newTransformerHandler();
            transformerHandler.setResult(result);
            Transformer transformer = transformerHandler.getTransformer();

            transformer.setOutputProperty(OutputKeys.VERSION, XML_Version_Default);
            transformer.setOutputProperty(OutputKeys.ENCODING, XML_Encoding_UTF8);
            transformer.setOutputProperty(OutputKeys.INDENT, XML_Indent);

            transformerHandler.startDocument();
            AttributesImpl attributesImpl = new AttributesImpl();

            attributesImpl.addAttribute(Epub.String_Null, Epub.String_Null, Container.Name_Version, Epub.Type_Object_String, Container.Value_Version);
            transformerHandler.startElement(Container.Uri, Epub.String_Null, Container.Sign, attributesImpl);

            attributesImpl.clear();

            transformerHandler.startElement(Epub.String_Null, Epub.String_Null, Container.Sign_Roots, attributesImpl);

            attributesImpl.addAttribute(Epub.String_Null, Epub.String_Null, Container.Name_Path, Epub.Type_Object_String, epub.getContainer().getFullPath());
            attributesImpl.addAttribute(Epub.String_Null, Epub.String_Null, Epub.Name_Media_Type, Epub.Type_Object_String, EpubMimetypesFileTypeMap.getContentType(epub.getContainer().getFullPath()));
            transformerHandler.startElement(Epub.String_Null, Epub.String_Null, Container.Sign_Root, attributesImpl);

            attributesImpl.clear();

            transformerHandler.endElement(Epub.String_Null, Epub.String_Null, Container.Sign_Root);

            transformerHandler.endElement(Epub.String_Null, Epub.String_Null, Container.Sign_Roots);

            transformerHandler.endElement(Epub.String_Null, Epub.String_Null, Container.Sign);

            transformerHandler.endDocument();
        } catch (FileNotFoundException | TransformerConfigurationException | SAXException e) {
            throw new RuntimeException(e);
        }
    }

    private void writeResource(File dirFile) throws IOException {
        if (null != resources && null != dirFile && dirFile.isDirectory()) {
            for (int i = 0; i < resources.length; i++) {
                File file = resources[i];
                String name = file.getName();
                File dirFile_Parent_Epub;
                if (null != paths) {
                    dirFile_Parent_Epub = new File(dirFile, paths.getProperty(file.getAbsolutePath()));
                    if (!dirFile_Parent_Epub.exists()) {
                        dirFile_Parent_Epub.mkdir();
                    }
                } else {
                    dirFile_Parent_Epub = dirFile;
                }
                File file_Epub = new File(dirFile_Parent_Epub, name);
                Files.copy(file.toPath(), file_Epub.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
        }
    }

    private void writeContent(File file) {
        try {
            Result result = new StreamResult(new FileOutputStream(file));
            SAXTransformerFactory saxTransformerFactory = (SAXTransformerFactory) SAXTransformerFactory.newInstance();
            TransformerHandler transformerHandler = saxTransformerFactory.newTransformerHandler();
            transformerHandler.setResult(result);
            Transformer transformer = transformerHandler.getTransformer();

            transformer.setOutputProperty(OutputKeys.VERSION, XML_Version_Default);
            transformer.setOutputProperty(OutputKeys.ENCODING, XML_Encoding_UTF8);
            transformer.setOutputProperty(OutputKeys.INDENT, XML_Indent);

            transformerHandler.startDocument();
            AttributesImpl attributesImpl = new AttributesImpl();

            attributesImpl.addAttribute(Epub.String_Null, Epub.String_Null, Content.Name_Version, Epub.Type_Object_String, Content.Value_Version);
            attributesImpl.addAttribute(Epub.String_Null, Epub.String_Null, Content.Name_UID, Epub.Type_Object_String, Content.Value_UID);
            transformerHandler.startElement(Content.Uri, Epub.String_Null, Content.Sign, attributesImpl);
            attributesImpl.clear();

            transformerHandler.startPrefixMapping(Content.Xmlns_Metadata_OPF, Content.Uri_Metadata_OPF);
            transformerHandler.startPrefixMapping(Content.Xmlns_Metadata_DC, Content.Uri_Metadata_DC);
            transformerHandler.startElement(Epub.String_Null, Epub.String_Null, Content.Sign_Metadata, attributesImpl);

            transformerHandler.startElement(Epub.String_Null, Epub.String_Null, Content.Sign_Metadata_Language, attributesImpl);
            transformerHandler.characters(epub.getContent().getLanguage().toCharArray(), 0, epub.getContent().getLanguage().toCharArray().length);
            transformerHandler.endElement(Epub.String_Null, Epub.String_Null, Content.Sign_Metadata_Language);

            transformerHandler.startElement(Epub.String_Null, Epub.String_Null, Content.Sign_Metadata_Title, attributesImpl);
            transformerHandler.characters(epub.getContent().getTitle().toCharArray(), 0, epub.getContent().getTitle().toCharArray().length);
            transformerHandler.endElement(Epub.String_Null, Epub.String_Null, Content.Sign_Metadata_Title);

            transformerHandler.startElement(Epub.String_Null, Epub.String_Null, Content.Sign_Metadata_Creator, attributesImpl);
            transformerHandler.characters(epub.getContent().getCreator().toCharArray(), 0, epub.getContent().getCreator().toCharArray().length);
            transformerHandler.endElement(Epub.String_Null, Epub.String_Null, Content.Sign_Metadata_Creator);

            transformerHandler.startElement(Epub.String_Null, Epub.String_Null, Content.Sign_Metadata_Publisher, attributesImpl);
            transformerHandler.characters(epub.getContent().getPublisher().toCharArray(), 0, epub.getContent().getPublisher().toCharArray().length);
            transformerHandler.endElement(Epub.String_Null, Epub.String_Null, Content.Sign_Metadata_Publisher);

            transformerHandler.startElement(Epub.String_Null, Epub.String_Null, Content.Sign_Metadata_Description, attributesImpl);
            transformerHandler.characters(epub.getContent().getDescription().toCharArray(), 0, epub.getContent().getDescription().toCharArray().length);
            transformerHandler.endElement(Epub.String_Null, Epub.String_Null, Content.Sign_Metadata_Description);

            attributesImpl.addAttribute(Epub.String_Null, Epub.String_Null, Content.Name_Metadata_ID, Epub.Type_Object_String, Content.Value_Metadata_ID);
            transformerHandler.startElement(Epub.String_Null, Epub.String_Null, Content.Sign_Metadata_Identifier, attributesImpl);
            attributesImpl.clear();
            transformerHandler.characters(epub.getContent().getID().toCharArray(), 0, epub.getContent().getID().toCharArray().length);
            transformerHandler.endElement(Epub.String_Null, Epub.String_Null, Content.Sign_Metadata_Identifier);

            transformerHandler.startElement(Epub.String_Null, Epub.String_Null, Content.Sign_Metadata_Rights, attributesImpl);
            transformerHandler.characters(epub.getContent().getRights().toCharArray(), 0, epub.getContent().getRights().toCharArray().length);
            transformerHandler.endElement(Epub.String_Null, Epub.String_Null, Content.Sign_Metadata_Rights);

            transformerHandler.startElement(Epub.String_Null, Epub.String_Null, Content.Sign_Metadata_Data, attributesImpl);
            transformerHandler.characters(epub.getContent().getData().toCharArray(), 0, epub.getContent().getData().toCharArray().length);
            transformerHandler.endElement(Epub.String_Null, Epub.String_Null, Content.Sign_Metadata_Data);

            transformerHandler.endElement(Epub.String_Null, Epub.String_Null, Content.Sign_Metadata);

            transformerHandler.startElement(Epub.String_Null, Epub.String_Null, Content.Sign_Manifest, attributesImpl);

            int index = 0;

            File file_Cover_Html = getFile(resources, Epub.Path_Cover_html);
            String name;
            if (null != file_Cover_Html) {
                name = file_Cover_Html.getName();
            } else {
                name = Epub.Path_Cover_html;
            }
            String path;
            if (null != paths) {
                path = paths.getProperty(file_Cover_Html.getAbsolutePath()) + tag_slash + name;
            } else {
                path = name;
            }
            attributesImpl.addAttribute(Epub.String_Null, Epub.String_Null, Content.Name_Manifest_Item_Href, Epub.Type_Object_String, path);
            attributesImpl.addAttribute(Epub.String_Null, Epub.String_Null, Content.Name_Manifest_Item_ID, Epub.Type_Object_String, Content.Value_Manifest_Item_ID + tag_underscore + index);
            attributesImpl.addAttribute(Epub.String_Null, Epub.String_Null, Content.Name_Manifest_Item_Media_Type, Epub.Type_Object_String, EpubMimetypesFileTypeMap.getContentType(name));
            transformerHandler.startElement(Epub.String_Null, Epub.String_Null, Content.Sign_Manifest_Item, attributesImpl);
            attributesImpl.clear();

            transformerHandler.endElement(Epub.String_Null, Epub.String_Null, Content.Sign_Manifest_Item);
            index++;

            File file_Toc_Html = getFile(resources, Epub.Path_Toc_html);
            if (null != file_Toc_Html) {
                name = file_Toc_Html.getName();
            } else {
                name = Epub.Path_Toc_html;
            }
            if (null != paths) {
                path = EpubMimetypesFileTypeMap.getContentType(name) + tag_slash + name;
            } else {
                path = name;
            }
            attributesImpl.addAttribute(Epub.String_Null, Epub.String_Null, Content.Name_Manifest_Item_Href, Epub.Type_Object_String, path);
            attributesImpl.addAttribute(Epub.String_Null, Epub.String_Null, Content.Name_Manifest_Item_ID, Epub.Type_Object_String, Content.Value_Manifest_Item_ID + tag_underscore + index);
            attributesImpl.addAttribute(Epub.String_Null, Epub.String_Null, Content.Name_Manifest_Item_Media_Type, Epub.Type_Object_String, EpubMimetypesFileTypeMap.getContentType(name));
            transformerHandler.startElement(Epub.String_Null, Epub.String_Null, Content.Sign_Manifest_Item, attributesImpl);
            attributesImpl.clear();

            transformerHandler.endElement(Epub.String_Null, Epub.String_Null, Content.Sign_Manifest_Item);
            index++;

            Properties properties = new Properties();
            index = addContentManifestItem(transformerHandler, attributesImpl, resources_No_Pages, index, properties);
            addContentManifestItem(transformerHandler, attributesImpl, resources_Pages, index, properties);

            transformerHandler.endElement(Epub.String_Null, Epub.String_Null, Content.Sign_Manifest);

            attributesImpl.addAttribute(Epub.String_Null, Epub.String_Null, Content.Name_Spine_Toc, Epub.Type_Object_String, Epub.Path_Toc);
            transformerHandler.startElement(Epub.String_Null, Epub.String_Null, Content.Sign_Spine, attributesImpl);
            attributesImpl.clear();

            index = 0;

            attributesImpl.addAttribute(Epub.String_Null, Epub.String_Null, Content.Name_Spine_Itemref_Idref, Epub.Type_Object_String, Content.Value_Spine_Itemref_Idref + tag_underscore + index);
            transformerHandler.startElement(Epub.String_Null, Epub.String_Null, Content.Sign_Spine_Itemref, attributesImpl);
            attributesImpl.clear();

            transformerHandler.endElement(Epub.String_Null, Epub.String_Null, Content.Sign_Spine_Itemref);
            index++;

            attributesImpl.addAttribute(Epub.String_Null, Epub.String_Null, Content.Name_Spine_Itemref_Idref, Epub.Type_Object_String, Content.Value_Spine_Itemref_Idref + tag_underscore + index);
            transformerHandler.startElement(Epub.String_Null, Epub.String_Null, Content.Sign_Spine_Itemref, attributesImpl);
            attributesImpl.clear();

            transformerHandler.endElement(Epub.String_Null, Epub.String_Null, Content.Sign_Spine_Itemref);
            index++;

            addContentSpineItem(transformerHandler, attributesImpl, resources_Pages, index, properties);

            transformerHandler.endElement(Epub.String_Null, Epub.String_Null, Content.Sign_Spine);

            transformerHandler.endElement(Epub.String_Null, Epub.String_Null, Content.Sign);

            transformerHandler.endDocument();
        } catch (FileNotFoundException | TransformerConfigurationException | SAXException e) {
            throw new RuntimeException(e);
        }
    }

    private File getFile(File[] array, String fileName) {
        if (null != array && null != fileName) {
            for (int i = 0; i < array.length; i++) {
                File file = array[i];
                String name = file.getName();
                String name_Prefix = getPrefix(name);
                if (fileName.equalsIgnoreCase(name) || fileName.equalsIgnoreCase(name_Prefix)) {
                    return file;
                }
            }
        }
        return null;
    }

    private int addContentManifestItem(TransformerHandler transformerHandler, AttributesImpl attributesImpl, File[] array, int index, Properties properties) throws SAXException {
        if (null != transformerHandler && null != attributesImpl && null != array && null != properties) {
            for (int i = 0; i < array.length; i++) {
                File file = array[i];
                String name = file.getName();
                if (!name.equalsIgnoreCase(Epub.Path_Cover_html) || !name.equalsIgnoreCase(Epub.Path_Toc_html)) {
                    String path;
                    if (null != paths) {
                        path = paths.getProperty(file.getAbsolutePath()) + tag_slash + name;
                    } else {
                        path = name;
                    }

                    attributesImpl.addAttribute(Epub.String_Null, Epub.String_Null, Content.Name_Manifest_Item_Href, Epub.Type_Object_String, path);
                    attributesImpl.addAttribute(Epub.String_Null, Epub.String_Null, Content.Name_Manifest_Item_ID, Epub.Type_Object_String, Content.Value_Manifest_Item_ID + tag_underscore + index);
                    attributesImpl.addAttribute(Epub.String_Null, Epub.String_Null, Content.Name_Manifest_Item_Media_Type, Epub.Type_Object_String, EpubMimetypesFileTypeMap.getContentType(name));
                    transformerHandler.startElement(Epub.String_Null, Epub.String_Null, Content.Sign_Manifest_Item, attributesImpl);
                    attributesImpl.clear();

                    transformerHandler.endElement(Epub.String_Null, Epub.String_Null, Content.Sign_Manifest_Item);
                    properties.put(file.getAbsolutePath(), index);
                    index++;
                }
            }
        }
        return index;
    }

    private void addContentSpineItem(TransformerHandler transformerHandler, AttributesImpl attributesImpl, File[] array, int index, Properties properties) throws SAXException {
        if (null != transformerHandler && null != attributesImpl && null != array && null != properties) {
            for (int i = 0; i < array.length; i++) {
                File file = array[i];
                String name = file.getName();
                if (!name.equalsIgnoreCase(Epub.Path_Cover_html) || !name.equalsIgnoreCase(Epub.Path_Toc_html)) {
                    Object value = properties.get(file.getAbsolutePath());
                    String id;
                    if (null != value) {
                        id = value.toString();
                    } else {
                        id = String.valueOf(index);
                    }
                    attributesImpl.addAttribute(Epub.String_Null, Epub.String_Null, Content.Name_Spine_Itemref_Idref, Epub.Type_Object_String, Content.Value_Spine_Itemref_Idref + tag_underscore + id);
                    transformerHandler.startElement(Epub.String_Null, Epub.String_Null, Content.Sign_Spine_Itemref, attributesImpl);
                    attributesImpl.clear();

                    transformerHandler.endElement(Epub.String_Null, Epub.String_Null, Content.Sign_Spine_Itemref);
                    index++;
                }
            }
        }
    }

    private void writeToc(File file) {
        try {
            Result result = new StreamResult(new FileOutputStream(file));
            SAXTransformerFactory saxTransformerFactory = (SAXTransformerFactory) SAXTransformerFactory.newInstance();
            TransformerHandler transformerHandler = saxTransformerFactory.newTransformerHandler();
            transformerHandler.setResult(result);
            Transformer transformer = transformerHandler.getTransformer();

            transformer.setOutputProperty(OutputKeys.VERSION, XML_Version_Default);
            transformer.setOutputProperty(OutputKeys.ENCODING, XML_Encoding_UTF8);
            transformer.setOutputProperty(OutputKeys.INDENT, XML_Indent);

            transformerHandler.startDocument();
            AttributesImpl attributesImpl = new AttributesImpl();

            attributesImpl.addAttribute(Epub.String_Null, Epub.String_Null, Toc.Name_Version, Epub.Type_Object_String, Toc.Value_Version);
            transformerHandler.startElement(Toc.Uri, Epub.String_Null, Toc.Sign, attributesImpl);
            attributesImpl.clear();

            transformerHandler.startElement(Epub.String_Null, Epub.String_Null, Toc.Sign_Head, attributesImpl);

            attributesImpl.addAttribute(Epub.String_Null, Epub.String_Null, Toc.Name_Head_Name, Epub.Type_Object_String, Toc.Value_Head_Name_UID);
            attributesImpl.addAttribute(Epub.String_Null, Epub.String_Null, Toc.Name_Head_Content, Epub.Type_Object_String, Toc.Value_Head_Content_UID);
            transformerHandler.startElement(Epub.String_Null, Epub.String_Null, Toc.Sign_Head_Meta, attributesImpl);
            attributesImpl.clear();
            transformerHandler.endElement(Epub.String_Null, Epub.String_Null, Toc.Sign_Head_Meta);

            attributesImpl.addAttribute(Epub.String_Null, Epub.String_Null, Toc.Name_Head_Name, Epub.Type_Object_String, Toc.Value_Head_Name_Depth);
            attributesImpl.addAttribute(Epub.String_Null, Epub.String_Null, Toc.Name_Head_Content, Epub.Type_Object_String, Toc.Value_Head_Content_Depth);
            transformerHandler.startElement(Epub.String_Null, Epub.String_Null, Toc.Sign_Head_Meta, attributesImpl);
            attributesImpl.clear();
            transformerHandler.endElement(Epub.String_Null, Epub.String_Null, Toc.Sign_Head_Meta);

            attributesImpl.addAttribute(Epub.String_Null, Epub.String_Null, Toc.Name_Head_Name, Epub.Type_Object_String, Toc.Value_Head_Name_Generator);
            attributesImpl.addAttribute(Epub.String_Null, Epub.String_Null, Toc.Name_Head_Content, Epub.Type_Object_String, Toc.Value_Head_Content_Generator);
            transformerHandler.startElement(Epub.String_Null, Epub.String_Null, Toc.Sign_Head_Meta, attributesImpl);
            attributesImpl.clear();
            transformerHandler.endElement(Epub.String_Null, Epub.String_Null, Toc.Sign_Head_Meta);

            attributesImpl.addAttribute(Epub.String_Null, Epub.String_Null, Toc.Name_Head_Name, Epub.Type_Object_String, Toc.Value_Head_Name_Count);
            attributesImpl.addAttribute(Epub.String_Null, Epub.String_Null, Toc.Name_Head_Content, Epub.Type_Object_String, Toc.Value_Head_Content_Count);
            transformerHandler.startElement(Epub.String_Null, Epub.String_Null, Toc.Sign_Head_Meta, attributesImpl);
            attributesImpl.clear();
            transformerHandler.endElement(Epub.String_Null, Epub.String_Null, Toc.Sign_Head_Meta);

            attributesImpl.addAttribute(Epub.String_Null, Epub.String_Null, Toc.Name_Head_Name, Epub.Type_Object_String, Toc.Value_Head_Name_Number);
            attributesImpl.addAttribute(Epub.String_Null, Epub.String_Null, Toc.Name_Head_Content, Epub.Type_Object_String, Toc.Value_Head_Content_Number);
            transformerHandler.startElement(Epub.String_Null, Epub.String_Null, Toc.Sign_Head_Meta, attributesImpl);
            attributesImpl.clear();

            transformerHandler.endElement(Epub.String_Null, Epub.String_Null, Toc.Sign_Head_Meta);

            transformerHandler.endElement(Epub.String_Null, Epub.String_Null, Toc.Sign_Head);

            transformerHandler.startElement(Epub.String_Null, Epub.String_Null, Toc.Sign_Title, attributesImpl);
            transformerHandler.startElement(Epub.String_Null, Epub.String_Null, Toc.Sign_Text, attributesImpl);
            transformerHandler.characters(epub.getContent().getTitle().toCharArray(), 0, epub.getContent().getTitle().toCharArray().length);
            transformerHandler.endElement(Epub.String_Null, Epub.String_Null, Toc.Sign_Text);
            transformerHandler.endElement(Epub.String_Null, Epub.String_Null, Toc.Sign_Title);

            transformerHandler.startElement(Epub.String_Null, Epub.String_Null, Toc.Sign_Author, attributesImpl);
            transformerHandler.startElement(Epub.String_Null, Epub.String_Null, Toc.Sign_Text, attributesImpl);
            transformerHandler.characters(epub.getContent().getCreator().toCharArray(), 0, epub.getContent().getCreator().toCharArray().length);
            transformerHandler.endElement(Epub.String_Null, Epub.String_Null, Toc.Sign_Text);
            transformerHandler.endElement(Epub.String_Null, Epub.String_Null, Toc.Sign_Author);

            transformerHandler.startElement(Epub.String_Null, Epub.String_Null, Toc.Sign_Nav_Map, attributesImpl);

            int index = 0;

            File file_Cover = getFile(resources, Epub.Path_Cover_html);
            String name;
            String path;
            if (null != file_Cover) {
                name = file_Cover.getName();
            } else {
                name = Epub.Path_Cover_html;
            }
            if (null != paths) {
                path = paths.getProperty(file_Cover.getAbsolutePath()) + tag_slash + name;
            } else {
                path = name;
            }
            String name_prefix = getPrefix(path);
            attributesImpl.addAttribute(Epub.String_Null, Epub.String_Null, Toc.Name_Nav_Point_ID, Epub.Type_Object_String, name_prefix);
            attributesImpl.addAttribute(Epub.String_Null, Epub.String_Null, Toc.Name_Nav_Point_Order, Epub.Type_Object_String, String.valueOf(index));
            transformerHandler.startElement(Epub.String_Null, Epub.String_Null, Toc.Sign_Nav_Point, attributesImpl);
            attributesImpl.clear();

            transformerHandler.startElement(Epub.String_Null, Epub.String_Null, Toc.Sign_Nav_Point_Label, attributesImpl);
            transformerHandler.startElement(Epub.String_Null, Epub.String_Null, Toc.Sign_Nav_Point_Text, attributesImpl);
            transformerHandler.characters(name_prefix.toCharArray(), 0, name_prefix.toCharArray().length);
            transformerHandler.endElement(Epub.String_Null, Epub.String_Null, Toc.Sign_Nav_Point_Text);
            transformerHandler.endElement(Epub.String_Null, Epub.String_Null, Toc.Sign_Nav_Point_Label);

            attributesImpl.addAttribute(Epub.String_Null, Epub.String_Null, Toc.Name_Nav_Point_Content_Src, Epub.Type_Object_String, path);
            transformerHandler.startElement(Epub.String_Null, Epub.String_Null, Toc.Sign_Nav_Point_Content, attributesImpl);
            attributesImpl.clear();

            transformerHandler.endElement(Epub.String_Null, Epub.String_Null, Toc.Sign_Nav_Point_Content);
            transformerHandler.endElement(Epub.String_Null, Epub.String_Null, Toc.Sign_Nav_Point);
            index++;

            File file_Toc_Html = getFile(resources, Epub.Path_Toc_html);
            if (null != file_Toc_Html) {
                name = file_Toc_Html.getName();
            } else {
                name = Epub.Path_Toc_html;
            }
            if (null != paths) {
                path = paths.getProperty(file_Toc_Html.getAbsolutePath()) + tag_slash + name;
            } else {
                path = name;
            }
            name_prefix = getPrefix(path);
            attributesImpl.addAttribute(Epub.String_Null, Epub.String_Null, Toc.Name_Nav_Point_ID, Epub.Type_Object_String, name_prefix);
            attributesImpl.addAttribute(Epub.String_Null, Epub.String_Null, Toc.Name_Nav_Point_Order, Epub.Type_Object_String, String.valueOf(index));
            transformerHandler.startElement(Epub.String_Null, Epub.String_Null, Toc.Sign_Nav_Point, attributesImpl);
            attributesImpl.clear();

            transformerHandler.startElement(Epub.String_Null, Epub.String_Null, Toc.Sign_Nav_Point_Label, attributesImpl);
            transformerHandler.startElement(Epub.String_Null, Epub.String_Null, Toc.Sign_Nav_Point_Text, attributesImpl);
            transformerHandler.characters(name_prefix.toCharArray(), 0, name_prefix.toCharArray().length);
            transformerHandler.endElement(Epub.String_Null, Epub.String_Null, Toc.Sign_Nav_Point_Text);
            transformerHandler.endElement(Epub.String_Null, Epub.String_Null, Toc.Sign_Nav_Point_Label);

            attributesImpl.addAttribute(Epub.String_Null, Epub.String_Null, Toc.Name_Nav_Point_Content_Src, Epub.Type_Object_String, path);
            transformerHandler.startElement(Epub.String_Null, Epub.String_Null, Toc.Sign_Nav_Point_Content, attributesImpl);
            attributesImpl.clear();

            transformerHandler.endElement(Epub.String_Null, Epub.String_Null, Toc.Sign_Nav_Point_Content);
            transformerHandler.endElement(Epub.String_Null, Epub.String_Null, Toc.Sign_Nav_Point);
            index++;

            addTocNavMapItem(transformerHandler, attributesImpl, resources_Pages, index);

            transformerHandler.endElement(Epub.String_Null, Epub.String_Null, Toc.Sign_Nav_Map);

            transformerHandler.endElement(Epub.String_Null, Epub.String_Null, Toc.Sign);

            transformerHandler.endDocument();
        } catch (FileNotFoundException | TransformerConfigurationException | SAXException e) {
            throw new RuntimeException(e);
        }
    }

    private void addTocNavMapItem(TransformerHandler transformerHandler, AttributesImpl attributesImpl, File[] array, int index) throws SAXException {
        if (null != transformerHandler && null != attributesImpl && null != array) {
            for (int i = 0; i < array.length; i++) {
                File file = array[i];
                String name = file.getName();
                if (!name.equalsIgnoreCase(Epub.Path_Cover_html) || !name.equalsIgnoreCase(Epub.Path_Toc_html)) {
                    String path;
                    if (null != paths) {
                        path = paths.getProperty(file.getAbsolutePath()) + tag_slash + name;
                    } else {
                        path = name;
                    }
                    String name_prefix = getPrefix(name);

                    attributesImpl.addAttribute(Epub.String_Null, Epub.String_Null, Toc.Name_Nav_Point_ID, Epub.Type_Object_String, name_prefix);
                    attributesImpl.addAttribute(Epub.String_Null, Epub.String_Null, Toc.Name_Nav_Point_Order, Epub.Type_Object_String, String.valueOf(index));
                    transformerHandler.startElement(Epub.String_Null, Epub.String_Null, Toc.Sign_Nav_Point, attributesImpl);
                    attributesImpl.clear();

                    transformerHandler.startElement(Epub.String_Null, Epub.String_Null, Toc.Sign_Nav_Point_Label, attributesImpl);
                    transformerHandler.startElement(Epub.String_Null, Epub.String_Null, Toc.Sign_Nav_Point_Text, attributesImpl);
                    transformerHandler.characters(name_prefix.toCharArray(), 0, name_prefix.toCharArray().length);
                    transformerHandler.endElement(Epub.String_Null, Epub.String_Null, Toc.Sign_Nav_Point_Text);
                    transformerHandler.endElement(Epub.String_Null, Epub.String_Null, Toc.Sign_Nav_Point_Label);

                    attributesImpl.addAttribute(Epub.String_Null, Epub.String_Null, Toc.Name_Nav_Point_Content_Src, Epub.Type_Object_String, path);
                    transformerHandler.startElement(Epub.String_Null, Epub.String_Null, Toc.Sign_Nav_Point_Content, attributesImpl);
                    attributesImpl.clear();

                    transformerHandler.endElement(Epub.String_Null, Epub.String_Null, Toc.Sign_Nav_Point_Content);
                    transformerHandler.endElement(Epub.String_Null, Epub.String_Null, Toc.Sign_Nav_Point);
                    index++;
                }
            }
        }
    }

    private void writeHtmlCover(File file) {
        try {
            Result result = new StreamResult(new FileOutputStream(file));
            SAXTransformerFactory saxTransformerFactory = (SAXTransformerFactory) SAXTransformerFactory.newInstance();
            TransformerHandler transformerHandler = saxTransformerFactory.newTransformerHandler();
            transformerHandler.setResult(result);
            Transformer transformer = transformerHandler.getTransformer();

            transformer.setOutputProperty(OutputKeys.VERSION, XML_Version_Default);
            transformer.setOutputProperty(OutputKeys.ENCODING, XML_Encoding_UTF8);
            transformer.setOutputProperty(OutputKeys.INDENT, XML_Indent);

            transformerHandler.startDocument();
            AttributesImpl attributesImpl = new AttributesImpl();

            transformerHandler.startElement(HtmlToc.Uri, Epub.String_Null, HtmlCover.Sign, attributesImpl);
            attributesImpl.clear();

            transformerHandler.startElement(Epub.String_Null, Epub.String_Null, HtmlCover.Sign_Head, attributesImpl);

            transformerHandler.startElement(Epub.String_Null, Epub.String_Null, HtmlCover.Sign_Head_Title, attributesImpl);
            transformerHandler.characters(HtmlCover.Value_Head_Title.toCharArray(), 0, HtmlCover.Value_Head_Title.toCharArray().length);
            transformerHandler.endElement(Epub.String_Null, Epub.String_Null, HtmlCover.Sign_Head_Title);

            transformerHandler.startElement(Epub.String_Null, Epub.String_Null, HtmlCover.Sign_Head_Style, attributesImpl);
            transformerHandler.characters(HtmlCover.Value_Head_Style.toCharArray(), 0, HtmlCover.Value_Head_Style.toCharArray().length);
            transformerHandler.endElement(Epub.String_Null, Epub.String_Null, HtmlCover.Sign_Head_Style);

            transformerHandler.endElement(Epub.String_Null, Epub.String_Null, HtmlCover.Sign_Head);

            transformerHandler.startElement(Epub.String_Null, Epub.String_Null, HtmlCover.Sign_Body, attributesImpl);

            transformerHandler.startElement(Epub.String_Null, Epub.String_Null, HtmlCover.Sign_Body_Table, attributesImpl);

            File file_Cover = getFile(resources_Images, Epub.Name_Cover);
            String name;
            String path;
            if (null != file_Cover) {
                name = file_Cover.getName();
            } else {
                name = Epub.Path_Cover_Png;
            }
            if (null != paths) {
                path = paths.getProperty(file_Cover.getAbsolutePath()) + tag_slash + name;
            } else {
                path = name;
            }
            attributesImpl.addAttribute(Epub.String_Null, Epub.String_Null, HtmlCover.Name_Body_Table_Image_Src, Epub.Type_Object_String, path);
            attributesImpl.addAttribute(Epub.String_Null, Epub.String_Null, HtmlCover.Name_Body_Table_Image_Alt, Epub.Type_Object_String, epub.getContent().getTitle());
            transformerHandler.startElement(Epub.String_Null, Epub.String_Null, HtmlCover.Sign_Body_Table_Image, attributesImpl);
            transformerHandler.endElement(Epub.String_Null, Epub.String_Null, HtmlCover.Sign_Body_Table_Image);
            attributesImpl.clear();

            transformerHandler.endElement(Epub.String_Null, Epub.String_Null, HtmlCover.Sign_Body_Table);

            transformerHandler.endElement(Epub.String_Null, Epub.String_Null, HtmlCover.Sign_Body);

            transformerHandler.endElement(Epub.String_Null, Epub.String_Null, HtmlCover.Sign);

            transformerHandler.endDocument();
        } catch (FileNotFoundException | TransformerConfigurationException | SAXException e) {
            throw new RuntimeException(e);
        }
    }

    private void writeHtmlToc(File file) {
        try {
            Result result = new StreamResult(new FileOutputStream(file));
            SAXTransformerFactory saxTransformerFactory = (SAXTransformerFactory) SAXTransformerFactory.newInstance();
            TransformerHandler transformerHandler = saxTransformerFactory.newTransformerHandler();
            transformerHandler.setResult(result);
            Transformer transformer = transformerHandler.getTransformer();

            transformer.setOutputProperty(OutputKeys.VERSION, XML_Version_Default);
            transformer.setOutputProperty(OutputKeys.ENCODING, XML_Encoding_UTF8);
            transformer.setOutputProperty(OutputKeys.INDENT, XML_Indent);

            transformerHandler.startDocument();
            AttributesImpl attributesImpl = new AttributesImpl();

            transformerHandler.startPrefixMapping(HtmlToc.Name_Epub, HtmlToc.Uri_Epub);
            transformerHandler.startElement(HtmlToc.Uri, Epub.String_Null, HtmlToc.Sign, attributesImpl);
            attributesImpl.clear();

            transformerHandler.startElement(Epub.String_Null, Epub.String_Null, HtmlToc.Sign_Head, attributesImpl);

            transformerHandler.startElement(Epub.String_Null, Epub.String_Null, HtmlToc.Sign_Head_Title, attributesImpl);
            transformerHandler.characters(HtmlToc.Value_Head_Title.toCharArray(), 0, HtmlToc.Value_Head_Title.toCharArray().length);
            transformerHandler.endElement(Epub.String_Null, Epub.String_Null, HtmlToc.Sign_Head_Title);

            transformerHandler.endElement(Epub.String_Null, Epub.String_Null, HtmlToc.Sign_Head);

            transformerHandler.startElement(Epub.String_Null, Epub.String_Null, HtmlToc.Sign_Body, attributesImpl);

            attributesImpl.addAttribute(Epub.String_Null, Epub.String_Null, HtmlToc.Name_Body_Nav_Data_Type, Epub.Type_Object_String, HtmlToc.Value_Body_Nav_Data_Type);
            attributesImpl.addAttribute(Epub.String_Null, Epub.String_Null, HtmlToc.Name_Body_Nav_ID, Epub.Type_Object_String, HtmlToc.Value_Body_Nav_ID);
            transformerHandler.startElement(Epub.String_Null, Epub.String_Null, HtmlToc.Sign_Body_Nav, attributesImpl);
            attributesImpl.clear();

            transformerHandler.startElement(Epub.String_Null, Epub.String_Null, HtmlToc.Sign_Body_Nav_H1, attributesImpl);
            transformerHandler.characters(HtmlToc.Value_Body_Nav_H1.toCharArray(), 0, HtmlToc.Value_Body_Nav_H1.toCharArray().length);
            transformerHandler.endElement(Epub.String_Null, Epub.String_Null, HtmlToc.Sign_Body_Nav_H1);

            transformerHandler.startElement(Epub.String_Null, Epub.String_Null, HtmlToc.Sign_Body_Nav_OL, attributesImpl);

            addHtmlTocBodyNavItem(transformerHandler, attributesImpl, resources_Pages);

            transformerHandler.endElement(Epub.String_Null, Epub.String_Null, HtmlToc.Sign_Body_Nav_OL);

            transformerHandler.endElement(Epub.String_Null, Epub.String_Null, HtmlToc.Sign_Body_Nav);

            transformerHandler.endElement(Epub.String_Null, Epub.String_Null, HtmlToc.Sign_Body);

            transformerHandler.endElement(Epub.String_Null, Epub.String_Null, HtmlToc.Sign);

            transformerHandler.endDocument();
        } catch (FileNotFoundException | TransformerConfigurationException | SAXException e) {
            throw new RuntimeException(e);
        }
    }

    private void addHtmlTocBodyNavItem(TransformerHandler transformerHandler, AttributesImpl attributesImpl, File[] array) throws SAXException {
        if (null != transformerHandler && null != attributesImpl && null != array) {
            for (int i = 0; i < array.length; i++) {
                File file = array[i];
                String name = file.getName();
                if (!name.equalsIgnoreCase(Epub.Path_Cover_html) || !name.equalsIgnoreCase(Epub.Path_Toc_html)) {
                    String name_Prefix = getPrefix(name);
                    String path;
                    if (null != paths) {
                        path = paths.getProperty(file.getAbsolutePath()) + tag_slash + name;
                    } else {
                        path = name;
                    }

                    transformerHandler.startElement(Epub.String_Null, Epub.String_Null, HtmlToc.Sign_Body_Nav_LI, attributesImpl);

                    attributesImpl.addAttribute(Epub.String_Null, Epub.String_Null, HtmlToc.Name_Body_Nav_A_Href, Epub.Type_Object_String, path);
                    transformerHandler.startElement(Epub.String_Null, Epub.String_Null, HtmlToc.Sign_Body_Nav_A, attributesImpl);
                    attributesImpl.clear();

                    transformerHandler.characters(name_Prefix.toCharArray(), 0, name_Prefix.toCharArray().length);

                    transformerHandler.endElement(Epub.String_Null, Epub.String_Null, HtmlToc.Sign_Body_Nav_A);

                    transformerHandler.endElement(Epub.String_Null, Epub.String_Null, HtmlToc.Sign_Body_Nav_LI);
                }
            }
        }
    }

    private String getPrefix(String string) {
        return newString(string, false);
    }

    private String getSuffix(String string) {
        return newString(string, true);
    }

    private String newString(String string, boolean isSuffix) {
        if (null != string) {
            String prefix;
            String suffix;
            int index = string.lastIndexOf(".");
            if (index != -1) {
                prefix = string.substring(0, index);
                suffix = string.substring(index);
            } else {
                prefix = string;
                suffix = "";
            }
            if (isSuffix) {
                return suffix;
            } else {
                return prefix;
            }
        }
        return null;
    }
}

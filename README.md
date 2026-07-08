# EpubTool
Pack epub files for Java.

## 关于（About）：
本工具用途为纯代码化，快速打包遵从Epub 2.0，3.0，或以上版本规范的相关媒体文件。

## 文件检测（EpubCheck）：
.epub文件规范官方检测工具下载：

官网：
https://www.w3.org/publishing/epubcheck/

Github：
https://github.com/w3c/epubcheck

## 使用方法（Usage）：
在代码编辑器中新建Java工程，将EpubTool.jar作为项目依赖包导入工程，然后在main(String[] args){}中执行EpubTool的read(File file)和write(File dirFile)方法，即可处理指定目录内的打包文件，在新文件夹内为其生成目录引导文件，最终将文件夹内文件整体打包为epub。

---

### epubTool.read()：
```java
public void read(File file){}
```
#### read方法读取epub基础信息（Key=Value）：

*.ini：
```txt
Path=文件夹目录（dirFile）
Language=zh_CN
Title=书名（My Book）
Author=作者（author）
Publisher=出版商（publisher）
Description=简介（My Novel）
ID=书籍验证（id）
```

*.ini可作为txt文件编辑，左侧Path，Language等保持首字母大写，=号右边为相对应的值。
比如将待处理的文件夹路径填入Path右边，使用read()方法即可进入epub打包预处理。

#### 例：
```java
String path = "1.ini";
File iniFile = new File(path);

EpubTool epubTool = new EpubTool();
epubTool.read(iniFile);
```

---

### epubTool.write()：
```java
public void write(File dirFile){}
```

write方法处理预读的信息，逐步生成*.epub需要的基础文件，并将所有目标文件复制进指定的文件夹。

#### 例：
```java
String dirPath = "novel";
File dirFile = new File(dirPath);

epubTool.write(dirFile);
```

### EpubTool.compressedEpub()：
```java
public void compressedEpub(File zipFile){}
```

经过EpubTool处理后的*.epub需要的所有基础文件，调用ZipTool的compressed方法压缩成zipFile文件（生成zip压缩包，后缀可以自己写），填"null"则默认生成epub文件于ini存储的对应路径文件夹内。

#### 例：
```java
File zipFile = null;
epubTool.compressedEpub(zipFile);
```

## 更新（Update）：
### v0.2.6：
* epub(✓)
    * container
    * content
    * htmlcover
    * htmltoc
    * mimetype 
    * toc
* filter(✓)
    * images
    * audios
    * videos
    * fonts
    * styles
    * pages
* zip(✓)

### 提示：
#### v0.2.0版本：
1.扫描*.ini内Path路径的文件，扫描深度为本文件夹内文件，即1.html、2.html等等都存进此目录。

2.已实现epub基础引导文件生成，未进行EpubCheck检测修复请手动自测。

3.文件分门别类入口EpubTool.write(File dirFile, boolean isFileFilter)，未实现HTML路径同步变更所以请慎用！

4.文件夹压缩未实现请手动打包为zip再改后缀名为epub。

#### v0.2.6版本：
已更新目录深度扫描，以及zip打包。

#### 0.3.0版本：
待更新HTML路径与文件分类同步。

## 捐赠（Donation）：
若本项目有帮助到您，请进行一些捐赠。

## 许可（License）：
---

License :
 [Apache License (Version 2.0)](http://www.apache.org/licenses/)

---
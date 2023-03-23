package com.sinam7.KookminNoticeBot;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.*;
import java.nio.charset.StandardCharsets;

@Slf4j
public abstract class Tools {

    public static String getLink(Element element) {
        String src = null;
//        log.info("getLink() element = {}", element);
        if (element.is("img")) { // If not an <img> tag element -> get href value
            src = element.attr("src");
//            log.info("img source = {}", src);
        } else if (element.is("a")) { // if <img> tag element -> get src value;
            src = element.attr("href");
            src = src.replaceAll("\\?currentPageNo=.", "");
//            log.info("file source = {}", src);
        } else {
            log.error("Not prepared element for get source by getLink(): {}", element);
        }
        return src;
    }

    public static String trimLastNewline(StringBuilder sb) {
        if (sb.length() >= 1 && sb.substring(sb.length() - 1).equals("\n"))
            return sb.substring(0, sb.length() - 1);
        if (sb.isEmpty()) return "";
        return sb.toString();
    }

    public static Document readHtmlToDocument(String url) {
        try {
            return Jsoup.parse(new File(url), "UTF-8", Constants.HOST);
        } catch (IOException e) {
            log.error("Failed to read file: {}", url);
            throw new RuntimeException(e);
        }
    }

    public static String validateEscapeChar(String title) {
        return title.replace("/", ".")
                .replace("\\", ".");

    }

    public static String getLastLine(File fileName) {
        BufferedReader input;
        try {
            input = new BufferedReader(new FileReader(fileName));
        } catch (FileNotFoundException e) {
            log.error("File Not Found - {}", e.getMessage());
            throw new RuntimeException(e);
        }
        String last = null, line;

        while (true) {
            try {
                if ((line = input.readLine()) == null) break;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            last = line;
        }

        return last;
    }

    public static void writeStringToFile(String src, String directoryPath, String fileNameWithExtension) {
        try {
            log.info("Downloading file {}", fileNameWithExtension);
            FileUtils.writeStringToFile(new File(directoryPath + fileNameWithExtension),
                    src, StandardCharsets.UTF_8);
            log.info("Document downloaded Successfully in {}", directoryPath + fileNameWithExtension);
        } catch (IOException e) {
            log.error("Document downloaded Failed: {} - {}", fileNameWithExtension, e.getMessage());
            throw new RuntimeException(e);
        }

    }
}

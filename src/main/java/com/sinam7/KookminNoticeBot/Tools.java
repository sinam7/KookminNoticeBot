package com.sinam7.KookminNoticeBot;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.*;

@Slf4j
public abstract class Tools {
    static final String HOST = "https://www.kookmin.ac.kr";
    static final String outputURI = "./src/output/";

    static String getLink(Element element) {
        String src = null;
//        log.info("getLink() element = {}", element);
        if (element.is("img")) { // If not an <img> tag element -> get href value
            src = element.attr("src");
//            log.info("img source = {}", src);
        } else if (element.is("a")){ // if <img> tag element -> get src value;
            src = element.select("a").attr("href");
            src = src.replaceAll("\\?currentPageNo=.", "");
//            log.info("file source = {}", src);
        } else {
            log.error("Not prepared element for get source by getLink(): {}", element);
        }
        return src;
    }

    static String trimLastNewline(StringBuilder sb) {
        if (sb.length() >= 1 && sb.substring(sb.length() - 1).equals("\n"))
            return sb.substring(0, sb.length() - 1);
        if (sb.isEmpty()) return "";
        return sb.toString();
    }

    static Document readHtmlToDocument(String url) {
        try {
            return Jsoup.parse(new File(url), "UTF-8", HOST);
        } catch (IOException e) {
            log.error("Failed to read file: {}", url);
            throw new RuntimeException(e);
        }
    }

    static String validateEscapeChar(String title) {
        return title.replace("/", ".")
                .replace("\\", ".");

    }

    static String getLastLine(File fileName) {
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
}
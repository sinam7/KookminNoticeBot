package com.sinam7.KookminNoticeBot.Webcrawler;

import com.sinam7.KookminNoticeBot.Tools;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

import static com.sinam7.KookminNoticeBot.Tools.*;
import static com.sinam7.KookminNoticeBot.Constants.*;
import static org.jsoup.Connection.Response;

@Slf4j
public class WebCrawler {
    final HashSet<String> alreadyDownloaded = getAlreadyDownloadedDocumentsURI();

    public List<Notice> getRecentNotices() {

        List<Element> noticeElements = getNoticeElementList();
        List<Notice> noticeList = new ArrayList<>();

        for (Element noticeElement : noticeElements) {
            Notice.NoticeBuilder noticeBuilder = new Notice.NoticeBuilder();

            String link = Tools.getLink(noticeElement);
            if (isFileAlreadyDownloaded(alreadyDownloaded, link)) continue;

            noticeBuilder.url(link);

            Document detailNoticeDocument = getDocument(HOST + link);
            Notice notice = NoticeParser.getNoticeDetailElements(detailNoticeDocument, noticeBuilder);

            noticeList.add(notice);
            downloadDocument(notice);
        }

        return noticeList;
    }

    private static boolean isFileAlreadyDownloaded(HashSet<String> alreadyDownloaded, String link) {
        return alreadyDownloaded.stream().anyMatch(s -> {
            boolean matches = Pattern.matches(s, link);
            // if link matches String in alreadyDownloaded, log it and pass for parsing
            if (matches) log.info("link already downloaded: abort crawling at uri {}", link);
            return matches;
        });
    }

    private static List<Element> getNoticeElementList() {
        Document document = getDocument(HOST + noticeListURI + 1);
        return getNoticeElements(document);
    }

    private static HashSet<String> getAlreadyDownloadedDocumentsURI() {
        final HashSet<String> alreadyDownloaded = new HashSet<>();
        final File folder = new File(outputURI);
        // get all files in the folder excluding sub-folders
        final File[] fileList = Objects.requireNonNull(folder.listFiles(File::isFile));
        for (File file : fileList) {
            alreadyDownloaded.add(getLastLine(file).replaceAll("view\\.do.*", "view.do"));
        }
        return alreadyDownloaded;
    }

    private static Document getDocument(String url) {
        final Response response;
        final Document doc;
        try {
            log.info("getDocument() called with: url = {}", url);
            response = Jsoup.connect(url).execute();
            doc = response.parse();
            log.info("Response successfully parsed");
            return doc;
        } catch (IOException e) {
            log.error("Something went wrong while connecting to web or parsing the response: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private static List<Element> getNoticeElements(Document document) {
        Elements noticeElements = document.body().select(cssQuery_ListToNoticeHref);
        for (Element element : noticeElements) {
            log.info("found a link in document: {}", Tools.getLink(element));
        }
        return noticeElements.stream().toList();
    }

    public static void downloadDocument(Notice notice) {
        try {
            log.info("Downloading file {}", Tools.validateEscapeChar(notice.getTitle()) + ".txt");
            FileUtils.writeStringToFile(new File(outputURI +
                    Tools.validateEscapeChar(notice.getTitle()) +".txt"), notice.toFileString(), StandardCharsets.UTF_8);    log.info("Document downloaded Successfully");
        } catch (IOException e) {
            log.error("Document downloaded Failed - {}", e.getMessage());
            throw new RuntimeException(e);
        }


    }
}

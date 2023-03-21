package com.sinam7.KookminNoticeBot;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

import static com.sinam7.KookminNoticeBot.Tools.*;
import static org.jsoup.Connection.Response;

@Slf4j
public class WebCrawler {

    static final HashSet<String> alreadyDownloaded = new HashSet<>();
    public static void main(String[] args) {

        WebCrawler webCrawler = new WebCrawler();

        Document document = webCrawler.getDocument(HOST + "/user/kmuNews/notice/index.do?notcLwprtCatgrCd=&currentPageNo=" + 1);
        List<Element> noticeElements = webCrawler.getNoticeElements(document);

        // get all files in the folder excluding sub-folders
        final File folder = new File(outputURI);
        final File[] fileList = Objects.requireNonNull(folder.listFiles(File::isFile));
        for (File file : fileList) {
            alreadyDownloaded.add(getLastLine(file));
        }

        for (Element noticeElement : noticeElements) {
            Notice.NoticeBuilder builder = new Notice.NoticeBuilder();

            String link = Tools.getLink(noticeElement);
            // TODO: 2023-03-21 이미 받은 공지사항은 무시하기
            builder.url(link);

            Document detailNotice = webCrawler.getDocument(HOST + link);
            Notice result = NoticeParser.getNoticeDetailElements(detailNotice, builder);

            downloadDocument(result);
        }

    }

    private Document getDocument(String url) {
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

    private List<Element> getNoticeElements(Document document) {
        Elements noticeElements = document.body().select("div.board_list > ul > li > a[href]");
        for (Element element : noticeElements) {
            log.info("element.select(\"a\").attr(\"href\") = {}", Tools.getLink(element));
        }
        return noticeElements.stream().toList();
    }

    public static void downloadDocument(Notice notice) {
        try {
            log.info("Downloading file {}", Tools.validateEscapeChar(notice.getTitle()) + ".txt");
            FileUtils.writeStringToFile(new File(Tools.outputURI +
                    Tools.validateEscapeChar(notice.getTitle()) +".txt"), notice.toFileString(), StandardCharsets.UTF_8);    log.info("Document downloaded Successfully");
        } catch (IOException e) {
            log.error("Document downloaded Failed - {}", e.getMessage());
            throw new RuntimeException(e);
        }


    }
}

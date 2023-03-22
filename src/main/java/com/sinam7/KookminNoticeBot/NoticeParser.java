package com.sinam7.KookminNoticeBot;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import static com.sinam7.KookminNoticeBot.Constants.*;

@Slf4j
public abstract class NoticeParser {

    public static Notice getNoticeDetailElements(Document document, Notice.NoticeBuilder builder) {
        StringBuilder sb = new StringBuilder();

        builder.title(getTitle(document))
                .content(getContent(document, sb))
                .metadata(getMetaData(document))
                .attachedFiles(getAttachedFile(document))
                .attachedFiles(getImageFile(document));

        return builder.build();
    }

    private static String getContent(Document document, StringBuilder sb) {
        Elements noticeElements = document.body().select(cssQuery_NoticeContent);
        for (Element element : noticeElements) {
            String text = element.text();
            if (text.isEmpty()) continue;
//            log.info("element.text() = {}", text);
            sb.append(text).append("\n");
        }
        return Tools.trimLastNewline(sb);
    }

    private static String getTitle(Document document) {
        Elements title = document.body().select(cssQuery_NoticeTitle);
        log.info("title = {}", title.text());
        return title.text();
    }

    private static List<String> getAttachedFile(Document document) {
        Elements attached = document.body().select(cssQuery_FileDownloadURL);
//        log.info("attached = {}", attached);
        ArrayList<String> result = new ArrayList<>();
        for (Element element : attached) {
            result.add("FILE:" + Tools.getLink(element));
        }
        return result;
    }

    private static List<String> getImageFile(Document document) {
        Elements imgs = document.body().select(cssQuery_ImageDownloadURL);
//        log.info("imgs = {}", imgs);
        ArrayList<String> result = new ArrayList<>();
        for (Element element : imgs) {
            result.add("IMAGE:" + Tools.getLink(element));
        }
        return result;
    }

    private static String getMetaData(Document document) {
        Elements etc = document.body().select(cssQuery_NoticeMetaData);
//        log.info("etc = {}", etc);
        StringBuilder sb = new StringBuilder();
        for (Element element : etc) {
//            log.info("etc.element.text() = {}", element.text());
            sb.append(element.text()).append("\n");
        }
        return Tools.trimLastNewline(sb); // remove last "\n"
    }

}

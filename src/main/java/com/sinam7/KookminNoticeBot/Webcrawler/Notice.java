package com.sinam7.KookminNoticeBot.Webcrawler;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@EqualsAndHashCode
@ToString
public class Notice {
    private String url;
    private String title;
    private String metadata;
    private String content;
    private List<String> attachedFiles;

    public static class NoticeBuilder {
        @SuppressWarnings({"MismatchedQueryAndUpdateOfCollection"})
        private List<String> attachedFiles;

        public NoticeBuilder attachedFiles(List<String> urlList) {
            if (attachedFiles == null) attachedFiles = new ArrayList<>();
            for (String url : urlList) {
                if (!url.isEmpty()) attachedFiles.add(url);
            }
            return this;
        }
    }

    public String toFileString() {
        return title +
                "\n=================\n" +
                metadata +
                "\n-----------------\n" +
                (content.isBlank() ? "NO CONTENT" : content) +
                "\n-----------------\n" +
                (getAttachedFiles() == null ? "NO FILES/IMAGES" :
                        getAttachedFiles().stream().map(Object::toString).collect(Collectors.joining("\n"))) +
                "\n=================\n" +
                url;

    }


}
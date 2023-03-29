package com.sinam7.KookminNoticeBot.Webcrawler;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

@Slf4j
public class FileDownloader {

    public void run(String fileURL, String pathname) {
        //Open a URL Stream
        Connection.Response resultImageResponse = null;
        try {
            resultImageResponse = Jsoup.connect(fileURL)
                    .ignoreContentType(true).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

//        resultImageResponse.
        // output here
        // TODO: 2023-03-29 저장형식 json으로 변경하고 다시
        File dir = new File(pathname);
        FileOutputStream out = null;
        try {
            log.info("dir.getAbsolutePath() = {}", dir.getAbsolutePath());
            out = (new FileOutputStream(dir.getAbsolutePath()));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        try {
            out.write(resultImageResponse.bodyAsBytes());  // resultImageResponse.body() is where the image's contents are.
            out.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

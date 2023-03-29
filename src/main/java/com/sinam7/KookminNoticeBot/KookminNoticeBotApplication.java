package com.sinam7.KookminNoticeBot;

import com.sinam7.KookminNoticeBot.Webcrawler.Notice;
import com.sinam7.KookminNoticeBot.Webcrawler.WebCrawler;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.List;

import static com.sinam7.KookminNoticeBot.Constants.outputURI;

@Slf4j
public class KookminNoticeBotApplication {

	public static void main(String[] args) {
		WebCrawler webCrawler = new WebCrawler();

		List<Notice> recentNotices = webCrawler.run();
		log.info("recentNotices = {}", recentNotices);
		WebCrawler.downloadFilesFromRecentNotices(recentNotices);
//		OCR ocrInstance = new OCR();
	}

	private static void downloadFilesFromAlreadyDownloadedDirs() {
// TODO: 2023-03-28 output 디렉토리에서 파일 찾아서 다운로드
		File dir = new File(outputURI);
		File[] subdir = dir.listFiles(File::isDirectory);
		if (subdir != null) {
			for (File noticeDir : subdir) {
				String name = noticeDir.getName();
				log.info("name = {}", name);
				File[] files = noticeDir.listFiles();
				for (
						File file : files) {
					/*if (file.getName().contains(name)) {
						List<String> attachedFiles = recentNotice.getAttachedFiles();
						String noticePath = outputURI + File.separator + Tools.validateEscapeChar(recentNotice.getTitle());
						for (String attachedFile : attachedFiles) {
							int beginIndex = 0;
							if (attachedFile.startsWith("IMAGE:")) beginIndex = 6;
							else if (attachedFile.startsWith("FILE:")) beginIndex = 5;

							String url = attachedFile.substring(beginIndex);
							FileDownloader downloader = new FileDownloader();
							downloader.run(url, noticePath);
						}
					}*/
				}
			}
		}
	}

}

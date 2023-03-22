package com.sinam7.KookminNoticeBot;

import java.util.List;

//@SpringBootApplication
public class KookminNoticeBotApplication {

//	public static void main(String[] args) {
//		SpringApplication.run(KookminNoticeBotApplication.class, args);
//	}

	public static void main(String[] args) {
		WebCrawler webCrawler = new WebCrawler();

		List<Notice> recentNotices = webCrawler.getRecentNotices();
	}

}

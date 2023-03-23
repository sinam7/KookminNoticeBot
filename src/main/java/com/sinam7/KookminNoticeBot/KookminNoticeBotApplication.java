package com.sinam7.KookminNoticeBot;

import com.sinam7.KookminNoticeBot.Webcrawler.Notice;
import com.sinam7.KookminNoticeBot.Webcrawler.WebCrawler;

import java.util.List;

public class KookminNoticeBotApplication {

	public static void main(String[] args) {
		WebCrawler webCrawler = new WebCrawler();

		List<Notice> recentNotices = webCrawler.getRecentNotices();
	}

}

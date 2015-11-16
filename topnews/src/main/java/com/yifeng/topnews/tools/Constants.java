package com.yifeng.topnews.tools;

import com.yifeng.topnews.bean.NewsClassify;

import java.util.ArrayList;

public class Constants {
	public static ArrayList<NewsClassify> getData() {
		ArrayList<NewsClassify> newsClassify = new ArrayList<NewsClassify>();
		NewsClassify classify = new NewsClassify();
		classify.setId(0);
		classify.setTitle("�Ƽ�");
		newsClassify.add(classify);
		classify = new NewsClassify();
		classify.setId(1);
		classify.setTitle("�ȵ�");
		newsClassify.add(classify);
		classify = new NewsClassify();
		classify.setId(2);
		classify.setTitle("����");
		newsClassify.add(classify);
		classify = new NewsClassify();
		classify.setId(3);
		classify.setTitle("����");
		newsClassify.add(classify);
		classify = new NewsClassify();
		classify.setId(4);
		classify.setTitle("���");
		newsClassify.add(classify);
		classify = new NewsClassify();
		classify.setId(5);
		classify.setTitle("����");
		newsClassify.add(classify);
		classify = new NewsClassify();
		classify.setId(6);
		classify.setTitle("�Ƽ�");
		newsClassify.add(classify);
		classify = new NewsClassify();
		classify.setId(7);
		classify.setTitle("����");
		newsClassify.add(classify);
		return newsClassify;
	}
}

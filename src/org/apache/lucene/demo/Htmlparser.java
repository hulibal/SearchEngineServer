package org.apache.lucene.demo;

import java.io.*;
import java.util.ArrayList;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.ParserHtml.*;

public class Htmlparser {
	private String file_path = null;
	xmlReader xmlreader = new xmlReader();

	public Htmlparser() {
		file_path = xmlreader.getParserPath("htmlpage");
	}

	public Htmlparser(String path) {
		file_path = path;
	}

	public void start() {
		TextExtract te = new TextExtract();
		WebInfoDao wDao = new WebInfoDao();
		Vector<WebInfoBean> v_data = new Vector<WebInfoBean>();
		String content = null;
		String result = null;
		String htmlUrl = null;
		String textname = null;
		String aa = null;
		String time = null;
		String title = null;

		ArrayList<WebInfoBean> webInfos = new ArrayList<WebInfoBean>();
		try {

			te.initialdata(xmlreader.getParserPath("parser"));

			te.addwebpage(file_path);// 该路径为爬虫保存html的路径"E:\\spider\\2013-10-28
			v_data = wDao.getwebpage();
			for (int i = 0; i < v_data.size(); i++) {
				WebInfoBean webInfo = v_data.get(i);
				// System.out.println(webInfo.getWb_address());
				content = te.getHTML(webInfo.getWb_address());
				htmlUrl = content.split("\n")[0];
				aa = content.split("\n")[1];
				Pattern p1 = Pattern
						.compile("<title>[\\r]?[\\n]?(.*?)[\\r]?[\\n]?</title>");
				Matcher m1 = p1.matcher(content);
				if (m1.find()) {
					result = m1.group(1);
				} else {
					result = "题目提取失败！" + webInfo.getWb_name();
				}
				title = te.StringFilter(result);
				Pattern p2 = Pattern
						.compile("20((0?[1-9])|(1[0-4]))(年|-)((0?[1-9])|(1[0-2]))"
								+ "(月|-)((0?[1-9])|([1-2][0-9])|30|31)日?[\\s]?((0?[0-9])|(1[0-9])|(2[0-3]))(:|时)([0-5][0-9])(:|分)([0-5][0-9])?秒?");
				Matcher m2 = p2.matcher(content.split("<begin!>")[1]);
				if (m2.find()) {
					time = m2.group().replaceAll("年|月", "-")
							.replaceAll("时|分", ":").replaceAll("日|秒", "");
				} else {
					time = "爬取时间: " + aa;
				}
				textname = title + te.StringFilter(webInfo.getWb_name());
				if (te.parse(content).length() != 0) {
					webInfo.setWb_url("'" + htmlUrl + "'");
					webInfo.setWb_title("'" + title + "'");
					webInfo.setWb_time("'" + time + "'");
					// webInfo.setWb_content(te.parse(content));//内容字符串太大，无法直接存入数据库中
					webInfos.add(webInfo);
					System.out.println(htmlUrl);
					System.out.println(title);
					System.out.println(time);
					System.out.println("********************************");
					wDao.updateWebInfos(webInfos);
					String base = xmlreader.getParserPath("parser") + textname
							+ ".txt";
					String txt_content = htmlUrl + "\n" + time + "\n"
							+ te.parse(content).replaceAll("\\n", "");					
					OutputStreamWriter write_file = new OutputStreamWriter(
							new FileOutputStream(base, true), "utf-8");
					BufferedWriter write_txt = new BufferedWriter(write_file);
					write_txt.write(txt_content);
					write_txt.close();

					// PrintStream ps = null;
					// ps = new PrintStream(new FileOutputStream(base, true));
					// ps.println(htmlUrl + "\n" + time + "\n"+
					// te.parse(content).replaceAll("\\n",""));
					// ps.close();
				} else {
					System.out.println(textname + "是空网页！");
					System.out.println("********************************");
					continue;
				}
			}

		} catch (Exception e1) {
			e1.printStackTrace();
		}

	}
}

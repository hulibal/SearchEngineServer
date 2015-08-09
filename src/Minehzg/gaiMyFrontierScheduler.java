package Minehzg;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
//import java.util.logging.Level;
//import java.util.logging.Logger;

import org.archive.crawler.datamodel.CandidateURI;
//import org.archive.crawler.datamodel.CrawlURI;
//import org.archive.crawler.datamodel.FetchStatusCodes;
//import org.archive.crawler.framework.Processor;
import org.archive.crawler.postprocessor.FrontierScheduler;

public class gaiMyFrontierScheduler extends FrontierScheduler {
	private static final long serialVersionUID = 1l;
	private String MovieUrl_path = "";
	private String MusicUrl_path = "";
	private String BookUrl_path = "";

	public gaiMyFrontierScheduler(String name) {
		super(name);
	}

	protected void schedule(CandidateURI caUri) {
		String uri = caUri.toString();
		int switchseed = 0;
		if (uri.contains("douban.com")) {
			// 控制台输出网址
			System.out.println(uri);

			switchseed = mathUrl(uri);
			String id = "";
			switch (switchseed) {
			case 1: { // 电影的策略
				Pattern ha = Pattern.compile("/[0-9]+/");
				Matcher na = ha.matcher(uri);
				if (na.find()) {
					id = na.group(); // 提取种子号
				}
				String phtmlname = "http://movie.douban.com/feed/subject" + id
						+ "reviews";// 重组网址
				String rphtmlname = "http://movie.douban.com/subject" + id
						+ "reviews";
				try {
					String htmlsource = getHtmlSource(uri);
					String movietitle = getTitle(htmlsource);
					String hefamovietitle = movietitle.replaceAll(
							"[/+\\?+:+\\*+]", "&");// 替换掉非法文件名字符
					System.out.println(movietitle);
					MovieUrl_path = "F:\\douban\\movie\\" + hefamovietitle
							+ ".txt";

					File filename = new File(MovieUrl_path);
					if (!filename.exists()) {
						filename.createNewFile();
						System.out.println("已创建" + "  " + movietitle);
						String sum = getSummary(htmlsource);
						System.out.println(sum);
						String temsource = getHtmlSource(phtmlname);
						String commen = getComments(temsource);

						// System.out.println(commen);
						WriteFile(movietitle, "\r\n电影页面：" + uri + "\r\n\r\n",
								MovieUrl_path);
						WriteFile("简介:\r\n" + sum, "", MovieUrl_path);
						WriteFile("\r\n\r\n关于" + movietitle + "评论:\r\n\r\n"
								+ commen,
								"\r\n\r\n评论网址：" + rphtmlname + "\r\n",
								MovieUrl_path);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			}
			case 2: { // 音乐的策略
				Pattern haa = Pattern.compile("/[0-9]+/");
				Matcher naa = haa.matcher(uri);
				if (naa.find()) {
					id = naa.group(); // 提取种子号
				}
				String mushtmlname = "http://music.douban.com/feed/subject"
						+ id + "reviews";// 重组网址
				String musrhtmlname = "http://music.douban.com/subject" + id
						+ "reviews";
				try {
					String htmlsource = getHtmlSource(uri);
					String musictitle = getTitle(htmlsource);
					String hefamusictitle = musictitle.replaceAll(
							"[/+\\?+:+\\*+]", "&");// 替换掉非法文件名字符
					System.out.println(musictitle);
					MusicUrl_path = "F:\\douban\\music\\" + hefamusictitle
							+ ".txt";

					File filename = new File(MusicUrl_path);
					if (!filename.exists()) {
						filename.createNewFile();
						System.out.println("已创建" + "  " + musictitle);
						String sum = getSummary(htmlsource);
						System.out.println(sum);
						String temsource = getHtmlSource(mushtmlname);
						String commen = getComments(temsource);

						// System.out.println(commen);
						WriteFile(musictitle, "\r\n电影页面：" + uri + "\r\n\r\n",
								MusicUrl_path);
						WriteFile("简介:\r\n" + sum, "", MusicUrl_path);
						WriteFile("\r\n\r\n关于" + musictitle + "评论：\r\n\r\n"
								+ commen, "\r\n\r\n评论网址：" + musrhtmlname
								+ "\r\n", MusicUrl_path);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			}
			case 3: { // 书的策略
				Pattern haaa = Pattern.compile("/[0-9]+/");
				Matcher naaa = haaa.matcher(uri);
				if (naaa.find()) {
					id = naaa.group(); // 提取种子号
				}
				String bhtmlname = "http://book.douban.com/feed/subject" + id
						+ "reviews";// 重组网址
				String brhtmlname = "http://book.douban.com/subject" + id
						+ "reviews";
				try {
					String htmlsource = getHtmlSource(uri);
					String booktitle = getTitle(htmlsource);
					String hefabooktitle = booktitle.replaceAll(
							"[/+\\?+:+\\*+]", "&");// 替换掉非法文件名字符
					System.out.println(booktitle);
					BookUrl_path = "F:\\douban\\book\\" + hefabooktitle
							+ ".txt";

					File filename = new File(BookUrl_path);
					if (!filename.exists()) {
						filename.createNewFile();
						System.out.println("已创建" + "  " + booktitle);
						String sum = getSummary(htmlsource);
						System.out.println(sum);
						String temsource = getHtmlSource(bhtmlname);
						String commen = getComments(temsource);
						// System.out.println(commen);
						WriteFile(booktitle, "\r\n电影页面：" + uri + "\r\n\r\n",
								BookUrl_path);
						WriteFile("简介:\r\n" + sum, "", BookUrl_path);
						WriteFile("\r\n\r\n关于" + booktitle + "评论:\r\n\r\n"
								+ commen,
								"\r\n\r\n评论网址：" + brhtmlname + "\r\n",
								BookUrl_path);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			}
			default:
				break;
			}
			// 将网址写入文本F盘的Result.txt文件
			try {
				BufferedWriter writer = new BufferedWriter(new FileWriter(
						new File("F:\\douban\\Result.txt"), true));
				writer.write(uri + "\r\n");
				writer.close();
			} catch (Exception e) {
			}
			getController().getFrontier().schedule(caUri);
			try { // 延时
				Thread.currentThread().sleep(300);// 300毫秒
			} catch (Exception e) {
			}
		}
	}

	public static String getHtmlSource(String HtmlUrl) {
		URL tempUrl;
		StringBuffer tempStore = new StringBuffer();
		try {
			tempUrl = new URL(HtmlUrl);
			HttpURLConnection connection = (HttpURLConnection) tempUrl
					.openConnection();
			connection.setRequestProperty("User-Agent",
					"Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");// 设置代理
			BufferedReader in = new BufferedReader(new InputStreamReader(
					connection.getInputStream(), "UTF-8"));// 读取网页全部内容
			String temp;
			while ((temp = in.readLine()) != null) {
				tempStore.append(temp);
			}
			in.close();
		} catch (MalformedURLException e) {
			System.out.println("URL格式有问题!");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return tempStore.toString();
	}

	public static String getTitle(String htmlSource) {
		List<String> list = new ArrayList<String>();
		String title = "";
		Pattern pa = Pattern.compile("<title>.*?</title>");// 源码中标题正则表达式
		Matcher ma = pa.matcher(htmlSource);
		while (ma.find())// 寻找符合的字串
		{
			list.add(ma.group());// 将符合的字串加入到list中
		}
		for (int i = 0; i < list.size(); i++) {
			if (i != 0) {
				title = title + "\r\n\r\n";
			}
			title = title + list.get(i);
		}
		title = title.replaceAll("<.*?>", "");// 去掉html源码中的标签
		return title;
	}

	public static String getSummary(String htmlSource) {
		String summary = "";
		Pattern paa = Pattern
				.compile("<span property=\"v:summary\">.*?</span>");// 源码中简介正则表达式
		Matcher maa = paa.matcher(htmlSource);
		Pattern spa = Pattern.compile("<div class=\"intro\">.*?</div>");// 书源码中简介正则表达式
		Matcher sma = spa.matcher(htmlSource);
		if (maa.find())
			summary = maa.group();
		else if (sma.find())
			summary = sma.group();
		else
			summary = "暂无简介";
		summary = summary.replaceAll("<br/>", "\r\n");
		summary = summary.replaceAll("</p>", "\r\n");
		summary = summary.replaceAll("<.*?>", "");
		return summary;
	}

	public static String getComments(String htmlsource) {
		String comments = "";
		List<String> title = new ArrayList<String>();
		List<String> links = new ArrayList<String>();
		List<String> contents = new ArrayList<String>();
		Pattern ha = Pattern.compile("<link>.*?</link>");
		Matcher na = ha.matcher(htmlsource);
		String templink = "";
		while (na.find()) {
			templink = na.group();
			templink = templink.replaceAll("<.*?>", "");// 去除标签
			links.add(templink);
		}
		for (int i = 1; i < links.size(); i++) {
			String tempsource = getHtmlSource(links.get(i));
			/*
			 * try{ //延时 Thread.currentThread().sleep(2000);//2000毫秒 }
			 * catch(Exception e){}
			 */
			Pattern pa = Pattern.compile("<title>.*?</title>");// 先找标题
			Matcher ma = pa.matcher(tempsource);
			while (ma.find()) {
				title.add(ma.group());
			}
			Pattern pa1 = Pattern
					.compile("<div property=\"v:description\">.*?</div>");// 源码中评论正则表达式
			Matcher ma1 = pa1.matcher(tempsource);
			Pattern pa2 = Pattern
					.compile("<span property=\"v:description\">.*?</span>");// 源码中评论正则表达式
			Matcher ma2 = pa2.matcher(tempsource);
			Pattern pa3 = Pattern
					.compile("<span property=\"v:description\" class=\"\">.*?</span>");// 源码中评论正则表达式
			Matcher ma3 = pa3.matcher(tempsource);
			while (ma1.find()) {
				String tempcontent = ma1.group();
				contents.add(tempcontent);
			}
			while (ma2.find()) {
				String tempcontent = ma2.group();
				contents.add(tempcontent);
			}
			while (ma3.find()) {
				String tempcontent = ma3.group();
				contents.add(tempcontent);
			}
		}
		for (int i = 0; i < contents.size(); i++) {
			if (i != 0) {
				comments = comments + "\r\n\r\n";
			}
			int k = i + 1;
			comments = comments + "第" + k + "篇:\r\n" + title.get(i);
			comments = comments + "\r\n\r\n" + contents.get(i);
		}
		comments = comments.replaceAll("<br/>", "\r\n");
		comments = comments.replaceAll("<.*?>", "");
		return comments;
	}

	public void WriteFile(String Name, String UrlAddress, String path)
			throws IOException {
		BufferedWriter writer = null;
		writer = new BufferedWriter(new FileWriter(path, true));
		writer.write(Name);
		writer.write("  ");
		writer.write(UrlAddress);
		writer.close();
	}

	public int mathUrl(String htmlname) {
		int controlseed = 0;
		Pattern movie = Pattern
				.compile("http://movie.douban.com/subject/[0-9]+/");
		Pattern music = Pattern
				.compile("http://music.douban.com/subject/[0-9]+/");
		Pattern book = Pattern
				.compile("http://book.douban.com/subject/[0-9]+/");
		Matcher mov = movie.matcher(htmlname);
		Matcher mus = music.matcher(htmlname);
		Matcher boo = book.matcher(htmlname);
		if (mov.find()) {
			Pattern movpaa = Pattern
					.compile("http://movie.douban.com/subject/[0-9]+/[A-Za-z0-9]+");
			Matcher movmaa = movpaa.matcher(htmlname);
			if (!movmaa.find()) {
				controlseed = 1;// 电影网站则返回1
			}
		}
		if (mus.find()) {
			Pattern muspaa = Pattern
					.compile("http://music.douban.com/subject/[0-9]+/[A-Za-z0-9]+");
			Matcher musmaa = muspaa.matcher(htmlname);
			if (!musmaa.find()) {
				controlseed = 2;// 音乐网站则返回2
			}
		}
		if (boo.find()) {
			Pattern boopaa = Pattern
					.compile("http://book.douban.com/subject/[0-9]+/[A-Za-z0-9]+");
			Matcher boomaa = boopaa.matcher(htmlname);
			if (!boomaa.find()) {
				controlseed = 3;// 书网站则返回3
			}
		}
		return controlseed;
	}
}
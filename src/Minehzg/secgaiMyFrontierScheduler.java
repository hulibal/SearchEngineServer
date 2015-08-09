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

import org.archive.crawler.datamodel.CandidateURI;

import org.archive.crawler.postprocessor.FrontierScheduler;

public class secgaiMyFrontierScheduler extends FrontierScheduler {
	private static final long serialVersionUID = 1l;

	public secgaiMyFrontierScheduler(String name) {
		super(name);
	}

	protected void schedule(CandidateURI caUri) {
		String uri = caUri.toString();
		if (uri.contains("douban.com")) {
			System.out.println("�ύ����" + uri);
			xiazai(uri);
			getController().getFrontier().schedule(caUri);
		}
	}

	public void xiazai(String uri) {
		int switchseed = 0;

		String MovieUrl_path = "";
		String MusicUrl_path = "";
		String BookUrl_path = "";

		switchseed = mathUrl(uri);
		String id = "";
		switch (switchseed) {
		case 1: { // ��Ӱ�Ĳ���
			Pattern ha = Pattern.compile("/[0-9]+/");
			Matcher na = ha.matcher(uri);
			if (na.find()) {
				id = na.group(); // ��ȡ���Ӻ�
			}
			String phtmlname = "http://movie.douban.com/feed/subject" + id
					+ "reviews";// ������ַ
			String rphtmlname = "http://movie.douban.com/subject" + id
					+ "reviews";
			try {
				String htmlsource = getHtmlSource(uri);
				try { // ��ʱ
					Thread.currentThread().sleep(1000);// 300����
				} catch (Exception e) {
				}
				String movietitle = getTitle(htmlsource);
				String hefamovietitle = movietitle.replaceAll("[/+\\?+:+\\*+]",
						"&");// �滻���Ƿ��ļ����ַ�
				System.out.println(movietitle);
				MovieUrl_path = "E:\\douban\\movie\\" + hefamovietitle + ".txt";

				File filename = new File(MovieUrl_path);
				if (!filename.exists()) {
					filename.createNewFile();
					System.out.println("�Ѵ���" + "  " + movietitle);
					String sum = getSummary(htmlsource);
					// System.out.println(sum);
					String temsource = getHtmlSource(phtmlname);
					String commen = getComments(temsource);
					// System.out.println(commen);
					WriteFile(movietitle, "\r\n��Ӱҳ�棺" + uri + "\r\n\r\n",
							MovieUrl_path);
					WriteFile("���:\r\n" + sum, "", MovieUrl_path);
					WriteFile("\r\n\r\n" + commen, "\r\n\r\n������ַ��" + rphtmlname
							+ "\r\n", MovieUrl_path);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		}
		case 2: { // ���ֵĲ���
			Pattern haa = Pattern.compile("/[0-9]+/");
			Matcher naa = haa.matcher(uri);
			if (naa.find()) {
				id = naa.group(); // ��ȡ���Ӻ�
			}
			String mushtmlname = "http://music.douban.com/feed/subject" + id
					+ "reviews";// ������ַ
			String musrhtmlname = "http://music.douban.com/subject" + id
					+ "reviews";
			try {
				String htmlsource = getHtmlSource(uri);
				try { // ��ʱ
					Thread.currentThread().sleep(1000);// 300����
				} catch (Exception e) {
				}
				String musictitle = getTitle(htmlsource);
				String hefamusictitle = musictitle.replaceAll("[/+\\?+:+\\*+]",
						"&");// �滻���Ƿ��ļ����ַ�
				System.out.println(musictitle);
				MusicUrl_path = "E:\\douban\\music\\" + hefamusictitle + ".txt";

				File filename = new File(MusicUrl_path);
				if (!filename.exists()) {
					filename.createNewFile();
					System.out.println("�Ѵ���" + "  " + musictitle);
					String sum = getSummary(htmlsource);
					// System.out.println(sum);
					String temsource = getHtmlSource(mushtmlname);
					String commen = getComments(temsource);
					// System.out.println(commen);
					WriteFile(musictitle, "\r\n����ҳ�棺" + uri + "\r\n\r\n",
							MusicUrl_path);
					WriteFile("���:\r\n" + sum, "", MusicUrl_path);
					WriteFile("\r\n\r\n" + commen, "\r\n\r\n������ַ��"
							+ musrhtmlname + "\r\n", MusicUrl_path);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		}
		case 3: { // ��Ĳ���
			Pattern haaa = Pattern.compile("/[0-9]+/");
			Matcher naaa = haaa.matcher(uri);
			if (naaa.find()) {
				id = naaa.group(); // ��ȡ���Ӻ�
			}
			String bhtmlname = "http://book.douban.com/feed/subject" + id
					+ "reviews";// ������ַ
			String brhtmlname = "http://book.douban.com/subject" + id
					+ "reviews";
			try {
				String htmlsource = getHtmlSource(uri);
				try { // ��ʱ
					Thread.currentThread().sleep(1000);// 300����
				} catch (Exception e) {
				}
				String booktitle = getTitle(htmlsource);
				String hefabooktitle = booktitle.replaceAll("[/+\\?+:+\\*+]",
						"&");// �滻���Ƿ��ļ����ַ�
				System.out.println(booktitle);
				BookUrl_path = "E:\\douban\\book\\" + hefabooktitle + ".txt";

				File filename = new File(BookUrl_path);
				if (!filename.exists()) {
					filename.createNewFile();
					System.out.println("�Ѵ���" + "  " + booktitle);
					String sum = getSummary(htmlsource);
					// System.out.println(sum);
					String temsource = getHtmlSource(bhtmlname);
					String commen = getComments(temsource);
					// System.out.println(commen);
					WriteFile(booktitle, "\r\n��ҳ�棺" + uri + "\r\n\r\n",
							BookUrl_path);
					WriteFile("���:\r\n" + sum, "", BookUrl_path);
					WriteFile("\r\n\r\n" + commen, "\r\n\r\n������ַ��" + brhtmlname
							+ "\r\n", BookUrl_path);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		}
		default:
			break;
		}
	}

	public String getHtmlSource(String HtmlUrl) {
		URL tempUrl;
		StringBuffer tempStore = new StringBuffer();
		try {
			tempUrl = new URL(HtmlUrl);
			HttpURLConnection connection = (HttpURLConnection) tempUrl
					.openConnection();
			connection.setRequestProperty("User-Agent",
					"Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");// ���ô���
			BufferedReader in = new BufferedReader(new InputStreamReader(
					connection.getInputStream(), "UTF-8"));// ��ȡ��ҳȫ������
			String temp;
			while ((temp = in.readLine()) != null) {
				tempStore.append(temp);
			}
			in.close();
		} catch (MalformedURLException e) {
			System.out.println("URL��ʽ������!");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return tempStore.toString();
	}

	public String getTitle(String htmlSource) {
		List<String> list = new ArrayList<String>();
		String title = "";
		Pattern pa = Pattern.compile("<title>.*?</title>");// Դ���б���������ʽ
		Matcher ma = pa.matcher(htmlSource);
		while (ma.find())// Ѱ�ҷ��ϵ��ִ�
		{
			list.add(ma.group());// �����ϵ��ִ����뵽list��
		}
		for (int i = 0; i < list.size(); i++) {
			if (i != 0) {
				title = title + "\r\n\r\n";
			}
			title = title + list.get(i);
		}
		title = title.replaceAll("<.*?>", "");// ȥ��htmlԴ���еı�ǩ
		return title;
	}

	public String getSummary(String htmlSource) {
		String summary = "";
		Pattern paa = Pattern
				.compile("<span property=\"v:summary\">.*?</span>");// Դ���м��������ʽ
		Matcher maa = paa.matcher(htmlSource);
		Pattern saa = Pattern
				.compile("<span property=\"v:summary\" class=\"\">.*?</span>");// Դ���м��������ʽ
		Matcher gaa = saa.matcher(htmlSource);
		Pattern spa = Pattern.compile("<div class=\"intro\">.*?</div>");// ��Դ���м��������ʽ
		Matcher sma = spa.matcher(htmlSource);
		if (maa.find())
			summary = maa.group();
		else if (sma.find())
			summary = sma.group();
		else if (gaa.find())
			summary = gaa.group();
		else
			summary = "���޼��";
		summary = summary.replaceAll("<br/>", "\r\n");
		summary = summary.replaceAll("</p>", "\r\n");
		summary = summary.replaceAll("<.*?>", "");
		return summary;
	}

	public String getComments(String htmlsource) {
		String comments = "";
		List<String> title = new ArrayList<String>();
		List<String> links = new ArrayList<String>();
		List<String> contents = new ArrayList<String>();
		Pattern ta = Pattern.compile("<title>.*?</title>");
		Matcher na = ta.matcher(htmlsource);
		while (na.find()) {
			title.add(na.group());
		}
		Pattern ha = Pattern.compile("<link>.*?</link>");
		Matcher naa = ha.matcher(htmlsource);
		while (naa.find()) {
			links.add(naa.group());
		}
		Pattern ca = Pattern.compile("<description>.*?</description>");
		Matcher naaa = ca.matcher(htmlsource);
		while (naaa.find()) {
			contents.add(naaa.group());
		}
		for (int i = 0; i < contents.size(); i++) {
			if (i == 0) {
				comments = "\r\n" + comments + title.get(i);
			} else {
				comments = comments + "\r\n\r\n\r\n";
				comments = comments + title.get(i) + "\r\n";
				comments = comments + links.get(i);
				comments = comments + "\r\n" + contents.get(i);
			}
		}
		comments = comments.replace("<![CDATA[", "");
		comments = comments.replace("]]>", "");
		comments = comments.replaceAll("<.*?>", "");
		comments = comments
				.replaceAll("����:.*?http://[a-z]+.douban.com/subject/[0-9]+/.?",
						"(������)\r\n");
		StringBuilder temtui = new StringBuilder(comments);
		Pattern tui = Pattern.compile("����:.?[\u4e00-\u9fa5]{2}");
		Matcher hao = tui.matcher(temtui);
		int zuobiao = -8;
		while (hao.find()) {
			zuobiao = temtui.indexOf("����", zuobiao + 8);// �ҵ���һ�����۵�λ��
			temtui.insert(zuobiao + 6, "\r\n");
		}
		comments = temtui.toString();
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
				controlseed = 1;// ��Ӱ��վ�򷵻�1
			}
		}
		if (mus.find()) {
			Pattern muspaa = Pattern
					.compile("http://music.douban.com/subject/[0-9]+/[A-Za-z0-9]+");
			Matcher musmaa = muspaa.matcher(htmlname);
			if (!musmaa.find()) {
				controlseed = 2;// ������վ�򷵻�2
			}
		}
		if (boo.find()) {
			Pattern boopaa = Pattern
					.compile("http://book.douban.com/subject/[0-9]+/[A-Za-z0-9]+");
			Matcher boomaa = boopaa.matcher(htmlname);
			if (!boomaa.find()) {
				controlseed = 3;// ����վ�򷵻�3
			}
		}
		return controlseed;
	}
}

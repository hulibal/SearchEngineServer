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

import org.archive.crawler.datamodel.CandidateURI;

import org.archive.crawler.postprocessor.FrontierScheduler;

public class spider extends FrontierScheduler {
	private static final long serialVersionUID = 1l;

	public spider(String name) {
		super(name);
	}

	protected void schedule(CandidateURI caUri) {
		String uri = caUri.toString();
		System.out.println(uri);
		
		String temurl = uri.substring(7);		
		if (temurl.indexOf("html") >= 0 || temurl.indexOf("shtml") >= 0) {
			temurl = temurl.replaceAll("[\\?/:*|<>\"]", "_");
			if (temurl.length() > 30)
				temurl = temurl.substring(0, 30) + ".html";
			String Htmlsource = getHtmlSource(uri);
			String path = "E:\\spider\\" + temurl;
			try {
				File newfile = new File(path);
				if (!newfile.exists()) {
					newfile.createNewFile();
				}
				BufferedWriter writer = null;
				writer = new BufferedWriter(new FileWriter(path, true));
				writer.write(Htmlsource);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}		
		getController().getFrontier().schedule(caUri);		
	}

	public String getHtmlSource(String HtmlUrl) {
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
				tempStore.append(temp + "\r\n");
				try { // 延时
					Thread.currentThread().sleep(200);// 200毫秒
				} catch (Exception e) {
				}
			}
			in.close();
		} catch (MalformedURLException e) {
			// System.out.println("URL格式有问题!");
		} catch (IOException e) {
			// e.printStackTrace();
		}
		return tempStore.toString();
	}
}

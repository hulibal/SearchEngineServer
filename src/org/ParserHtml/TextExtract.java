package org.ParserHtml;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class TextExtract {

	private static List<String> lines;
	private final static int blocksWidth;
	private static int threshold;
	private static String html;
	private static boolean flag;
	private static int start;
	private static int end;
	private static StringBuilder text;
	private static ArrayList<Integer> indexDistribution;
	private static Vector<WebInfoBean> v_data = new Vector<WebInfoBean>();
	private WebInfoDao wDao;
	private static String htmlcharset = "ISO_8859-1";
	static {
		lines = new ArrayList<String>();
		indexDistribution = new ArrayList<Integer>();
		text = new StringBuilder();
		blocksWidth = 3;
		flag = false;
		/* �����ȡ����ҳ�����������ɿ�����ű���δ�޳�ʱ��ֻҪ�������ֵ���ɡ� */
		/* ��ֵ����׼ȷ�������ٻ����½���ֵ��С�������󣬵����Ա�֤�鵽ֻ��һ�仰������ */
		threshold = 90;
	}

	public void setthreshold(int value) {
		threshold = value;
	}

	/**
	 * ��ȡ��ҳ���ģ����жϸ���ҳ�Ƿ���Ŀ¼�͡�����֪����Ŀ϶��ǿ��Գ�ȡ���ĵ���������ҳ��
	 * 
	 * @param _html
	 *            ��ҳHTML�ַ�
	 * 
	 * @return ��ҳ����string
	 * @throws Throwable
	 */
	public String parse(String _html) {
		return parse(_html, false);
	}

	/**
	 * �жϴ���HTML��������������ҳ�����ȡ���ģ��������<b>"unkown"</b>��
	 * 
	 * @param _html
	 *            ��ҳHTML�ַ�
	 * @param _flag
	 *            true�����������ж�, ʡ�Դ˲�����Ĭ��Ϊfalse
	 * 
	 * @return ��ҳ����string
	 * @throws Throwable
	 */
	public String parse(String _html, boolean _flag) {
		flag = _flag;
		html = _html;
		preProcess();
		// System.out.println(html);
		return getText();
	}

	private void preProcess() {
		html = html.replaceAll("(?is)<!DOCTYPE.*?>","");
		html = html.replaceAll("(?is)<!--.*?-->",""); // remove html comment
		html = html.replaceAll("(?is)<script.*?>.*?</script>",""); // remove javascript
		html = html.replaceAll("(?is)<style.*?>.*?</style>",""); // remove css
		html = html.replaceAll("&.{2,5};|&#.{2,5};",""); // remove special char
		html = html.replaceAll("  +",""); 
		html = html.replaceAll("(?is)<.*?>","");// <!--[if
													// !IE]>|xGv00|9900d21eb16fa4350a3001b3974a9415<![endif]-->
	}

	private String getText() {
		lines = Arrays.asList(html.split("\n"));
		indexDistribution.clear();

		for (int i = 0; i < lines.size() - blocksWidth; i++) {
			int wordsNum = 0;
			for (int j = i; j < i + blocksWidth; j++) {
				lines.set(j, lines.get(j).replaceAll("\\s+", ""));
				wordsNum += lines.get(j).length();
			}
			indexDistribution.add(wordsNum);
			// System.out.println(wordsNum);
		}

		start = -1;
		end = -1;
		boolean boolstart = false, boolend = false;
		text.setLength(0);

		for (int i = 0; i < indexDistribution.size() - 1; i++) {
			if (indexDistribution.get(i) > threshold && !boolstart) {
				if (indexDistribution.get(i + 1).intValue() != 0
						|| indexDistribution.get(i + 2).intValue() != 0
						|| indexDistribution.get(i + 3).intValue() != 0) {
					boolstart = true;
					start = i;
					continue;
				}
			}
			if (boolstart) {
				if (indexDistribution.get(i).intValue() == 0
						|| indexDistribution.get(i + 1).intValue() == 0) {
					end = i;
					boolend = true;
				}
			}
			StringBuilder tmp = new StringBuilder();
			if (boolend) {
				// System.out.println(start+1 + "\t\t" + end+1);
				for (int ii = start; ii <= end; ii++) {
					if (lines.get(ii).length() < 5)
						continue;
					tmp.append(lines.get(ii) + "\n");
				}
				String str = tmp.toString();
				// System.out.println(str);
				if (str.contains("Copyright") || str.contains("��Ȩ����"))
					continue;
				text.append(str);
				boolstart = boolend = false;
			}
		}
		return text.toString();
	}
	
	
	
	public String getCharset(String s){
		String charset = null;
		Pattern p = Pattern.compile("charset\\s?=\\s?\"?((utf-?8)?(gbk)?(gb2312)?)\"?", Pattern.CASE_INSENSITIVE
				+ Pattern.UNICODE_CASE);
		Pattern pu = Pattern.compile("utf-?8",Pattern.CASE_INSENSITIVE
				+ Pattern.UNICODE_CASE);
		Pattern pg =Pattern.compile("gbk",Pattern.CASE_INSENSITIVE
				+ Pattern.UNICODE_CASE);
		Pattern pg2 =Pattern.compile("gb2312",Pattern.CASE_INSENSITIVE
				+ Pattern.UNICODE_CASE);
		Matcher m = p.matcher(s);
		if(m.find()){
			Matcher mu = pu.matcher(m.group(1));
			Matcher mg = pg.matcher(m.group(1));
			Matcher mg2 = pg2.matcher(m.group(1));
			if(mu.find()){
				charset = "utf-8";
			}else if(mg.find()){
				charset = "gbk";
			}else if(mg2.find()){
				charset = "gb2312";
			}
		}
		return charset;
	}
	
	
	public String getHTML(String strURL) throws IOException {
		URL url = new URL(strURL);
		String str = null;
		BufferedReader br_UTF8 = new BufferedReader(new InputStreamReader(url
				.openStream(), "utf-8"));
		BufferedReader br_GBK = new BufferedReader(new InputStreamReader(url
				.openStream(), "gbk")); // �ò�����ҳ�����޷�ֱ�Ӷ�ȡcharset,��ü����ǲ��ֶַ��������
		String s1 = "";
		String s2 = "";
		StringBuilder sb1 = new StringBuilder("");
		StringBuilder sb2 = new StringBuilder("");
		while ((s1 = br_UTF8.readLine()) != null) {
			sb1.append(s1 + "\n");
		}
		while ((s2 = br_GBK.readLine()) != null) {
			sb2.append(s2 + "\n");
		}
		String str_UTF8 = sb1.toString();
		String str_GBK = sb2.toString();
		if (getCharset(str_UTF8)=="utf-8") {
			htmlcharset = "utf-8";
			str = str_UTF8;
		} else {
			htmlcharset = "gbk";
			str = str_GBK;
		}
		return str;
	}

	public String StringFilter(String str) throws PatternSyntaxException {
		// ����\��/��:��*��?��"��<��>��|��Щ�ַ��ļ����ļ���涨���ܰ���Щ�ַ�
		String regEx = "[`~!@#$%^&*\"()+=|{}':;'\\[\\].<>/?~����]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		return m.replaceAll("").trim();
	}

	public void addwebpage(String filepath) {
		List<WebInfoBean> webInfoBeans = new ArrayList<WebInfoBean>();
		WebInfoDao wDao = new WebInfoDao();
		try {
			File file = new File(filepath);
			if (!file.isDirectory()) {
				System.out.println("�ļ���" + file.getName());
			} else if (file.isDirectory()) {
				String[] list = file.list();
				for (int i = 0; i < list.length; i++) {
					File document = new File(filepath + "\\" + list[i]);
					if (document.isDirectory()) {
						System.out.println("�ļ���" + document.getName());
					} else if (!document.isDirectory()) {
						int maxId = wDao.getMaxId();
						WebInfoBean webInfo = new WebInfoBean();
						webInfo.setWb_id("'" + String.valueOf(maxId + i + 1)
								+ "'");
						webInfo.setWb_name("'" + document.getName()+ "'");
						//System.out.println(document.getAbsolutePath());
						webInfo.setWb_address("'file:///"
								+ document.getAbsolutePath().replaceAll("\\\\",
										"/") + "'");
						webInfo.setWb_size(document.length());
						webInfoBeans.add(webInfo);
					}
				}
			}
			wDao.insertWebInfos(webInfoBeans);
			//System.out.println("�����ӳɹ���");
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}
	
	
	public void initialdata(String path)throws Exception{
		WebInfoDao wDao = new WebInfoDao();
		wDao.deleteWebpage();
		File file = new File(path);//textExtract��·��
		if (!file.isDirectory()) {
			System.out.println("�ļ���" + file.getName());
		} else if (file.isDirectory()) {
			String[] tempList =file.list();
			for (int i = 0; i < tempList.length; i++) {
				File document = new File(path + "\\" + tempList[i]);
				if (document.isDirectory()) {
					System.out.println("�ļ���" + document.getName());
				} else if (!document.isDirectory()) {
					document.delete();
				}
				}
		}		
	}
}

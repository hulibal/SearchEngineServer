package org.apache.lucene.demo;

import java.io.File;

import javax.management.InvalidAttributeValueException;

import org.archive.crawler.event.CrawlStatusListener;
import org.archive.crawler.framework.CrawlController;
import org.archive.crawler.framework.exceptions.InitializationException;
import org.archive.crawler.settings.XMLSettingsHandler;
//import org.apache.commons.httpclient.URIException;


public class Crawler {
	private String orderFile = null;
	//private CrawlController controller = new CrawlController(); // Heritrix�Ŀ�����
	CrawlStatusListener listener = null;// ������

	public Crawler() {
		xmlReader xmlreader = new xmlReader();
		orderFile = xmlreader.getCrawlerPath("crawler_order")+"/order.xml";
		//orderFile = "D:/Work/WEB app/SearchEngineServer/crawler/job/order.xml";
		//System.out.println("Crawler Start...");
	}

	public Crawler(String orderfile_path) {
		orderFile = orderfile_path;
	}
	
	public void deleteFile(File file) {
		if (file.exists()) { 
			if (file.isFile()) { 
				file.delete(); 
			} else if (file.isDirectory()) { 
				File files[] = file.listFiles(); 
				for (int i = 0; i < files.length; i++) { 
					deleteFile(files[i]); 
				}
			}
			file.delete();
		} else {
			System.out.println("the file you want to delete isn't exist!" + '\n');
		}
		System.out.println("deleting html_files has finished!");
	}

	public  void startCrawler() throws InterruptedException {
		XMLSettingsHandler handler = null; // ��ȡorder.xml�ļ��Ĵ�����
		try {
			File file = new File(orderFile); // order.xml�ļ�
			xmlReader delete_reader = new xmlReader();
			String download_path = delete_reader.getCrawlerPath("download");
			String state_delete = delete_reader.getCrawlerPath("crawler_order") + "/state";
			File html_files = new File(download_path);
			File state_path = new File(state_delete);
			deleteFile(html_files);
			deleteFile(state_path);
			handler = new XMLSettingsHandler(file);
			handler.initialize();// ��ȡorder.xml�еĸ�������
			
			CrawlController controller = new CrawlController(); // Heritrix�Ŀ�����
			controller.initialize(handler);// �Ӷ�ȡ��order.xml�еĸ�����������ʼ��������

			if (listener != null) {
				controller.addCrawlStatusListener(listener);// ��������Ӽ�����
			}
			controller.requestCrawlStart();// ��ʼץȡ

			/*
			 * ���Heritrix��һֱ��������ȴ�
			 */
			while (true) {
				if (controller.isRunning() == false) {
					break;
				}
				Thread.sleep(1000);
			}

			// ���Heritrix����������ֹͣ
			controller.requestCrawlStop();

		} catch (InvalidAttributeValueException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InitializationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) { // TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/*
	public void pauseCrawler() throws InterruptedException {
		controller.requestCrawlPause();
	}

	public void resumeCrawler() throws InterruptedException {
		controller.requestCrawlResume();
	}

	public void stopCrawler() throws InterruptedException {
		controller.requestCrawlStop();
	}
	*/
}

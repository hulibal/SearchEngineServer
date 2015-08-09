package Minehzg;

import java.io.File;

import javax.management.InvalidAttributeValueException;

import org.archive.crawler.event.CrawlStatusListener;
import org.archive.crawler.framework.CrawlController;
import org.archive.crawler.framework.exceptions.InitializationException;
import org.archive.crawler.settings.XMLSettingsHandler;

public class StartCrawler_byIDE {
	private String orderFile = null;
	private CrawlController controller = new CrawlController(); // Heritrix�Ŀ�����
	CrawlStatusListener listener = null;// ������

	public StartCrawler_byIDE() {
		orderFile = "E:/spider/job/order.xml";
	}

	public StartCrawler_byIDE(String orderfile_path) {
		orderFile = orderfile_path;
	}

	public void startCrawler() throws InterruptedException {
		XMLSettingsHandler handler = null; // ��ȡorder.xml�ļ��Ĵ�����
		try {
			File file = new File(orderFile); // order.xml�ļ�
			handler = new XMLSettingsHandler(file);
			handler.initialize();// ��ȡorder.xml�еĸ�������
			//
			controller.initialize(handler);// �Ӷ�ȡ��order.xml�еĸ�����������ʼ��������

			if (listener != null) {
				controller.addCrawlStatusListener(listener);// ��������Ӽ�����
			}
			controller.requestCrawlStart();// ��ʼץȡ
			
			/*
			 * ���Heritrix��һֱ��������ȴ�
			 */
			int i = 0;
			while (true) {
				if (controller.isRunning() == false) {
					break;
				}
				i++;
				System.out.println(i);
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

	public void pauseCrawler() throws InterruptedException {
		controller.requestCrawlPause();
	}

	public void resumeCrawler() throws InterruptedException {
		controller.requestCrawlResume();
	}

	public void stopCrawler() throws InterruptedException {
		controller.requestCrawlStop();
	}
}

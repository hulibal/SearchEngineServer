package Minehzg;

import java.io.File;
import javax.management.InvalidAttributeValueException;
import org.archive.crawler.event.CrawlStatusListener;
import org.archive.crawler.framework.CrawlController;
import org.archive.crawler.framework.exceptions.InitializationException;
import org.archive.crawler.settings.XMLSettingsHandler;

public class StartHeritrixByEclipse {

	public void run() throws InterruptedException {
		String orderFile = "E:/spider/job/order.xml";// order.xml�ļ�·��
		File file = null; // order.xml�ļ�

		CrawlStatusListener listener = null;// ������
		XMLSettingsHandler handler = null; // ��ȡorder.xml�ļ��Ĵ�����
		CrawlController controller = null; // Heritrix�Ŀ�����
		try {
			file = new File(orderFile);
			handler = new XMLSettingsHandler(file);
			handler.initialize();// ��ȡorder.xml�еĸ�������

			controller = new CrawlController();//
			controller.initialize(handler);// �Ӷ�ȡ��order.xml�еĸ�����������ʼ��������

			controller.addCrawlStatusListener(listener);// ��������Ӽ�����

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
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
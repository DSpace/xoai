package test.com.lyncode.xoai.serviceprovider;

import com.lyncode.xoai.serviceprovider.HarvesterManager;
import com.lyncode.xoai.serviceprovider.configuration.Configuration;
import com.lyncode.xoai.serviceprovider.exceptions.HarvestException;
import com.lyncode.xoai.serviceprovider.exceptions.InternalHarvestException;
import com.lyncode.xoai.serviceprovider.verbs.Identify;

public class SimpleIdentify {
	public static void main(String... args) {
		Configuration config = new Configuration();
		config.setResumptionInterval(1000); // 1 second

		String baseUrl = "http://localhost:8080/xoai/request";

		HarvesterManager harvester = new HarvesterManager(config, baseUrl);

		try {
			Identify id = harvester.identify();
			System.out.println(id.getRepositoryName());
		} catch (HarvestException e) {
			System.out.println(e.getClass().getName());
			System.out.println(e.getMessage());
		} catch (InternalHarvestException e) {
			e.printStackTrace();
		}
	}
}

package test.com.lyncode.xoai.serviceprovider;

import com.lyncode.xoai.serviceprovider.HarvesterManager;
import com.lyncode.xoai.serviceprovider.configuration.Configuration;
import com.lyncode.xoai.serviceprovider.data.Set;
import com.lyncode.xoai.serviceprovider.exceptions.HarvestException;
import com.lyncode.xoai.serviceprovider.iterators.SetIterator;

public class SimpleSetListing {
	public static void main(String... args) {
		Configuration config = new Configuration();
		config.setResumptionInterval(1000); // 1 second

		String baseUrl = "http://localhost:8080/xoai/request";

		HarvesterManager harvester = new HarvesterManager(config, baseUrl);

		SetIterator it = harvester.listSets().iterator();
		try {
			while (it.hasNext()) {
				Set s = it.next();
				System.out.println(s.getSetName() + ": " + s.getSetSpec());
			}
		} catch (Exception e) {
			System.out.println(e.getClass().getName());
			System.out.println(e.getMessage());
		}
	}
}

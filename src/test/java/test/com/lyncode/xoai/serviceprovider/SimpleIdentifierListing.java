package test.com.lyncode.xoai.serviceprovider;

import com.lyncode.xoai.serviceprovider.HarvesterManager;
import com.lyncode.xoai.serviceprovider.configuration.Configuration;
import com.lyncode.xoai.serviceprovider.data.Identifier;
import com.lyncode.xoai.serviceprovider.exceptions.HarvestException;
import com.lyncode.xoai.serviceprovider.iterators.IdentifierIterator;

public class SimpleIdentifierListing {
	public static void main(String... args) {
		Configuration config = new Configuration();
		config.setResumptionInterval(1000); // 1 second

		String baseUrl = "http://localhost:8080/xoai/request";
		String metadataPrefix = "oai_dc";

		HarvesterManager harvester = new HarvesterManager(config, baseUrl);

		IdentifierIterator it = harvester.listIdentifiers(metadataPrefix)
				.iterator();
		try {
			while (it.hasNext()) {
				Identifier r = it.next();
				System.out.println(r.getHeader().getIdentifier());
			}
		} catch (Exception e) {
			System.out.println(e.getClass().getName());
			System.out.println(e.getMessage());
		}
	}
}

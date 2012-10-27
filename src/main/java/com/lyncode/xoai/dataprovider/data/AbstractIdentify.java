/**
 * Copyright 2012 Lyncode
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lyncode.xoai.dataprovider.data;

import java.util.Date;
import java.util.List;

import com.lyncode.xoai.dataprovider.core.DeleteMethod;
import com.lyncode.xoai.dataprovider.core.Granularity;

/**
 * Base class (required extension) to identify the OAI interface.
 * 
 * @author Development @ Lyncode <development@lyncode.com>
 * @version 2.2.9
 */
public abstract class AbstractIdentify {
	/**
	 * Gets the name of the repository NOTE: This is just for identifying
	 * purposes.
	 * 
	 * @return Repository Name
	 */
	public abstract String getRepositoryName();

	/**
	 * Gets the administrator emails. NOTE: This is just for identifying
	 * purposes.
	 * 
	 * @return Administrator emails
	 */
	public abstract List<String> getAdminEmails();

	/**
	 * Gets the base url. NOTE: This is just for identifying purposes.
	 * 
	 * @return Base url
	 */
	public abstract String getBaseUrl();

	/**
	 * Gets the earliest date on the system. Any item should have a date lower
	 * than this one.
	 * 
	 * @return The earliest date
	 */
	public abstract Date getEarliestDate();

	/**
	 * Repositories must declare one of three levels of support for deleted
	 * records in the deletedRecord element of the Identify response.
	 * 
	 * @return The delete method
	 */
	public abstract DeleteMethod getDeleteMethod();

	/**
	 * Repositories must support selective harvesting with the from and until
	 * arguments expressed at day granularity. Optional support for seconds
	 * granularity is indicated in the response to the Identify request.
	 * 
	 * @return Granularity
	 */
	public abstract Granularity getGranularity();

}

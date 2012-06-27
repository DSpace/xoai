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

package com.lyncode.xoai.common.dataprovider.core;

/**
 * If a record is no longer available then it is said to be deleted. Repositories must declare one of three levels of support for deleted records in the deletedRecord element of the Identify response:
 *
 * no - the repository does not maintain information about deletions. A repository that indicates this level of support must not reveal a deleted status in any response.
 * persistent - the repository maintains information about deletions with no time limit. A repository that indicates this level of support must persistently keep track of the full history of deletions and consistently reveal the status of a deleted record over time.
 * transient - the repository does not guarantee that a list of deletions is maintained persistently or consistently. A repository that indicates this level of support may reveal a deleted status for records.
 *
 * If a repository does not keep track of deletions then such records will simply vanish from responses and there will be no way for a harvester to discover deletions through continued incremental harvesting. If a repository does keep track of deletions then the datestamp of the deleted record must be the date and time that it was deleted. Responses to GetRecord request for a deleted record must then include a header with the attribute status="deleted", and must not include metadata or about parts. Similarly, responses to selective harvesting requests with set membership and date range criteria that include deleted records must include the headers of these records. Incremental harvesting will thus discover deletions from repositories that keep track of them.

 * Deleted status is a property of individual records. Like a normal record, a deleted record is identified by a unique identifier, a metadataPrefix and a datestamp. Other records, with different metadataPrefix but the same unique identifier, may remain available for the item.
 */

/**
 * @author melo
 */
public enum DeleteMethod {
    NO, // There is no delete
    PERSISTENT, // The delete is persistent
    TRANSIENT // The delete is transient
}

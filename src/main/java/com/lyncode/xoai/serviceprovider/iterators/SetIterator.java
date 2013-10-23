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
 *
 * @author Development @ Lyncode <development@lyncode.com>
 * @version 3.1.0
 */

package com.lyncode.xoai.serviceprovider.iterators;

import com.lyncode.xoai.serviceprovider.oaipmh.spec.SetType;
import com.lyncode.xoai.serviceprovider.verbs.ListSets;
import com.lyncode.xoai.serviceprovider.verbs.runners.RetrieveListSets;
import com.lyncode.xoai.util.ProcessingQueue;


/**
 * @author Development @ Lyncode <development@lyncode.com>
 * @version 3.1.0
 */
public class SetIterator {
    private ListSets listSets;


    public SetIterator(ListSets listSets) {
        this.listSets = listSets;
    }

    public ProcessingQueue<SetType> harvest() {
        ProcessingQueue<SetType> list = new ProcessingQueue<SetType>();
        RetrieveListSets l = new RetrieveListSets(list, listSets.getServiceProvider(), listSets.getParameters());
        Thread t = new Thread(l);
        t.start();
        return list;
    }

}

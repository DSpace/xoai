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

package com.lyncode.xoai.dataprovider;

import com.lyncode.xoai.dataprovider.core.OAIParameters;
import com.lyncode.xoai.dataprovider.core.XOAIContext;
import com.lyncode.xoai.dataprovider.core.XOAIManager;
import com.lyncode.xoai.dataprovider.data.*;
import com.lyncode.xoai.dataprovider.data.internal.ItemRepository;
import com.lyncode.xoai.dataprovider.data.internal.SetRepository;
import com.lyncode.xoai.dataprovider.exceptions.*;
import com.lyncode.xoai.dataprovider.handlers.*;
import com.lyncode.xoai.dataprovider.xml.oaipmh.*;
import com.lyncode.xoai.util.DateUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.codehaus.stax2.XMLOutputFactory2;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Development @ Lyncode <development@lyncode.com>
 * @version 3.1.0
 */
public class OAIDataProvider {
    private static Logger log = LogManager.getLogger(OAIDataProvider.class);

    private static XMLOutputFactory factory = XMLOutputFactory2.newFactory();

    private XOAIManager _manager;

    private AbstractIdentify _identify;
    private SetRepository _listSets;
    private ItemRepository _itemRepo;
    private List<String> _compressions;
    private XOAIContext _context;
    private AbstractResumptionTokenFormat _format;

    private GetRecordHandler getRecordHandler;
    private IdentifyHandler identifyHandler;
    private ListIdentifiersHandler listIdentifiersHandler;
    private ListMetadataFormatsHandler listMetadataFormatsHandler;
    private ListRecordsHandler listRecordsHandler;
    private ListSetsHandler listSetsHandler;
    private ErrorHandler errorHandler;


    public OAIDataProvider(XOAIManager manager, String contexturl, AbstractIdentify identify,
                           AbstractSetRepository listsets,
                           AbstractItemRepository itemRepository)
            throws InvalidContextException {
        log.debug("Context chosen: " + contexturl);

        _manager = manager;

        _context = _manager.getContextManager().getOAIContext(contexturl);

        if (_context == null)
            throw new InvalidContextException("Context \"" + contexturl
                    + "\" does not exist");
        _identify = identify;
        _listSets = new SetRepository(listsets);
        _itemRepo = new ItemRepository(itemRepository);
        _compressions = new ArrayList<String>();
        _format = new DefaultResumptionTokenFormat();

        getRecordHandler = new GetRecordHandler(_context, _itemRepo, _identify);
        identifyHandler = new IdentifyHandler(_identify, _compressions);
        listMetadataFormatsHandler = new ListMetadataFormatsHandler(_itemRepo, _context);
        listRecordsHandler = new ListRecordsHandler(_manager.getMaxListRecordsSize(), _listSets, _itemRepo, _identify, _context, _format);
        listIdentifiersHandler = new ListIdentifiersHandler(_manager.getMaxListIdentifiersSize(), _listSets, _itemRepo, _identify, _context, _format);
        listSetsHandler = new ListSetsHandler(_manager.getMaxListSetsSize(), _listSets, _context, _format);
        errorHandler = new ErrorHandler();
    }

    public OAIDataProvider(XOAIManager manager, String contexturl, AbstractIdentify identify,
                           AbstractSetRepository listsets,
                           AbstractItemRepository itemRepository,
                           AbstractResumptionTokenFormat format)
            throws InvalidContextException {
        log.debug("Context chosen: " + contexturl);

        _manager = manager;

        _context = _manager.getContextManager().getOAIContext(contexturl);

        if (_context == null)
            throw new InvalidContextException("Context \"" + contexturl
                    + "\" does not exist");
        _identify = identify;
        _listSets = new SetRepository(listsets);
        _itemRepo = new ItemRepository(itemRepository);
        _compressions = new ArrayList<String>();
        _format = format;

        getRecordHandler = new GetRecordHandler(_context, _itemRepo, _identify);
        identifyHandler = new IdentifyHandler(_identify, _compressions);
        listMetadataFormatsHandler = new ListMetadataFormatsHandler(_itemRepo, _context);
        listRecordsHandler = new ListRecordsHandler(_manager.getMaxListRecordsSize(), _listSets, _itemRepo, _identify, _context, _format);
        listIdentifiersHandler = new ListIdentifiersHandler(_manager.getMaxListIdentifiersSize(), _listSets, _itemRepo, _identify, _context, _format);
        listSetsHandler = new ListSetsHandler(_manager.getMaxListSetsSize(), _listSets, _context, _format);
        errorHandler = new ErrorHandler();
    }

    public OAIDataProvider(XOAIManager manager, String contexturl, AbstractIdentify identify,
                           AbstractSetRepository listsets,
                           AbstractItemRepository itemRepository, List<String> compressions)
            throws InvalidContextException {
        _manager = manager;

        _context = _manager.getContextManager().getOAIContext(contexturl);

        if (_context == null)
            throw new InvalidContextException();
        _identify = identify;
        _listSets = new SetRepository(listsets);
        _itemRepo = new ItemRepository(itemRepository);
        _compressions = compressions;
        _format = new DefaultResumptionTokenFormat();

        getRecordHandler = new GetRecordHandler(_context, _itemRepo, _identify);
        identifyHandler = new IdentifyHandler(_identify, _compressions);
        listMetadataFormatsHandler = new ListMetadataFormatsHandler(_itemRepo, _context);
        listRecordsHandler = new ListRecordsHandler(_manager.getMaxListRecordsSize(), _listSets, _itemRepo, _identify, _context, _format);
        listIdentifiersHandler = new ListIdentifiersHandler(_manager.getMaxListIdentifiersSize(), _listSets, _itemRepo, _identify, _context, _format);
        listSetsHandler = new ListSetsHandler(_manager.getMaxListSetsSize(), _listSets, _context, _format);
        errorHandler = new ErrorHandler();
    }

    public OAIPMH handle(OAIRequestParameters params) throws OAIException {
        log.debug("Starting handling OAI request");

        OAIPMH response = new OAIPMH(_manager);
        OAIPMHtype info = new OAIPMHtype();
        response.setInfo(info);

        RequestType request = new RequestType();
        info.setRequest(request);
        info.setResponseDate(new Date());

        request.setValue(this._identify.getBaseUrl());
        try {
            OAIParameters parameters = new OAIParameters(params, _format);
            VerbType verb = parameters.getVerb();
            request.setVerb(verb);

            if (params.getResumptionToken() != null)
                request.setResumptionToken(params.getResumptionToken());
            if (params.getIdentifier() != null)
                request.setIdentifier(parameters.getIdentifier());
            if (params.getFrom() != null)
                try {
                    request.setFrom(new DateInfo(DateUtils.parse(params.getFrom()), _identify.getGranularity().toGranularityType()));
                } catch (java.text.ParseException e) {
                    throw new BadArgumentException("Invalid date given in until parameter");
                }
            if (params.getMetadataPrefix() != null)
                request.setMetadataPrefix(params.getMetadataPrefix());
            if (params.getSet() != null)
                request.setSet(params.getSet());
            if (params.getUntil() != null)
                try {
                    request.setUntil(new DateInfo(DateUtils.parse(params.getUntil()), _identify.getGranularity().toGranularityType()));
                } catch (java.text.ParseException e) {
                    throw new BadArgumentException("Invalid date given in until parameter");
                }

            switch (verb) {
                case IDENTIFY:
                    info.setIdentify(identifyHandler.handle(parameters));
                    break;
                case LIST_SETS:
                    info.setListSets(listSetsHandler.handle(parameters));
                    break;
                case LIST_METADATA_FORMATS:
                    info.setListMetadataFormats(listMetadataFormatsHandler.handle(parameters));
                    break;
                case GET_RECORD:
                    info.setGetRecord(getRecordHandler.handle(parameters));
                    break;
                case LIST_IDENTIFIERS:
                    info.setListIdentifiers(listIdentifiersHandler.handle(parameters));
                    break;
                case LIST_RECORDS:
                    info.setListRecords(listRecordsHandler.handle(parameters));
                    break;
            }
        } catch (HandlerException e) {
            log.debug(e.getMessage(), e);
            info.getError().add(errorHandler.handle(e));
        }

        return response;
    }

    public void handle(OAIRequestParameters params, OutputStream out)
            throws OAIException, XMLStreamException, WritingXmlException {

        XMLStreamWriter writer = factory.createXMLStreamWriter(out);
        writer.writeStartDocument();
        this.handle(params).write(writer);
        writer.writeEndDocument();
        writer.flush();
        writer.close();
    }
}

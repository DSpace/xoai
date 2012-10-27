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
 * @version 2.2.9
 */

package com.lyncode.xoai.dataprovider;

import java.io.OutputStream;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.lyncode.xoai.dataprovider.core.Granularity;
import com.lyncode.xoai.dataprovider.core.ListItemIdentifiersResult;
import com.lyncode.xoai.dataprovider.core.ListItemsResults;
import com.lyncode.xoai.dataprovider.core.ListSetsResult;
import com.lyncode.xoai.dataprovider.core.OAIParameters;
import com.lyncode.xoai.dataprovider.core.ReferenceSet;
import com.lyncode.xoai.dataprovider.core.ResumptionToken;
import com.lyncode.xoai.dataprovider.core.Set;
import com.lyncode.xoai.dataprovider.core.XOAIContext;
import com.lyncode.xoai.dataprovider.core.XOAIManager;
import com.lyncode.xoai.dataprovider.data.AbstractAbout;
import com.lyncode.xoai.dataprovider.data.AbstractIdentify;
import com.lyncode.xoai.dataprovider.data.AbstractItem;
import com.lyncode.xoai.dataprovider.data.AbstractItemIdentifier;
import com.lyncode.xoai.dataprovider.data.AbstractItemRepository;
import com.lyncode.xoai.dataprovider.data.AbstractSetRepository;
import com.lyncode.xoai.dataprovider.data.MetadataFormat;
import com.lyncode.xoai.dataprovider.data.internal.Item;
import com.lyncode.xoai.dataprovider.data.internal.ItemIdentify;
import com.lyncode.xoai.dataprovider.data.internal.ItemRepository;
import com.lyncode.xoai.dataprovider.data.internal.SetRepository;
import com.lyncode.xoai.dataprovider.exceptions.BadArgumentException;
import com.lyncode.xoai.dataprovider.exceptions.BadResumptionToken;
import com.lyncode.xoai.dataprovider.exceptions.CannotDisseminateRecordException;
import com.lyncode.xoai.dataprovider.exceptions.DoesNotSupportSetsException;
import com.lyncode.xoai.dataprovider.exceptions.DuplicateDefinitionException;
import com.lyncode.xoai.dataprovider.exceptions.IdDoesNotExistException;
import com.lyncode.xoai.dataprovider.exceptions.IllegalVerbException;
import com.lyncode.xoai.dataprovider.exceptions.InvalidContextException;
import com.lyncode.xoai.dataprovider.exceptions.MarshallingException;
import com.lyncode.xoai.dataprovider.exceptions.NoMatchesException;
import com.lyncode.xoai.dataprovider.exceptions.NoMetadataFormatsException;
import com.lyncode.xoai.dataprovider.exceptions.OAIException;
import com.lyncode.xoai.dataprovider.exceptions.UnknownParameterException;
import com.lyncode.xoai.dataprovider.exceptions.XSLTransformationException;
import com.lyncode.xoai.dataprovider.util.MarshallingUtils;
import com.lyncode.xoai.dataprovider.util.XSLTUtils;
import com.lyncode.xoai.dataprovider.xml.ExportManager;
import com.lyncode.xoai.dataprovider.xml.oaipmh.AboutType;
import com.lyncode.xoai.dataprovider.xml.oaipmh.DeletedRecordType;
import com.lyncode.xoai.dataprovider.xml.oaipmh.DescriptionType;
import com.lyncode.xoai.dataprovider.xml.oaipmh.GetRecordType;
import com.lyncode.xoai.dataprovider.xml.oaipmh.GranularityType;
import com.lyncode.xoai.dataprovider.xml.oaipmh.HeaderType;
import com.lyncode.xoai.dataprovider.xml.oaipmh.IdentifyType;
import com.lyncode.xoai.dataprovider.xml.oaipmh.ListIdentifiersType;
import com.lyncode.xoai.dataprovider.xml.oaipmh.ListMetadataFormatsType;
import com.lyncode.xoai.dataprovider.xml.oaipmh.ListRecordsType;
import com.lyncode.xoai.dataprovider.xml.oaipmh.ListSetsType;
import com.lyncode.xoai.dataprovider.xml.oaipmh.MetadataFormatType;
import com.lyncode.xoai.dataprovider.xml.oaipmh.MetadataType;
import com.lyncode.xoai.dataprovider.xml.oaipmh.OAIPMHerrorType;
import com.lyncode.xoai.dataprovider.xml.oaipmh.OAIPMHerrorcodeType;
import com.lyncode.xoai.dataprovider.xml.oaipmh.OAIPMHtype;
import com.lyncode.xoai.dataprovider.xml.oaipmh.ObjectFactory;
import com.lyncode.xoai.dataprovider.xml.oaipmh.RecordType;
import com.lyncode.xoai.dataprovider.xml.oaipmh.RequestType;
import com.lyncode.xoai.dataprovider.xml.oaipmh.ResumptionTokenType;
import com.lyncode.xoai.dataprovider.xml.oaipmh.SetType;
import com.lyncode.xoai.dataprovider.xml.oaipmh.StatusType;
import com.lyncode.xoai.dataprovider.xml.oaipmh.VerbType;
import com.lyncode.xoai.dataprovider.xml.xoaidescription.XOAIDescription;
import com.lyncode.xoai.serviceprovider.exceptions.CannotDisseminateFormatException;

/**
 * @author Development @ Lyncode <development@lyncode.com>
 * @version 2.2.9
 */
public class OAIDataProvider {
	private static Logger log = LogManager.getLogger(OAIDataProvider.class);

	private static final String PROTOCOL_VERSION = "2.0";
	private static final String XOAI_DESC = "XOAI: OAI-PMH Java Toolkit";

	private AbstractIdentify _identify;
	private ObjectFactory _factory;
	private SetRepository _listSets;
	private ItemRepository _itemRepo;
	private List<String> _compressions;
	private XOAIContext _context;

	public OAIDataProvider(String contexturl, AbstractIdentify identify,
			AbstractSetRepository listsets,
			AbstractItemRepository itemRepository)
			throws InvalidContextException {
		log.debug("Context chosen: " + contexturl);

		_context = XOAIManager.getManager().getContextManager()
				.getOAIContext(contexturl);
		if (_context == null)
			throw new InvalidContextException("Context \"" + contexturl
					+ "\" does not exist");
		_factory = new ObjectFactory();
		_identify = identify;
		_listSets = new SetRepository(listsets);
		_itemRepo = new ItemRepository(itemRepository);
		_compressions = new ArrayList<String>();
	}

	public OAIDataProvider(String contexturl, AbstractIdentify identify,
			AbstractSetRepository listsets,
			AbstractItemRepository itemRepository, List<String> compressions)
			throws InvalidContextException {
		_context = XOAIManager.getManager().getContextManager()
				.getOAIContext(contexturl);
		if (_context == null)
			throw new InvalidContextException();
		_factory = new ObjectFactory();
		_identify = identify;
		_listSets = new SetRepository(listsets);
		_itemRepo = new ItemRepository(itemRepository);
		_compressions = compressions;
	}

	public void handle(OAIRequestParameters params, OutputStream out)
			throws OAIException {
		log.debug("Starting handling OAI request");
		ExportManager manager = new ExportManager();
		OAIPMHtype response = _factory.createOAIPMHtype();
		response.setResponseDate(this.dateToString(new Date()));
		RequestType request = _factory.createRequestType();
		request.setValue(this._identify.getBaseUrl());
		try {
			OAIParameters parameters = new OAIParameters(params);
			VerbType verb = parameters.getVerb();
			request.setVerb(verb);
			
			if (params.getResumptionToken() != null)
				request.setResumptionToken(params.getResumptionToken());
			if (params.getIdentifier() != null)
				request.setIdentifier(parameters.getIdentifier());
			if (params.getFrom() != null)
				request.setFrom(params.getFrom());
			if (params.getMetadataPrefix() != null)
				request.setMetadataPrefix(params.getMetadataPrefix());
			if (params.getSet() != null)
				request.setSet(params.getSet());
			if (params.getUntil() != null)
				request.setUntil(params.getUntil());

			switch (verb) {
			case IDENTIFY:
				response.setIdentify(this.build(manager,
						_factory.createIdentifyType()));
				break;
			case LIST_SETS:
				response.setListSets(this.build(manager, parameters,
						_factory.createListSetsType()));
				break;
			case LIST_METADATA_FORMATS:
				response.setListMetadataFormats(this.build(manager, parameters,
						_factory.createListMetadataFormatsType()));
				break;
			case GET_RECORD:
				response.setGetRecord(this.build(manager, parameters,
						_factory.createGetRecordType()));
				break;
			case LIST_IDENTIFIERS:
				response.setListIdentifiers(this.build(manager, parameters,
						_factory.createListIdentifiersType()));
				break;
			case LIST_RECORDS:
				log.debug("List Records");
				response.setListRecords(this.build(manager, parameters,
						_factory.createListRecordsType()));
				break;
			}
		} catch (IllegalVerbException e) {
			log.debug(e.getMessage(), e);
			OAIPMHerrorType error = new OAIPMHerrorType();
			error.setValue("Illegal verb");
			error.setCode(OAIPMHerrorcodeType.BAD_VERB);
			response.getError().add(error);
		} catch (DoesNotSupportSetsException e) {
			log.debug(e.getMessage(), e);
			OAIPMHerrorType error = new OAIPMHerrorType();
			error.setValue("This repository does not support sets");
			error.setCode(OAIPMHerrorcodeType.NO_SET_HIERARCHY);
			response.getError().add(error);
		} catch (NoMatchesException e) {
			log.debug(e.getMessage(), e);
			OAIPMHerrorType error = new OAIPMHerrorType();
			error.setValue("No matches for the query");
			error.setCode(OAIPMHerrorcodeType.NO_RECORDS_MATCH);
			response.getError().add(error);
		} catch (BadResumptionToken e) {
			log.debug(e.getMessage(), e);
			OAIPMHerrorType error = new OAIPMHerrorType();
			error.setValue("The resumption token is invalid");
			error.setCode(OAIPMHerrorcodeType.BAD_RESUMPTION_TOKEN);
			response.getError().add(error);
		} catch (IdDoesNotExistException e) {
			log.debug(e.getMessage(), e);
			OAIPMHerrorType error = new OAIPMHerrorType();
			error.setValue("The given id does not exist");
			error.setCode(OAIPMHerrorcodeType.ID_DOES_NOT_EXIST);
			response.getError().add(error);
		} catch (NoMetadataFormatsException e) {
			log.debug(e.getMessage(), e);
			OAIPMHerrorType error = new OAIPMHerrorType();
			error.setValue("The item does not have any metadata format available for dissemination");
			error.setCode(OAIPMHerrorcodeType.NO_METADATA_FORMATS);
			response.getError().add(error);
		} catch (BadArgumentException e) {
			log.debug(e.getMessage(), e);
			OAIPMHerrorType error = new OAIPMHerrorType();
			error.setValue(e.getMessage());
			error.setCode(OAIPMHerrorcodeType.BAD_ARGUMENT);
			response.getError().add(error);
		} catch (CannotDisseminateRecordException e) {
			log.debug(e.getMessage(), e);
			OAIPMHerrorType error = new OAIPMHerrorType();
			error.setValue("Cannot disseminate item with the given format");
			error.setCode(OAIPMHerrorcodeType.CANNOT_DISSEMINATE_FORMAT);
			response.getError().add(error);
		} catch (CannotDisseminateFormatException e) {
			log.debug(e.getMessage(), e);
			OAIPMHerrorType error = new OAIPMHerrorType();
			error.setValue("Unknown metadata format");
			error.setCode(OAIPMHerrorcodeType.CANNOT_DISSEMINATE_FORMAT);
			response.getError().add(error);
		} catch (DuplicateDefinitionException e) {
			log.debug(e.getMessage(), e);
			OAIPMHerrorType error = new OAIPMHerrorType();
			error.setValue(e.getMessage());
			error.setCode(OAIPMHerrorcodeType.BAD_ARGUMENT);
			response.getError().add(error);
		} catch (UnknownParameterException e) {
			log.debug(e.getMessage(), e);
			OAIPMHerrorType error = new OAIPMHerrorType();
			error.setValue(e.getMessage());
			error.setCode(OAIPMHerrorcodeType.BAD_ARGUMENT);
			response.getError().add(error);
		}

		response.setRequest(request);
		manager.export(response, out);
	}

	private IdentifyType build(ExportManager manager, IdentifyType ident)
			throws OAIException {
		ident.setBaseURL(_identify.getBaseUrl());
		ident.setRepositoryName(_identify.getRepositoryName());
		for (String mail : _identify.getAdminEmails())
			ident.getAdminEmail().add(mail);
		ident.setEarliestDatestamp(this.dateToString(_identify
				.getEarliestDate()));
		ident.setDeletedRecord(DeletedRecordType.valueOf(_identify
				.getDeleteMethod().name()));

		switch (_identify.getGranularity()) {
		case Day:
			ident.setGranularity(GranularityType.YYYY_MM_DD);
			break;
		case Second:
			ident.setGranularity(GranularityType.YYYY_MM_DD_THH_MM_SS_Z);
			break;
		}

		ident.setProtocolVersion(PROTOCOL_VERSION);
		for (String com : this._compressions)
			ident.getCompression().add(com);

		DescriptionType desc = _factory.createDescriptionType();
		XOAIDescription description = new XOAIDescription();
		description.setValue(XOAI_DESC);

		String id = "##DESC##";
		try {
			manager.addMap(id,
					MarshallingUtils.marshalWithoutXMLHeader(description));
		} catch (MarshallingException e) {
			throw new OAIException(e);
		}
		desc.setAny(id);
		ident.getDescription().add(desc);

		return ident;
	}

	private ListSetsType build(ExportManager exporter,
			OAIParameters parameters, ListSetsType listSets)
			throws DoesNotSupportSetsException, NoMatchesException,
			BadResumptionToken {

		if (!_listSets.supportSets())
			throw new DoesNotSupportSetsException();

		ResumptionToken resumptionToken = parameters.getResumptionToken();
		int length = XOAIManager.getManager().getMaxListSetsSize();
		log.debug("Length: " + length);
		ListSetsResult result = _listSets.getSets(_context,
				resumptionToken.getOffset(), length);
		List<Set> sets = result.getResults();

		if (sets.isEmpty() && resumptionToken.isEmpty())
			throw new NoMatchesException();

		if (sets.size() > length)
			sets = sets.subList(0, length);

		for (Set s : sets) {
			SetType set = _factory.createSetType();
			set.setSetName(s.getSetName());
			set.setSetSpec(s.getSetSpec());

			if (s.hasDescription()) {
				DescriptionType desc = _factory.createDescriptionType();
				String obj = s.getDescription();
				String id = "##set-" + s.getSetSpec() + "##";
				exporter.addMap(id, obj);
				desc.setAny(id);
				set.getSetDescription().add(desc);
			}

			listSets.getSet().add(set);
		}
		ResumptionToken rtoken;
		if (result.hasMore()) {
			rtoken = new ResumptionToken(resumptionToken.getOffset() + length);
		} else {
			rtoken = new ResumptionToken();
		}
		

		if (parameters.hasResumptionToken() || !rtoken.isEmpty()) {
			ResumptionTokenType token = _factory.createResumptionTokenType();
			token.setValue(rtoken.toString());
			token.setCursor(setCursor(resumptionToken.getOffset()));
			
			if (result.hasTotalResults()) {
				int total = result.getTotalResults();
			    token.setCompleteListSize(total(total));
			    log.debug("Total results: "+total);
			} else {
				log.debug("Has no total results shown");
			}
			listSets.setResumptionToken(token);
		}

		return listSets;
	}

	private ListMetadataFormatsType build(ExportManager manager,
			OAIParameters parameters,
			ListMetadataFormatsType listMetadataFormatsType)
			throws IdDoesNotExistException, NoMetadataFormatsException,
			OAIException {
		if (parameters.hasIdentifier()) {
			AbstractItem item = _itemRepo.getItem(parameters.getIdentifier());
			List<MetadataFormat> forms = _context.getFormats(item);
			if (forms.isEmpty())
				throw new NoMetadataFormatsException();
			for (MetadataFormat f : forms) {
				MetadataFormatType format = _factory.createMetadataFormatType();
				format.setMetadataPrefix(f.getPrefix());
				format.setMetadataNamespace(f.getNamespace());
				format.setSchema(f.getSchemaLocation());
				listMetadataFormatsType.getMetadataFormat().add(format);
			}
		} else {
			List<MetadataFormat> forms = _context.getFormats();
			if (forms.isEmpty())
				throw new OAIException(
						"The respository should have at least one metadata format");
			for (MetadataFormat f : _context.getFormats()) {
				MetadataFormatType format = _factory.createMetadataFormatType();
				format.setMetadataPrefix(f.getPrefix());
				format.setMetadataNamespace(f.getNamespace());
				format.setSchema(f.getSchemaLocation());
				listMetadataFormatsType.getMetadataFormat().add(format);
			}
		}

		return listMetadataFormatsType;
	}

	private String dateToString(Date date) {
		SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
		if (_identify.getGranularity() == Granularity.Second)
			formatDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		return formatDate.format(date);
	}

	private GetRecordType build(ExportManager manager,
			OAIParameters parameters, GetRecordType getRecordType)
			throws IdDoesNotExistException, BadArgumentException,
			CannotDisseminateRecordException, OAIException,
			NoMetadataFormatsException, CannotDisseminateFormatException {
		RecordType record = _factory.createRecordType();
		HeaderType header = _factory.createHeaderType();
		MetadataFormat format = _context.getFormatByPrefix(parameters
				.getMetadataPrefix());
		Item item = new Item(_itemRepo.getItem(parameters.getIdentifier()));
		if (!_context.isItemShown(item.getItem()))
			throw new IdDoesNotExistException("Context ignores this item");
		if (!format.isApplyable(item.getItem()))
			throw new CannotDisseminateRecordException("Format not appliable to this item");
		header.setIdentifier(item.getItem().getIdentifier());
		header.setDatestamp(this.dateToString(item.getItem().getDatestamp()));
		for (ReferenceSet s : item.getSets(_context))
			header.getSetSpec().add(s.getSetSpec());
		if (item.getItem().isDeleted())
			header.setStatus(StatusType.DELETED);
		record.setHeader(header);

		if (!item.getItem().isDeleted()) {
			MetadataType metadata = _factory.createMetadataType();
			String id = "##metadata-" + item.getItem().getIdentifier() + "##";
			try {
				if (_context.getTransformer().hasTransformer())
					manager.addMap(id, XSLTUtils.transform(_context
							.getTransformer().getXSLTFile(), format
							.getXSLTFile(), item.getItem()));
				else
					manager.addMap(id,
							XSLTUtils.transform(format.getXSLTFile(), item.getItem()));
			} catch (XSLTransformationException e) {
				throw new OAIException(e);
			}
			metadata.setAny(id);
			record.setMetadata(metadata);

			int i = 0;
			if (item.getItem().hasAbout()) {
				for (AbstractAbout abj : item.getItem().getAbout()) {
					AboutType about = _factory.createAboutType();
					String aid = "##about" + i + "-" + item.getItem().getIdentifier()
							+ "##";
					manager.addMap(aid, abj.getXML());
					about.setAny(aid);
					record.getAbout().add(about);
					i++;
				}
			}
		}

		getRecordType.setRecord(record);
		return getRecordType;
	}

	private ListIdentifiersType build(ExportManager manager,
			OAIParameters parameters, ListIdentifiersType listIdentifiersType)
			throws BadResumptionToken, BadArgumentException,
			CannotDisseminateRecordException, DoesNotSupportSetsException,
			NoMatchesException, OAIException, NoMetadataFormatsException, CannotDisseminateFormatException {
		ResumptionToken token = parameters.getResumptionToken();

		if (parameters.hasSet() && !_listSets.supportSets())
			throw new DoesNotSupportSetsException();

		int length = XOAIManager.getManager().getMaxListIdentifiersSize();
		ListItemIdentifiersResult result;
		if (!parameters.hasSet()) {
			if (parameters.hasFrom() && !parameters.hasUntil())
				result = _itemRepo.getItemIdentifiers(_context,
						token.getOffset(), length,
						parameters.getMetadataPrefix(), parameters.getFrom());
			else if (!parameters.hasFrom() && parameters.hasUntil())
				result = _itemRepo.getItemIdentifiersUntil(_context,
						token.getOffset(), length,
						parameters.getMetadataPrefix(), parameters.getUntil());
			else if (parameters.hasFrom() && parameters.hasUntil())
				result = _itemRepo.getItemIdentifiers(_context,
						token.getOffset(), length,
						parameters.getMetadataPrefix(), parameters.getFrom(),
						parameters.getUntil());
			else
				result = _itemRepo.getItemIdentifiers(_context,
						token.getOffset(), length,
						parameters.getMetadataPrefix());
		} else {
			if (!_listSets.exists(_context, parameters.getSet()))
				throw new NoMatchesException();
			if (parameters.hasFrom() && !parameters.hasUntil())
				result = _itemRepo.getItemIdentifiers(_context,
						token.getOffset(), length,
						parameters.getMetadataPrefix(), parameters.getSet(),
						parameters.getFrom());
			else if (!parameters.hasFrom() && parameters.hasUntil())
				result = _itemRepo.getItemIdentifiersUntil(_context,
						token.getOffset(), length,
						parameters.getMetadataPrefix(), parameters.getSet(),
						parameters.getUntil());
			else if (parameters.hasFrom() && parameters.hasUntil())
				result = _itemRepo.getItemIdentifiers(_context,
						token.getOffset(), length,
						parameters.getMetadataPrefix(), parameters.getSet(),
						parameters.getFrom(), parameters.getUntil());
			else
				result = _itemRepo.getItemIdentifiers(_context,
						token.getOffset(), length,
						parameters.getMetadataPrefix(), parameters.getSet());
		}

		List<AbstractItemIdentifier> results = result.getResults();
		if (results.isEmpty())
			throw new NoMatchesException();

		ResumptionToken newToken;
		if (result.hasMore()) {
			newToken = new ResumptionToken(token.getOffset() + length,
					parameters);
		} else {
			newToken = new ResumptionToken();
		}

		if (parameters.hasResumptionToken() || !newToken.isEmpty()) {
			ResumptionTokenType resToken = _factory.createResumptionTokenType();
			resToken.setValue(newToken.toString());
			resToken.setCursor(identifiersCursor(token.getOffset()));
			if (result.hasTotalResults())
			    resToken.setCompleteListSize(total(result.getTotal()));
			listIdentifiersType.setResumptionToken(resToken);
		}

		for (AbstractItemIdentifier ii : results)
			listIdentifiersType.getHeader().add(
					this.createHeader(parameters, ii));

		return listIdentifiersType;
	}

	private static BigInteger total(int total)
    {
        return new BigInteger(total+"");
    }

    private HeaderType createHeader(OAIParameters parameters,
			AbstractItemIdentifier ii) throws BadArgumentException,
			CannotDisseminateRecordException, OAIException,
			NoMetadataFormatsException, CannotDisseminateFormatException {
		MetadataFormat format = _context.getFormatByPrefix(parameters
				.getMetadataPrefix());
		if (!ii.isDeleted() && !format.isApplyable(ii))
			throw new CannotDisseminateRecordException();

		HeaderType header = _factory.createHeaderType();
		header.setDatestamp(this.dateToString(ii.getDatestamp()));
		header.setIdentifier(ii.getIdentifier());
		if (ii.isDeleted())
			header.setStatus(StatusType.DELETED);
		
		ItemIdentify ident = new ItemIdentify(ii);
		
		for (ReferenceSet s : ident.getSets(_context))
			header.getSetSpec().add(s.getSetSpec());
		return header;
	}

	private ListRecordsType build(ExportManager manager,
			OAIParameters parameters, ListRecordsType listRecordsType)
			throws BadArgumentException, CannotDisseminateRecordException,
			DoesNotSupportSetsException, NoMatchesException, OAIException,
			NoMetadataFormatsException, CannotDisseminateFormatException {
		ResumptionToken token = parameters.getResumptionToken();
		int length = XOAIManager.getManager().getMaxListRecordsSize();

		if (parameters.hasSet() && !_listSets.supportSets())
			throw new DoesNotSupportSetsException();

		log.debug("Getting items from data source");
		ListItemsResults result;
		if (!parameters.hasSet()) {
			if (parameters.hasFrom() && !parameters.hasUntil())
				result = _itemRepo.getItems(_context, token.getOffset(),
						length, parameters.getMetadataPrefix(),
						parameters.getFrom());
			else if (!parameters.hasFrom() && parameters.hasUntil())
				result = _itemRepo.getItemsUntil(_context, token.getOffset(),
						length, parameters.getMetadataPrefix(),
						parameters.getUntil());
			else if (parameters.hasFrom() && parameters.hasUntil())
				result = _itemRepo.getItems(_context, token.getOffset(),
						length, parameters.getMetadataPrefix(),
						parameters.getFrom(), parameters.getUntil());
			else
				result = _itemRepo.getItems(_context, token.getOffset(),
						length, parameters.getMetadataPrefix());
		} else {
			if (!_listSets.exists(_context, parameters.getSet()))
				throw new NoMatchesException();
			if (parameters.hasFrom() && !parameters.hasUntil())
				result = _itemRepo.getItems(_context, token.getOffset(),
						length, parameters.getMetadataPrefix(),
						parameters.getSet(), parameters.getFrom());
			else if (!parameters.hasFrom() && parameters.hasUntil())
				result = _itemRepo.getItemsUntil(_context, token.getOffset(),
						length, parameters.getMetadataPrefix(),
						parameters.getSet(), parameters.getUntil());
			else if (parameters.hasFrom() && parameters.hasUntil())
				result = _itemRepo.getItems(_context, token.getOffset(),
						length, parameters.getMetadataPrefix(),
						parameters.getSet(), parameters.getFrom(),
						parameters.getUntil());
			else
				result = _itemRepo.getItems(_context, token.getOffset(),
						length, parameters.getMetadataPrefix(),
						parameters.getSet());
		}
		log.debug("Items retrived from data source");

		List<AbstractItem> results = result.getResults();
		if (results.isEmpty())
			throw new NoMatchesException();

		ResumptionToken newToken;
		if (result.hasMore()) {
			newToken = new ResumptionToken(token.getOffset() + length,
					parameters);
		} else {
			newToken = new ResumptionToken();
		}

		if (parameters.hasResumptionToken() || !newToken.isEmpty()) {
			ResumptionTokenType resToken = _factory.createResumptionTokenType();
			resToken.setValue(newToken.toString());
			resToken.setCursor(recordsCursor(token.getOffset()));
			if (result.hasTotalResults())
			    resToken.setCompleteListSize(total(result.getTotal()));
			listRecordsType.setResumptionToken(resToken);
		}

		log.debug("Now adding records to the OAI-PMH Output");
		for (AbstractItem i : results)
			listRecordsType.getRecord().add(
					this.createRecord(manager, parameters, i));

		return listRecordsType;
	}

	private RecordType createRecord(ExportManager manager,
			OAIParameters parameters, AbstractItem item)
			throws BadArgumentException, CannotDisseminateRecordException,
			OAIException, NoMetadataFormatsException, CannotDisseminateFormatException {
		log.debug("Metadata format: " + parameters.getMetadataPrefix());
		MetadataFormat format = _context.getFormatByPrefix(parameters
				.getMetadataPrefix());
		RecordType record = _factory.createRecordType();
		HeaderType header = _factory.createHeaderType();
		log.debug("Item: " + item.getIdentifier());
		header.setIdentifier(item.getIdentifier());
		
		Item itemWrap = new Item(item);
		
		header.setDatestamp(this.dateToString(item.getDatestamp()));
		for (ReferenceSet s : itemWrap.getSets(_context))
			header.getSetSpec().add(s.getSetSpec());
		if (item.isDeleted())
			header.setStatus(StatusType.DELETED);
		record.setHeader(header);

		if (!item.isDeleted()) {
			log.debug("Outputting Metadata");
			MetadataType metadata = _factory.createMetadataType();
			String id = "##metadata-" + item.getIdentifier() + "##";
			try {
				if (_context.getTransformer().hasTransformer()) {
					log.debug("Transforming metadata (using transformer)");
					manager.addMap(id, XSLTUtils.transform(_context
							.getTransformer().getXSLTFile(), format
							.getXSLTFile(), item));
				} else {
					log.debug("Transforming metadata (without transformer)");
					manager.addMap(id,
							XSLTUtils.transform(format.getXSLTFile(), item));
				}
				metadata.setAny(id);
				record.setMetadata(metadata);
			} catch (XSLTransformationException e) {
				throw new OAIException(e);
			}

			log.debug("Outputting About");
			int i = 0;
			if (item.hasAbout()) {
				for (AbstractAbout abj : item.getAbout()) {
					AboutType about = _factory.createAboutType();
					String aid = "##about" + i + "-" + item.getIdentifier()
							+ "##";
					manager.addMap(aid, abj.getXML());
					about.setAny(aid);
					record.getAbout().add(about);
					i++;
				}
			}
		}
		return record;
	}
	
	private static BigInteger setCursor (int offset) {
	    return new BigInteger (((offset/XOAIManager.getManager().getMaxListSetsSize()))+"");
	}

    private static BigInteger recordsCursor (int offset) {
        return new BigInteger (((offset/XOAIManager.getManager().getMaxListRecordsSize()))+"");
    }
    private static BigInteger identifiersCursor (int offset) {
        return new BigInteger (((offset/XOAIManager.getManager().getMaxListIdentifiersSize()))+"");
    }
}

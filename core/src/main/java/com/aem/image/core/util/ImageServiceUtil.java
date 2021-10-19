package com.aem.image.core.util;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aem.image.core.constants.ImageUtilConstants;
import com.aem.image.core.services.AssetData;
import com.day.cq.dam.api.Asset;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode; 

public class ImageServiceUtil {

	private static final Logger log = LoggerFactory.getLogger(ImageServiceUtil.class);

	/**
	 * Get all child assets from the specified path as JSON. This uses the JCR SQL2. 
	 * Since there is no limit option to restrict the output results this is not used.
	 *
	 * @param resourceResolver  {@code ResourceResolver}
	 * @param resourceResolver  {@code ResourceResolver}
	 * @return                  {@code Iterator}
	 * @throws JsonProcessingException 
	 */
	public static String getAssetsJSON(ResourceResolver resolver, String path, int assetLimit) throws JsonProcessingException{

		log.info("Inside getAssetsJSON with path: "+ path);
		Iterator<Resource> allChildAssets = resolver.findResources(
				String.format(ImageUtilConstants.IMAGE_GET_SQL, path),javax.jcr.query.Query.JCR_SQL2);
		ObjectMapper mapper = new ObjectMapper();
		ArrayNode arrayNode = mapper.createArrayNode();

		while(allChildAssets.hasNext()) {
			Asset asset = allChildAssets.next().adaptTo(Asset.class);
			if(null != asset && StringUtils.contains(asset.getMimeType(), "image")) {      // Added this check to avoid ContentFragments and restrict to image mimetypes only.
				ObjectNode assetJSON = mapper.createObjectNode();
				assetJSON.put("title", StringUtils.defaultString(asset.getMetadataValue("dc:title")));
				assetJSON.put("name", asset.getName());
				assetJSON.put("path", asset.getPath());
				assetJSON.put("format", asset.getMimeType());
				arrayNode.addAll(Arrays.asList(assetJSON));
			}
		}
		String json = arrayNode.toString();

		log.debug(json);
		return json;
	}

	/**
	 * Get List of assets from the provided JSON.
	 *
	 * @param resourceResolver  {@code ResourceResolver}
	 * @param resourceResolver  {@code ResourceResolver}
	 * @return ObjectMapper
	 * @throws JsonProcessingException 
	 */
	public static List<AssetData> getAssetData(String json) {
		final ObjectMapper objectMapper = new ObjectMapper();
		List<AssetData> langList = null;
		try {
			langList = objectMapper.readValue(json, new TypeReference<List<AssetData>>(){});
			log.info("Printing List items" + langList.size());
			langList.forEach(x -> log.info(x.getPath()));
			return langList;
		} catch (JsonParseException e) {
			log.error(e.getMessage()); // TO-DO add custom exception handling for the project.
		} catch (JsonMappingException e) {
			log.error(e.getMessage()); // TO-DO add custom exception handling for the project.
		} catch (IOException e) {
			log.error(e.getMessage()); // TO-DO add custom exception handling for the project.
		}
		return langList;
	}


	/**
	 * Find all assets under the contentPath that have type dam:Asset
	 *
	 * @param session the resource resolver used to find the Assets to move.
	 * @param builder      Query Builder.
	 * @param contentPath      the DAM contentPath which the task covers.
	 * @param assetLimit      Limit of search results.
	 * @return the json string of assets from the given content path.
	 */
	public static String  findAssets(Session session, QueryBuilder builder, String contentPath, int assetLimit) {
		log.debug("Inside findAssets method");
		Map<String, String> params = new HashMap<String, String>();
		params.put("path", contentPath);
		params.put("type", "dam:Asset");
		params.put("p.limit", String.valueOf(assetLimit));
		Query query = builder.createQuery(PredicateGroup.create(params), session);

		SearchResult result = query.getResult();
		log.debug("Result count:" + result.getHits().size());

		ObjectMapper mapper = new ObjectMapper();
		ArrayNode arrayNode = mapper.createArrayNode();

		for(Hit hit : result.getHits()) {
			try {
				Asset asset = hit.getResource().adaptTo(Asset.class);
				if(null != asset && StringUtils.contains(asset.getMimeType(), "image")) { // Added this check to avoid ContentFragments and restrict to image mimetypes only.
					ObjectNode assetJSON = mapper.createObjectNode();
					assetJSON.put("title", StringUtils.defaultString(asset.getMetadataValue("dc:title"), asset.getName()));
					assetJSON.put("path", asset.getPath());
					assetJSON.put("name", asset.getName());
					assetJSON.put("format", asset.getMimeType());
					arrayNode.addAll(Arrays.asList(assetJSON));
				}
			} catch (RepositoryException e) {
				log.error(e.getMessage());  // TO-DO add custom exception handling for the project.
			}			
		}
		String json = arrayNode.toString();
		log.debug(json);
		return json;
	}
}

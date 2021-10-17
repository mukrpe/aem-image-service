package com.aem.image.core.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aem.image.core.constants.ImageUtilConstants;
import com.day.cq.dam.api.Asset;
import com.day.cq.dam.api.DamConstants;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class ImageServiceUtil {
	
	private static final Logger log = LoggerFactory.getLogger(ImageServiceUtil.class);
	
	/**
     * Get all child assets from the specified path as JSON
     *
     * @param resourceResolver  {@code ResourceResolver}
     * @param resourceResolver  {@code ResourceResolver}
     * @return                  {@code Iterator}
	 * @throws JsonProcessingException 
     */
	public static String getAssetsJSON(ResourceResolver resolver, String path, int assetLimit) throws JsonProcessingException{
		
		log.info("Inside getAssetsJSON with path: "+ path);
		log.info("UserId for the resolver"+ resolver.getUserID());
		Iterator<Resource> allChildAssets = resolver.findResources(
				  String.format(ImageUtilConstants.IMAGE_GET_SQL, path),javax.jcr.query.Query.JCR_SQL2);
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode assetJSON = mapper.createObjectNode();
		while(allChildAssets.hasNext()) {
			Asset asset = allChildAssets.next().adaptTo(Asset.class);
			if(null != asset && StringUtils.contains(asset.getMimeType(), "image")) {
	            ObjectNode props = mapper.createObjectNode();
	            props.put("dc:title", StringUtils.defaultString(asset.getMetadataValue("dc:title")));
	            props.put("name", asset.getName());
	            props.put("path", asset.getPath());
	            props.put("dc:format", asset.getMimeType());
	            assetJSON.set(asset.getName(), props);
			}
		}
		log.info(assetJSON.toString());
		log.info(mapper.writeValueAsString(assetJSON));
		return assetJSON.toString();
	}
	
	/**
     * Find all assets under the contentPath that have type dam:Asset.
     *
     * @param session the resource resolver used to find the Assets to move.
     * @param builder      Query Builder.
     * @param contentPath      the DAM contentPath which the task covers.
     * @param contentPath      the DAM contentPath which the task covers.
     * @return the json string of assets from the given content path.
     */
	public static void  findAssets(Session session, QueryBuilder builder, String contentPath) {
	    	log.info("Inside findAssets method");
	    	
	    	Map<String, String> params = new HashMap<String, String>();
	    	params.put("path", contentPath);
	    	params.put("type", DamConstants.DAM_ASSET_NT);
	    	params.put("p.limit", "-1");
	        Query query = builder.createQuery(PredicateGroup.create(params), session);

	        SearchResult result = query.getResult();
	        log.info("Result count:" + result.getHits().size());
	        for(Hit hit : result.getHits()) {
				try {
					String path = hit.getPath();
					log.info("Path in result: " + path);
				} catch (RepositoryException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}			
			}
	    }
}

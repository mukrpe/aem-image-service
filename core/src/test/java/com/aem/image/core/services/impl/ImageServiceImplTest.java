/**
 * 
 */
package com.aem.image.core.services.impl;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.aem.image.core.util.ImageServiceUtil;
import com.day.cq.dam.api.Asset;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

/**
 * @author pendyala
 *
 */

@ExtendWith(AemContextExtension.class)
class ImageServiceImplTest {
	
	  private final AemContext context = new AemContext();

	  private ImageServiceUtil imageServiceUtil = new ImageServiceUtil();

	  @Test
	  public void testContentLoad() {
			context.load().json("/assets-activities.json", "/content/dam/we-retail/en/activities");
		    Resource resource = context.resourceResolver().getResource("/content/dam/we-retail/en/activities");
		    Asset asset = resource.getChild("running/marathon-shoes.jpg").adaptTo(Asset.class);
		    assertTrue(asset.getName().contentEquals("marathon-shoes.jpg"));
	    
	  }
	  
	  
	  @Test
	  public void testGetAssetData() {
	/*	  ObjectMapper objectMapper = new ObjectMapper();
		  ArrayNode arrayNode = objectMapper.createArrayNode();
		  ObjectNode assetJSON = objectMapper.createObjectNode();
		  assetJSON.put("title", "Marathon Shoes");
		  assetJSON.put("name", "marathon-shoes.jpg");
		  assetJSON.put("path", "/content/dam/we-retail/en/activities/running/marathon-shoes.jpg");
		  assetJSON.put("format", "image/jpeg");
		  arrayNode.addAll(Arrays.asList(assetJSON));
		  
		  String json = arrayNode.toString();
		  
		  List<AssetData> list = imageServiceUtil.getAssetData(json);
		  assertTrue(list.size() == 1); */
	  }
	  
	  
	  
}

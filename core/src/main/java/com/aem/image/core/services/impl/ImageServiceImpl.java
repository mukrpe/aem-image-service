package com.aem.image.core.services.impl;

import static org.apache.sling.api.resource.ResourceResolverFactory.SUBSERVICE;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.jcr.Session;

import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aem.image.core.services.AssetData;
import com.aem.image.core.services.ImageService;
import com.aem.image.core.util.ImageServiceUtil;
import com.day.cq.search.QueryBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * Image Service Impl.
 *
 * <p>Is the implementation of the Image Service interface.</p>
 *
 * <p>This service returns the images requested via the OSGI Config.</p>
 */
@Component(immediate = true,
name = "Image Service Configuration Service",
service = ImageService.class,
configurationPolicy = ConfigurationPolicy.REQUIRE,
configurationPid = "com.aem.image.core.services.impl.ImageServiceImpl"
		)
@Designate(ocd = ImageServiceImpl.Config.class)
public class ImageServiceImpl implements ImageService {

	@Reference
	protected ResourceResolverFactory resourceResolverFactory;

	private Session session;

	@Reference
	private QueryBuilder builder;

	public List<AssetData> assetList; 

	
	/**
	 * Service user mapping. Service is mapped to image-service-user(service user)
	 */
	private static final Map<String, Object> SERVICE_MAP = Collections.singletonMap(SUBSERVICE, "getResourceResolver");

	private static final Logger log = LoggerFactory.getLogger(ImageServiceImpl.class);

	@ObjectClassDefinition(
			name = "AEM - Image Service",
			description = "OSGi Service to configure the root path for images in DAM"
			)
	@interface Config {

		@AttributeDefinition(
				name = "Path in AEM DAM",
				description = "Configure path for the assets to be rendered on the page. Default value: /content/dam/we-retail/en/activities"
				)
		String rootPath() default "/content/dam/we-retail/en/activities";

		@AttributeDefinition(
				name = "Enable this config",
				description = "Select to enable this config"
				)
		boolean isEnabled() default true;

		@AttributeDefinition(
				name = "Asset Count",
				description = "Configure the number of assets this service needs to use. Added this config to avoid scenarios of user entering root path /content/dam and cause performance issue to the server. Default value:12"
				)
		int assetLimit() default 12;

	}

	private String rootPath;

	private boolean isEnabled;

	private int assetLimit;

	@Activate
	protected void activate(Config config) {
		this.rootPath = config.rootPath();
		this.isEnabled = config.isEnabled(); 
		this.assetLimit = config.assetLimit();
		log.info("Enabled ImageServiceImpl for DAM location [ {} ]", String.join(", ", this.rootPath));

	}

	@Deactivate
	protected void deactivate() {
		log.info("ImageServiceImpl has been deactivated!");
	}
	
	/**
	 *This method return the string representation of the JSON object. This can be used by any future servlet to consume.
	 *@return json JSON in string representation.
	 */
	public String getImageDataJSON() {

		try {
			ResourceResolver resolver = resourceResolverFactory.getServiceResourceResolver(SERVICE_MAP);
			session = resolver.adaptTo(Session.class);
			log.info("Calling JSON Generator");
			return 	ImageServiceUtil.findAssets(session, builder, rootPath, assetLimit);

		} catch (LoginException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}finally {
			if(session != null) {
				session.logout();
			}
		}
		return "Somthing bad happened.";
	}	

	/**
	 * Retrieve the asset information and return a List object of Assets.
	 * @return
	 */
	public List<AssetData> getAssetData() {	    	
		try {
			ResourceResolver resolver = resourceResolverFactory.getServiceResourceResolver(SERVICE_MAP);
			session = resolver.adaptTo(Session.class);
			assetList = ImageServiceUtil.getAssetData(ImageServiceUtil.findAssets(session, builder, rootPath, assetLimit));
			log.info("Total assets found: " + assetList.size());
		} catch (LoginException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		} 
		log.info(assetList.toString());
		return assetList;
	}	 
}

package com.aem.image.core.services.impl;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;

import java.util.ArrayList;
import java.util.List;

import javax.jcr.Session;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.day.cq.dam.api.Asset;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.SearchResult;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

/**
 * @author pendyala
 *
 */
@ExtendWith({ AemContextExtension.class, MockitoExtension.class })
class ImageServiceImplTest {

	private final AemContext aemContext = new AemContext(ResourceResolverType.JCR_MOCK);

	@Mock
	private QueryBuilder queryBuilder;

	@Mock
	private ResourceResolver resolver;

	@Mock
	private Query query;

	@Mock
	private SearchResult searchResult;

	private List<Resource> queryResults = new ArrayList<>(); // Populate this collection with actual results/test assets, to be checked in response JSON String


	@Test
	public void testfindAssets() {
		/* Query Builder is adapted from Resource Resolver */
		Asset asset = aemContext.create().asset("/content/dam/sample1.gif",
				100, 50, "image/gif");
		queryResults.add(asset.adaptTo(Resource.class));
		asset = aemContext.create().asset("/content/dam/sample2.gif",
				100, 50, "image/gif");
		queryResults.add(asset.adaptTo(Resource.class));
		aemContext.registerAdapter(ResourceResolver.class, QueryBuilder.class, queryBuilder);
		lenient().when(queryBuilder.createQuery(any(PredicateGroup.class), any(Session.class))).thenReturn(query);
		lenient().when(query.getResult()).thenReturn(searchResult);
		lenient().when(searchResult.getResources()).thenReturn(queryResults.iterator());
		assertTrue(queryResults.size()==2);
	}

}

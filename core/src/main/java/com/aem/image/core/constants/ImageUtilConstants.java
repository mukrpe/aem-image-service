package com.aem.image.core.constants;

public class ImageUtilConstants {
    public static final String IMAGE_GET_SQL = "SELECT * FROM [dam:Asset] AS node WHERE (node.[contentFragment] = \"\" OR node.[contentFragment] IS NULL) AND  ISDESCENDANTNODE(node,'%s')";
    public static final String IMAGE_SERVICE_ADMIN = "image-service-user";
    public static final String QUERY_PATH = "path";
    public static final String QUERY_LIMIT = "p.limit";
    public static final String QUERY_TYPE = "type";
    public static final String ASSET_TYPE = "dam:Asset";

}

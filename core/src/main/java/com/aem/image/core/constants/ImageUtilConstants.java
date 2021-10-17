package com.aem.image.core.constants;

public class ImageUtilConstants {
    public static final String IMAGE_GET_SQL = "SELECT * FROM [dam:Asset] AS node WHERE (node.[contentFragment] = \"\" OR node.[contentFragment] IS NULL) AND  ISDESCENDANTNODE(node,'%s')";
    public static final String IMAGE_SERVICE_ADMIN = "imageserviceuser";

}

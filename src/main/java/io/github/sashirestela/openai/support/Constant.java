package io.github.sashirestela.openai.support;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Constant {

  public final static List<String> HTTP_METHODS = Arrays.asList("GET", "POST", "PUT", "DELETE");
  public final static List<String> PARAMETER_TYPES = Arrays.asList("Path", "Query", "Body");
  public final static List<String> MULTIPART_AS_LIST = Arrays.asList("Multipart");

  public final static String JSON_EMPTY_CLASS = "{\"type\":\"object\",\"properties\":{}}";

  public final static String HEADER_CONTENT_TYPE = "Content-Type";

  public final static String TYPE_APP_JSON = "application/json";
  public final static String TYPE_MULTIPART = "multipart/form-data";

  public final static String BOUNDARY_TITLE = "; boundary=";
  public final static String BOUNDARY_VALUE = new BigInteger(256, new Random()).toString();

  public final static String DEF_ANNOT_ATTRIB = "value";

  public final static String REGEX_PATH_PARAM_URL = "\\{(.*?)\\}";

}
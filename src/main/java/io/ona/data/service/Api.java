package io.ona.data.service;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.google.gson.Gson;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * This class exposes services offered by this application as REST endpoints
 * 
 * @author nelson.okello
 * 
 */
@Path("/")
public class Api {

  private @Value("${response.success.code}")
  int RESPONSE_CODE_SUCCESS;

  private @Value("${response.success.msg}")
  String RESPONSE_MSG_SUCCESS;

  private @Autowired
  FileProcessingService fileProcessingService;

  @Produces({ "application/x-javascript", MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
  @Path("/test")
  @GET
  public Response test(@QueryParam("format") final String format,
      @QueryParam("callback") final String callback) {

    Map<String, Object> _resp = new HashMap<String, Object>();
    _resp.put("responseCode", RESPONSE_CODE_SUCCESS);
    _resp.put("message", RESPONSE_MSG_SUCCESS);
    _resp.put("data", "This is just a test. It was successful");

    Response response = null;
    if (format != null && format.equals("xml")) {
      XStream xStream = new XStream(new DomDriver());
      xStream.alias("items", java.util.Set.class);

      if (callback != null) {
        response = Response.status(Status.OK).type("application/x-javascript")
            .entity(xStream.toXML(_resp)).build();
      } else {
        response = Response.status(Status.OK).type("application/xml").entity(xStream.toXML(_resp))
            .build();
      }

    } else {
      if (callback != null) {
        response = Response.status(Status.OK).type("application/x-javascript")
            .entity(new Gson().toJson(_resp)).build();
      } else {
        response = Response.status(Status.OK).type("application/json")
            .entity(new Gson().toJson(_resp)).build();
      }
    }

    return response;

  }

  /**
   * Retrieves the resource at the specified URL, apply the business logic and serves the response
   * in the format specified or as JSON if a format is not specified
   * 
   * @param url
   *          URL from which to retrieve a resource
   * @param type
   *          The type of resource to serve to the requesting client
   * @param format
   *          The format in which to serve the response. JSON is served by default
   * @param callback
   *          If response is requested using JSONP, the callback function name is included in the
   *          request
   * @return Returns a response of the specified type in the specified format
   */
  @Produces({ "application/x-javascript", MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
  @Path("/getData")
  @GET
  public Response get(@QueryParam("url") final String url,
      @QueryParam("format") final String format, @QueryParam("callback") final String callback) {

    Map<String, Object> _resp = new HashMap<String, Object>();
    _resp.put("responseCode", RESPONSE_CODE_SUCCESS);
    _resp.put("message", RESPONSE_MSG_SUCCESS);
    _resp.put("data", fileProcessingService.process(url));

    Response response = null;
    if (format != null && format.equals("xml")) {
      XStream xStream = new XStream(new DomDriver());
      xStream.alias("items", java.util.Set.class);

      if (callback != null) {
        response = Response.status(Status.OK).type("application/x-javascript")
            .entity(xStream.toXML(_resp)).build();
      } else {
        response = Response.status(Status.OK).type("application/xml").entity(xStream.toXML(_resp))
            .build();
      }

    } else {
      if (callback != null) {
        response = Response.status(Status.OK).type("application/x-javascript")
            .entity(new Gson().toJson(_resp)).build();
      } else {
        response = Response.status(Status.OK).type("application/json")
            .entity(new Gson().toJson(_resp)).build();
      }
    }

    return response;
  }
}

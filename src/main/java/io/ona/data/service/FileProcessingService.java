package io.ona.data.service;

import io.ona.data.utils.MapUtil;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.stereotype.Service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * This class is responsible for retrieving a file at a specified URL, parse the file and perform
 * the necessary business logic on the file
 * 
 * @author nelson.okello
 * 
 */
@Service(value = "fileProcessingService")
public class FileProcessingService {

  private JsonParser jsonParser = new JsonParser();

  private static final String PROCESS_FAILED_MSG = "Data processing failed";

  /**
   * Retrieves and processes the file at the specified URL
   * 
   * @param url
   * @return
   */
  public synchronized Map<String, Object> process(final String url) {
    String fileString = "";
    Map<String, Object> r = new HashMap<String, Object>();

    try {
      URL fileUrl = new URL(url);
      BufferedReader in = new BufferedReader(new InputStreamReader(fileUrl.openStream()));

      String inputLine;
      while ((inputLine = in.readLine()) != null) {
        fileString = fileString + inputLine;
        System.out.println(inputLine);
      }

      if (fileString != null && fileString.trim().length() > 0) {
        JsonArray dataArray = jsonParser.parse(fileString).getAsJsonArray();
        r.put("numWaterPoints", getFunctionalPoints(dataArray));
        r.put("pointsPerCommunity", getPointsPerCommunity(dataArray));
        r.put("rankByBrokenPoints", getRankByBrokenPoints(dataArray));
      }

    } catch (Exception e) {
      System.out.println(e.getMessage() + "\n" + ExceptionUtils.getStackTrace(e) + "\n\n");
      r.put("error", PROCESS_FAILED_MSG);
    }

    return r;
  }

  /**
   * Aggregates functional water points
   * 
   * @param dataArray
   *          Source of data to aggregate
   * @return Returns the total number of water points
   * @throws Exception
   */
  public long getFunctionalPoints(final JsonArray dataArray) throws Exception {
    long functionalPoints = 0;
    for (JsonElement e : dataArray) {
      JsonObject object = e.getAsJsonObject();
      String condition = object.get("water_point_condition").getAsString();
      if (condition != null && condition.toUpperCase().equals("FUNCTIONING")) {
        ++functionalPoints;
      }
    }

    return functionalPoints;
  }

  /**
   * Counts the number of water points per community and returns it as a map of <community, number
   * of water points>
   * 
   * @param dataArray
   *          Source of data to use
   * @return Returns a map of community and the corresponding number of water points
   * @throws Exception
   */
  public Map<String, Long> getPointsPerCommunity(final JsonArray dataArray) throws Exception {
    Map<String, Long> communities = new HashMap<String, Long>();
    for (JsonElement e : dataArray) {
      JsonObject object = e.getAsJsonObject();
      String community = object.get("communities_villages").getAsString();

      Long currentCount = communities.get(community);
      if (currentCount == null) {
        communities.put(community, 1l);
      } else {
        ++currentCount;
        communities.put(community, currentCount);
      }

    }

    return communities;
  }

  /**
   * Calculates percentage of broken water points by community, then returns the <community, broken
   * points percentage>
   * 
   * @param dataArray
   *          Data source to use in the calculation
   * @return Return a map of <community, broken water points percentage>
   * @throws Exception
   */
  public Map<String, Double> getRankByBrokenPoints(final JsonArray dataArray) throws Exception {
    Map<String, Long> pointsPerCommunity = getPointsPerCommunity(dataArray);
    Map<String, Long> brokenPoints = new HashMap<String, Long>();
    Map<String, Double> brokenPointsPct = new HashMap<String, Double>();

    // Count broken water points
    for (JsonElement e : dataArray) {
      JsonObject object = e.getAsJsonObject();
      String community = object.get("communities_villages").getAsString();
      String functioning = object.get("water_functioning").getAsString();

      if (functioning != null && !functioning.toUpperCase().equals("YES")) {
        Long currentCount = brokenPoints.get(community);
        if (currentCount == null) {
          brokenPoints.put(community, 1l);
        } else {
          ++currentCount;
          brokenPoints.put(community, currentCount);
        }
      }
    }

    // Calculate percentages
    for (Map.Entry<String, Long> e : brokenPoints.entrySet()) {
      String community = e.getKey();
      Long broken = e.getValue();
      Long total = pointsPerCommunity.get(community);
      double pct = (double) broken / (double) total;
      brokenPointsPct.put(community, pct);
    }

    return MapUtil.sortByValue(brokenPointsPct);
  }
}

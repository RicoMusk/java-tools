/*
 Copyright (C) 2018-2021 YouYu information technology (Shanghai) Co., Ltd.
 <p>
 All right reserved.
 <p>
 This software is the confidential and proprietary
 information of YouYu Company of China.
 ("Confidential Information"). You shall not disclose
 such Confidential Information and shall use it only
 in accordance with the terms of the contract agreement
 you entered into with YouYu inc.
*/
package com.ricoandilet.commons.utils;

import com.alibaba.excel.EasyExcel;
import com.amazonaws.util.json.Jackson;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ricoandilet.commons.model.AttachInfo;
import com.ricoandilet.commons.model.FullInfo;
import com.ricoandilet.commons.model.FullInfoExcel;
import org.apache.commons.compress.utils.Lists;
import org.apache.logging.log4j.util.Strings;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.util.CollectionUtils;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * @author: rico
 * @date: 2023/11/6
 */
public class AnyTimeDataUtil {

  public static List<FullInfo> fetchList(String url) {

    List<FullInfo> fullInfoList = new ArrayList<>();
    Document document = HtmlUtil.getDocumentByUrl(url);
    Elements elements = document.select(".theme-location-item");
    for (Element element : elements) {
      String title = element.selectFirst("h3.t-title").text();
      String price = element.selectFirst(".t-price b").text();
      Element addr = element.selectFirst(".t-addr");
      String[] lines = addr.html().replace("\n", "").split("<br>");
      String address = lines[0];
      String[] other = lines[1].split(",");
      String[] state = other[1].trim().split(" ");
      String href = element.selectFirst("a.gt-plan").attr("href");
      // add new element to list.
      FullInfo fullInfo = new FullInfo();
      fullInfo.setTitle(title);
      fullInfo.setPrice(price);
      fullInfo.setAddr(addr.text());
      fullInfo.setStreet(address);
      fullInfo.setCity(other[0]);
      fullInfo.setState(state[0]);
      fullInfo.setZipcode(state[1]);
      fullInfo.setHref(href);
      fullInfoList.add(fullInfo);
    }
    return fullInfoList;
  }

  public static AttachInfo getMeta(FullInfo fullInfo) {

    // log start
    System.out.println(
        String.format("AnyTimeDataUtil.getMeta:  = %s, ", Jackson.toJsonString(fullInfo)));
    // AttachInfo attachInfo = getRespDataByFirstLogic(fullInfo);
    AttachInfo attachInfo = getRespData(fullInfo);
    // log end

    return attachInfo;
  }

  private static AttachInfo getRespData(FullInfo fullInfo) {

    try {
      String street = fullInfo.getStreet().replace(" ", "+");
      String city = fullInfo.getCity();
      String state = fullInfo.getState();
      String zipcode = fullInfo.getZipcode();

      String apiUrl =
          "https://www.demo.com/street-address?"
              .concat("key=21102174564513388")
              .concat("&&agent=smarty+(website:demo%2Fsingle-address%40latest)")
              .concat("&match=enhanced")
              .concat("&candidates=5")
              .concat("&geocode=true")
              .concat("&license=us-rooftop-geocoding-cloud")
              .concat("&street=")
              .concat(street)
              .concat("&street2=")
              //.concat("&city=")
              //.concat(city.replace(" ", "+"))
              .concat("&state=")
              .concat(state)
              .concat("&zipcode=")
              .concat(zipcode);

      HttpClient httpClient = HttpClient.newHttpClient();
      HttpRequest httpRequest =
          HttpRequest.newBuilder()
              .uri(URI.create(apiUrl))
              .header("authority", "us-street.api.smarty.com")
              .header("accept", "application/json, text/plain, */*")
              .header("origin", "https://www.smarty.com")
              .header("referer", "https://www.smarty.com")
              .header("accept-language", "en-US,en;q=0.9,zh-CN;q=0.8,zh;q=0.7")
              .headers(
                  "user-agent",
                  "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/118.0.0.0 Safari/537.36")
              .header(
                  "sec-ch-ua",
                  "\"Chromium\";v=\"118\", \"Google Chrome\";v=\"118\", \"Not=A?Brand\";v=\"99\"")
              .build();
      HttpResponse<String> response =
          httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
      if (response.statusCode() == 200) {

        AttachInfo attachInfo = new AttachInfo();
        // Parse JSON response using Jackson
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(response.body());
        // Extract specific fields from the JSON
        JsonNode data = jsonNode.get(0);
        if (Objects.nonNull(data.get("metadata"))) {
          JsonNode metadata = data.get("metadata");
          attachInfo.setRdi(metadata.get("rdi") != null ? metadata.get("rdi").asText() : "");
        }
        if (Objects.nonNull(data.get("analysis"))) {
          JsonNode analysis = data.get("analysis");
          attachInfo.setCmra(
              analysis.get("dpv_cmra") != null ? analysis.get("dpv_cmra").asText() : "");
        }
        return attachInfo;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  private static AttachInfo getRespDataByFirstLogic(FullInfo fullInfo) {
    try {
      String street = fullInfo.getStreet();
      String city = fullInfo.getCity();
      String state = fullInfo.getState();
      String zipcode = fullInfo.getZipcode();

      String apiUrl = "https://api.yourdomain.com/demo/v1/verify";
      Map<Object, Object> formData = new HashMap<>();
      formData.put("primary_line", street);
      formData.put("city", city);
      formData.put("state", state);
      formData.put("zip", zipcode);
      HttpClient httpClient = HttpClient.newHttpClient();
      HttpRequest httpRequest =
          HttpRequest.newBuilder()
              .uri(URI.create(apiUrl))
              .header("authority", "api.firstlogic.com")
              .header("accept", "application/json, text/plain, */*")
              .header("origin", "https://firstlogic.com")
              .header("referer", "https://firstlogic.com")
              .header("accept-language", "en-US,en;q=0.9,zh-CN;q=0.8,zh;q=0.7")
              .headers(
                  "user-agent",
                  "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/118.0.0.0 Safari/537.36")
              .header(
                  "sec-ch-ua",
                  "\"Chromium\";v=\"118\", \"Google Chrome\";v=\"118\", \"Not=A?Brand\";v=\"99\"")
              .POST(HttpRequest.BodyPublishers.ofString(mapToFormData(formData)))
              .build();
      HttpResponse<String> response =
          httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
      if (response.statusCode() == 200) {
        AttachInfo attachInfo = new AttachInfo();
        // Parse JSON response using Jackson
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(response.body());

        if (Objects.nonNull(jsonNode)) {
          JsonNode address_components = jsonNode.get("address_components");
          JsonNode dpv = jsonNode.get("dpv");
          attachInfo.setRdi(address_components.get("rdi").asText());
          attachInfo.setCmra(dpv.get("dpv_cmra").asText());
        }
        // Extract specific fields from the JSON
        return attachInfo;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  private static String mapToFormData(Map<Object, Object> data) {
    StringBuilder formData = new StringBuilder();
    for (Map.Entry<Object, Object> entry : data.entrySet()) {
      if (formData.length() > 0) {
        formData.append("&");
      }
      formData.append(URLEncoder.encode(entry.getKey().toString(), StandardCharsets.UTF_8));
      formData.append("=");
      formData.append(URLEncoder.encode(entry.getValue().toString(), StandardCharsets.UTF_8));
    }
    return formData.toString();
  }

  public static List<FullInfo> getUnfinished(List<FullInfo> errorFullInfoList){

    String key = "fullData";
    String newKey = "newFullData";
    List<FullInfo> srcFullInfoList = RedisUtil.get(key, 0);
    List<FullInfo> finishedFullInfoList = RedisUtil.get(newKey, 0);

    // get all finished data
    List<String> rdiList = new ArrayList<>(8);
    if (!CollectionUtils.isEmpty(finishedFullInfoList)){
      rdiList = finishedFullInfoList.stream()
              .filter(f-> Strings.isNotEmpty(f.getRdi()))
              .map(FullInfo::getZipcode)
              .collect(Collectors.toList());
    }
    // get all unfinished data
    List<String> finalRdiList = rdiList;
    List<FullInfo> unfinishedData = srcFullInfoList.stream()
            .filter(f-> !finalRdiList.contains(f.getZipcode()))
            .collect(Collectors.toList());

    // remove error data
    if (!CollectionUtils.isEmpty(errorFullInfoList)){
      List<String> errorZipcode = Optional.ofNullable(errorFullInfoList.stream().map(FullInfo::getZipcode).collect(Collectors.toList()))
                      .orElse(new ArrayList<>());
      return unfinishedData.stream().filter(f-> !errorZipcode.contains(f.getZipcode())).collect(Collectors.toList());
    }
    return unfinishedData;
  }

  public static void main(String[] args) {

    String key = "fullData";
    String newKey = "newFullData";
    List<FullInfo> srcFullInfoList = RedisUtil.get(key, 0);
    List<FullInfo> errorFullInfoList = new ArrayList<>();

    int retry = 0;
    Random random_lv1 = new Random();
    while (true) {
      // get all fullInfoData
      List<FullInfo> finishedFullInfo = RedisUtil.get(newKey, 0);
      List<String> rdiList = new ArrayList<>(8);
      if (!CollectionUtils.isEmpty(finishedFullInfo)){
        rdiList = finishedFullInfo.stream().filter(f-> Strings.isNotEmpty(f.getRdi())).map(FullInfo::getZipcode).collect(Collectors.toList());
      }
      // get unfinished fullInfoData
      List<String> errorZipcode =
          Optional.ofNullable(
                  errorFullInfoList.stream().map(FullInfo::getZipcode).collect(Collectors.toList()))
              .orElse(new ArrayList<>());

      List<String> finalRdiList = rdiList;
      List<FullInfo> unfinishedData = srcFullInfoList.stream()
                      .filter(f-> !finalRdiList.contains(f.getZipcode()))
                      .collect(Collectors.toList());

      unfinishedData = unfinishedData.stream().filter(f-> !errorZipcode.contains(f.getZipcode())).collect(Collectors.toList());

      try {
        int seconds = (5+random_lv1.nextInt(5));
        System.out.println(String.format("while sleep time = %s", seconds));
        Thread.sleep(seconds * 1000);

        Random random = new Random();
        for (FullInfo fullInfo : unfinishedData) {

          int fSeconds = (random.nextInt(5) + 3);
          System.out.println(String.format("foreach sleep time = %s", fSeconds));
          Thread.sleep(1000 * fSeconds);
          //
          AttachInfo attachInfo = AnyTimeDataUtil.getMeta(fullInfo);
          if (Objects.nonNull(attachInfo)) {
            fullInfo.setRdi(attachInfo.getRdi());
            fullInfo.setCmra(attachInfo.getCmra());
            RedisUtil.set(newKey, fullInfo);
          }else {
            errorFullInfoList.add(fullInfo);
          }
          // output data
          System.out.println(
              String.format(
                  "%s, get data:address_components = %s, rdi = %s, dpv_cmra = %s",
                  fullInfo.getAddr(),
                  Jackson.toJsonString(attachInfo),
                  attachInfo.getRdi(),
                  attachInfo.getCmra()));
        }

        String filePath = "anytime.xlsx";
        List<FullInfo> finishedData = RedisUtil.get(newKey, 0);
        // Data to be exported
        List<FullInfoExcel> dataList = new ArrayList<>();
        for (FullInfo fullInfo : finishedData) {
          FullInfoExcel fullInfoExcel = new FullInfoExcel();
          fullInfoExcel.setTitle(fullInfo.getTitle());
          fullInfoExcel.setPrice(fullInfo.getPrice());
          fullInfoExcel.setAddr(fullInfo.getAddr());
          fullInfoExcel.setStreet(fullInfo.getStreet());
          fullInfoExcel.setCity(fullInfo.getCity());
          fullInfoExcel.setState(fullInfo.getState());
          fullInfoExcel.setZipcode(fullInfo.getZipcode());
          fullInfoExcel.setRdi(fullInfo.getRdi());
          fullInfoExcel.setCmra(fullInfo.getCmra());
          dataList.add(fullInfoExcel);
        }
        // Export data to Excel
        EasyExcel.write(filePath, FullInfoExcel.class).sheet("Sheet1").doWrite(dataList);

      } catch (Exception e) {
        System.out.println(e);
      }
    }
  }
}

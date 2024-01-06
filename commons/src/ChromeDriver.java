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

import com.google.common.collect.Lists;
import com.ricoandilet.commons.model.FullInfo;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @author: rico
 * @date: 2023/11/16
 **/
public class ChromeDriver {

    public static WebDriver initDriver() {

        // Setting system properties of ChromeDriver
        System.setProperty(
                "webdriver.chrome.driver", "D:\\other\\spring-tools\\chromedriver.exe");

        // Creating an object of ChromeDriver
       WebDriver driver = new org.openqa.selenium.chrome.ChromeDriver();

       driver.manage().window().maximize();

        return driver;
    }

    public static void ten(List<FullInfo> infoList) {
        //
        WebDriver driver = initDriver();
        // meta
        driver.get("https://www.demo.com/products/single-address/");

        String html = driver.getPageSource();
        List<WebElement> webElement = driver.findElements(By.cssSelector("button"));
        WebElement button =
                driver.findElement(
                        By.xpath("//button[contains(@class, 'btn--blue')][text()='View results']"));

        WebElement address = driver.findElement(By.xpath("//input[@id=':R4bqa1i6m:']"));
        WebElement city = driver.findElement(By.id(":R1bqa1i6m:"));
        WebElement state = driver.findElement(By.id(":R1jqa1i6m:"));
        WebElement zip_code = driver.findElement(By.id(":R1rqa1i6m:"));

        try {
            Random random = new Random();
            // foreach
            for (FullInfo info : infoList) {
                int fSeconds = (random.nextInt(2) + 2);
                System.out.println(String.format("foreach sleep time = %s", fSeconds));
                Thread.sleep(1000 * fSeconds);

                // input params
                address.sendKeys(Keys.HOME, Keys.chord(Keys.SHIFT, Keys.END), info.getStreet());
                Thread.sleep(1000 * 2);
                // address.sendKeys();
                // city.clear();
                city.sendKeys(Keys.HOME, Keys.chord(Keys.SHIFT, Keys.END), info.getCity());
                Thread.sleep(1000 * 1);
                // state.clear();
                state.sendKeys(Keys.HOME, Keys.chord(Keys.SHIFT, Keys.END), info.getState());
                Thread.sleep(1000 * 2);
                // zip_code.clear();
                zip_code.sendKeys(Keys.HOME, Keys.chord(Keys.SHIFT, Keys.END), info.getZipcode());

                Thread.sleep(1000 * 1);
                button.click();
                // get data
                Thread.sleep(1000 * 2);
                List<WebElement> dataElements =
                        driver.findElements(By.xpath("//ul[@class='detailSection_detailsList__A5y7p']"));
                List<WebElement> spans = dataElements.get(0).findElements(By.xpath("//li/div/span"));

                Map<String, String> kvs = new HashMap<>();
                for (int i = 0; i < spans.size(); i++) {
                    WebElement value = spans.get(i);
                    kvs.put(value.getText(), spans.get(i + 1).getText());
                    i++;
                }
                System.out.println(String.format("meta values = %s", RedisUtil.asString(kvs)));
                String rdi = kvs.get("RDI");
                String cmra = kvs.get("CMRA");
                info.setRdi(rdi);
                info.setCmra(cmra);
                RedisUtil.set("newFullData", info);
                kvs = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // close
        driver.close();
        driver.quit();
    }

    public static void getAllData() {

        List<FullInfo> errorList = new ArrayList<>();
        while (true) {
            List<FullInfo> infoList = AnyTimeDataUtil.getUnfinished(errorList);
            try{
                for (List<FullInfo> list: Lists.partition(infoList,infoList.size()/18)) {
                    ten(list);
                }
                Random random = new Random();
                int seconds = (random.nextInt(1) + 2);
                System.out.println(String.format("while sleep time = %s", seconds));
                Thread.sleep(1000 * seconds);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

  public static void main(String[] args) {
    getAllData();
  }
}

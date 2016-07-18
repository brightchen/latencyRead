package com.example;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import redis.clients.jedis.Jedis;

/**
 * Created by sandesh on 4/6/16.
 */
public class LatencyRead
{

  private static Integer WINDOW_WIDTH = 10000;

  public static void main(String[] args) throws IOException
  {
    calculate(new Jedis(args[0]));
  }

  private static void calculate(Jedis jedis) throws IOException
  {

    Set<String> campaigns = jedis.smembers("campaigns");

    //     System.out.println(campaigns);

    //  System.out.println("-------------------------------");
    //  System.out.println("campaign window_time seen latency");

    List<Long> latencies = new ArrayList<Long>();

    long totalCount = 0;
    int totalWindow = 0;
    for (String campaign : campaigns) {

      String windows_key = jedis.hget(campaign, "windows");
      Long window_count = jedis.llen(windows_key);
      // It should start from 1, as we are dropping the data from 0th place.
      // Reason: Window is half complete and the latency calcuation will show negative numbers if the window is not closed.
      List<String> windows = jedis.lrange(windows_key, 1, window_count);

      for (String window_time : windows) {

        String window_key = jedis.hget(campaign, window_time);
        String seen = jedis.hget(window_key, "seen_count");
        String time_updated = jedis.hget(window_key, "time_updated");
        Long latency = Long.parseLong(time_updated) - Long.parseLong(window_time);

        latency = latency - WINDOW_WIDTH;

        latencies.add(latency);
        
        totalCount += Long.parseLong(seen);
      }
      totalWindow = Math.max(totalWindow, windows.size());
    }
    
    
    int[] counts = new int[10];
    System.out.println("sorted latencies:");
    Collections.sort(latencies);
    for (long latency : latencies) {
      System.out.println(latency);
      
      int latencyIndex = (int)latency/100;
      if(latencyIndex > 9) {
        latencyIndex = 9;
      }
      counts[latencyIndex]++;
    }

    int accumulateCount = 0;
    System.out.println("latency group by 100 milli second");
    for(int index = 0; index < counts.length; ++index) {
      System.out.println("index: " + index + "; range: " + 100 * index + " - " + (index+1 == counts.length ? "" :100 * (index+1)));
      System.out.println("seperate:   " + counts[index] + "; percentage: " + counts[index]*100/latencies.size());
      accumulateCount += counts[index];
      System.out.println("acculation: " + accumulateCount + "; percentage: " + accumulateCount*100/latencies.size());
      System.out.println();
    }
    
    System.out.println("total count: " + totalCount);
    System.out.println("total windows: " + totalWindow);
    System.out.println("throughput/second : " + totalCount/(--totalWindow)/(WINDOW_WIDTH/1000));
  }
}

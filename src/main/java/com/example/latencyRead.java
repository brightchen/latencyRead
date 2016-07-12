package com.example;

import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;


/**
 * Created by sandesh on 4/6/16.
 */
public class latencyRead {

    private static Integer WINDOW_WIDTH = 10000;

    public static void main(String[] args) throws IOException {

       calculate(new Jedis(args[0]));
    }

    private static void calculate(Jedis jedis) throws IOException {

        Set<String> campaigns = jedis.smembers("campaigns");

   //     System.out.println(campaigns);

      //  System.out.println("-------------------------------");
      //  System.out.println("campaign window_time seen latency");

        List<Long> latencies = new ArrayList<Long>();

        for (String campaign : campaigns) {

            String windows_key = jedis.hget(campaign, "windows");
            Long window_count = jedis.llen(windows_key);
            List<String> windows = jedis.lrange(windows_key, 1, window_count);

            for (String window_time : windows) {

                String window_key = jedis.hget(campaign, window_time);
                String seen = jedis.hget(window_key, "seen_count");
                String time_updated = jedis.hget(window_key, "time_updated");
                Long latency = Long.parseLong(time_updated) - Long.parseLong(window_time);

                latency = latency - WINDOW_WIDTH;

                latencies.add(latency);
                // System.out.println(campaign + " " + window_key + " " + window_time + " " + seen + " " + latency.toString());
                 // latencies.add(latency);
            }
        }
       // Collections.sort(latencies);

        for ( long latency: latencies) {
            System.out.println(latency);
        }

     //   System.out.println("Success: latency calculation done.");
    }
}

package com.example;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import redis.clients.jedis.Jedis;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Set;


/**
 * Created by sandesh on 4/6/16.
 */
public class latencyRead {

    public static void main(String[] args) throws IOException {

        if ( args.length != 3 ) {
            System.out.println("Usage: either one of the below ");
            System.out.println("read redis outputfile");
            System.out.println("reset redis mappingFile");
            return ;
        }

       if ( args[0] == "read" ) {
            calculate( new Jedis(args[1]), args[2] );
       }
        else {
           reset( args[1], args[2] );
       }
    }

    private static void reset(String jedis, String fileLocation) throws IOException {

        RedisHelper redisHelper = new RedisHelper();
        redisHelper.init(jedis);
        redisHelper.clear();
        redisHelper.fillDB(fileLocation);

        System.out.println("Success: Redis reset done.");
    }

    private static void calculate(Jedis jedis, String fileName) throws IOException {

        Set<String> campaigns = jedis.smembers("campaigns");

        Path filePath = new Path(fileName);

        Configuration configuration = new Configuration();
        FileSystem fs;
        fs = FileSystem.newInstance(filePath.toUri(), configuration);

        FSDataOutputStream outputStream = fs.create(filePath, true);
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));

        for (String campaign : campaigns) {

            String windows_key = jedis.hget(campaign, "windows");
            Long window_count = jedis.llen(windows_key);
            List<String> windows = jedis.lrange(windows_key, 0, window_count);

            for (String window_time : windows) {

                String window_key = jedis.hget(campaign, window_time);
                String seen = jedis.hget(window_key, "seen_count");
                String time_updated = jedis.hget(window_key, "time_updated");
                Long latency = Long.parseLong(time_updated) - Long.parseLong(window_time);

                String output = seen + " " + latency.toString() + "\n";

                System.out.println(output);
                bufferedWriter.write(output);
            }
        }

        bufferedWriter.close();

        System.out.println("Success: latency calculation done.");
    }
}

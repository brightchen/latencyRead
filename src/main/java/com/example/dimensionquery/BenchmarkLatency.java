package com.example.dimensionquery;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;


import com.google.common.collect.Lists;


public class BenchmarkLatency
{
  protected String campaignFileName = "campaigns.data";
  public static void main(String[] argvs)
  {
    BenchmarkLatency instance = new BenchmarkLatency();
    instance.run();
  }
  
  public void run()
  {
    List<Integer> latencies = Lists.newArrayList();
    try {
      List<String> campaigns = readCampaigns();
      for (String campaign : campaigns) {
        getLatencies(campaign, latencies);
        Collections.sort(latencies);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  public void getLatencies(String campaign, List<Integer> latencies)
  {
    
  }
  
  protected List<String> readCampaigns() throws IOException
  {
    List<String> campaigns = Lists.newArrayList();
    FileReader fr = new FileReader(campaignFileName);
    BufferedReader br = new BufferedReader(fr);
    campaigns.add(br.readLine().trim());
    return campaigns;
  }
}

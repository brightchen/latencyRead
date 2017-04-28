package com.example.dimensionquery;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import com.google.common.collect.Lists;

public class LatencyRetriever
{
  protected String uriString = "ws://node20.morado.com:10001/pubsub";
  protected WebsocketClientEndpoint clientEndPoint;
  
  protected final String queryFormat = "{\"type\":\"publish\",\"topic\":\"Query-AppWithDCWithoutDe\",\"data\":{\"id\":0.1,\"type\":\"dataQuery\",\"data\":{\"time\":{\"bucket\":\"10s\",\"from\":\"%d\",\"to\":\"%d\",\"latestNumBuckets\":120},\"incompleteResultOK\":true,\"keys\":{\"campaignId\":\"%s\"},\"fields\":[\"latency:MAX\"]},\"countdown\":299,\"incompleteResultOK\":true}}";
  protected String queryFormattedByTime;
  protected long beginTime;
  protected long endTime;
  
  protected List<Integer> latencies = Lists.newArrayList();
  public void init() throws URISyntaxException
  {
    // open websocket
    clientEndPoint = new WebsocketClientEndpoint(new URI(uriString));

    // add listener
    clientEndPoint.addMessageHandler(new WebsocketClientEndpoint.MessageHandler()
    {
      public void handleMessage(String message)
      {
        parseMessage(message);
      }
    });
    
    // send message to websocket
    clientEndPoint.sendMessage("{\"type\":\"subscribe\",\"topic\":\"QueryResult-AppWithDCWithoutDe.0.1\"}");
    System.out.println("subscribed topic");
  }
  
  /**
   * use current time as the end time;
   */
  public void setTime()
  {
    endTime = System.currentTimeMillis();
    beginTime = endTime - 20 * 60 * 1000;   //20 minutes
    queryFormattedByTime = String.format(queryFormat, beginTime, endTime);
  }
  
  public void getLatencies(String campaign)
  {
    clientEndPoint.sendMessage(String.format(queryFormattedByTime, campaign));
  }
  
  /**
   * parse the message and write latencies into latencies
   * @param message
   */
  protected static final String LATENCY_TAG = "\"latency:MAX\":";
  public void parseMessage(String message)
  {
    int fromIndex = 0;
    int 
    message.indexOf("\"latency:MAX\":", fromIndex);
    "latency:MAX":"32"
  }
}

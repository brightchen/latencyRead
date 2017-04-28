package com.example.dimensionquery;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Test;

public class WebSocketTest
{
  @Test
  public void test()
  {
    try {
      // open websocket
      final WebsocketClientEndpoint clientEndPoint = new WebsocketClientEndpoint(
          new URI("ws://node20.morado.com:10001/pubsub"));

      // add listener
      clientEndPoint.addMessageHandler(new WebsocketClientEndpoint.MessageHandler()
      {
        public void handleMessage(String message)
        {
          System.out.println(message);
        }
      });

      // send message to websocket
      clientEndPoint.sendMessage("{\"type\":\"subscribe\",\"topic\":\"QueryResult-AppWithDCWithoutDe.0.1\"}");
      System.out.println("subscribe topic");
      
      clientEndPoint.sendMessage("{\"type\":\"publish\",\"topic\":\"Query-AppWithDCWithoutDe\",\"data\":{\"id\":0.1,\"type\":\"dataQuery\",\"data\":{\"time\":{\"bucket\":\"10s\",\"latestNumBuckets\":10},\"incompleteResultOK\":true,\"keys\":{\"campaignId\":\"0005c563-dec5-4ec3-b3f4-fb6e084a8f26\"},\"fields\":[\"time\",\"campaignId\",\"latency:MAX\",\"latency:COUNT\",\"latency:SUM\",\"latency:AVG\"]},\"countdown\":299,\"incompleteResultOK\":true}}");
      System.out.println("publish topic");
      
      // wait 5 seconds for messages from websocket
      Thread.sleep(5000);

    } catch (InterruptedException ex) {
      System.err.println("InterruptedException exception: " + ex.getMessage());
    } catch (URISyntaxException ex) {
      System.err.println("URISyntaxException exception: " + ex.getMessage());
    }
  }

}

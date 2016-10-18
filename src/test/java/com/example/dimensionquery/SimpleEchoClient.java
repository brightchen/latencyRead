package com.example.dimensionquery;

import java.net.URI;
import java.util.concurrent.TimeUnit;

import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;
import org.junit.Test;

public class SimpleEchoClient
{
  protected String destUri = "ws://node20.morado.com:10001/pubsub";
  
  @Test
  public void test()
  {
    WebSocketClient client = new WebSocketClient();
    SimpleEchoSocket socket = new SimpleEchoSocket();
    try {
      client.start();

      URI echoUri = new URI(destUri);
      ClientUpgradeRequest request = new ClientUpgradeRequest();
      client.connect(socket, echoUri, request);
      System.out.printf("Connecting to : %s%n", echoUri);

      // wait for closed socket connection.
      socket.awaitClose(100, TimeUnit.SECONDS);
    } catch (Throwable t) {
      t.printStackTrace();
    } finally {
      try {
        client.stop();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
}

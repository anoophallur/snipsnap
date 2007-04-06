package org.snipsnap.util;

import org.apache.xmlrpc.DefaultXmlRpcTransport;
import org.apache.xmlrpc.XmlRpcClient;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.XmlRpcRequest;

import java.io.IOException;
import java.net.URL;
import java.util.Vector;

public class XmlRpcUtil {
  public static void main(String[] args) {

    try {
      URL xmlRpcUrl = new URL(new URL("http://localhost:8668"), "RPC2");
      XmlRpcClient xmlrpc = new XmlRpcClient(xmlRpcUrl);
      DefaultXmlRpcTransport transport = new DefaultXmlRpcTransport(xmlRpcUrl);
      transport.setBasicAuthentication("leo", "leo");

      Vector params = new Vector();
//      for(int n = 1; n < args.length; n++) {
//        params.add(args[n]);
//      }

      params.add("SuperCalc/Speichern von Logdatein/Schaltfläche Speichern von Logdatein");

      Object result = (Object) xmlrpc.execute(
              new XmlRpcRequest("AuthEudibamus.getModelElemByName", params));
      System.out.println("result=" + result);
    } catch (IOException e) {
      System.err.println("IOException " + e);
      e.printStackTrace();
    } catch (XmlRpcException e) {
      System.err.println("XmlRpcException " + e);
      e.printStackTrace();
    }
  }
}

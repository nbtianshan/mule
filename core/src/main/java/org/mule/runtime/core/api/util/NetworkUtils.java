/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.core.api.util;

import java.net.InetSocketAddress;

import java.net.InetAddress;
import java.net.UnknownHostException;
import javafx.util.Pair;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class NetworkUtils {

  private static final Map<Pair<String, Class>, InetAddress> addressPerHost = new ConcurrentHashMap<>();
  private static InetAddress localHost;

  private NetworkUtils() {
    // utility class only
  }

  public static InetAddress getLocalHost() throws UnknownHostException {
    if (localHost == null) {
      localHost = InetAddress.getLocalHost();
    }
    return localHost;
  }

  /**
   * Resolves a local IP for a host name.
   *
   * This method should not be used to resolve external host ips since it has a cache that can grow indefinitely.
   *
   * For performance reasons returns the ip and not the {@link java.net.InetAddress} since the {@link java.net.InetAddress}
   * performs logic each time it has to resolve the host address.
   *
   * @param host the host name
   * @return the host ip
   * @throws UnknownHostException
   */
  public static String getLocalHostIp(String host) throws UnknownHostException {
    return getLocalHostAddress(host).getHostAddress();
  }

  /**
   * Resolves a local IP for a host name.
   *
   * This method should not be used to resolve external host ips since it has a cache that can grow indefinitely.
   *
   * For performance reasons returns the ip and not the {@link java.net.InetAddress} since the {@link java.net.InetAddress}
   * performs logic each time it has to resolve the host address.
   *
   * @param host the host name
   * @return the host ip
   * @throws UnknownHostException
   */
  public static InetAddress getLocalHostAddress(String host) throws UnknownHostException {
    InetAddress ip = addressPerHost.get(host);
    if (ip == null) {
      ip = new InetSocketAddress(0).getAddress();
      addressPerHost.put(new Pair(ip.getClass(), host), ip);
    }
    return ip;
  }
}

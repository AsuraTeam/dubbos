/*
 * Copyright 1999-2011 Alibaba Group.
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.dubbo.container.jetty;

import org.mortbay.jetty.Server;
import org.mortbay.jetty.nio.SelectChannelConnector;
import org.mortbay.jetty.servlet.ServletHandler;
import org.mortbay.jetty.servlet.ServletHolder;
import org.mortbay.jetty.servlet.ServletMapping;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.Extension;
import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.dubbo.common.utils.ConfigUtils;
import com.alibaba.dubbo.common.utils.NetUtils;
import com.alibaba.dubbo.container.Container;
import com.alibaba.dubbo.container.page.PageServlet;
import com.alibaba.dubbo.container.page.ResourceServlet;

/**
 * JettyContainer. (SPI, Singleton, ThreadSafe)
 * 
 * @author william.liangf
 */
@Extension("jetty")
public class JettyContainer implements Container {

    private static final Logger logger = LoggerFactory.getLogger(JettyContainer.class);

    public static final String JETTY_PORT = "dubbo.jetty.port";

    public static final String JETTY_DIRECTORY = "dubbo.jetty.directory";

    public static final String JETTY_PAGES = "dubbo.jetty.page";

    public static final int DEFAULT_JETTY_PORT = 8080;

    private SelectChannelConnector connector;

    public void start() {
        String serverPort = ConfigUtils.getProperty(JETTY_PORT);
        int port;
        if (serverPort == null || serverPort.length() == 0) {
            port = DEFAULT_JETTY_PORT;
        } else {
            port = Integer.parseInt(serverPort);
        }
        connector = new SelectChannelConnector();
        connector.setPort(port);
        ServletHandler handler = new ServletHandler();
        
        String resources = ConfigUtils.getProperty(JETTY_DIRECTORY);
        if (resources != null && resources.length() > 0) {
            String[] directories = Constants.COMMA_SPLIT_PATTERN.split(resources);
            ResourceServlet resourceServlet = new ResourceServlet();
            resourceServlet.setResources(directories);
            ServletHolder resourceHolder = new ServletHolder(resourceServlet);
            resourceHolder.setInitOrder(1);
            handler.addServlet(resourceHolder);
            for (String directory : directories) {
                directory = directory.replace('\\', '/');
                int i = directory.lastIndexOf('/');
                String name;
                if (i >= 0) {
                    name = directory.substring(i + 1);
                } else {
                    name = directory;
                }
                ServletMapping resourceMapping = new ServletMapping();
                resourceMapping.setServletName(resourceHolder.getName());
                resourceMapping.setPathSpec("/" + name + "/*");
                handler.addServletMapping(resourceMapping);
            }
        }

        ServletHolder pageHolder = handler.addServletWithMapping(PageServlet.class, "/*");
        pageHolder.setInitParameter("pages", ConfigUtils.getProperty(JETTY_PAGES));
        pageHolder.setInitOrder(2);
        
        Server server = new Server();
        server.addConnector(connector);
        server.addHandler(handler);
        try {
            server.start();
        } catch (Exception e) {
            throw new IllegalStateException("Failed to start jetty server on " + NetUtils.getLocalHost() + ":" + port + ", cause: " + e.getMessage(), e);
        }
    }

    public void stop() {
        try {
            if (connector != null) {
                connector.close();
                connector = null;
            }
        } catch (Throwable e) {
            logger.error(e.getMessage(), e);
        }
    }

    public SelectChannelConnector getConnector() {
        return connector;
    }

}
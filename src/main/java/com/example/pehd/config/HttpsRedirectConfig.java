package com.example.pehd.config;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * HTTPS重定向配置类
 * 实现HTTP请求自动重定向到HTTPS
 */
@Configuration
public class HttpsRedirectConfig {

    /**
     * 配置Tomcat服务器，添加HTTP连接器并强制重定向到HTTPS
     * @return ServletWebServerFactory
     */
    @Bean
    public ServletWebServerFactory servletContainer() {
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory() {
            @Override
            protected void postProcessContext(Context context) {
                // 创建安全约束，要求所有请求必须使用HTTPS
                SecurityConstraint securityConstraint = new SecurityConstraint();
                securityConstraint.setUserConstraint("CONFIDENTIAL");
                
                // 应用到所有URL路径
                SecurityCollection collection = new SecurityCollection();
                collection.addPattern("/*");
                securityConstraint.addCollection(collection);
                
                // 将安全约束添加到上下文
                context.addConstraint(securityConstraint);
            }
        };
        
        // 添加HTTP连接器，用于接收HTTP请求并重定向到HTTPS
        tomcat.addAdditionalTomcatConnectors(redirectHttpConnector());
        return tomcat;
    }

    /**
     * 创建HTTP连接器，监听8080端口并重定向到HTTPS端口8443
     * @return Connector
     */
    private Connector redirectHttpConnector() {
        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        connector.setScheme("http");
        connector.setPort(8080);      // HTTP端口
        connector.setSecure(false);
        connector.setRedirectPort(8443); // HTTPS端口，与application.properties中的server.port一致
        return connector;
    }
}
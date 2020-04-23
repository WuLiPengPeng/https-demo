package com.wlp.demo.config;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TomcatConfig {

    /**
     * 此配置是基于spring boot2.0以上版本，此项目是spring boot 2.2.5版本
     * 如果spring boot版本低于2.0，配置方式可能会有不同
     * @return
     */
    @Bean
    public TomcatServletWebServerFactory tomcatServletWebServerFactory() {
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory() {
            @Override
            protected void postProcessContext(Context context) {
                //安全约束
                SecurityConstraint securityConstraint = new SecurityConstraint();
                /*
                    设置用户安全约束，可取值：
                    NONE
                    INTEGRAL
                    CONFIDENTIAL
                 */
                securityConstraint.setUserConstraint("CONFIDENTIAL");
                SecurityCollection collection = new SecurityCollection();
                //配置需要使用安全约束的匹配路径，/* 匹配所有
                collection.addPattern("/*");
                securityConstraint.addCollection(collection);
                context.addConstraint(securityConstraint);
            }
        };
        /*  添加连接器，可以有多个连接器，但是连接器端口不能相同，否则将导致端口占用，无法启动。
            重定向的端口是允许相同的

            例如：
            Connector1：访问8081，重定向到8443
            Connector2：访问8082，重定向到8443

         */
        tomcat.addAdditionalTomcatConnectors(httpConnector());
        return tomcat;
    }

    /**
     * 返回一个连接器
     * @return
     */
    public Connector httpConnector() {
        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        connector.setScheme("http");
        /*
            页面中访问http://localhost:8081/hello
            将会跳转到https://localhost:8443/hello
         */
        connector.setPort(8081);
        connector.setSecure(false);
        connector.setRedirectPort(8443);
        return connector;
    }
}

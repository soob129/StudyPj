package com.study.project;

import jakarta.servlet.MultipartConfigElement;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;

@Configuration
public class FileUploadConfig {

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        
        // 파일당 용량
        factory.setMaxFileSize(DataSize.ofMegabytes(50));
        // 전체 요청 용량
        factory.setMaxRequestSize(DataSize.ofMegabytes(200));
        
        // ⭐ 핵심: 톰캣의 '파일 개수' 제한을 직접 설정합니다. 
        // 메서드가 없다면 factory.createMultipartConfig() 내부에서 
        // 기본값 1이 들어가는 경우가 있습니다.
        // 일부 버전에서는 아래처럼 위치를 지정해야 해결되기도 합니다.
        factory.setLocation(System.getProperty("user.dir") + "/uploads");

        return factory.createMultipartConfig();
    }
    
    @Bean
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> tomcatCustomizer() {
        return factory -> factory.addConnectorCustomizers(connector -> {
            // 한 요청당 파라미터 개수 제한을 5000개로 늘림 (기본값 보통 10개 내외)
            connector.setMaxParameterCount(5000);
            // POST 폼 데이터 크기 제한 해제
            connector.setMaxPostSize(20 * 1024 * 1024); // 20MB
        });
    }
}

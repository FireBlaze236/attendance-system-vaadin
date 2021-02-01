package com.packagename.prototype1;

import com.packagename.prototype1.backend.facerec.FaceRecognizerModule;
import com.packagename.prototype1.backend.repository.UserRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * The entry point of the Spring Boot application.
 */
@SpringBootApplication (exclude = ErrorMvcAutoConfiguration.class)
@EnableJpaRepositories (basePackageClasses = UserRepository.class)
public class Application extends SpringBootServletInitializer {
    static {
        nu.pattern.OpenCV.loadLocally();
        //System.loadLibrary(org.opencv.core.Core.NATIVE_LIBRARY_NAME);
    }

    public static void main(String[] args) {

        SpringApplication.run(Application.class, args);
        FaceRecognizerModule.init();
    }

}

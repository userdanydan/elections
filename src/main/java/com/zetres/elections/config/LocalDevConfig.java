package com.zetres.elections.config;

import com.zetres.elections.aop.BackupCRUDOpAspect;
import com.zetres.elections.repos.BackupRecordsDAO;
import com.zetres.elections.repos.BackupRecordsDAOImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templateresolver.FileTemplateResolver;

import java.io.File;
import java.io.IOException;


@Configuration
@Profile("local")
@EnableAspectJAutoProxy
public class LocalDevConfig implements WebMvcConfigurer {

//    /***
//     * @param registry
//     */
//    @Override
//    public void addFormatters(FormatterRegistry registry) {
//        registry.addConverter(new Converter<String, Integer>(){
//            @Override
//            public Integer convert(String source) {
//                System.err.println(source);
//               return Integer.parseInt(source);
//            }
//        });
//    }

    public LocalDevConfig(final TemplateEngine templateEngine) throws IOException {
        File sourceRoot = new ClassPathResource("application.yml").getFile().getParentFile();
        while (sourceRoot.listFiles((dir, name) -> name.equals("gradlew")).length != 1) {
            sourceRoot = sourceRoot.getParentFile();
        }
        final FileTemplateResolver fileTemplateResolver = new FileTemplateResolver();
        fileTemplateResolver.setPrefix(sourceRoot.getPath() + "/src/main/resources/templates/");
        fileTemplateResolver.setSuffix(".html");
        fileTemplateResolver.setCacheable(false);
        fileTemplateResolver.setCharacterEncoding("UTF-8");
        fileTemplateResolver.setCheckExistence(true);

        templateEngine.setTemplateResolver(fileTemplateResolver);
    }

    @Bean
    public BackupRecordsDAO backupRecordsDAO(){
        return new BackupRecordsDAOImpl();
    }

    @Bean
    public BackupCRUDOpAspect backupCRUDOpAspect(){
        return new BackupCRUDOpAspect();
    }

}

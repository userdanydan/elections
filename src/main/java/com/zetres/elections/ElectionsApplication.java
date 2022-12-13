package com.zetres.elections;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class ElectionsApplication {
//
//    @Autowired
//    private AuditReader reader;
//
    public static void main(final String[] args) {
        SpringApplication.run(ElectionsApplication.class, args);
    }
//    @Bean
//    public CommandLineRunner loadData(){
//        return args -> {
//          var first = reader.createQuery().forEntitiesAtRevision(GeoLocalite.class,10).getResultList();
//            System.err.println(first);
//        };
//    }

}

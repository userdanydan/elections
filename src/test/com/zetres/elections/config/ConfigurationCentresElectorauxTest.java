package com.zetres.elections.config;

import com.zetres.elections.repos.BackupRecordsDAO;
import com.zetres.elections.repos.BackupRecordsDAOImpl;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class ConfigurationCentresElectorauxTest {

    @Bean
    public BackupRecordsDAO backupRecordsDAO(){
        return new BackupRecordsDAOImpl();
    }
}
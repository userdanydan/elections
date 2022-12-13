package com.zetres.elections.repos;

import com.zetres.elections.config.ConfigurationCentresElectorauxTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


@ExtendWith(SpringExtension.class)
@Import(ConfigurationCentresElectorauxTest.class)
public class BackupRecordDAOImplTest {
    @Autowired
    private BackupRecordsDAO backupRecordsDAO;

    @Value("${INSERT_RECORDS_TEST}")
    private String insertRecordsTest;

    @BeforeEach
    void setUp(){
        ((BackupRecordsDAOImpl)backupRecordsDAO).setInsertRecordsEnvVar(insertRecordsTest);
        try {
            if(Files.notExists(Paths.get(insertRecordsTest)))
                Files.createFile(Paths.get(insertRecordsTest));
            else
                Files.writeString(Paths.get(insertRecordsTest),
                        "",
                        StandardOpenOption.WRITE,
                        StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testInsertInFile(){
        String expected = "inserted;";
        backupRecordsDAO.insert("inserted");
        String actual = backupRecordsDAO.getLine(1);
        assertEquals(expected, actual);
    }

    @Test
    void testReadAllLinesFromFile(){
        var expected = List.of(
                "inserted;",
                "inserted;",
                "inserted;");
        backupRecordsDAO.insert("inserted");
        backupRecordsDAO.insert("inserted");
        backupRecordsDAO.insert("inserted");
        var actual = backupRecordsDAO.getAllRecords();
        assertEquals(expected.size(), actual.size());
        assertEquals(expected, actual);
    }

}

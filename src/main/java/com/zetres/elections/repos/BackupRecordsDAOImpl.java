package com.zetres.elections.repos;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

@Repository
public class BackupRecordsDAOImpl implements BackupRecordsDAO {
    @Value("${INSERT_RECORDS}")
    private String insertRecordsEnvVar;
    public void insert(String record) {
        try(var bw = Files.newBufferedWriter(Paths.get(insertRecordsEnvVar),
                                                          StandardOpenOption.APPEND)){
            bw.append(record).append(";").append(System.lineSeparator());
        } catch (IOException e) {
            throw new RuntimeException("Error when getting file for backup.", e);
        }
    }
    @Override
    public String getLine(int numLine) {
        try(var lines = Files.lines(Paths.get(insertRecordsEnvVar))){
            return lines
                    .skip(numLine-1)
                    .findFirst()
                    .orElseThrow();
        } catch (IOException e) {
            throw new RuntimeException("Error when getting file for baclkup records", e);
        }
    }

    @Override
    public List<String> getAllRecords() {
        try(var lines = Files.lines(Paths.get(insertRecordsEnvVar))) {
            return lines.toList();
        } catch (IOException e) {
            throw new RuntimeException("Error when getting file for baclkup records", e);
        }
    }

    public void setInsertRecordsEnvVar(String insertRecordsEnvVar) {
        this.insertRecordsEnvVar = insertRecordsEnvVar;
    }
}

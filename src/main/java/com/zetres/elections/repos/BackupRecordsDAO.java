package com.zetres.elections.repos;

import java.util.List;

public interface BackupRecordsDAO {
    void insert(String record);
    String getLine(int numLine);
    List<String> getAllRecords();
}

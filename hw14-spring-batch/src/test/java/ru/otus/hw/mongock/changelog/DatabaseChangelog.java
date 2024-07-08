package ru.otus.hw.mongock.changelog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;

@ChangeLog(order = "001")
public class DatabaseChangelog {
    @ChangeSet(order = "000", id = "dropDb", author = "ivan", runAlways = true)
    public void dropDb(MongoDatabase database) {
        database.drop();
    }
}

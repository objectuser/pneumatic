package com.surgingsystems.etl.filter.mapping;

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.surgingsystems.etl.record.Record;
import com.surgingsystems.etl.record.RecordCapture;

public class LogRejectRecordStrategy implements RejectRecordStrategy {

    private String loggerName = LogRejectRecordStrategy.class.getName();

    private Level level = Level.WARN;

    private Logger logger = LogManager.getFormatterLogger(loggerName);

    public LogRejectRecordStrategy() {
    }

    public LogRejectRecordStrategy(String loggerName) {
        this.loggerName = loggerName;
        this.logger = LogManager.getFormatterLogger(loggerName);
    }

    @PostConstruct
    public void setupLogger() {
        logger = LogManager.getFormatterLogger(loggerName);
    }

    @Override
    public void rejected(Record record) {
        logger.log(level, "Record was rejected: %s", RecordCapture.capture(record));
    }

    public String getLoggerName() {
        return loggerName;
    }

    public void setLoggerName(String loggerName) {
        this.loggerName = loggerName;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    @Override
    public void close() {
    }
}

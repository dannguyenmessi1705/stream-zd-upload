package com.didan.streaming.video.config;

import com.didan.streaming.video.listener.DebeziumListener;
import io.debezium.config.Configuration;
import io.debezium.engine.ChangeEvent;
import io.debezium.engine.DebeziumEngine;
import java.io.File;
import org.apache.kafka.connect.storage.FileOffsetBackingStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

@org.springframework.context.annotation.Configuration
public class DebeziumConfig {

    @Value("${spring.datasource.username}")
    private String dbUsername;

    @Value("${spring.datasource.password}")
    private String dbPassword;

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${debezium.offset.storage.file.filename}")
    private String offsetStorageFile;

    @Bean
    public Configuration debeziumConfig() {
        // Đảm bảo thư mục offset tồn tại
        File offsetDir = new File(offsetStorageFile).getParentFile();
        if (!offsetDir.exists()) {
            offsetDir.mkdirs();
        }

        // Lấy thông tin database từ JDBC URL
        String[] urlParts = dbUrl.split("/");
        String dbName = urlParts[urlParts.length - 1];
        String dbHost = urlParts[2].split(":")[0];
        String dbPort = urlParts[2].split(":")[1];

        return Configuration.create()
                .with("name", "streaming-video-connector")
                .with("connector.class", "io.debezium.connector.postgresql.PostgresConnector")
                .with("offset.storage", FileOffsetBackingStore.class.getName())
                .with("offset.storage.file.filename", offsetStorageFile)
                .with("offset.flush.interval.ms", "1000")
                // Cấu hình PostgreSQL
                .with("database.hostname", dbHost)
                .with("database.port", dbPort)
                .with("database.user", dbUsername)
                .with("database.password", dbPassword)
                .with("database.dbname", dbName)
                .with("database.server.name", "streaming-video")
                // Cấu hình CDC
                .with("schema.include.list", "public")
                .with("table.include.list", "public.videos,public.livestreams")
                .with("plugin.name", "pgoutput")
                .with("publication.name", "streaming_publication")
                // Cấu hình snapshot
                .with("snapshot.mode", "initial")
                .build();
    }

    @Bean
    public DebeziumEngine<ChangeEvent<String, String>> debeziumEngine(
            Configuration config,
            DebeziumListener debeziumListener
    ) {
        return DebeziumEngine.create(io.debezium.engine.format.Json.class)
                .using(config.asProperties())
                .notifying(debeziumListener)
                .build();
    }
} 
package gr.itml.mts.filewatch.services;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class WatchServiceConfig {

    @Value("${mts.folders.monitored}")
    String folderPath;

    @Bean
    public WatchService folderWatchService() {
        WatchService watchService = null;
        try {
            log.info("Starting monitoring: " + folderPath);
            watchService = FileSystems.getDefault().newWatchService();
            Path path = Paths.get(folderPath);

            if (!Files.isDirectory(path)) {
                throw new RuntimeException("No folder found: " + path);
            }

            path.register(
                    watchService,
                    StandardWatchEventKinds.ENTRY_MODIFY,
                    StandardWatchEventKinds.ENTRY_CREATE
            );
        } catch (IOException e) {
            log.error("exception for watch service creation:", e);
        }
        return watchService;
    }
}
package gr.itml.mts.filewatch.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import gr.itml.mts.kafka.producers.CloudSenderService;
import lombok.extern.slf4j.Slf4j;

@EnableAsync
@Slf4j
@Service
public class WatchServiceLauncher {

    @Autowired
    WatchService folderWatchService;
    
    @Autowired
    CloudSenderService cloudSenderService;

    @Value("${mts.file.sender.id}")
    String fileSenderId;

    @Value("${mts.file.deleteUploads}")
    boolean deleteFilesAfterSent;

    @Value("${mts.folders.monitored}")
    String monitoredFolderPath;

    @Async
    @EventListener(ApplicationStartedEvent.class)
    public void launchUploadsFolderMonitoring(){
        try {
            WatchKey key;
            while ((key = folderWatchService.take()) != null) {
                for (WatchEvent<?> event : key.pollEvents()) {
                    Path fileName = (Path) event.context();
                    File file = new File(monitoredFolderPath + "/" + fileName.toString());
                    cloudSenderService.sendFileToCloud(fileSenderId, file);
                    if (deleteFilesAfterSent){
                        file.delete();
                    }
                }
                key.reset();
            }
        } catch (InterruptedException e) {
            log.warn("Interrupted folder watching...");
        }
    }

    @PreDestroy
    public void stopMonitoring() {
        log.info("Stopping watching service...");

        if (folderWatchService != null) {
            try {
                folderWatchService.close();
            } catch (IOException e) {
                log.error("Exception while closing the monitoring service: " + e.getMessage());
            }
        }
    }
    
}

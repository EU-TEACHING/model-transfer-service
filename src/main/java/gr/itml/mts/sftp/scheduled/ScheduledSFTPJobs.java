package gr.itml.mts.sftp.scheduled;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import gr.itml.mts.core.cloud.CloudSenderService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@ConditionalOnProperty(value = "mts.mode", havingValue = "SFTP", matchIfMissing = false)
@EnableAsync
public class ScheduledSFTPJobs {

    @Autowired
    CloudSenderService cloudSenderService;

    @Value("${mts.file.deleteUploads}")
    boolean deleteFilesAfterSent;

    @Value("${mts.folders.monitored}")
    String monitoredFolderPath;

    @Value("${mts.file.sender.id}")
    String fileSenderId;

    @Value("${mts.sftp.remote.downloads.folder}")
    String remoteFile;

    @Value("${mts.sftp.remote.downloads.folder}")
    String remoteDownloadsFolder;

    @Scheduled(cron = "0 * * * * ?")
    public void sendCachedFiles() {
        File storageDirectory = new File(monitoredFolderPath);
        for (File file : storageDirectory.listFiles()) {
            boolean isSent = cloudSenderService.sendFileToCloud(fileSenderId, file);
            if (isSent) {
                file.delete();
            } else {
                log.debug("Could not send file: " + file.getName());
            }
        }
    }

    @Scheduled(cron = "0 * * * * ?")
    public void downloadFiles() {
        try {
            cloudSenderService.downloadFiles();
        } catch (IOException e) {
            log.debug("Could not download files.");
            log.debug(e.getMessage());
        }
    }
}

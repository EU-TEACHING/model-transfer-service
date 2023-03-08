package gr.itml.mts.sftp.services;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import gr.itml.mts.core.cloud.CloudSenderService;
import gr.itml.mts.core.crypto.CryptoService;
import gr.itml.mts.sftp.clients.SFTPClientImpl;
import gr.itml.mts.sftp.dtos.SFTPExportRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@ConditionalOnProperty(value = "mts.mode", havingValue = "SFTP", matchIfMissing = false)
public class CloudSenderServiceImpl implements CloudSenderService {

    @Autowired
    CryptoService cryptoService;

    @Autowired
    SFTPClientImpl sftpClient;

    @Value(value = "${mts.sftp.remote.sftpserver}")
    private String sftpServer;

    @Value(value = "${mts.sftp.remote.port}")
    private String sftpServerPort;

    @Value(value = "${mts.sftp.remote.uploads.folder}")
    private String sftpServerFolder;

    @Value(value = "${mts.sftp.remote.downloads.folder}")
    private String sftpServerDownloadsFolder;

    @Value(value = "${mts.sftp.username}")
    private String sftpUsername;

    @Value(value = "${mts.sftp.password}")
    private String sftpPassword;

    @Value(value = "${mts.folders.downloads}")
    private String localDownloadsFolder;

    public boolean sendFileToCloud(String fileSenderId, File file) {
        try {
            doSendFileToCloud(fileSenderId, file.getName(), file);
            return true;
        } catch (Exception e) {
            log.warn("Could not send file: " + file.getAbsolutePath());
            log.debug(e.getMessage());
            return false;
        }
    }

    public void doSendFileToCloud(String fileSenderId, String fileName, File file) throws IOException {
        SFTPExportRequest sftpExportRequest = new SFTPExportRequest(sftpServer, sftpServerPort, sftpUsername,
                sftpPassword, file.getAbsolutePath(), sftpServerFolder);
        sftpClient.export(sftpExportRequest);
    }

    public void downloadFiles() throws IOException {
        SFTPExportRequest sftpExportRequest = new SFTPExportRequest(sftpServer, sftpServerPort, sftpUsername,
                sftpPassword, null, null);
        sftpClient.downloadFilesInFolder(sftpServerDownloadsFolder, localDownloadsFolder, sftpExportRequest);
    }
}
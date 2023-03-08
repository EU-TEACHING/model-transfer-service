package gr.itml.mts.sftp.clients;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Component;

import gr.itml.mts.sftp.dtos.SFTPExportRequest;
import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.sftp.RemoteResourceInfo;
import net.schmizz.sshj.sftp.SFTPClient;
import net.schmizz.sshj.transport.verification.PromiscuousVerifier;

@Component
public class SFTPClientImpl {

    public void export(SFTPExportRequest request) throws IOException {
        try (
                SSHClient sshClient = setupClient(new SSHClient(), request);
                SFTPClient sftpClient = sshClient.newSFTPClient()) {
            File file = new File(request.getLocalFile());
            sftpClient.put(request.getLocalFile(), request.getRemoteFolder() + "/" + file.getName());
        }
    }

    public boolean testConnectivity(SFTPExportRequest request) {
        try (SSHClient client = setupClient(new SSHClient(), request)) {
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public SSHClient setupClient(SSHClient client, SFTPExportRequest request) throws IOException {
        client.addHostKeyVerifier(new PromiscuousVerifier());
        client.connect(request.getSftpServer(), Integer.valueOf(request.getPort()));
        client.authPassword(request.getUsername(), request.getPassword());
        return client;
    }

    public boolean fileExists(SFTPExportRequest request) throws IOException {
        try (
                SSHClient sshClient = setupClient(new SSHClient(), request);
                SFTPClient sftpClient = sshClient.newSFTPClient()) {
            File file = new File(request.getLocalFile());
            List<RemoteResourceInfo> list = sftpClient.ls(request.getRemoteFolder());
            return list.stream().anyMatch(li -> li.getName().equals(file.getName()));
        }
    }

    public boolean downloadFilesInFolder(String remoteFolder, String localFolder, SFTPExportRequest request)
            throws IOException {
        try (
                SSHClient sshClient = setupClient(new SSHClient(), request);
                SFTPClient sftpClient = sshClient.newSFTPClient()) {
            List<RemoteResourceInfo> fileList = sftpClient.ls(remoteFolder);
            for (RemoteResourceInfo remoteFile : fileList) {
                if (remoteFile.isRegularFile()) {
                    sftpClient.get(remoteFile.getPath(), localFolder);
                }
            }
            return true;
        }
    }

    public boolean deleteFile(SFTPExportRequest request) {
        try (
                SSHClient sshClient = setupClient(new SSHClient(), request);
                SFTPClient sftpClient = sshClient.newSFTPClient()) {
            File file = new File(request.getLocalFile());
            sftpClient.rm(request.getRemoteFolder() + "/" + file.getName());
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}

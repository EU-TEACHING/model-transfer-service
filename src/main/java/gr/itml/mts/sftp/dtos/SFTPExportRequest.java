package gr.itml.mts.sftp.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SFTPExportRequest {

    private String sftpServer;
    private String port;
    private String username;
    private String password;
    private String localFile;
    private String remoteFolder;
}

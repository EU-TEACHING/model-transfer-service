package gr.itml.mts.core.cloud;

import java.io.File;
import java.io.IOException;

import org.springframework.stereotype.Service;

@Service
public interface CloudSenderService {

    public boolean sendFileToCloud(String fileSenderId, File file);

    public void downloadFiles() throws IOException;
}

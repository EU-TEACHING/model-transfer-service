package gr.itml.mts.kafka.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import gr.itml.mts.core.crypto.CryptoService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FileWriteUtils {

    @Value(value = "${mts.folders.downloads}")
    private String fileDownloadsPath;
    
    @Autowired
    CryptoService cryptoService;

    public String writeFileUploadDTOToFile(String fileSenderId, String fileName, byte[] fileBytes){
        File file = new File(String.join("/", fileDownloadsPath, fileName));
        try {
            byte[] decryptedBytes = cryptoService.decrypt(fileBytes, fileSenderId);
            writeToFile(file, fileName, decryptedBytes);
            return file.getAbsolutePath();
        }catch (Exception e) {
            log.info("Could not write file.");
            log.info(e.getMessage());
            return null;
        }
    }

    private void writeToFile(File file, String fileName, byte[] fileBytes) throws IOException{
        try (OutputStream os = new FileOutputStream(file)){
            os.write(fileBytes);
            log.info("Successfully wrote: " + fileName);
        }
    }
}
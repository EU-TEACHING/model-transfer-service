package gr.itml.mts.kafka.producers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import gr.itml.mts.core.cloud.CloudSenderService;
import gr.itml.mts.core.crypto.CryptoService;
import gr.itml.mts.kafka.dtos.FileUploadDTO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@ConditionalOnProperty(value = "mts.mode", havingValue = "KAFKA", matchIfMissing = false)
public class CloudSenderServiceImpl implements CloudSenderService {

    @Autowired
    private KafkaTemplate<String, FileUploadDTO> kafkaTemplate;

    @Autowired
    CryptoService cryptoService;

    @Value(value = "${kafka.uploadsTopicName}")
    private String kafkaUploadsTopicName;

    public boolean sendFileToCloud(String fileSenderId, File file) {
        try {
            byte[] encryptedArray = readAndEncrypt(fileSenderId, file);
            if (encryptedArray.length > 0) {
                doSendFileToCloud(fileSenderId, file.getName(), encryptedArray);
                return true;
            }
            log.info("No bytes were read from file: " + file.getAbsolutePath());
        } catch (Exception e) {
            log.warn("Could not send file: " + file.getAbsolutePath());
        }
        return false;
    }

    public byte[] readAndEncrypt(String fileSenderId, File file) {
        try (FileInputStream fl = new FileInputStream(file)) {
            byte[] arr = new byte[(int) file.length()];
            int fileBytes = fl.read(arr);
            if (fileBytes > 0)
                return cryptoService.encrypt(arr, fileSenderId);
        } catch (IOException e) {
            log.warn("Could not encrypt file: " + file.getAbsolutePath());
        }
        return new byte[0];
    }

    public void doSendFileToCloud(String fileSenderId, String fileName, byte[] encryptedArray) {
        FileUploadDTO fileUpload = new FileUploadDTO(fileSenderId, fileName, encryptedArray);
        kafkaTemplate.send(kafkaUploadsTopicName, fileUpload);
    }

    @Override
    public void downloadFiles() {
        throw new UnsupportedOperationException("Unimplemented method 'downloadFiles'");
    }
}
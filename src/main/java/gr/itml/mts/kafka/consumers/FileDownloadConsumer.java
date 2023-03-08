package gr.itml.mts.kafka.consumers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import gr.itml.mts.kafka.dtos.FileUploadDTO;
import gr.itml.mts.kafka.utils.FileWriteUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@ConditionalOnProperty(value = "mts.mode", havingValue = "KAFKA", matchIfMissing = false)
public class FileDownloadConsumer {

    @Autowired
    FileWriteUtils fileWriteUtilsService;

    @KafkaListener(topics = "${kafka.downloadsTopicName}", groupId = "${kafka.groupId}")
    public void downloadFiles(FileUploadDTO fileUploadDTO) {
        log.debug("File to download...");
        fileWriteUtilsService.writeFileUploadDTOToFile(fileUploadDTO.getFileSenderId(), fileUploadDTO.getFileName(),
                fileUploadDTO.getEncryptedFile());
    }
}

package gr.itml.mts.kafka.serializers;

import org.apache.kafka.common.serialization.Deserializer;

import com.fasterxml.jackson.databind.ObjectMapper;

import gr.itml.mts.kafka.dtos.FileUploadDTO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileUploadDTODeserializer implements Deserializer<FileUploadDTO> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public FileUploadDTO deserialize(String topic, byte[] data) {
        try {
            if (data == null){
                log.warn("Null received at deserializing");
                return null;
            }
            return objectMapper.readValue(data, FileUploadDTO.class);
        } catch (Exception e) {
            log.info("Error when deserializing");
            log.info(e.getMessage());
            return null;
        }
    }
    
}
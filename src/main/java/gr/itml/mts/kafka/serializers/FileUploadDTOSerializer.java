package gr.itml.mts.kafka.serializers;

import org.apache.kafka.common.serialization.Serializer;

import com.fasterxml.jackson.databind.ObjectMapper;

import gr.itml.mts.kafka.dtos.FileUploadDTO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileUploadDTOSerializer implements Serializer<FileUploadDTO> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public byte[] serialize(String topic, FileUploadDTO data) {
        try {
            if (data == null){
                log.info("Null received at serializing");
            }
            return objectMapper.writeValueAsString(data).getBytes();
        } catch (Exception e) {
            log.info("Error while serializing.");
            log.info(e.getMessage());
        }
        return new byte[0];
    }
    
}
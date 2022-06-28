package gr.itml.mts.kafka.dtos;

import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@NoArgsConstructor
@Setter
public class FileUploadDTO {
    
    @JsonProperty("fileSenderId")
    private String fileSenderId;
    @JsonProperty("fileName")
    private String fileName;
    @JsonProperty("encryptedFile")
    private byte[] encryptedFile;
}
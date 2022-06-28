package gr.itml.mts.core.crypto.implementations;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.crypto.encrypt.BytesEncryptor;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.stereotype.Component;

import gr.itml.mts.core.crypto.CryptoService;

@Component
@ConditionalOnProperty(
  value="crypto.algorithm", 
  havingValue = "AES", 
  matchIfMissing = false)
public class AESCryptoServiceImplementation implements CryptoService {

    @Value("#{${crypto.aes.passwords}}")
    private Map<String,String> passwords;

    @Value("#{${crypto.aes.salts}}")
    private Map<String,String> salts;
    
    @Override
    public byte[] encrypt(byte[] byteArrayToEncrypt, String fileSenderId) {
        BytesEncryptor encryptor = getBytesEncryptor(passwords.get(fileSenderId), salts.get(fileSenderId));
        return encryptor.encrypt(byteArrayToEncrypt);
    }

    @Override
    public byte[] decrypt(byte[] byteArrayToDecrypt, String fileSenderId) {
        BytesEncryptor encryptor = getBytesEncryptor(passwords.get(fileSenderId), salts.get(fileSenderId));
        return encryptor.decrypt(byteArrayToDecrypt);
    }

    public BytesEncryptor getBytesEncryptor(String password, String salt){
        return Encryptors.stronger(password, salt);
    }
}
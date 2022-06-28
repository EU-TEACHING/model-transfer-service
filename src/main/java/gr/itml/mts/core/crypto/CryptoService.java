package gr.itml.mts.core.crypto;

import org.springframework.stereotype.Service;

@Service
public interface CryptoService {
    
    public byte[] encrypt(byte[] byteArrayToEncrypt, String fileSenderId);

    public byte[] decrypt(byte[] byteArrayToDecrypt, String fileSenderId);
}
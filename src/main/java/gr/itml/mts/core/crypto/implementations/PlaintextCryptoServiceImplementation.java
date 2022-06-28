package gr.itml.mts.core.crypto.implementations;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import gr.itml.mts.core.crypto.CryptoService;

@Component
@ConditionalOnProperty(
  value="crypto.algorithm", 
  havingValue = "PLAINTEXT", 
  matchIfMissing = false)
public class PlaintextCryptoServiceImplementation implements CryptoService{

    @Override
    public byte[] encrypt(byte[] byteArrayToEncrypt, String fileSenderId) {
        return byteArrayToEncrypt;
    }

    @Override
    public byte[] decrypt(byte[] byteArrayToDecrypt, String fileSenderId) {
        return byteArrayToDecrypt;
    }
    
}
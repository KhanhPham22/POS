package service;

import java.security.MessageDigest;
import java.util.Base64;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HashService {

    private static final Logger Log = LogManager.getLogger(HashService.class);

    public HashService() {
        Log.info("HashService initialized with SHA-256");
    }

    // Băm chuỗi bất kỳ (thường là password)
    public String hash(String data) throws Exception {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(data.getBytes("UTF-8"));
            String hashed = Base64.getEncoder().encodeToString(encodedHash);
            Log.info("Hashing successful");
            return hashed;
        } catch (Exception e) {
            Log.error("Hashing failed", e);
            throw new Exception("Failed to hash data");
        }
    }

    // So sánh password người dùng nhập với password đã hash
    public boolean verify(String inputPassword, String hashedPassword) throws Exception {
        String hashedInput = hash(inputPassword);
        return hashedInput.equals(hashedPassword);
    }
}


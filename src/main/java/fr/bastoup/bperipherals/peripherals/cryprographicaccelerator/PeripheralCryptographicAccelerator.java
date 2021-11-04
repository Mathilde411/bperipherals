package fr.bastoup.bperipherals.peripherals.cryprographicaccelerator;

import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.peripheral.IPeripheral;
import fr.bastoup.bperipherals.util.Config;
import fr.bastoup.bperipherals.util.Util;
import fr.bastoup.bperipherals.util.peripherals.BPeripheral;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class PeripheralCryptographicAccelerator extends BPeripheral {

    public static final String TYPE = "cryptographic_accelerator";


    public PeripheralCryptographicAccelerator(BlockEntityCryptographicAccelerator tile) {
        super(tile);
    }

    @Nonnull
    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public boolean equals(@Nullable IPeripheral other) {
        return this == other || other instanceof PeripheralCryptographicAccelerator && ((PeripheralCryptographicAccelerator) other).blockEntity == blockEntity;
    }

    @LuaFunction
    public final byte[] randomBytes(int length) throws LuaException {
        if (0 >= length || length > Config.MAX_RANDOM_BYTES_SIZE)
            throw new LuaException("Length must be between 1 and " + Config.MAX_RANDOM_BYTES_SIZE);
        byte[] res = new byte[length];
        new SecureRandom().nextBytes(res);
        return res;
    }

    @LuaFunction
    public final byte[] decodeBase64(String base64) throws LuaException {
        try {
            return Base64.getDecoder().decode(base64);
        } catch (IllegalArgumentException e) {
            throw new LuaException("This is not a valid base64 string.");
        }
    }

    @LuaFunction
    public final String encodeBase64(ByteBuffer str) {
        byte[] strArray = Util.getByteBufferArray(str);
        return new String(Base64.getEncoder().encode(strArray));
    }

    @LuaFunction
    public final byte[] encryptAES(ByteBuffer data, ByteBuffer key, ByteBuffer iv) throws LuaException {
        byte[] keyArray = Util.getByteBufferArray(key);
        byte[] ivArray = Util.getByteBufferArray(iv);
        byte[] dataArray = Util.getByteBufferArray(data);
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyArray, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(ivArray);
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivSpec);
            return cipher.doFinal(dataArray);
        } catch (InvalidKeyException | InvalidAlgorithmParameterException e) {
            throw new LuaException(e.getMessage());
        } catch (NoSuchAlgorithmException | IllegalBlockSizeException | BadPaddingException | NoSuchPaddingException e) {
            e.printStackTrace();
            throw new LuaException("Internal error, check the logs for more info.");
        }
    }

    @LuaFunction
    public final byte[] decryptAES(ByteBuffer data, ByteBuffer key, ByteBuffer iv) throws LuaException {
        byte[] keyArray = Util.getByteBufferArray(key);
        byte[] ivArray = Util.getByteBufferArray(iv);
        byte[] dataArray = Util.getByteBufferArray(data);
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyArray, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(ivArray);
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivSpec);
            return cipher.doFinal(dataArray);
        } catch (InvalidKeyException | InvalidAlgorithmParameterException e) {
            throw new LuaException(e.getMessage());
        } catch (NoSuchAlgorithmException | IllegalBlockSizeException | BadPaddingException | NoSuchPaddingException e) {
            e.printStackTrace();
            throw new LuaException("Internal error, check the logs for more info.");
        }
    }

    @LuaFunction
    public final Map<String, byte[]> generateRSAKeys(int keySize) throws LuaException {
        if (512 > keySize || keySize > 1024)
            throw new LuaException("Key size must be between 512 and 1024");
        try {
            KeyPairGenerator kpGen = KeyPairGenerator.getInstance("RSA");
            kpGen.initialize(keySize);
            KeyPair kp = kpGen.generateKeyPair();
            Map<String, byte[]> res = new HashMap<>();
            res.put("public", kp.getPublic().getEncoded());
            res.put("private", kp.getPrivate().getEncoded());
            return res;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new LuaException("Internal error, check the logs for more info.");
        }

    }

    @LuaFunction
    public final byte[] encryptRSA(ByteBuffer data, ByteBuffer publicKey) throws LuaException {
        byte[] publicKeyArray = Util.getByteBufferArray(publicKey);
        byte[] dataArray = Util.getByteBufferArray(data);
        try {
            PublicKey key = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(publicKeyArray));
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return cipher.doFinal(dataArray);
        } catch (InvalidKeyException e) {
            throw new LuaException(e.getMessage());
        } catch (NoSuchAlgorithmException | IllegalBlockSizeException | BadPaddingException | NoSuchPaddingException | InvalidKeySpecException e) {
            e.printStackTrace();
            throw new LuaException("Internal error, check the logs for more info.");
        }
    }

    @LuaFunction
    public final byte[] decryptRSA(ByteBuffer data, ByteBuffer privateKey) throws LuaException {
        byte[] privateKeyArray = Util.getByteBufferArray(privateKey);
        byte[] dataArray = Util.getByteBufferArray(data);
        try {
            PrivateKey key = KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(privateKeyArray));
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, key);
            return cipher.doFinal(dataArray);
        } catch (InvalidKeyException e) {
            throw new LuaException(e.getMessage());
        } catch (NoSuchAlgorithmException | IllegalBlockSizeException | BadPaddingException | NoSuchPaddingException | InvalidKeySpecException e) {
            e.printStackTrace();
            throw new LuaException("Internal error, check the logs for more info.");
        }
    }

    @LuaFunction
    public final byte[] hashMD5(ByteBuffer data) throws LuaException {
        byte[] dataArray = Util.getByteBufferArray(data);
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            return md.digest(dataArray);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new LuaException("Internal error, check the logs for more info.");
        }
    }

    @LuaFunction
    public final byte[] hashSHA512(ByteBuffer data) throws LuaException {
        byte[] dataArray = Util.getByteBufferArray(data);
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            return md.digest(dataArray);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new LuaException("Internal error, check the logs for more info.");
        }
    }

    @LuaFunction
    public final byte[] hmacSHA512(ByteBuffer data, ByteBuffer key) throws LuaException {
        byte[] dataArray = Util.getByteBufferArray(data);
        byte[] keyArray = Util.getByteBufferArray(key);
        byte[] res;
        try {
            Mac mac = Mac.getInstance("HmacSHA512");
            SecretKeySpec secretKeySpec = new SecretKeySpec(keyArray, "HmacSHA512");
            mac.init(secretKeySpec);
            res = mac.doFinal(dataArray);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new LuaException("Internal error, check the logs for more info.");
        } catch (InvalidKeyException e) {
            throw new LuaException(e.getMessage());
        }
        return res;
    }

    @LuaFunction
    public final byte[] hmacMD5(ByteBuffer data, ByteBuffer key) throws LuaException {
        byte[] dataArray = Util.getByteBufferArray(data);
        byte[] keyArray = Util.getByteBufferArray(key);
        byte[] res;
        try {
            Mac mac = Mac.getInstance("HmacMD5");
            SecretKeySpec secretKeySpec = new SecretKeySpec(keyArray, "HmacMD5");
            mac.init(secretKeySpec);
            res = mac.doFinal(dataArray);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new LuaException("Internal error, check the logs for more info.");
        } catch (InvalidKeyException e) {
            throw new LuaException(e.getMessage());
        }
        return res;
    }


}

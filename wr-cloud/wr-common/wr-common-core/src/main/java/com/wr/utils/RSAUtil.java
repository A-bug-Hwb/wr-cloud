package com.wr.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import com.wr.constants.HttpStatus;
import com.wr.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.util.Base64Utils;

@Slf4j
public class RSAUtil {

    /**
     * RSA密钥长度必须是64的倍数，在512~65536之间。默认是1024
     */
    public static final int KEY_SIZE = 1024;

    /**
     * 生成公钥、私钥对(keysize=1024)
     */
    public static RSAUtil.KeyPairInfo getKeyPair() {
        return getKeyPair(KEY_SIZE);
    }

    /**
     * 生成公钥、私钥对
     *
     * @param keySize
     * @return
     */
    public static RSAUtil.KeyPairInfo getKeyPair(int keySize) {
        try {
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
            keyPairGen.initialize(keySize);
            // 生成一个密钥对，保存在keyPair中
            KeyPair keyPair = keyPairGen.generateKeyPair();
            // 得到私钥
            RSAPrivateKey oraprivateKey = (RSAPrivateKey) keyPair.getPrivate();
            // 得到公钥
            RSAPublicKey orapublicKey = (RSAPublicKey) keyPair.getPublic();

            RSAUtil.KeyPairInfo pairInfo = new RSAUtil.KeyPairInfo(keySize);
            //公钥
            byte[] publicKeybyte = orapublicKey.getEncoded();
            String publicKeyString = Base64.encodeBase64String(publicKeybyte);
            pairInfo.setPublicKey(publicKeyString);
            //私钥
            byte[] privateKeybyte = oraprivateKey.getEncoded();
            String privateKeyString = Base64.encodeBase64String(privateKeybyte);
            pairInfo.setPrivateKey(privateKeyString);

            return pairInfo;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取公钥对象
     *
     * @param publicKeyBase64
     * @return
     * @throws InvalidKeySpecException
     * @throws NoSuchAlgorithmException
     */
    public static PublicKey getPublicKey(String publicKeyBase64)
            throws InvalidKeySpecException, NoSuchAlgorithmException {

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec publicpkcs8KeySpec =
                new X509EncodedKeySpec(Base64.decodeBase64(publicKeyBase64));
        PublicKey publicKey = keyFactory.generatePublic(publicpkcs8KeySpec);
        return publicKey;
    }

    /**
     * 获取私钥对象
     *
     * @param privateKeyBase64
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public static PrivateKey getPrivateKey(String privateKeyBase64)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PKCS8EncodedKeySpec privatekcs8KeySpec =
                new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKeyBase64));
        PrivateKey privateKey = keyFactory.generatePrivate(privatekcs8KeySpec);
        return privateKey;
    }

    /**
     * 使用公钥加密
     *
     * @param content         待加密内容
     * @param publicKeyBase64 公钥 base64 编码
     * @return 经过 base64 编码后的字符串
     */
    public static String encipher(String content, String publicKeyBase64) {
        return encipher(content, publicKeyBase64, KEY_SIZE / 8 - 11);
    }

    /**
     * 使用公钥加密（分段加密）
     *
     * @param content         待加密内容
     * @param publicKeyBase64 公钥 base64 编码
     * @param segmentSize     分段大小,一般小于 keySize/8（段小于等于0时，将不使用分段加密）
     * @return 经过 base64 编码后的字符串
     */
    public static String encipher(String content, String publicKeyBase64, int segmentSize) {
        try {
            PublicKey publicKey = getPublicKey(publicKeyBase64);
            return encipher(content, publicKey, segmentSize);
        } catch (Exception e) {
            throw new ServiceException("加密失败");
        }
    }

    /**
     * 分段加密
     *
     * @param ciphertext  密文
     * @param key         加密秘钥
     * @param segmentSize 分段大小，<=0 不分段
     * @return
     */
    public static String encipher(String ciphertext, java.security.Key key, int segmentSize) {
        try {
            // 用公钥加密
            byte[] srcBytes = ciphertext.getBytes();

            // Cipher负责完成加密或解密工作，基于RSA
            Cipher cipher = Cipher.getInstance("RSA");
            // 根据公钥，对Cipher对象进行初始化
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] resultBytes = null;

            if (segmentSize > 0) {
                resultBytes = cipherDoFinal(cipher, srcBytes, segmentSize); //分段加密
            } else {
                resultBytes = cipher.doFinal(srcBytes);
            }
            String base64Str = Base64Utils.encodeToString(resultBytes);
            return base64Str;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 分段大小
     *
     * @param cipher
     * @param srcBytes
     * @param segmentSize
     * @return
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     * @throws IOException
     */
    public static byte[] cipherDoFinal(Cipher cipher, byte[] srcBytes, int segmentSize)
            throws IllegalBlockSizeException, BadPaddingException, IOException {
        if (segmentSize <= 0) {
            throw new RuntimeException("分段大小必须大于0");
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int inputLen = srcBytes.length;
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > segmentSize) {
                cache = cipher.doFinal(srcBytes, offSet, segmentSize);
            } else {
                cache = cipher.doFinal(srcBytes, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * segmentSize;
        }
        byte[] data = out.toByteArray();
        out.close();
        return data;
    }

    /**
     * 使用私钥解密
     *
     * @param contentBase64    待加密内容,base64 编码
     * @param privateKeyBase64 私钥 base64 编码
     * @return
     * @segmentSize 分段大小
     */
    public static String decipher(String contentBase64, String privateKeyBase64) {
        return decipher(contentBase64, privateKeyBase64, KEY_SIZE / 8);
    }

    /**
     * 使用私钥解密（分段解密）
     *
     * @param contentBase64    待加密内容,base64 编码
     * @param privateKeyBase64 私钥 base64 编码
     * @return
     * @segmentSize 分段大小
     */
    public static String decipher(String contentBase64, String privateKeyBase64, int segmentSize) {
        try {
            PrivateKey privateKey = getPrivateKey(privateKeyBase64);
            return decipher(contentBase64, privateKey, segmentSize);
        } catch (Exception e) {
            throw new ServiceException("秘钥校验失败！", HttpStatus.KEY_LOSE);
        }
    }

    /**
     * 分段解密
     *
     * @param contentBase64 密文
     * @param key           解密秘钥
     * @param segmentSize   分段大小（小于等于0不分段）
     * @return
     */
    public static String decipher(String contentBase64, java.security.Key key, int segmentSize) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException {
        // 用私钥解密
        byte[] srcBytes = Base64Utils.decodeFromString(contentBase64);
        // Cipher负责完成加密或解密工作，基于RSA
        Cipher deCipher = Cipher.getInstance("RSA");
        // 根据公钥，对Cipher对象进行初始化
        deCipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decBytes = null;//deCipher.doFinal(srcBytes);
        if (segmentSize > 0) {
            decBytes = cipherDoFinal(deCipher, srcBytes, segmentSize);
        } //分段加密
        else {
            decBytes = deCipher.doFinal(srcBytes);
        }

        String decrytStr = new String(decBytes);
        return decrytStr;
    }

    /**
     * 加签
     *
     * @param privateKeyStr 私钥字符串
     * @param plainStr      需要加签的字符串
     * @return
     * @throws Exception
     */
    public static String sign(String privateKeyStr, String plainStr) throws Exception {
        // 解码私钥
        byte[] keyBytes = Base64Utils.decodeFromString(privateKeyStr);
        // 构造PKCS8EncodedKeySpec对象
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyBytes);
        // 指定加密算法
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        // 取私钥匙对象
        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        // 用私钥对信息生成数字签名
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey);
        // 获取待加签数据字节数组
        byte[] plainBytes = plainStr.getBytes(StandardCharsets.UTF_8);
        //签名获取字节数组
        signature.update(plainBytes);
        byte[] signBytes = signature.sign();
        //Base64编码
        return Base64Utils.encodeToString(signBytes);
    }

    /**
     * 校验数字签名
     *
     * @param plainStr     待加签数据
     * @param publicKeyStr 公钥
     * @param signStr      数字签名
     * @return
     * @throws Exception
     */
    public static boolean verify(String plainStr, String publicKeyStr, String signStr) throws Exception {
        // 解密公钥
        byte[] keyBytes = Base64Utils.decodeFromString(publicKeyStr);
        // 构造X509EncodedKeySpec对象
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);
        // 指定加密算法
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        // 取公钥匙对象
        PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initVerify(publicKey);
        // 获取待加签数据字节数组
        byte[] plainBytes = plainStr.getBytes(StandardCharsets.UTF_8);
        signature.update(plainBytes);
        // 验证签名是否正常
        return signature.verify(Base64Utils.decodeFromString(signStr));
    }


    /**
     * 秘钥对
     */
    public static class KeyPairInfo {
        String privateKey;
        String publicKey;
        int keySize = 0;

        public KeyPairInfo(int keySize) {
            setKeySize(keySize);
        }

        public KeyPairInfo(String publicKey, String privateKey) {
            setPrivateKey(privateKey);
            setPublicKey(publicKey);
        }

        public String getPrivateKey() {
            return privateKey;
        }

        public void setPrivateKey(String privateKey) {
            this.privateKey = privateKey;
        }

        public String getPublicKey() {
            return publicKey;
        }

        public void setPublicKey(String publicKey) {
            this.publicKey = publicKey;
        }

        public int getKeySize() {
            return keySize;
        }

        public void setKeySize(int keySize) {
            this.keySize = keySize;
        }
    }

    public static void main(String[] args) throws Exception {
        String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA0hg1oRLjdwKerVdUTaIlv9cTVqhfk2GbM/d2Paieo70DcEZLxwTDHr9l6ijc5/VgcEOqOqw6Q6MmZSV2Ezy14Bj6/0CC9uED7pOF6EngWhqChwokbtGfVMLDUjmaE6fJVi8dIo1Rl13eir7SE+ONkksSdMTTqZw2HzkUCahiWPYLvNR9pPIkKMWplbbtZ97ac/z+xg5aHoDlxFK4bBzcnAW/TpxmVZCOnsX9fhE90hwjGsLpPr5JmKOOATecXIfSAFfYzaBKXqDjT7GKKd+IerSX5mD1jbNPDD+RmNV223VJBDJZ9r2+hIZRvAebF2brlwEQ05IthVWPvz8IBHBB3QIDAQAB";
        String privateKey = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDSGDWhEuN3Ap6tV1RNoiW/1xNWqF+TYZsz93Y9qJ6jvQNwRkvHBMMev2XqKNzn9WBwQ6o6rDpDoyZlJXYTPLXgGPr/QIL24QPuk4XoSeBaGoKHCiRu0Z9UwsNSOZoTp8lWLx0ijVGXXd6KvtIT442SSxJ0xNOpnDYfORQJqGJY9gu81H2k8iQoxamVtu1n3tpz/P7GDloegOXEUrhsHNycBb9OnGZVkI6exf1+ET3SHCMawuk+vkmYo44BN5xch9IAV9jNoEpeoONPsYop34h6tJfmYPWNs08MP5GY1XbbdUkEMln2vb6EhlG8B5sXZuuXARDTki2FVY+/PwgEcEHdAgMBAAECggEBAMlwZvNCvhAc1fGT2wP4pUM0P5mGJOWV347LBXcHFtwd3Y8oB34SBdeUYm5KscxCDC534l8RRWgWpTiz22uEeUoeCYL14tw9UVzN28WmF9OsuVwrEFWk7aUCfO54xk90P+o/kCrR3tpIKZlUbu6c7t/G1TJaiiuE2KvFOX4tEecCfBa8J72rGK9qCWJPKgVkVO65fcM1NwR3PetFrGg0zYx+wWIwx/d5zyiBe/PETaRs5FiaCQVGc2Fz2OYiEEIm8nyulBnH3TApEnHLk4PzvJZltMcHaAkNKhwyFR5GMXfNpcJYRvr3SFJFbYGXjPRGJ1PuYw8K+d/tjlrlXKkuJyECgYEA/hxWVDieDK09N3BsqHUmJlENqW2ckjdbGhByVY5GZXsI8avDGJNN1/RGkHppTWHns4MQbvUQxY9wfVcqJgsbeL597d/tvSdUISU+H52GF8/IN6wBIyA4iuyU3ghVr6gWpffHxqqMoRbwUTE0JyAHjdag1rvlo32BO4KYbD+ANXUCgYEA06gYDnQq5t1zDdAj/hDbDfkE/0IhjdlosJRoAQApHs139ja2C+J+yWF+F328yMyPnBAM+tOLk47y9GYRwS593D4zXSzWQ8TtrzKaV6JpzrAKBJFDuBMvmoa/SDuLz07ZOvxKiCNKpESlopUxtSS1OmN16LIUZhoh3Wlt/lJvBckCgYB+aOZhO1EUiVG8tIa5Pp3dzdB4CnkrG7KMQsY93WsTrKMEZifFn/HHjlNUKDEeN6gl2vtf1zWisfqJv8TN7BScelb28MC5qkjlvDstSxWb3teBKmXebXbzVUGxOoA1w+6QXbTaFZmjwV+HZGzHUd2wJ6cXa52POS4CxrLAls5WyQKBgQCDhJjmKlMaP5Kli6dB0RF83ja3sk0NSoHd72ms7X+fr0cSjC6UtfnYgqcKS8xJ7apy7BpUpay+VP4s1VvE8XGtsEzUYe8GD1EuI0B6oeYjU4n2CpnMwMc8uuwTI0Ud3T3ohCp/64YPCsTtgM1xZMzj6jezDyzTwPs4DLIXbPul2QKBgHV2tILNj0qKskWuJVhKCdbmE3T7UXCmve7+V4j0Avq5QpK7fgKOW43dliuuK27ecflQfqOF6dT+f4VGjOZQNd1EAlDd6R8fLAeFdLKQt/ev6+f4GRPPPDe7HgW0vdkcLCXUjCfZ/Tnryk+LuHdwJlaG/uAWlfkKT2ZXRR3B/Kw+";
        String sig = sign(privateKey, "0E57F221-0AA6-4D91-B8B6-4D890BF6F247abcd");
        System.out.println(new Date().getTime());
        System.out.println(sig);
        System.out.println(verify("0E57F221-0AA6-4D91-B8B6-4D890BF6F247abcd", publicKey, sig));
    }
}

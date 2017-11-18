package com.bbcow.service.util;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.math.BigInteger;
import java.security.*;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

public class RSA {
    public static final String CHAR_ENCODING = "UTF-8";
    public static final String RSA_ALGORITHM = "RSA/ECB/PKCS1Padding";

    /** 指定key的大小 */
    private static int KEYSIZE = 2048;

    /**
     * 生成密钥对
     */
    public static Map<String, String> generateKeyPair() throws Exception {
        /** RSA算法要求有一个可信任的随机数源 */
        SecureRandom sr = new SecureRandom();
        /** 为RSA算法创建一个KeyPairGenerator对象 */
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        /** 利用上面的随机数据源初始化这个KeyPairGenerator对象 */
        kpg.initialize(KEYSIZE, sr);
        /** 生成密匙对 */
        KeyPair kp = kpg.generateKeyPair();
        /** 得到公钥 */
        Key publicKey = kp.getPublic();
        byte[] publicKeyBytes = publicKey.getEncoded();
        String pub = new String(Base64.encodeBase64(publicKeyBytes), CHAR_ENCODING);
        /** 得到私钥 */
        Key privateKey = kp.getPrivate();
        byte[] privateKeyBytes = privateKey.getEncoded();
        String pri = new String(Base64.encodeBase64(privateKeyBytes), CHAR_ENCODING);

        Map<String, String> map = new HashMap<String, String>();
        map.put("publicKey", pub);
        map.put("privateKey", pri);
        RSAPublicKey rsp = (RSAPublicKey) kp.getPublic();
        BigInteger bint = rsp.getModulus();
        byte[] b = bint.toByteArray();
        byte[] deBase64Value = Base64.encodeBase64(b);
        String retValue = new String(deBase64Value);
        map.put("modulus", retValue);
        return map;
    }
    /**
     * 加密方法 source： 源数据
     */
    public static String encrypt(String source, String publicKey) throws Exception {
        Key key = getPublicKey(publicKey);
        /** 得到Cipher对象来实现对源数据的RSA加密 */
        Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] b = source.getBytes();
        /** 执行加密操作 */
        byte[] b1 = cipher.doFinal(b);
        return new String(Base64.encodeBase64(b1), CHAR_ENCODING);
    }

    /**
     * 解密算法 cryptograph:密文
     */
    public static String decrypt(String cryptograph, String privateKey) throws Exception {
        Key key = getPrivateKey(privateKey);
        /** 得到Cipher对象对已用公钥加密的数据进行RSA解密 */
        Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] b1 = Base64.decodeBase64(cryptograph.getBytes());
        /** 执行解密操作 */
        byte[] b = cipher.doFinal(b1);
        return new String(b);
    }

    /**
     * 得到公钥
     *
     * @param key
     *            密钥字符串（经过base64编码）
     * @throws Exception
     */
    public static PublicKey getPublicKey(String key) throws Exception {
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.decodeBase64(key.getBytes()));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(keySpec);
        return publicKey;
    }

    /**
     * 得到私钥
     *
     * @param key
     *            密钥字符串（经过base64编码）
     * @throws Exception
     */
    public static PrivateKey getPrivateKey(String key) throws Exception {
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(key.getBytes()));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        return privateKey;
    }

    public static String sign(String content, String privateKey) {
        String charset = CHAR_ENCODING;
        try {
            PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKey.getBytes()));
            KeyFactory keyf = KeyFactory.getInstance("RSA");
            PrivateKey priKey = keyf.generatePrivate(priPKCS8);

            Signature signature = Signature.getInstance("SHA1WithRSA");

            signature.initSign(priKey);
            signature.update(content.getBytes(charset));

            byte[] signed = signature.sign();

            return new String(Base64.encodeBase64(signed));
        } catch (Exception e) {

        }

        return null;
    }

    public static boolean checkSign(String content, String sign, String publicKey) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] encodedKey = Base64.decodeBase64(publicKey);
            PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));

            java.security.Signature signature = java.security.Signature.getInstance("SHA1WithRSA");

            signature.initVerify(pubKey);
            signature.update(content.getBytes(CHAR_ENCODING));

            boolean bverify = signature.verify(Base64.decodeBase64(sign));
            return bverify;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public static void main(String[] args) {
        try {
            String privateKey = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCVIi/hePKyPgF0kPNogiLnNiRh4KB6Atk8KbWBMphoH3kkK+hXDiSBLO77VhDXs/q5PkMOdTQBB2sSL9VeBiZIcCDxAGJYJ1NCot8rzPWRtf/xHpLvzsEk8RC01RsqMTHTVVRMCtaj9G8xV0VylPLjwvcnLB2k9S6EZYY+kgha6SZLde8gQ0NDfUUHB0ektHK3244XLw4o0fiDp41NeOzJbkRXviDCoAE5DVqRFjmYx6VfN8mEoZbajVh6qYCqDuV7i9W/ZANkS9pp1I2BFf/JHpv671p7H5fpNtlHXkuQmQZU3CWyCEBAwSsejtIoXlzJzmVXQf4f67WdQHq9CBnvAgMBAAECggEAAYB6ovao2wqs0RO0n9TPrSxqaWHtu8zTizuQTGDKECJhAfA/QsEttO9S6M0RFlsFp1/tJPQhNfFxLpm0uGGveQNIEaVDWEftGrqxLKu/vVvr2+3G5Qtb/pc+59NNlrswGUS+uJVTXAtWI1URqfm+CHUqMAbNtZ+SfFrZYz/tPZ0ff7/wKd3z9vytlQBvZrhWg6KPaNHq0b9dTkk+buoAKNtz0GzPQfKKpbs0NgaeKp+P7Ii5CiYnN3iQrf2FyvHzBdMehYc4T94B3O4NAAoviZCEQl7EhMsGuqEnPz9cqc3jKXuQqBg6YKQUAMEsFfMe/yx6py9oHFbxfXWMiqEswQKBgQDhgB3B7lCfqXaIPnrq06mdMLPqnwILHogf9PFz0KxR4NKZaOpulkhjCvvLpE+sX+EfC8SmZnoAdGYmZPF5RppisDXfeIIDt1ceaHQaVbvBWokpA2SqJLpyJ9sXKZ8+xqM0lHyx4BiB6z8r/pJR8ukmK7Q5nUjoa7YTRwAn9lr/VwKBgQCpTePYto6hr3+I7o3B/iMt+YzbkHxHb9dmqBkeFK3vIioljNbsD7eUw9jzS99NIPUNRTw5Rs6fFAHmnvMSEVMqchGPoDH2uHfxK00VRH4XvbCrQNck3xrcMRz4Q4WtsbK6uRvoUNFOSbHYA5pt7mVUwf2I8pSCaI1cXNR+JjRTKQKBgB/yq8SfA/Mq6i0xVO7SIBSyIrtZ3cs/fx+v70luRguvo4ayk4wpZIYLt1LJq7QLPXTNkQXWPPC1jQdhg8if1R2oQ2muxBTMs94OBGz6uogRUJ9r6KYmX/fuZ57nebVGJTc81lHJIw+9CY0tGwPbO2b6CCWlf//Vysz+YSpIP+ovAoGBAJD2IirCVOnXzIlASJxbr1+EFrlAw0ifWH2LXMZGyo7AX/n612MUKgn0juPyiOYPXALazy/zkqLejKocWt+TefQT6zlg3qbzV69ldgDJvnUxc/2N7Sii5uq2GqnvlpwcVH5QVLbby/sOWnosy6NoxdaGq0EBA2uNfcwhp8fDinXRAoGBALiSV2K4hSPjxqBG0U5NEUWMI+7Vl9Uev5rGzL0cW/3qU+6UDdh35RtWI/W8VxBHFpMLn3UoYAHhFRE50Q4Pk2d+K3lW3m9bst9PjcT0SZNutdFC+CSvyA+mqRkadIlJHGDwbbsKoyRUeofRDA9kVr+XOwsxgVDtnF7q5EWNNG+H";
            String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAlSIv4Xjysj4BdJDzaIIi5zYkYeCgegLZPCm1gTKYaB95JCvoVw4kgSzu+1YQ17P6uT5DDnU0AQdrEi/VXgYmSHAg8QBiWCdTQqLfK8z1kbX/8R6S787BJPEQtNUbKjEx01VUTArWo/RvMVdFcpTy48L3JywdpPUuhGWGPpIIWukmS3XvIENDQ31FBwdHpLRyt9uOFy8OKNH4g6eNTXjsyW5EV74gwqABOQ1akRY5mMelXzfJhKGW2o1YeqmAqg7le4vVv2QDZEvaadSNgRX/yR6b+u9aex+X6TbZR15LkJkGVNwlsghAQMErHo7SKF5cyc5lV0H+H+u1nUB6vQgZ7wIDAQAB";

            String en = RSA.encrypt("123", publicKey);

            System.out.println(RSA.decrypt(en, privateKey));

            System.out.println(RSA.getPrivateKey(privateKey));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

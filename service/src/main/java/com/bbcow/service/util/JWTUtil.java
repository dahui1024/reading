package com.bbcow.service.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.Verification;
import org.apache.commons.lang3.time.DateUtils;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;

public class JWTUtil {

    private static String privateKey = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCVIi/hePKyPgF0kPNogiLnNiRh4KB6Atk8KbWBMphoH3kkK+hXDiSBLO77VhDXs/q5PkMOdTQBB2sSL9VeBiZIcCDxAGJYJ1NCot8rzPWRtf/xHpLvzsEk8RC01RsqMTHTVVRMCtaj9G8xV0VylPLjwvcnLB2k9S6EZYY+kgha6SZLde8gQ0NDfUUHB0ektHK3244XLw4o0fiDp41NeOzJbkRXviDCoAE5DVqRFjmYx6VfN8mEoZbajVh6qYCqDuV7i9W/ZANkS9pp1I2BFf/JHpv671p7H5fpNtlHXkuQmQZU3CWyCEBAwSsejtIoXlzJzmVXQf4f67WdQHq9CBnvAgMBAAECggEAAYB6ovao2wqs0RO0n9TPrSxqaWHtu8zTizuQTGDKECJhAfA/QsEttO9S6M0RFlsFp1/tJPQhNfFxLpm0uGGveQNIEaVDWEftGrqxLKu/vVvr2+3G5Qtb/pc+59NNlrswGUS+uJVTXAtWI1URqfm+CHUqMAbNtZ+SfFrZYz/tPZ0ff7/wKd3z9vytlQBvZrhWg6KPaNHq0b9dTkk+buoAKNtz0GzPQfKKpbs0NgaeKp+P7Ii5CiYnN3iQrf2FyvHzBdMehYc4T94B3O4NAAoviZCEQl7EhMsGuqEnPz9cqc3jKXuQqBg6YKQUAMEsFfMe/yx6py9oHFbxfXWMiqEswQKBgQDhgB3B7lCfqXaIPnrq06mdMLPqnwILHogf9PFz0KxR4NKZaOpulkhjCvvLpE+sX+EfC8SmZnoAdGYmZPF5RppisDXfeIIDt1ceaHQaVbvBWokpA2SqJLpyJ9sXKZ8+xqM0lHyx4BiB6z8r/pJR8ukmK7Q5nUjoa7YTRwAn9lr/VwKBgQCpTePYto6hr3+I7o3B/iMt+YzbkHxHb9dmqBkeFK3vIioljNbsD7eUw9jzS99NIPUNRTw5Rs6fFAHmnvMSEVMqchGPoDH2uHfxK00VRH4XvbCrQNck3xrcMRz4Q4WtsbK6uRvoUNFOSbHYA5pt7mVUwf2I8pSCaI1cXNR+JjRTKQKBgB/yq8SfA/Mq6i0xVO7SIBSyIrtZ3cs/fx+v70luRguvo4ayk4wpZIYLt1LJq7QLPXTNkQXWPPC1jQdhg8if1R2oQ2muxBTMs94OBGz6uogRUJ9r6KYmX/fuZ57nebVGJTc81lHJIw+9CY0tGwPbO2b6CCWlf//Vysz+YSpIP+ovAoGBAJD2IirCVOnXzIlASJxbr1+EFrlAw0ifWH2LXMZGyo7AX/n612MUKgn0juPyiOYPXALazy/zkqLejKocWt+TefQT6zlg3qbzV69ldgDJvnUxc/2N7Sii5uq2GqnvlpwcVH5QVLbby/sOWnosy6NoxdaGq0EBA2uNfcwhp8fDinXRAoGBALiSV2K4hSPjxqBG0U5NEUWMI+7Vl9Uev5rGzL0cW/3qU+6UDdh35RtWI/W8VxBHFpMLn3UoYAHhFRE50Q4Pk2d+K3lW3m9bst9PjcT0SZNutdFC+CSvyA+mqRkadIlJHGDwbbsKoyRUeofRDA9kVr+XOwsxgVDtnF7q5EWNNG+H";
    private static String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAlSIv4Xjysj4BdJDzaIIi5zYkYeCgegLZPCm1gTKYaB95JCvoVw4kgSzu+1YQ17P6uT5DDnU0AQdrEi/VXgYmSHAg8QBiWCdTQqLfK8z1kbX/8R6S787BJPEQtNUbKjEx01VUTArWo/RvMVdFcpTy48L3JywdpPUuhGWGPpIIWukmS3XvIENDQ31FBwdHpLRyt9uOFy8OKNH4g6eNTXjsyW5EV74gwqABOQ1akRY5mMelXzfJhKGW2o1YeqmAqg7le4vVv2QDZEvaadSNgRX/yR6b+u9aex+X6TbZR15LkJkGVNwlsghAQMErHo7SKF5cyc5lV0H+H+u1nUB6vQgZ7wIDAQAB";

    public static String generateToken(String id, int validSecond){
        JWTCreator.Builder builder = JWT.create();

        try {
            Date now = new Date();
            builder.withJWTId(id);
            builder.withIssuedAt(now);
            builder.withSubject("signIn");
            builder.withIssuer("");
            if (validSecond > 0){
                builder.withExpiresAt(DateUtils.addSeconds(now, validSecond));
            }

            String token = builder.sign(Algorithm.RSA256((RSAPublicKey) RSA.getPublicKey(publicKey), (RSAPrivateKey) RSA.getPrivateKey(privateKey)));
            return token;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
    public static JWTDO decode(String token){
        return decode(token, 0);
    }
    public static JWTDO decode(String token, int leewaySecond){
        try {
            Algorithm algorithm = Algorithm.RSA256((RSAPublicKey) RSA.getPublicKey(publicKey), (RSAPrivateKey) RSA.getPrivateKey(privateKey));

            Verification verification = JWT.require(algorithm);

            verification.acceptLeeway(leewaySecond);

            JWTVerifier jwtVerifier = verification.build();
            DecodedJWT decodedJWT = jwtVerifier.verify(token);

            if (decodedJWT.getExpiresAt().after(new Date())) {
                return new JWTDO(decodedJWT.getId(), 0);
            }

            return new JWTDO(decodedJWT.getId(), 1);
        } catch (Exception e) {
            e.printStackTrace();
            return new JWTDO(null, -1);
        }
    }

    public static class JWTDO {
        String id;
        int status;

        JWTDO(String id, int status){
            this.id = id;
            this.status = status;
        }

    }
}

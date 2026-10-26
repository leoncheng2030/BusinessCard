
package vip.wqs.auth.core.util;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.ECDSASigner;
import com.nimbusds.jose.crypto.ECDSAVerifier;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.ECKey;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import vip.wqs.common.exception.CommonException;

import java.util.Map;

/**
 * Token工具类
 *
 * @author jetox
 * @date 2024/12/30 00:14
 **/
public class AuthTokenUtil {

    /**
     * 生成JWT令牌
     *
     * @param algorithm 算法，RS256或ES256
     * @param privateKeyBase64 私钥Base64
     * @param claims 声明
     * @return JWT令牌
     */
    public static String generateToken(String algorithm, String keyId, String privateKeyBase64, String publicKeyBase64, Map<String, Object> claims) {
        try {
            JWTClaimsSet claimsSet = JWTClaimsSet.parse(claims);
            if (JWSAlgorithm.RS256.getName().equalsIgnoreCase(algorithm)) {
                RSAKey rsaKey = AuthKeyUtil.getRasKey(keyId, privateKeyBase64, publicKeyBase64);
                JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.RS256)
                        .keyID(rsaKey.getKeyID()).build();
                SignedJWT jwt = new SignedJWT(header, claimsSet);
                jwt.sign(new RSASSASigner(rsaKey));
                return jwt.serialize();
            } else if (JWSAlgorithm.ES256.getName().equalsIgnoreCase(algorithm)) {
                ECKey ecKey = AuthKeyUtil.getEcKey(keyId, privateKeyBase64, publicKeyBase64);
                JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.ES256)
                        .keyID(ecKey.getKeyID()).build();
                SignedJWT jwt = new SignedJWT(header, claimsSet);
                jwt.sign(new ECDSASigner(ecKey));
                return jwt.serialize();
            } else {
                throw new CommonException("不支持的算法类型");
            }
        } catch (Exception e) {
            throw new CommonException("令牌生成失败：{}", e);
        }
    }

    /**
     * 验证JWT令牌
     *
     * @param algorithm 算法, RS256或ES256
     * @param publicKeyStr 公钥，Base64编码或JWKS字符串
     * @param token 令牌
     */
    public static boolean verifyToken(String algorithm, String publicKeyStr, String token) {
        try {
            SignedJWT jwt = SignedJWT.parse(token);
            String kid = jwt.getHeader().getKeyID();
            if (JWSAlgorithm.RS256.getName().equalsIgnoreCase(algorithm)) {
                return jwt.verify(new RSASSAVerifier(AuthKeyUtil.parseRSAPublicKeyFromStr(publicKeyStr, kid)));
            } else if (JWSAlgorithm.ES256.getName().equalsIgnoreCase(algorithm)) {
                return jwt.verify(new ECDSAVerifier(AuthKeyUtil.parseEcPublicKeyFromStr(publicKeyStr, kid)));
            }
            return false;
        } catch (Exception e) {
            throw new CommonException("令牌验证失败：{}", e);
        }
    }
}

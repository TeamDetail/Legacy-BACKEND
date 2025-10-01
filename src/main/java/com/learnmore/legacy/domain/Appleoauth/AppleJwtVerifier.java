package com.learnmore.legacy.domain.Appleoauth;

import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import java.net.URL;
import java.security.interfaces.RSAPublicKey;

public class AppleJwtVerifier {

    public static JWTClaimsSet verify(String idToken) throws Exception {
        // 1. String -> SignedJWT 변환
        SignedJWT signedJWT = SignedJWT.parse(idToken);

        // 2. 검증 로직 (서명 검증, 만료 체크 등)
        // 예: JWKSet에서 Apple 공개키 가져와 검증
        JWKSet jwkSet = JWKSet.load(new URL("https://appleid.apple.com/auth/keys"));
        JWK jwk = jwkSet.getKeyByKeyId(signedJWT.getHeader().getKeyID());

        if (!(jwk instanceof RSAKey rsaKey)) {
            throw new Exception("Apple 공개키 문제");
        }

        RSAPublicKey publicKey = rsaKey.toRSAPublicKey();
        JWSVerifier verifier = new RSASSAVerifier(publicKey);

        if (!signedJWT.verify(verifier)) {
            throw new Exception("Apple JWT 서명 검증 실패");
        }

        // 3. Claims 반환
        return signedJWT.getJWTClaimsSet();
    }
}



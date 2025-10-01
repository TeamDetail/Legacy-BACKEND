package com.learnmore.legacy.domain.Appleoauth;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import java.net.URL;
import java.util.List;

public class AppleJwtVerifier {

    private static final String APPLE_JWKS_URL = "https://appleid.apple.com/auth/keys";

    public static JWTClaimsSet verify(String idToken) throws Exception {
        SignedJWT signedJWT = SignedJWT.parse(idToken);

        // 1. Apple 공개키(JWKS) 가져오기
        JWKSet jwkSet = JWKSet.load(new URL(APPLE_JWKS_URL));
        List<JWK> keys = jwkSet.getKeys();

        // 2. 토큰의 kid와 alg 가져오기
        String kid = signedJWT.getHeader().getKeyID();
        String alg = signedJWT.getHeader().getAlgorithm().getName();

        // 3. kid에 맞는 공개키 찾기
        RSAKey rsaKey = keys.stream()
                .filter(jwk -> jwk.getKeyID().equals(kid))
                .findFirst()
                .map(jwk -> {
                    if (jwk instanceof RSAKey) {
                        return (RSAKey) jwk;
                    } else {
                        throw new IllegalArgumentException("Apple 공개키가 RSA 타입이 아닙니다.");
                    }
                })
                .orElseThrow(() -> new IllegalArgumentException("Apple 공개키를 찾을 수 없습니다."));


        // 4. 서명 검증
        if (!signedJWT.verify(new com.nimbusds.jose.crypto.RSASSAVerifier(rsaKey.toRSAPublicKey()))) {
            throw new IllegalArgumentException("Apple JWT 서명 검증 실패");
        }

        // 5. claim 반환
        return signedJWT.getJWTClaimsSet();
    }
}


package com.example.demo.utils;

import com.example.demo.domain.dto.AccountsDto;
import com.example.demo.domain.model.Accounts;
import com.example.demo.domain.model.InvalidationToken;
import com.example.demo.domain.model.RefreshToken;
import com.example.demo.domain.request.LogoutTokenRequest;
import com.example.demo.domain.request.RefreshTokenRequest;
import com.example.demo.domain.response.RefreshTokenResponse;
import com.example.demo.exception.TokenExpiredTimeException;
import com.example.demo.mappers.UserMapper;
import com.example.demo.repository.InvalidatedTokenRepository;
import com.example.demo.repository.RefreshTokenRepository;
import com.example.demo.repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

@RequiredArgsConstructor
@Component
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class JwtUtils {

    // Ve cơ bản thì 1 Jwt utils cần có các method như sau: generateToken, validateToken,
    // getUserNameFromToken, isExpired
    // Một JWT TOKEN cần đảm bảo có subject(username), issuer - từ đâu cấp, issuedAt - ngày tạo,
    // expirationTime, jwtId, claim là tùy chọn
    // 1 JWT Token bao gom 3 phần: header, payload, signature

   /* refresh token chỉ dùng 1 lần. nghĩa là dù refresh token đó chưa hết hạn nhưng đã dùng để get accessToken rồi, thì không thể dùng thêm lần nào nữa. Lúc này bạn cần cung cấp 1 refresh token mới.
    refresh token này sẽ k bao giờ hết hạn. Cái này không đúng
    Ví dụ, access token lifetime là 1 ngày, refresh token là 1 tuần.
    - Sau mỗi ngày, dùng refresh token để get access token và refresh token mới.
    - Giả sử người dùng không dùng trang web trong 1 tháng. cả access token và refesh token đều hết hạn. người dùng sẽ phải đăng nhập lại
    */


    @NonFinal
    @Value("${jwt.secret}")
    String SECRET_KEY;

    @NonFinal
    long EXPIRATION_TIME = 1000 * 60; //1phut

    long REFRESH_DURATION = 1000 * 60 * 2; //1phut

    InvalidatedTokenRepository invalidatedTokenRepository;

    RefreshTokenRepository refreshTokenRepository;

    UserMapper userMapper;

    UserRepository userRepository;

    public String generateAccessToken(AccountsDto user) {
        Date expirationTime = new Date(Instant.now().plus(EXPIRATION_TIME, ChronoUnit.SECONDS).toEpochMilli());
        return generateToken(user, expirationTime);
    }

    public String generateRefreshToken(AccountsDto user) {
        Date expirationTime = new Date(Instant.now().plus(REFRESH_DURATION, ChronoUnit.SECONDS).toEpochMilli());
        return generateToken(user, expirationTime);
    }

    public String generateRefreshToken(AccountsDto user, Date expiryTimeOldToken) {
        return generateToken(user, expiryTimeOldToken);
    }

    private String generateToken(AccountsDto user, Date expiration){
        // Đây la cách xây dựng JWT TOKEN dựa trên Nimbus thoe tiêu chuẩn JOSE (JAVA OBJECT SIGNING AND ENCRYPTION)
        // bảo mật các đối tượng dữ liệu json dưới hình thức ký số và mã hóa
        // trong Nimbus có các loại khái niệm quan trọng cac thành phân JOSE
        // JWS - JSON WEB SIGNATURE: đây là cái dùng để tạo ký số cho JSON nhằm đảm bảo xác thực tính toàn vẹn dữ liệu.
        //                              Cái dùng để setup thuật toán kí cho JWT khi tạo thành một JWT TOKEN
        // JWE - JSON WEB ENCRYPTION: đây là thành phần ẩn trong Nimbus JOSE giúp mã hóa dữ liệu JSON theo dạng
        //                              của JWS setup
        // JWK - JSON WEB KEY: đây là 1 định dạng Json web key dùng để trao đổi khóa giữa các third party
        // JWA - JSON WEB ALGORITHMS: thuật toán bảo mật cho JWS và JWE
        //==> JWSEAK

        try {
            byte[] keyBytes = SECRET_KEY.getBytes();
            if(keyBytes.length < 32) {
                // < 32 tức la dưới 256 kí tự bởi vì một byte = 1 kí tự = 8 bit
                throw new IllegalArgumentException("Secret key must be at least 256-bit (32 bytes)");
            }

            // Tạo ra header cho token
            JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.HS512)
                    .type(JOSEObjectType.JWT) // đặt rõ header này là một jwt dễ nhận biết và có một số cái cần chuẩn này để xác thực
                    .build();

            // Tạo ra payload thông qua claimset
            Instant now = Instant.now();
            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .subject(user.getUsername())
                    .jwtID(UUID.randomUUID().toString())
                    .issuer("devkaicld.com")
                    .issueTime(Date.from(now))
                    .notBeforeTime(Date.from(now))
                    .expirationTime(expiration)
                    .build();

            // tạo ra một jwt được kí tên
            SignedJWT signJWT = new SignedJWT(header, claimsSet);
            signJWT.sign(new MACSigner(SECRET_KEY.getBytes()));

            return signJWT.serialize(); // trả về jwt dạng string
        } catch (JOSEException e){
            throw new RuntimeException("Lỗi khi tạo JWT", e);
        }
    }

    public RefreshTokenResponse refreshToken(RefreshTokenRequest request) throws ParseException, JOSEException {
        SignedJWT signedJWT = validateToken(request.getRefreshToken());

        // time của token refresh cũ cần up vào time của refresh Token mới
        var expiredTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        Accounts loadedByUser = userRepository.findByUsername(signedJWT.getJWTClaimsSet().getSubject()).orElseThrow(
                () -> new RuntimeException("User not found")
        );

        String newAccessToken = generateAccessToken(userMapper.mapToDto(loadedByUser));
        String newRefreshToken = generateRefreshToken(userMapper.mapToDto(loadedByUser), expiredTime);

        RefreshToken refreshToken = RefreshToken.builder()
                .jwtId(UUID.randomUUID().toString())
                .token(newRefreshToken).expiryTime(expiredTime.toInstant())
                .userId(loadedByUser.getId())
                .revoked(false).createdAt(Instant.now()).build();

        if(!refreshToken.getExpiryTime().isAfter(Instant.now())) throw new TokenExpiredTimeException("Expired Time!");

        InvalidationToken invalidationToken = InvalidationToken.builder()
                .jwtId(signedJWT.getJWTClaimsSet().getJWTID())
                .build();

        invalidatedTokenRepository.save(invalidationToken);
        refreshTokenRepository.save(refreshToken);
        refreshTokenRepository.deleteById(signedJWT.getJWTClaimsSet().getJWTID());
        return RefreshTokenResponse.builder().newAccessToken(newAccessToken).newRefreshToken(newRefreshToken).build();
    }

    public SignedJWT validateToken(String token) throws ParseException, JOSEException {
        SignedJWT signedJWT = SignedJWT.parse(token);
        JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();
        Date expiryTime = claimsSet.getExpirationTime();
        boolean verified = signedJWT.verify(new MACVerifier(SECRET_KEY.getBytes()));

        if(!(verified & expiryTime.after(new Date()))) throw new RuntimeException("UNAUTHENTICATED");

        if(invalidatedTokenRepository.existsById(claimsSet.getJWTID())) throw new RuntimeException("UNAUTHENTICATED");

        return signedJWT;
    }

    public void logout(LogoutTokenRequest request) throws ParseException, JOSEException{
        var signToken = validateToken(request.getToken());
        String jit = signToken.getJWTClaimsSet().getJWTID();
        String username = signToken.getJWTClaimsSet().getSubject();

        Accounts loadedUser = userRepository.findByUsername(username).orElseThrow(
                () -> new RuntimeException("Unauthenticated")
        );

        InvalidationToken invalidatedToken = InvalidationToken.builder().jwtId(jit).build();
        refreshTokenRepository.deleteByUserId(loadedUser.getId());
        invalidatedTokenRepository.save(invalidatedToken);
    }

    public Date retrieveExpirationTime(String token) throws ParseException {
        return SignedJWT.parse(token).getJWTClaimsSet().getExpirationTime();
    }

    public String retrieveJwtId(String token) throws ParseException {
        return SignedJWT.parse(token).getJWTClaimsSet().getJWTID();
    }
}

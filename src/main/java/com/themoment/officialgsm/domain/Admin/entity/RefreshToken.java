package com.themoment.officialgsm.domain.Admin.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@RedisHash("refreshToken")
public class RefreshToken {
    @Id
    private String token;
    private String userId;
    @TimeToLive
    private Long expiredAt;

    public void exchangeRefreshToken(String token){
        this.token = token;
    }
}

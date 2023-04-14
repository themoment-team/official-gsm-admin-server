package com.themoment.officialgsm.domain.Admin.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@RedisHash("refreshToken")
public class RefreshToken {
    @Id
    private String userId;
    @Indexed
    private String token;
    @TimeToLive
    private Long expiredAt;

    public void exchangeRefreshToken(String token){
        this.token = token;
    }
}

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
@RedisHash("blackList")
public class BlackList {
    @Id
    private String userId;
    @Indexed
    private String accessToken;
    @TimeToLive
    private Long timeToLive;
}

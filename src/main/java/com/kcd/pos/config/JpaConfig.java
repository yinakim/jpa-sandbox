package com.kcd.pos.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing // AuditingEntityListener 사용하기 위함
public class JpaConfig {
}

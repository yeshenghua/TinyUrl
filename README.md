# TinyUrl
A SpringBoot project which lets user shorten their long url.
REST API has been used , MYSQL database and Redis Cache has been used for higher read throughput. A Client wise rate limiter has been introduced so that one client can only request the service maximum 5 times over a span of 2 seconds.

## Quick Start
```bash
# 先克隆仓库
git clone https://github.com/yourname/tinyurl.git
cd tinyurl

# 1️⃣ 运行单元测试
./mvnw clean test

# 2️⃣ 启动依赖（本地 MySQL & Redis，也可用 Docker）
docker-compose up -d mysql redis

# 3️⃣ 启动服务
./mvnw spring-boot:run
```

## Tech Stack
- **Java 17** & **Spring Boot 3.2**
- **MySQL 8.0** – 持久化短链 ↔ 长链映射
- **Redis 7.2** – L1 缓存，Key TTL 24 h
- **Guava RateLimiter** – 每个客户端 5 req / 2 s

## Roadmap (🟡 = 进行中, ✅ = 完成)
- 🟡 **Snowflake ID** ➜ Base62 编码
- ⬜ **/api/v1/shorten** REST + Swagger UI
- ⬜ **缓存穿透保护**（null cache + BloomFilter）
- ⬜ **wrk 压测**：目标 **10 k RPS / P99 < 80 ms**
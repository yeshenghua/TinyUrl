# TinyUrl
A SpringBoot project which lets user shorten their long url.
REST API has been used , MYSQL database and Redis Cache has been used for higher read throughput. A Client wise rate limiter has been introduced so that one client can only request the service maximum 5 times over a span of 2 seconds.
* **长链 → 短链** 6 字符随机码（后续可替换 Snowflake + Base62）
* **MySQL** 持久化、**Redis** 读缓存（TTL 24 h）
* **客户端限流**：同一 IP 5 req / 2 s
* RESTful API，默认运行在 `localhost:8080`

## Quick Start
### 先决条件

| 工具 | 版本建议 | 说明 |
|------|----------|------|
| **JDK** | 17 及以上 | `java -version` 验证 |
| **Maven** | 3.9+ | `mvn -v` 验证 |
| **Docker Desktop** | ≥ 4.x | 已内置 Docker Engine & Compose |

> Mac（Apple Silicon）用户已在 `docker-compose.yml` 里加了 `platform: linux/arm64`，直接用即可。

---

### 1. 拉起依赖服务（MySQL & Redis）
```bash
# 先克隆仓库
git clone https://github.com/yourname/tinyurl.git
cd tinyurl

# 1️⃣ 运行单元测试
mvnw clean test

# 2️⃣ 启动依赖（本地 MySQL & Redis，也可用 Docker）
docker-compose up -d mysql redis

# 3️⃣ 启动服务
mvnw spring-boot:run

# 4️⃣️ 如需停掉
docker compose down
```

### 2. 启动应用
```bash
# 编译 + 启动
mvn clean package           # 可选：跑一次编译 & 单元测试
mvn spring-boot:run         # 本地 8080 端口
```

首次运行日志里应看到：
Tomcat started on port(s): 8080 (http)
Started Application in x.x seconds

### 3. API试用
1. 生成短链
```bash
curl -s "http://localhost:8080/api/v1/shorten?url=https://www.example.com"
```
返回http://localhost:8080/api/v1/abc123（已带换行，粘贴即用）

2. 访问短链
```bash
# 也可浏览器打开上一步返回地址 
# 或运行下面命令
curl -I http://localhost:8080/api/v1/abc123
```
说明：302 跳转到原长链



## Tech Stack
- **Java 17** & **Spring Boot 3.2**
- **MySQL 8.0** – 持久化短链 ↔ 长链映射
- **Redis 7.2** – L1 缓存，Key TTL 24 h
- **Guava RateLimiter** – 每个客户端 5 req / 2 s
- **Maven** – 依赖管理 & 打包

## Roadmap (🟡 = 进行中, ✅ = 完成)
- 🟡 **Snowflake ID** ➜ Base62 编码
- ⬜ **/api/v1/shorten** REST + Swagger UI
- ⬜ **缓存穿透保护**（null cache + BloomFilter）
- ⬜ **wrk 压测**：目标 **10 k RPS / P99 < 80 ms**

## Common Questions
| **症状** | **解决办法** |
|----------|----------------------------------------------------------------------------------------------------------------------------------------|
| Access to DialectResolutionInfo cannot be null | 确认 `application.properties` 里的 `spring.datasource.*` 与 `docker-compose.yml` 一致（库名 `tinyurl`、密码 `root`）。 |
| cURL 输出末尾出现 `%` | 已在代码中 `ResponseEntity.ok(link + "\n")` 修复。 |
| 端口被占用 | 修改 `docker-compose.yml` 的 `3306:3306` / `6379:6379`，或在 `application.properties` 里改应用端口。 |
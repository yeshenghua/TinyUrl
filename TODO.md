## TODO / Roadmap 🗓 2025-06-20 Update
> 标注示例：🟡 进行中 ✅ 已完成 🚧 Blocked

### 🥇 近期（Week 1-3）
- [ ] 🟡 **Fork 骨架 & 单测跑通**
    - `./mvnw clean test` 通过 → README 更新 Quick Start
- [ ] **Snowflake ID → Base62 编码**
    - 依赖  `com.github.ben-manes.caffeine` 生成雪花算法
    - 单元测试：100 万次生成无重复
- [ ] **Redis 一级缓存接入**
    - 注解 `@Cacheable("urlMap")` + TTL 24 h
    - Null-Cache 保护缓存穿透
- [ ] **Guava RateLimiter**：每 IP 5 req / 2 s
    - 触发限流返回 HTTP 429

### 🥈 功能完善（Week 4-5）
- [ ] **/api/v1/shorten & /{code}** REST 接口
    - 参数校验（Hibernate Validator）
    - Swagger UI 文档
- [ ] **Prometheus Actuator Metrics**
    - 暴露 `http_server_requests_seconds`
    - Redis Cache Hit Ratio
- [ ] **wrk 压测脚本** (`wrk -t8 -c400 -d30s`)
    - 目标：**10 k RPS**、P99 < 80 ms
    - 压测报告保存至 `benchmarks/first-run.md`

### 🥉 可观测 & 稳定性（Week 6-7）
- [ ] **Zipkin 链路追踪**（Spring Cloud Sleuth）
- [ ] **Resilience4j Bulkhead + Retry**
    - 限制 DB 连接池并发；降级 fallback
- [ ] **Dockerfile & docker-compose.yml**
    - 多阶段构建 JAR；Compose 起 MySQL + Redis
- [ ] **GitHub Actions CI/CD**
    - `mvn test` → 构建镜像 → 推送 ghcr.io
    - Badge 添加到 README

### 🛠 优化 / 拓展（Week 8-9）
- [ ] **K8s Kind 部署**（Deployment + Service + Ingress）
    - HPA 基于 CPU / QPS 自动扩缩
- [ ] **Nacos 配置中心 + 灰度路由**
    - Canary 版本 v2 测试流量 10 %
- [ ] **Grafana Dashboard**
    - QPS、P99、Redis Hit 曲线 & 警报规则
- [ ] **最终压测**：`benchmarks/final-report.md`
    - 对比优化前后指标
    - 成果截图嵌入 `README`

### 🚀 里程碑交付物
- [ ] **Demo 视频**（≤ 3 min）上传 GitHub Release
- [ ] **Medium / 知乎 博客 2 篇**：
    1. “Java-Redis 短链服务 10 k RPS 优化实录”
    2. “从 0 到 K8s：短 URL 服务全链路可观测落地”  
# Ragna：多租户企业知识库 SaaS（RAG）

> 基于 **Java 21 + Spring Boot 3 + Spring AI 1.0** 的多租户企业知识库平台，支持多租户隔离、多格式文档导入、混合检索问答（RAG）、Function Calling、开放 API 与网页嵌入。
> 在线演示：（部署后补）　|　演示账号：（补）

## ✨ 功能特性

- **多租户 SaaS**：企业注册即开通独立租户，数据按租户隔离（JWT + 租户上下文 + API Key 双通道认证）
- **知识库管理**：支持 PDF / DOCX / TXT / Markdown 导入，自动解析、切分、向量化
- **RAG 问答**：向量 + 关键词（BM25）混合召回、RRF 融合、重排，答案标注来源，降低幻觉
- **多轮对话**：会话记忆（ChatMemory），流式输出（SSE）
- **Agent / Function Calling**：模型可调用自定义工具查询业务数据
- **开放 API + 网页嵌入**：对外问答接口，一段 JS 把问答组件嵌入第三方页面
- **运营看板**：问答量、命中率、badcase 标注与回流

## 🧱 技术栈

| 层        | 技术                                                     |
| --------- | -------------------------------------------------------- |
| 后端      | Java 21、Spring Boot 3.3、Spring AI 1.0 GA、MyBatis-Plus |
| 对话模型  | DeepSeek（deepseek-chat，OpenAI 兼容）                   |
| Embedding | 硅基流动 BAAI/bge-m3（1024 维）                          |
| 向量库    | PostgreSQL 16 + pgvector（HNSW + 余弦距离）              |
| 缓存      | Redis 7                                                  |
| 前端      | Vue 3 + Vite                                             |
| 部署      | Docker / docker-compose                                  |

## 🏗 系统架构

文档导入：`文件上传 → Tika 解析 → 按结构切分 chunk → Embedding 向量化 → 写入 pgvector`
问答链路：`用户提问 → 问题向量化 → 向量召回 + BM25 召回 → RRF 融合/重排 → 拼上下文 Prompt → LLM 流式生成 → 标注来源`
（架构图后面补到 docs/images/）

## 📁 项目结构

```text
ragna/
├── backend/src/main/java/com/ragna/
│   ├── common/     # 统一返回、全局异常
│   ├── config/     # 双模型配置
│   ├── auth/       # 注册登录、JWT
│   ├── tenant/     # 租户上下文、隔离
│   ├── knowledge/  # 知识库与文档管理
│   ├── rag/        # 切分、检索、重排
│   ├── chat/       # 对话、记忆、流式
│   ├── tool/       # Function Calling
│   ├── openapi/    # 开放 API、API Key
│   └── stats/      # 看板、badcase
├── frontend/       # Vue3 前端
├── embed/          # 网页嵌入脚本
├── docs/           # 设计文档、截图、笔记
└── docker-compose.yml
```

## 🚀 快速开始

```bash
# 1. 启动基础设施
docker compose up -d

# 2. 配置 Key（PowerShell）
$env:DEEPSEEK_API_KEY="你的 DeepSeek Key"
$env:SILICONFLOW_API_KEY="你的硅基流动 Key"

# 3. 启动后端
cd backend
mvn -DskipTests package
java -jar target/ragna.jar
```

浏览器打开 http://localhost:8080/api/ping ，返回 `status: UP` 即成功。

## 🔑 核心技术点（面试向）

- 多租户隔离：JWT 携带 tenantId → 请求进入写入 ThreadLocal → 查询统一带租户条件
- RAG 全链路：解析 → 切分（结构优先 + overlap + 父子块）→ Embedding → 向量检索
- 混合检索：向量（语义）+ BM25（关键词），RRF 融合，解决专业术语/编号匹配
- 防幻觉：Prompt 约束"只按资料答、没有就说没有、标来源"，召回分数阈值兜底转人工
- 知识更新：chunk 携带 doc_id，更新时按 doc_id 删旧块后重切重灌
- Function Calling：工具 description 写给模型看，写操作二次确认 + 限流

## 📌 开发进度

- [x] D1 环境搭建 + Spring Boot 骨架
- [ ] D2 多租户认证与隔离
- [ ] D3 知识库管理与文档导入
- [ ] D4 RAG 对话 + 混合检索
- [ ] D5 开放 API + 网页嵌入
- [ ] D6 管理看板 + badcase
- [ ] D7 Agent 扩展 + 安全
- [ ] D8 部署 + 演示

## 📄 License

MIT
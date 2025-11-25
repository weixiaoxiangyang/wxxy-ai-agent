# JavaAI面试官——AI超级智能体

## 1.项目名称（Project Name）

[JavaAI面试官——AI超级智能体] - [JavaAI interviewer - AI super intelligent agent]

## 2.项目简介（Project Introduction）

<img width="1206" height="737" alt="image" src="https://github.com/user-attachments/assets/7b54e3b3-b38f-4f36-aec0-dbd047ae16f3" />


### 2.1核心定位

基于 Spring Boot 3 + Spring AI + RAG + Tool Calling + MCP 的企业级 AI 面试官智能体，为用户提供不同级别的Java知识面试。支持多轮对话、记忆持久化、RAG 知识库检索等能力，并且基于 ReAct 模式，能够自主思考并调用工具来完成复杂任务，比如利用网页搜索、资源下载和 PDF 生成工具制定完整的提问流程并生成文档。

### 2.2技术栈（Technology Stack）

#### 后端技术

- Java 21 + Spring Boot 3 框架
- Spring AI + LangChain4j
- RAG 知识库
- PGvector 向量数据库
- Tool Calling 工具调用
- MCP 模型上下文协议
- ReAct Agent 智能体构建
- Serverless 计算服务
- AI 大模型开发平台台炼
- Cursor AI 代码生成
- SSE 异步推送
- 第三方接口：如 SearchAPI / Pexels API
- Ollama 大模型部署
- 工具库如：Kryo 高性能序列化 + Jsoup 网页抓取 + iText PDF 生成 + Knife4j 接口文档

#### 前端技术

- Vue3
- Vue Router
- Axios
- SSE (Server-Sent Events)

## 3.系统架构

<img width="530" height="703" alt="image" src="https://github.com/user-attachments/assets/075c5b47-db69-4011-8c87-26680188db7e" />


## 4.项目结构（Project Structure）

### 后端结构

```plaintext
wxxy-ai-agent/                   # 项目根目录
├── .idea/                      # IDEA 项目配置目录
├── .mvn/                       # Maven 相关配置（如_wrapper）
└── src/                        # 源代码目录
    └── main/                   # 主程序代码目录
        └── java/               # Java 源代码根目录
            └── com.yupi.yuaiagent/  # 基础包路径
                ├── advisor/        # 增强器/通知器（如 AOP 增强）
                ├── agent/          # 智能体核心模块（Agent 实现）
                ├── annotation/     # 自定义注解
                ├── aop/            # AOP 切面相关（如日志、权限切面）
                ├── app/            # 应用入口相关配置
                ├── chatmemory/     # 对话记忆模块（存储/管理对话历史）
                ├── common/         # 公共工具类、通用模型
                ├── config/         # 配置类（如 Bean 配置、框架配置）
                ├── constant/       # 常量定义（如枚举、固定值）
                ├── controller/     # 控制器（API 接口层）
                ├── demo.invoke/    # 调用示例模块（演示用）
                ├── exception/      # 异常处理（自定义异常、全局异常处理器）
                ├── generator/      # 生成器（如代码生成、内容生成）
                ├── mapper/         # 数据访问层（如 MyBatis Mapper）
                ├── model/          # 数据模型（实体类、DTO、VO）
                ├── rag/            # RAG 相关模块（检索增强生成）
                ├── service/        # 业务逻辑层（接口及实现）
                │   └── impl/       # 业务逻辑实现类
                ├── tools/          # 工具类模块（如第三方工具调用封装）
                └── WxxyAiAgentApplication.java  # 项目启动类
```

### 前端结构

```plaintext
wxxy-ai-agent-frontend/
├── public/                      # 静态资源（无需编译的图片、图标等）
├── src/
│   ├── api/                     # 接口请求封装（按模块拆分）
│   ├── assets/                  # 静态资源（图片、样式、字体等）
│   ├── components/              # 公共组件（全局组件、业务组件）
│   ├── router/                  # 路由配置（路由规则、权限守卫）
│   ├── store/                   # 状态管理（Pinia/Redux 配置）
│   ├── views/                   # 页面视图（按业务模块拆分文件夹）
│   ├── App.vue                  # 根组件
│   ├── main.ts                  # 入口文件
│   └── env.d.ts                 # 环境类型声明（TypeScript）
├── .env.development             # 开发环境配置
├── .env.production              # 生产环境配置
├── package.json                 # 依赖配置
└── vite.config.ts               # Vite 配置
```

## 5.功能模块（Function Modules）

列出项目核心功能模块及关键功能点，可采用表格或分点形式：

| 模块名称      | 核心功能                                                     |
| ------------- | ------------------------------------------------------------ |
| 用户管理模块  | 账号注册、登录（账号密码）                                   |
| JavaAI 面试官 | 用户可以Java相关知识点的面试，支持多轮对话、对话记忆持久化、RAG 知识库检索、工具调用、MCP 服务调用 |
| AI 超级智能体 | 可以根据用户的需求，自主推理和行动，直到完成目标。           |
<img width="570" height="544" alt="image" src="https://github.com/user-attachments/assets/f284992c-edf0-47f3-8bb0-8469ac3cbc9b" />
<img width="1206" height="767" alt="image" src="https://github.com/user-attachments/assets/6c7ab349-9071-4439-b7f0-e8007d49ddbc" />
<img width="1206" height="777" alt="image" src="https://github.com/user-attachments/assets/5c460335-496b-461f-a261-733d7845346b" />





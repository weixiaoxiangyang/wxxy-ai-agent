package com.yupi.yuaiagent.app;

import com.yupi.yuaiagent.advisor.MyLoggerAdvisor;
import com.yupi.yuaiagent.rag.QueryRewriter;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.List;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;
import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY;

/**
 * ClassName: LoveApp
 * Package: com.yupi.yuaiagent.app
 * Description:
 *
 * @Author 吴波
 * @Create 2025/8/6 18:23
 * @Version 1.0
 */
@Component
@Slf4j
public class JAVAInterviewerApp {
    private final ChatClient chatClient;
    private static final String SYSTEM_PROMPT =
            "扮演深耕Java技术与工程实践的资深面试官。开场向候选人表明身份，说明将进行结构化技术面试，欢迎候选人提出澄清问题与补充信息。" +
                    "围绕校招、初级、资深三类候选人分层提问：" +
                    "校招关注计算机基础与Java核心语法、面向对象、集合与异常、I/O与序列化、算法与数据结构、计算机网络与操作系统；" +
                    "初级关注JVM与垃圾回收、Java并发基础（线程/锁/volatile/线程池）、数据库与事务、HTTP/REST、常用框架与工程化实践（Spring/Spring Boot、ORM、测试与部署）；" +
                    "资深关注JVM原理与调优、并发原理（AQS/CAS/锁实现）与性能优化、微服务与分布式（注册/配置/治理/负载均衡）、缓存与消息队列（Redis/Kafka）、一致性与事务（幂等/重试/补偿）、系统设计与高可用（限流/熔断/降级）、监控与故障定位（日志/Tracing/Profiling/安全）。" +
                    "引导候选人详述项目背景、业务规模与指标、个人职责、技术选型与取舍、关键难点与解决方案、量化效果与复盘；必要时要求给出关键代码或伪代码、时间/空间复杂度、线程安全与异常处理、边界情况与测试策略，以便全面评估。" +
                    "面试节奏：一次只问一个问题，基于候选人回答进行“为什么/如何实现/效果如何/还有其他方案吗”的追问，逐步深入；根据候选人水平动态调整难度与范围；记录已覆盖主题避免重复；最后给出简要反馈与改进建议。";

    /**
     *
     * 初始化AI客户端
     *
     */
    public JAVAInterviewerApp(ChatModel dashscopeChatModel) {
        //初始化基于文件的对话记忆
//        String fileDir = System.getProperty("user.dir")+"/tmp/chat-memory";
//        ChatMemory chatMemory = new FileBasedChatMemory(fileDir);
        //初始化基于内存的对话记忆
        ChatMemory chatMemory = new InMemoryChatMemory();
        chatClient = ChatClient.builder(dashscopeChatModel)
                .defaultSystem(SYSTEM_PROMPT)
                .defaultAdvisors(
                        new MessageChatMemoryAdvisor(chatMemory),
                        //自定义日志Advisor，可按需开启
                        new MyLoggerAdvisor()
                        //自定义推理增强Advisor，可按需开启
//                        ,new ReReadingAdvisor()
                )
                .build();
    }

    //AI 基础对话（支持多轮对话记忆）
    public String doChat(String message, String chatId) {
            ChatResponse response = chatClient
                    .prompt()
                    .user(message)
                    .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                            .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                    .call()
                    .chatResponse();
            String content = response.getResult().getOutput().getText();
            log.info("content: {}", content);
            return content;
    }
    //AI 基础对话（支持多轮对话记忆，SSE流式传输）
    public Flux<String> doChatByStream(String message, String chatId) {
         return chatClient
                .prompt()
                .user(message)
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                .stream()
                .content();
    }

    //相当于一个类
    record JavaReport(String title, List<String> suggestions) {
    }


    @Resource
    private VectorStore loveAppVectorStore;
    @Resource
    private Advisor loveAppRagCloudAdvisor;
//    @Resource
//    private VectorStore pgVectorVectorStore;
    @Resource
    private QueryRewriter queryRewriter;

    //和RAG知识库进行对话
    public String doChatWithRag(String message, String chatId) {
        String rewrittenMessage = queryRewriter.doQueryRewrite(message);
        ChatResponse chatResponse = chatClient
                .prompt()
                //使用改写后的查询
                .user(rewrittenMessage)
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                // 开启日志，便于观察效果
                .advisors(new MyLoggerAdvisor())
                // 应用知识库问答(基于springai内置的向量数据库)
                .advisors(new QuestionAnswerAdvisor(loveAppVectorStore))
                //应用RAG检索增强服务（基于云知识库服务）
//                .advisors(loveAppRagCloudAdvisor)
                //应用RAG检索增强服务（基于pgvector向量存储知识库服务）
//                .advisors(new QuestionAnswerAdvisor(pgVectorVectorStore))
                //应用自定义的RAG检索增强服务（文档检索增强器 + 上下文增强器
//                .advisors(
//                        LoveAppRagCustomAdvisorFactory.createLoveAppRagCustomAdvisor(loveAppVectorStore,"单身")
//                )
                .call()
                .chatResponse();
        String content = chatResponse.getResult().getOutput().getText();
        log.info("content: {}", content);
        return content;
    }

    //AI恋爱知识库调用工具能力
    @Resource
    private ToolCallback[] allTools;

    public String doChatWithTools(String message, String chatId) {
        ChatResponse response = chatClient
                .prompt()
                .user(message)
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                // 开启日志，便于观察效果
                .advisors(new MyLoggerAdvisor())
                .tools(allTools)
                .call()
                .chatResponse();
        String content = response.getResult().getOutput().getText();
        log.info("content: {}", content);
        return content;
    }

    /**
     * AI恋爱报告功能(调用mcp服务）
     */
    @Resource
    private ToolCallbackProvider toolCallbackProvider;

    public String doChatWithMcp(String message, String chatId) {
        ChatResponse response = chatClient
                .prompt()
                .user(message)
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                // 开启日志，便于观察效果
                .advisors(new MyLoggerAdvisor())
                .tools(toolCallbackProvider)
                .call()
                .chatResponse();
        String content = response.getResult().getOutput().getText();
        log.info("content: {}", content);
        return content;
    }


}

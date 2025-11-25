package com.yupi.yuaiagent.agent.model;

/**
 * ClassName: AgentState
 * Package: com.yupi.yuaiagent.agent.model
 * Description:代理执行状态的枚举类
 *
 * @Author 吴波
 * @Create 2025/8/29 10:00
 * @Version 1.0
 */
public enum AgentState {

    /**
     * 空闲状态
     */
    IDLE,

    /**
     * 运行中状态
     */
    RUNNING,

    /**
     * 已完成状态
     */
    FINISHED,

    /**
     * 错误状态
     */
    ERROR
}


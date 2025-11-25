package com.yupi.yuaiagent.agent;

import com.yupi.yuaiagent.agent.model.AgentState;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * ClassName: ReActAgent
 * Package: com.yupi.yuaiagent.agent.model
 * Description:ReAct (Reasoning and Acting) 模式的代理抽象类
 *实现了思考-行动的循环模式
 * @Author 吴波
 * @Create 2025/8/29 10:48
 * @Version 1.0
 */
//如果你的类有继承，并且父类里有决定“对象标识/相等性”的字段，那么应该设为 true
@EqualsAndHashCode(callSuper = true)
@Data
public abstract class ReActAgent extends BaseAgent {
    /**
     * 处理当前状态并决定下一步行动
     *
     * @return 是否需要执行行动，true表示需要执行，false表示不需要执行
     */
    public abstract boolean think();

    /**
     * 执行决定的行动
     *
     * @return 行动执行结果
     */
    public abstract String act();

    /**
     * 执行单个步骤：思考和行动
     *
     * @return 步骤执行结果
     */
    @Override
    public String step() {
        try {
            boolean shouldAct = think();
            if (!shouldAct) {
                setState(AgentState.FINISHED);
                return "思考完成 - 无需行动";
            }
            return act();
        } catch (Exception e) {
            // 记录异常日志
            e.printStackTrace();
            return "步骤执行失败: " + e.getMessage();
        }
    }
}

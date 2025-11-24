package com.yupi.yuaiagent.tools;

import org.springframework.ai.tool.annotation.Tool;

/**
 * ClassName: TerminateTool
 * Package: com.yupi.yuaiagent.tools
 * Description: 终止工具
 *
 * @Author 吴波
 * @Create 2025/9/1 9:52
 * @Version 1.0
 */
public class TerminateTool {
    @Tool(description = """  
            Terminate the interaction when the request is met OR if the assistant cannot proceed further with the task.  
            "When you have finished all the tasks, call this tool to end the work.  
            """)
    public String doTerminate() {
        return "任务结束";
    }

}

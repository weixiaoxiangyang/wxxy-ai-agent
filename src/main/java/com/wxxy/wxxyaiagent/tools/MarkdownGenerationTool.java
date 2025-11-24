package com.yupi.yuaiagent.tools;

import cn.hutool.core.io.FileUtil;
import com.yupi.yuaiagent.constant.FileConstant;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Markdown 文件生成工具
 */
public class MarkdownGenerationTool {

    @Tool(description = "Generate a Markdown file with given content", returnDirect = false)
    public String generateMarkdown(
            @ToolParam(description = "Name of the file to save the generated Markdown") String fileName,
            @ToolParam(description = "Content to be included in the Markdown file") String content) {
        String fileDir = FileConstant.FILE_SAVE_DIR + "/markdown";
        String filePath = fileDir + "/" + fileName;
        
        try {
            // 创建目录
            FileUtil.mkdir(fileDir);
            // 写入Markdown内容到文件
            FileUtil.writeString(content, filePath, StandardCharsets.UTF_8);
            return "Markdown file generated successfully to: " + filePath;
        } catch (Exception e) {
            return "Error generating Markdown file: " + e.getMessage();
        }
    }
}
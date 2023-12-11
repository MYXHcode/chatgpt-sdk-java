package com.myxh.chatgpt.session;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

/**
 * @author MYXH
 * @date 2023/12/11
 * @description 配置信息
 * @GitHub <a href="https://github.com/MYXHcode">MYXHcode</a>
 */
@Getter
@Slf4j
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Configuration
{
    @NotNull
    private String apiKey;

    private String apiHost;

    // @NotNull
    private String authToken;
}

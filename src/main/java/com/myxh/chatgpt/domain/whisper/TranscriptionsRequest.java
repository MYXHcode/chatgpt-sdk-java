package com.myxh.chatgpt.domain.whisper;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;

/**
 * @author MYXH
 * @date 2024/1/6
 * @description Transcriptions 请求
 * @GitHub <a href="https://github.com/MYXHcode">MYXHcode</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TranscriptionsRequest
{
    /**
     * 模型：WHISPER_1
     */
    @Builder.Default
    private String model = WhisperEnum.Model.WHISPER_1.getCode();

    /**
     * 提示语
     */
    private String prompt;

    /**
     * 输出格式
     */
    @JsonProperty("response_format")
    @Builder.Default
    private String responseFormat = WhisperEnum.ResponseFormat.JSON.getCode();

    /**
     * 控制温度【随机性】：0 到 2 之间。较高的值(如 0.8)将使输出更加随机，而较低的值(如 0.2)将使输出更加集中和确定
     */
    private double temperature = 0.2;

    /**
     * 音频语言：ISO-639-1
     */
    private String language;
}

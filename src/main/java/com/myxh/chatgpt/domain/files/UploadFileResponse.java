package com.myxh.chatgpt.domain.files;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author MYXH
 * @date 2024/1/6
 * @description 上传文件应答：封装个空的文件应答结果对象
 * @GitHub <a href="https://github.com/MYXHcode">MYXHcode</a>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UploadFileResponse extends File implements Serializable
{

}

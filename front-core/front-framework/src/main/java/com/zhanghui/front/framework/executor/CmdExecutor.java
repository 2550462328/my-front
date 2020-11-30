package com.zhanghui.front.framework.executor;

import com.zhanghui.front.framework.executor.job.Job;

/**
 * @author: ZhangHui
 * @date: 2020/11/13 10:37
 * @version：1.0
 */
public interface CmdExecutor {
    void submit(Job job);
}

package com.zhanghui.front.framework.executor;

import com.zhanghui.front.framework.annotation.BeanIfMissing;
import com.zhanghui.front.framework.executor.job.Job;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author: ZhangHui
 * @date: 2020/11/13 10:36
 * @version：1.0
 */
@Slf4j
@BeanIfMissing
public class DefaultCmdExecutor implements CmdExecutor {

    private ThreadPoolExecutor poolExecutor;

    public DefaultCmdExecutor() {
        this.poolExecutor = new ThreadPoolExecutor(500, 3000, 60L, TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue(500), new ThreadPoolExecutor.AbortPolicy());
    }

    @Override
    public void submit(Job job) {
        poolExecutor.execute(job);
    }
}

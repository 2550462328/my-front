package com.zhanghui.front.framework.executor.connector;

import com.zhanghui.front.framework.executor.bean.CmdAnswer;

/**
 * @author: ZhangHui
 * @date: 2020/11/13 10:41
 * @version：1.0
 */
public interface AnswerAble {
    void answer(CmdAnswer cmdAnswer, String tradeCode);
}

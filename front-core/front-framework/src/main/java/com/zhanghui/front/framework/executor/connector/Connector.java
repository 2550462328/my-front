package com.zhanghui.front.framework.executor.connector;

/**
 * @author: ZhangHui
 * @date: 2020/11/12 15:39
 * @version：1.0
 */
public interface Connector {
    /**
     * Start the protocol.
     */
    void start();

    /**
     * Pause the protocol (optional).
     */
    void pause() ;

    /**
     * Resume the protocol (optional).
     */
    void resume() ;

    /**
     * Destroy the protocol (optional).
     */
    void destroy() ;
}

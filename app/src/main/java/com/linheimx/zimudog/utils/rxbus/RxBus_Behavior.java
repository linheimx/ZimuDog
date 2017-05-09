package com.linheimx.zimudog.utils.rxbus;

import io.reactivex.Flowable;
import io.reactivex.processors.BehaviorProcessor;
import io.reactivex.processors.FlowableProcessor;

/**
 * Created by x1c on 2017/5/6.
 */

public class RxBus_Behavior {

    private final FlowableProcessor<Object> _Bus;
    private static volatile RxBus_Behavior sRxBus = null;

    private RxBus_Behavior() {
        _Bus = BehaviorProcessor.create().toSerialized();
    }

    public static synchronized RxBus_Behavior getInstance() {
        if (sRxBus == null) {
            synchronized (RxBus_Behavior.class) {
                if (sRxBus == null) {
                    sRxBus = new RxBus_Behavior();
                }
            }
        }
        return sRxBus;
    }


    /**
     * 发送消息
     *
     * @param o
     */
    public void post(Object o) {
        _Bus.onNext(o);
    }

    /**
     * 确定接收消息的类型
     *
     * @param aClass
     * @param <T>
     * @return
     */
    public <T> Flowable<T> toFlowable(Class<T> aClass) {
        return _Bus.ofType(aClass);
    }

    /**
     * 判断是否有订阅者
     *
     * @return
     */
    public boolean hasSubscribers() {
        return _Bus.hasSubscribers();
    }

}

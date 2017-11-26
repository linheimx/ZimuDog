package com.linheimx.zimudog.utils.rxbus

import io.reactivex.Flowable
import io.reactivex.processors.FlowableProcessor
import io.reactivex.processors.PublishProcessor
import io.reactivex.subscribers.SerializedSubscriber

/**
 * Created by x1c on 2017/5/6.
 */

object RxBus {

    private val _Bus: FlowableProcessor<Any>

    init {
        _Bus = PublishProcessor.create<Any>().toSerialized()
    }


    /**
     * 发送消息
     *
     * @param o
     */
    fun post(o: Any) {
        _Bus.onNext(o)
    }

    /**
     * 确定接收消息的类型
     *
     * @param aClass
     * @param <T>
     * @return
    </T> */
    fun <T> toFlowable(aClass: Class<T>): Flowable<T> {
        return _Bus.ofType(aClass)
    }

    /**
     * 判断是否有订阅者
     *
     * @return
     */
    fun hasSubscribers(): Boolean {
        return _Bus.hasSubscribers()
    }

}

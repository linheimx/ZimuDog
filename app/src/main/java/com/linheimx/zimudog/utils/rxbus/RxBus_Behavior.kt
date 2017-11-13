package com.linheimx.zimudog.utils.rxbus

import io.reactivex.Flowable
import io.reactivex.processors.BehaviorProcessor
import io.reactivex.processors.FlowableProcessor

/**
 * Created by x1c on 2017/5/6.
 */

class RxBus_Behavior private constructor() {

    private val _Bus: FlowableProcessor<Any>

    init {
        _Bus = BehaviorProcessor.create<Any>().toSerialized()
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

    companion object {
        @Volatile private var sRxBus: RxBus_Behavior? = null

        val instance: RxBus_Behavior?
            @Synchronized get() {
                if (sRxBus == null) {
                    synchronized(RxBus_Behavior::class.java) {
                        if (sRxBus == null) {
                            sRxBus = RxBus_Behavior()
                        }
                    }
                }
                return sRxBus
            }
    }

}

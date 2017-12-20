package com.song.fastmq.client.impl

import com.song.fastmq.client.utils.ClientUtils
import io.openmessaging.*
import io.openmessaging.observer.Observer

/**
 * @author song
 */
class MessagingAccessPointImpl(val properties: KeyValue) : MessagingAccessPoint {

    override fun startup() {
    }

    override fun createSequenceProducer(): SequenceProducer {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun createSequenceProducer(properties: KeyValue?): SequenceProducer {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun createIterableConsumer(queueName: String?): IterableConsumer {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun createIterableConsumer(queueName: String?, properties: KeyValue?): IterableConsumer {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun addObserver(observer: Observer?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getResourceManager(): ResourceManager {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun properties(): KeyValue {
        return this.properties
    }

    override fun createProducer(): Producer {
        return DefaultProducer(this.properties)
    }

    override fun createProducer(properties: KeyValue): Producer {
        return DefaultProducer(ClientUtils.buildKeyValue(this.properties, properties))
    }

    override fun createPushConsumer(): PushConsumer {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun createPushConsumer(properties: KeyValue?): PushConsumer {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun createPullConsumer(queueName: String?): PullConsumer {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun createPullConsumer(queueName: String?, properties: KeyValue?): PullConsumer {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun createServiceEndPoint(): ServiceEndPoint {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun createServiceEndPoint(properties: KeyValue?): ServiceEndPoint {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun shutdown() {
    }

    override fun deleteObserver(observer: Observer?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
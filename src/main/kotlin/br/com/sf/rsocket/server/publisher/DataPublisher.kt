package br.com.sf.rsocket.server.publisher

import io.rsocket.Payload
import org.reactivestreams.Publisher
import org.reactivestreams.Subscriber


class DataPublisher : Publisher<Payload> {

    private var subscriber: Subscriber<in Payload>? = null
    override fun subscribe(subscriber: Subscriber<in Payload>) {
        this.subscriber = subscriber
    }

    fun publish(payload: Payload) {
        subscriber?.onNext(payload)
    }

    fun complete() {
        subscriber?.onComplete()
    }
}

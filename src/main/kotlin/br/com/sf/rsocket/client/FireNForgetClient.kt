package br.com.sf.rsocket.client

import br.com.sf.rsocket.server.Server
import io.rsocket.Payload
import io.rsocket.RSocket
import io.rsocket.RSocketFactory
import io.rsocket.transport.netty.client.TcpClientTransport
import io.rsocket.util.DefaultPayload
import reactor.core.publisher.Flux
import java.nio.ByteBuffer
import java.time.Duration
import java.util.*


class FireNForgetClient {

    private val socket: RSocket = RSocketFactory.connect()
        .transport(TcpClientTransport.create("localhost", Server.TCP_PORT))
        .start()
        .block()!!

    private var data: List<Float> = Collections.unmodifiableList(generateData())

    /**
     * Send binary velocity (float) every 50ms
     */
    fun sendData() {
        Flux.interval(Duration.ofMillis(500))
            .take(data.size.toLong())
            .map(::createFloatPayload)
            .flatMap { payload: Payload? ->
                socket.fireAndForget(
                    payload
                )
            }
            .blockLast()
    }

    private fun generateData(): List<Float> {
        val dataList: MutableList<Float> = ArrayList<Float>(DATA_LENGTH)
        var velocity = 0f
        for (i in 0..<DATA_LENGTH) {
            velocity += Math.random().toFloat()
            dataList.add(velocity)
        }
        return dataList
    }

    /**
     * Create a binary payload containing a single float value
     *
     * @param index Index into the data list
     * @return Payload ready to send to the server
     */
    private fun createFloatPayload(index: Long): Payload {
        val velocity = data[index.toInt()]
        val buffer: ByteBuffer = ByteBuffer.allocate(4).putFloat(velocity)
        buffer.rewind()
        return DefaultPayload.create(buffer)
    }

    fun dispose() {
        socket.dispose()
    }

    fun getData() = data

    companion object {
        const val DATA_LENGTH = 30
    }
}

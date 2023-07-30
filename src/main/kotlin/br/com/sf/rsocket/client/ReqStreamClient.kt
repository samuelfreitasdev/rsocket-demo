package br.com.sf.rsocket.client

import br.com.sf.rsocket.server.Server
import io.rsocket.Payload
import io.rsocket.RSocket
import io.rsocket.RSocketFactory
import io.rsocket.transport.netty.client.TcpClientTransport
import io.rsocket.util.DefaultPayload
import reactor.core.publisher.Flux
import java.nio.ByteBuffer


class ReqStreamClient {

    private val socket: RSocket = RSocketFactory.connect()
        .transport(TcpClientTransport.create("localhost", Server.TCP_PORT))
        .start()
        .block()!!

    fun getDataStream(): Flux<Float> {
        return socket
            .requestStream(DefaultPayload.create(DATA_STREAM_NAME))
            .map { obj: Payload -> obj.data }
            .map { buf: ByteBuffer -> buf.getFloat() }
            .onErrorReturn(0F)
    }

    fun dispose() {
        socket.dispose()
    }

    companion object {
        const val DATA_STREAM_NAME = "data"
    }

}

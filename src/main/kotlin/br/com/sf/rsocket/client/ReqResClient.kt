package br.com.sf.rsocket.client

import br.com.sf.rsocket.server.Server
import io.rsocket.Payload
import io.rsocket.RSocket
import io.rsocket.RSocketFactory
import io.rsocket.transport.netty.client.TcpClientTransport
import io.rsocket.util.DefaultPayload


class ReqResClient {

    private val socket: RSocket = RSocketFactory.connect()
        .transport(TcpClientTransport.create("localhost", Server.TCP_PORT))
        .start()
        .block()!!

    fun callBlocking(string: String): String {
        return socket
            .requestResponse(DefaultPayload.create(string))
            .map { obj: Payload -> obj.dataUtf8 }
            .block()!!
    }

    fun dispose() {
        socket.dispose()
    }

}

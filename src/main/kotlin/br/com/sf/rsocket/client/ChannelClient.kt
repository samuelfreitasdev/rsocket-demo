package br.com.sf.rsocket.client

import br.com.sf.rsocket.server.Server.Companion.TCP_PORT
import br.com.sf.rsocket.server.publisher.GameController
import io.rsocket.RSocketFactory
import io.rsocket.transport.netty.client.TcpClientTransport
import reactor.core.publisher.Flux


class ChannelClient {

    private val gameController: GameController = GameController("Client Player")

    private val socket = RSocketFactory.connect()
        .transport(TcpClientTransport.create("localhost", TCP_PORT))
        .start()
        .block()!!

    fun playGame() {
        socket.requestChannel(Flux.from(gameController))
            .doOnNext(gameController::processPayload)
            .blockLast()
    }

    fun dispose() {
        socket.dispose()
    }
}

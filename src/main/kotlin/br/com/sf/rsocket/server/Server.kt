package br.com.sf.rsocket.server

import br.com.sf.rsocket.server.publisher.DataPublisher
import br.com.sf.rsocket.server.publisher.GameController
import io.rsocket.*
import io.rsocket.transport.netty.server.TcpServerTransport
import org.reactivestreams.Publisher
import reactor.core.Disposable
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.logging.Logger


class Server {

    private val LOG: Logger = Logger.getLogger(Server::class.java.name)

    private val dataPublisher: DataPublisher = DataPublisher()

    private val gameController: GameController = GameController("Server Player")

    private val server: Disposable = RSocketFactory.receive()
        .acceptor { _: ConnectionSetupPayload?, _: RSocket -> Mono.just(RSocketImpl(dataPublisher, gameController)) }
        .transport(TcpServerTransport.create("localhost", TCP_PORT))
        .start()
        .doOnNext { LOG.info("Server started") }
        .subscribe()

    fun dispose() {
        dataPublisher.complete()
        server.dispose()
    }


    private class RSocketImpl(
        private val dataPublisher: DataPublisher,
        private val gameController: GameController
    ) : AbstractRSocket() {
        override fun requestResponse(payload: Payload): Mono<Payload> {
            return try {
                Mono.just(payload) // reflect the payload back to the sender
            } catch (x: Exception) {
                Mono.error(x)
            }
        }

        override fun fireAndForget(payload: Payload): Mono<Void> {
            return try {
                dataPublisher.publish(payload) // forward the payload
                Mono.empty()
            } catch (x: java.lang.Exception) {
                Mono.error(x)
            }
        }

        override fun requestStream(payload: Payload): Flux<Payload> {
            return Flux.from(dataPublisher)
        }

        override fun requestChannel(payloads: Publisher<Payload>): Flux<Payload> {
            Flux.from(payloads)
                .subscribe(gameController::processPayload)
            return Flux.from(gameController)
        }
    }

    companion object {
        const val TCP_PORT = 7101
    }


}

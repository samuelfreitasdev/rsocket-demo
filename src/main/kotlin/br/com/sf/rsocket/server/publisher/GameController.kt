package br.com.sf.rsocket.server.publisher

import io.rsocket.Payload
import io.rsocket.util.DefaultPayload
import org.reactivestreams.Publisher
import org.reactivestreams.Subscriber
import reactor.core.publisher.Flux
import java.util.logging.Logger
import kotlin.math.ceil


class GameController(private val playerName: String) : Publisher<Payload> {

    private val LOG: Logger = Logger.getLogger(GameController::class.java.name)

    private val shots: List<Long> = generateShotList()
    private var truce = false

    private lateinit var subscriber: Subscriber<in Payload>

    /**
     * Create a random list of time intervals, 0-1000ms
     *
     * @return List of time intervals
     */
    private fun generateShotList(): List<Long> {
        return Flux.range(1, SHOT_COUNT)
            .map { _: Int -> ceil(Math.random() * 1000) }
            .map { it.toLong() }
            .collectList()
            .block()!!
    }

    override fun subscribe(subscriber: Subscriber<in Payload>) {
        this.subscriber = subscriber
        fireAtWill()
    }

    /**
     * Publish game events asynchronously
     */
    private fun fireAtWill() {
        val generator = Runnable {
            for (shotDelay in shots) {
                try {
                    Thread.sleep(shotDelay)
                    if (truce) {
                        break
                    }

                    LOG.info("$playerName: bang!")
                    val payload = DefaultPayload.create("bang!")
                    subscriber.onNext(payload)
                } catch (x: InterruptedException) {
                    LOG.severe(x.message)
                }
            }

            if (!truce) {
                LOG.info("${playerName}: I give up!")
                subscriber.onNext(DefaultPayload.create("I give up"))
            }
            subscriber.onComplete()
        }

        Thread(generator).start()
    }

    fun processPayload(payload: Payload) {
        val message = payload.dataUtf8
        when (message) {
            "bang!" -> {
                val result = if (Math.random() < 0.5) "Haha missed!" else "Ow!"
                LOG.info("$playerName: $result")
            }

            "I give up" -> {
                truce = true
                LOG.info("${playerName}: OK, truce")
            }
        }
    }

    companion object {
        private const val SHOT_COUNT = 10
    }
}

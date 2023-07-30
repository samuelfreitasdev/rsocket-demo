package br.com.sf.rsocket

import br.com.sf.rsocket.client.ChannelClient
import br.com.sf.rsocket.client.FireNForgetClient
import br.com.sf.rsocket.client.ReqResClient
import br.com.sf.rsocket.client.ReqStreamClient
import br.com.sf.rsocket.server.Server
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import reactor.core.Disposable
import java.util.logging.Level
import java.util.logging.Logger


class RSocketIntegrationTest {

    private val LOG: Logger = Logger.getLogger(RSocketIntegrationTest::class.java.name)

    private var server: Server? = null

    @BeforeEach
    fun setUp() {
        server = Server()
    }

    @AfterEach
    fun tearDown() {
        server!!.dispose()
    }

    @Test
    fun `when sending a string then receive the same string`() {
        val client = ReqResClient()
        val string = "Hello RSocket"
        val receivedValue = client.callBlocking(string)

        assertEquals(string, receivedValue)
        client.dispose()
    }

    @Test
    fun `when sending stream then receive the same stream`() {
        // create the client that pushes data to the server and start sending
        val fnfClient = FireNForgetClient()
        // create a client to read a stream from the server and subscribe to events
        val streamClient = ReqStreamClient()

        // get the data that is used by the client
        val data: List<Float> = fnfClient.getData()
        // create a place to count the results
        val dataReceived: MutableList<Float> = ArrayList()

        // assert that the data received is the same as the data sent
        val subscription: Disposable = streamClient.getDataStream()
            .index()
            .subscribe(
                { tuple ->
                    assertEquals(data[tuple.t1.toInt()], tuple.t2, "Wrong value")
                    dataReceived.add(tuple.t2)
                    LOG.info("Received: ${tuple.t1}: ${tuple.t2}")
                }
            ) { err -> LOG.log(Level.SEVERE, err.message) }

        // start sending the data
        fnfClient.sendData()

        // wait a short time for the data to complete then dispose everything
        try {
            Thread.sleep(500)
        } catch (_: Exception) {
        }
        subscription.dispose()
        fnfClient.dispose()
        streamClient.dispose()

        // verify the item count
        assertEquals(data.size, dataReceived.size, "Wrong data count received")
    }

    @Test
    fun `when running channel game then log the results`() {
        val client1 = ChannelClient()
        val client2 = ChannelClient()

        client1.playGame()
        client2.playGame()

        client1.dispose()
        client2.dispose()
    }


}

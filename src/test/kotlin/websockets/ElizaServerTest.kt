@file:Suppress("NoWildcardImports")

package websockets

import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.websocket.ClientEndpoint
import jakarta.websocket.ContainerProvider
import jakarta.websocket.OnMessage
import jakarta.websocket.Session
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.boot.test.web.server.LocalServerPort
import java.net.URI
import java.util.concurrent.CountDownLatch

private val logger = KotlinLogging.logger {}

@SpringBootTest(webEnvironment = RANDOM_PORT)
class ElizaServerTest {
    @LocalServerPort
    private var port: Int = 0

    @Test
    fun onOpen() {
        logger.info { "This is the test worker" }
        val latch = CountDownLatch(3)
        val list = mutableListOf<String>()

        val client = SimpleClient(list, latch)
        client.connect("ws://localhost:$port/eliza")
        latch.await()
        assertEquals(3, list.size)
        assertEquals("The doctor is in.", list[0])
    }

    @Test
    fun onChat() {
        logger.info { "Test thread" }
        val latch = CountDownLatch(4)
        val list = mutableListOf<String>()

        val client = ComplexClient(list, latch)
        client.connect("ws://localhost:$port/eliza")
        latch.await()

        val size = list.size

        // 1. EXPLAIN WHY size = list.size IS NECESSARY

        // Guardamos size en una variable para capturar el valor en este punto del tiempo,
        // porque list.size podría cambiar por concurrencia si llegaran más mensajes.

        // 2. REPLACE BY assertXXX expression that checks an interval; assertEquals must not be used;

        // Usamos assertTrue(size >= 4 && size <= 5) en vez de assertEquals para tolerar variaciones en timing.

        org.junit.jupiter.api.Assertions
            .assertTrue(size >= 4 && size <= 5)

        // 3. EXPLAIN WHY assertEquals CANNOT BE USED AND WHY WE SHOULD CHECK THE INTERVAL

        // assertEquals es frágil en tests asíncronos ya que la cantidad exacta de mensajes puede variar por
        // latencia/ordenación. Comprobar un intervalo o una condición más general evita falsos fallos.

        // 4. COMPLETE assertEquals(XXX, list[XXX])

        // Comprobamos que el mensaje inicial es correcto.
        org.junit.jupiter.api.Assertions
            .assertEquals("The doctor is in.", list[0])
    }
}

@ClientEndpoint
class SimpleClient(
    private val list: MutableList<String>,
    private val latch: CountDownLatch,
) {
    @OnMessage
    fun onMessage(message: String) {
        logger.info { "Client received: $message" }
        list.add(message)
        latch.countDown()
    }
}

@ClientEndpoint
class ComplexClient(
    private val list: MutableList<String>,
    private val latch: CountDownLatch,
) {
    @OnMessage
    fun onMessage(
        message: String,
        session: Session,
    ) {
        logger.info { "Client received: $message" }
        list.add(message)
        latch.countDown()

        if (message.contains("What's on your mind") || message.contains("What's on your mind?")) {
            // Enviar un mensaje de prueba al servidor Eliza que debería accionar una regla.
            session.basicRemote.sendText("I am feeling sad")
        }
    }
}

fun Any.connect(uri: String) {
    ContainerProvider.getWebSocketContainer().connectToServer(this, URI(uri))
}

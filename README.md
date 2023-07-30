# RSocket Demo

This is a demo project to show how to use RSocket.

### RSocket 

RSocket is a protocol designed for building reactive, low-latency, and resilient communication systems. It was developed by Netflix and is now an open-source project supported by the Reactive Foundation. RSocket is built on top of TCP (Transmission Control Protocol) and provides a bi-directional, multiplexed, and flow-controlled communication channel between applications.

Key features of RSocket include:

Reactive Streams Semantics: RSocket embraces the principles of Reactive Programming, allowing data to be exchanged in a non-blocking and backpressure-aware manner. This enables efficient handling of data streams and makes it well-suited for reactive applications.

Request-Response Model: RSocket supports several communication models, including request-response, request-stream, fire-and-forget, and channel. This flexibility makes it suitable for various use cases, from simple one-to-one interactions to complex streaming scenarios.

Multiplexing: RSocket can multiplex multiple requests and responses over a single connection. This reduces the overhead of establishing multiple connections, leading to improved performance and reduced resource consumption.

Resumability: One of the significant advantages of RSocket is its ability to resume communication after a connection failure. Applications can retain their state and continue communication once the connection is reestablished, making it resilient to network disruptions.

Transport Independence: Although RSocket is primarily designed to run over TCP, it is not tied to a specific transport protocol. RSocket has been adapted to run over other protocols, such as WebSockets, Aeron, and QUIC.

Polyglot Support: RSocket has implementations available in multiple programming languages, allowing applications written in different languages to communicate seamlessly.

RSocket is commonly used in scenarios where low-latency, real-time communication is essential, such as in microservices architectures, IoT (Internet of Things) applications, gaming, and other reactive systems.

Overall, RSocket aims to provide a more efficient, versatile, and reactive alternative to traditional communication protocols like HTTP, especially in situations where real-time responsiveness and resiliency are critical.

### Use Cases

This demo project shows how to use RSocket in the following scenarios:

- Request-Response: A client sends a request to the server and waits for a single response.
- Request-Stream: A client sends a request to the server and receives a stream of responses.
- Fire-and-Forget: A client sends a request to the server and does not wait for a response.
- Channel: A client and server exchange a stream of messages in both directions.

### How to run

The examples could be executed on class `com.example.rsocketdemo.RSocketDemoApplicationTests` as JUnit test cases.




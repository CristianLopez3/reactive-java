# Reactive-programming-using-reactor

This project has the necessary code for the reactive programming course using Project Reactor

* Reactive programming use Functional style code.

  * In imperative programming, you tell the computer what to do step by step that's a **how**, but in functional programming, you tell the computer what you want to achieve that's a **what**.

## Repetitive questions to dominate reactive programming ⁉️⁉

- What is an epoch?
- What is a callback?
- What is backpressure?
- What is the Schedulers class made for?
- How you test reactive code?

>[!TIP]
> Understand examples and doming the concept to pass the interviews and of course take notes to improve constantly.

## Testing ✅

Use the **Step Verifier** to test the reactive code.

```java
StepVerifier.create(Flux.just("A", "B", "C"))
    .expectNext("A")
    .expectNext("B")
    .expectNext("C")
    .verifyComplete();
```


### Flux And Mono Operators ✅

* **Flux** is a stream of 0..N elements.
* **Mono** is a stream of 0..1 elements.

```java
Flux<String> flux = Flux.just("A", "B", "C")
        
    .map(String::toLowerCase)
    .filter(s -> s.contains("b"))
    .log()
    .concat() // Flux.concat(a, b)
    .concatWith()
    .transform()
    .swiftIfEmpty()
    .flatMap()
    .merge() // Flux.merge(a, b)
    .mergeWith()
    .reduce() 
    .mergerSequential() // Flux.mergeSequential(a, b)
    .zip() // Flux.zip(a, b, (first, second) -> {})
    .zipWith()

    // DoOn* CallBack Operators
    .doOnComplete(() -> System.out.println("Completed"))
    .doOnNext(System.out::println)
    .doOnSubscribe(subscription -> System.out.println("Subscribed"))
    .doOnRequest(l -> System.out.println("Request: " + l))
    .doOnCancel(() -> System.out.println("Cancelled"))
    .doOnError(e -> System.out.println("Error: " + e.getMessage()))

    // Error Handling Operators
    .onErrorReturn("Z")
    .onErrorResume(e -> Flux.just("Z", "Y"))
    .onErrorContinue((e, o) -> {})
    .onErrorMap(e -> new RuntimeException("Something bad happened"))
    .doOnError(e -> System.out.println("Error: " + e.getMessage()))
        
    // Recover from an error
    .retry()
    .retry(N) // retry N times
    .retryWhen()
        
    // thread execution
    .publushOn(Schedulers.parallel()) // use when we have a blocking operation
    .subscribeOn(Schedulers.boundedElastic()) // use when we have a blocking operation

```

## Important Concepts ✅

* **Backpressure**: It's a mechanism to ensure that a fast producer doesn't overwhelm a slow consumer.
* **Cold Publisher**: It's a publisher that generates the data when the subscriber subscribes.
* **Hot Publisher**: It's a publisher that generates the data even if there are no subscribers.
* **Flux**: It's a stream of 0..N elements.
* **Mono**: It's a stream of 0..1 elements.
* **Step Verifier**: It's a tool to test the reactive code.
* **Schedulers**: It's a way to control the execution of the code.
* **Callback Hell**: It's a situation where you have a lot of nested callbacks.
* **Callback**: It's a function that is called when the asynchronous operation is completed.


### Concurrent Programming 🆚 Reactive Programming

**Programación concurrente**:
Se trata de diseñar sistemas que puedan ejecutar múltiples tareas de forma simultánea o aparentemente simultánea, sin importar si se realizan en un solo núcleo de CPU o en varios. La clave está en la interleaved execution (ejecución entrelazada) y el manejo de hilos o procesos.
Ejemplo típico: Descargar un archivo mientras el programa sigue respondiendo al usuario.

_Herramientas y conceptos comunes_:

* Hilos (threads)
* Locks y sincronización
* Ejecución paralela

* **Programación reactiva**:
* 
Un paradigma de programación orientado a flujos de datos asíncronos y a responder a cambios o eventos. Los programas reactivos manejan streams (flujos continuos de datos) y responden automáticamente cuando llegan nuevos datos o eventos.
Ejemplo típico: Una aplicación de chat que muestra mensajes tan pronto como llegan.

_Herramientas y conceptos comunes_:

* Observables (en frameworks como RxJava, Reactor)
* Streams y operadores
* Programación funcional

📊 Comparación directa

| Aspecto                     | Programación Concurrente                      | Programación Reactiva                       |
|-----------------------------|----------------------------------------------|--------------------------------------------|
| Foco                        | Manejo explícito de múltiples tareas.        | Manejo de flujos de datos asíncronos.     |
| Nivel de abstracción        | Bajo (control directo de hilos).             | Alto (frameworks manejan concurrencia).    |
| Estrategia                  | Divide tareas entre procesos o hilos.        | Responde a eventos en tiempo real.         |
| Complejidad                 | Necesita gestión manual de sincronización y estados. | Simplifica mediante abstracciones de streams. |
| Uso típico                  | Operaciones intensivas en cálculo (e.g., algoritmos paralelos). | Sistemas intensivos en I/O (e.g., APIs, flujos de eventos). |
--------------------------------------------------------------------------------------------------------------------------------------------------------------

> [!IMPORTANT]
> Ambos conceptos son complementarios: la concurrencia gestiona los recursos, mientras que la reactividad los organiza en un flujo de datos limpio y eficiente.


## Backpressure ✅

It's a mechanism to ensure that a fast producer doesn't overwhelm a slow consumer.
The concept of backpressure is formalized in the Reactive Streams specification. It defines how producers and consumers can communicate about the rate of data flow using a subscription model.
Request Mechanism:

In a typical reactive setup, the consumer can request a specific amount of data from the producer. For example, a consumer can say, "I can handle 5 more items," and the producer will then send exactly 5 items. This is often done using the request(n) method.
**_Handling Backpressure_**:

If a consumer cannot keep up with the incoming data, several strategies can be employed:

* Buffering: Temporarily store excess data until the consumer is ready to process it.
* Dropping: Discard excess data if it cannot be processed in time.
* Throttling: Slow down the producer to match the consumer's processing speed.

## Schedulers ✅

Schedulers are a way to control the execution of the code. They allow you to specify where and when the code should run.
For example, you can run a piece of code on a separate thread or on the main thread. Schedulers
are especially useful when dealing with blocking operations or when you need to control the concurrency of the code.

**_Common Schedulers_**:
* Schedulers.immediate(): Run the code immediately on the current thread.
* Schedulers.single(): Run the code on a single reusable thread.
* Schedulers.parallel(): Run the code on a fixed pool of parallel threads.
* Schedulers.elastic(): Run the code on an elastic pool of threads.
* Schedulers.boundedElastic(): Run the code on a bounded elastic pool of threads.

**Operators used with Schedulers**: You can use the publishOn() and subscribeOn() operators to specify the scheduler for
a particular part of the code.

**subscribeOn**: Specifies the scheduler on which the subscription should happen (e.g., when you call subscribe(), i.e. from the start ).

**publishOn**: Specifies the scheduler on which the subsequent operators should run (e.g., when you call map(), filter(), etc.).


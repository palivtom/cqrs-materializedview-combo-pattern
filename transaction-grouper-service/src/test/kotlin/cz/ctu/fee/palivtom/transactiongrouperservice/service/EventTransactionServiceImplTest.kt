package cz.ctu.fee.palivtom.transactiongrouperservice.service

import cz.ctu.fee.palivtom.transactiongrouperservice.kafka.KafkaConfig
import cz.ctu.fee.palivtom.transactiongrouperservice.kafka.TableConsumer
import cz.ctu.fee.palivtom.transactiongrouperservice.kafka.TransactionConsumer
import cz.ctu.fee.palivtom.transactiongrouperservice.model.*
import cz.ctu.fee.palivtom.transactiongrouperservice.kafka.TransactionGroupProducer
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.mockito.BDDMockito
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.boot.test.mock.mockito.SpyBean
import org.springframework.test.util.ReflectionTestUtils
import java.util.*
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.stream.Stream

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@EnableAutoConfiguration(exclude = [KafkaAutoConfiguration::class])
class EventTransactionServiceImplTest {
    @MockBean
    lateinit var kafkaConfig: KafkaConfig

    @MockBean
    lateinit var transactionGroupProducer: TransactionGroupProducer

    @MockBean
    lateinit var tableConsumer: TableConsumer

    @MockBean
    lateinit var transactionConsumer: TransactionConsumer

    @SpyBean
    lateinit var eventTransactionService: EventTransactionService

    lateinit var asyncTransactions: ExecutorService
    lateinit var asyncEvents: ExecutorService

    @BeforeAll
    fun setUp() {
        asyncTransactions = Executors.newSingleThreadExecutor()
        asyncEvents = Executors.newFixedThreadPool(4)
    }

    @AfterAll
    fun tearDown() {
        asyncTransactions.shutdown()
        asyncEvents.shutdown()
    }

    @ParameterizedTest
    @MethodSource("provideMultipleValuesSettings")
    fun `register transaction and add its sorted events - executes in transaction order`(txCount: Int, minEventCountPerTx: Int, maxEventCountPerTx: Int) {
        val random = Random()
        val events = arrayListOf<TableEvent>()
        val transactions = Array(txCount) {
            random.nextInt(minEventCountPerTx, maxEventCountPerTx).run {
                events.addAll(generateEvents(it.toString(), this))
                generateTransaction(it.toString(), this)
            }
        }

        val transactionTasks = transactions.map {
            CompletableFuture.runAsync(
                { eventTransactionService.registerTransaction(it) }, asyncTransactions
            )
        }.toTypedArray()

        val eventTasks = events.map {
            CompletableFuture.runAsync(
                { eventTransactionService.addEventToTransaction(it) }, asyncEvents
            )
        }.toTypedArray()

        CompletableFuture.allOf(*transactionTasks, *eventTasks).join()

        BDDMockito.verify(eventTransactionService, BDDMockito.times(txCount)).registerTransaction(any())
        BDDMockito.verify(eventTransactionService, BDDMockito.times(events.size)).addEventToTransaction(any())

        val order = BDDMockito.inOrder(transactionGroupProducer)
        transactions.forEach {
            order.verify(transactionGroupProducer).sendTransactionGroup(eq(it.id), any())
        }

        val locksPrivate = ReflectionTestUtils.getField(eventTransactionService, "locks") as Map<*, *>
        val transactionsPrivate = ReflectionTestUtils.getField(eventTransactionService, "transactions") as Map<*, *>
        val queuePrivate = ReflectionTestUtils.getField(eventTransactionService, "queue") as Queue<*>

        Assertions.assertTrue(locksPrivate.isEmpty())
        Assertions.assertTrue(transactionsPrivate.isEmpty())
        Assertions.assertTrue(queuePrivate.isEmpty())
    }

    @ParameterizedTest
    @MethodSource("provideMultipleValuesSettings")
    fun `register transaction and add its consorted events - executes in transaction order`(txCount: Int, minEventCountPerTx: Int, maxEventCountPerTx: Int) {
        val random = Random()
        val events = arrayListOf<TableEvent>()
        val transactions = Array(txCount) {
            random.nextInt(minEventCountPerTx, maxEventCountPerTx).run {
                events.addAll(generateEvents(it.toString(), this))
                generateTransaction(it.toString(), this)
            }
        }
        events.shuffle()

        val transactionTasks = transactions.map {
            CompletableFuture.runAsync(
                { eventTransactionService.registerTransaction(it) }, asyncTransactions
            )
        }.toTypedArray()

        val eventTasks = events.map {
            CompletableFuture.runAsync(
                { eventTransactionService.addEventToTransaction(it) }, asyncEvents
            )
        }.toTypedArray()

        CompletableFuture.allOf(*transactionTasks, *eventTasks).join()

        BDDMockito.verify(eventTransactionService, BDDMockito.times(txCount)).registerTransaction(any())
        BDDMockito.verify(eventTransactionService, BDDMockito.times(events.size)).addEventToTransaction(any())

        val order = BDDMockito.inOrder(transactionGroupProducer)
        transactions.forEach {
            order.verify(transactionGroupProducer).sendTransactionGroup(eq(it.id), any())
        }

        val locksPrivate = ReflectionTestUtils.getField(eventTransactionService, "locks") as Map<*, *>
        val transactionsPrivate = ReflectionTestUtils.getField(eventTransactionService, "transactions") as Map<*, *>
        val queuePrivate = ReflectionTestUtils.getField(eventTransactionService, "queue") as Queue<*>

        Assertions.assertTrue(locksPrivate.isEmpty())
        Assertions.assertTrue(transactionsPrivate.isEmpty())
        Assertions.assertTrue(queuePrivate.isEmpty())
    }

    @ParameterizedTest
    @MethodSource("provideMultipleValuesSettings")
    fun `register consorted transaction and add its consorted events - executes in transaction order`(txCount: Int, minEventCountPerTx: Int, maxEventCountPerTx: Int) {
        val random = Random()
        val events = arrayListOf<TableEvent>()
        val transactions = Array(txCount) {
            random.nextInt(minEventCountPerTx, maxEventCountPerTx).run {
                events.addAll(generateEvents(it.toString(), this))
                generateTransaction(it.toString(), this)
            }
        }
        transactions.shuffle()
        events.shuffle()

        val transactionTasks = transactions.map {
            CompletableFuture.runAsync(
                { eventTransactionService.registerTransaction(it) }, asyncTransactions
            )
        }.toTypedArray()

        val eventTasks = events.map {
            CompletableFuture.runAsync(
                { eventTransactionService.addEventToTransaction(it) }, asyncEvents
            )
        }.toTypedArray()

        CompletableFuture.allOf(*transactionTasks, *eventTasks).join()

        BDDMockito.verify(eventTransactionService, BDDMockito.times(txCount)).registerTransaction(any())
        BDDMockito.verify(eventTransactionService, BDDMockito.times(events.size)).addEventToTransaction(any())

        val order = BDDMockito.inOrder(transactionGroupProducer)
        transactions.forEach {
            order.verify(transactionGroupProducer).sendTransactionGroup(eq(it.id), any())
        }

        val locksPrivate = ReflectionTestUtils.getField(eventTransactionService, "locks") as Map<*, *>
        val transactionsPrivate = ReflectionTestUtils.getField(eventTransactionService, "transactions") as Map<*, *>
        val queuePrivate = ReflectionTestUtils.getField(eventTransactionService, "queue") as Queue<*>

        Assertions.assertTrue(locksPrivate.isEmpty())
        Assertions.assertTrue(transactionsPrivate.isEmpty())
        Assertions.assertTrue(queuePrivate.isEmpty())
    }

    @ParameterizedTest
    @MethodSource("provideMultipleValuesSettings")
    fun `add sorted events and register transaction - executes in transaction order`(txCount: Int, minEventCountPerTx: Int, maxEventCountPerTx: Int) {
        val random = Random()
        val events = arrayListOf<TableEvent>()
        val transactions = Array(txCount) {
            random.nextInt(minEventCountPerTx, maxEventCountPerTx).run {
                events.addAll(generateEvents(it.toString(), this))
                generateTransaction(it.toString(), this)
            }
        }

        val eventTasks = events.map {
            CompletableFuture.runAsync(
                { eventTransactionService.addEventToTransaction(it) }, asyncEvents
            )
        }.toTypedArray()

        val transactionTasks = transactions.map {
            CompletableFuture.runAsync(
                { eventTransactionService.registerTransaction(it) }, asyncTransactions
            )
        }.toTypedArray()

        CompletableFuture.allOf(*transactionTasks, *eventTasks).join()

        BDDMockito.verify(eventTransactionService, BDDMockito.times(txCount)).registerTransaction(any())
        BDDMockito.verify(eventTransactionService, BDDMockito.times(events.size)).addEventToTransaction(any())

        val order = BDDMockito.inOrder(transactionGroupProducer)
        transactions.forEach {
            order.verify(transactionGroupProducer).sendTransactionGroup(eq(it.id), any())
        }

        val locksPrivate = ReflectionTestUtils.getField(eventTransactionService, "locks") as Map<*, *>
        val transactionsPrivate = ReflectionTestUtils.getField(eventTransactionService, "transactions") as Map<*, *>
        val queuePrivate = ReflectionTestUtils.getField(eventTransactionService, "queue") as Queue<*>

        Assertions.assertTrue(locksPrivate.isEmpty())
        Assertions.assertTrue(transactionsPrivate.isEmpty())
        Assertions.assertTrue(queuePrivate.isEmpty())
    }

    @ParameterizedTest
    @MethodSource("provideMultipleValuesSettings")
    fun `add consorted events and register transaction - executes in transaction order`(txCount: Int, minEventCountPerTx: Int, maxEventCountPerTx: Int) {
        val random = Random()
        val events = arrayListOf<TableEvent>()
        val transactions = Array(txCount) {
            random.nextInt(minEventCountPerTx, maxEventCountPerTx).run {
                events.addAll(generateEvents(it.toString(), this))
                generateTransaction(it.toString(), this)
            }
        }
        events.shuffle()

        val eventTasks = events.map {
            CompletableFuture.runAsync(
                { eventTransactionService.addEventToTransaction(it) }, asyncEvents
            )
        }.toTypedArray()

        val transactionTasks = transactions.map {
            CompletableFuture.runAsync(
                { eventTransactionService.registerTransaction(it) }, asyncTransactions
            )
        }.toTypedArray()

        CompletableFuture.allOf(*transactionTasks, *eventTasks).join()

        BDDMockito.verify(eventTransactionService, BDDMockito.times(txCount)).registerTransaction(any())
        BDDMockito.verify(eventTransactionService, BDDMockito.times(events.size)).addEventToTransaction(any())

        val order = BDDMockito.inOrder(transactionGroupProducer)
        transactions.forEach {
            order.verify(transactionGroupProducer).sendTransactionGroup(eq(it.id), any())
        }

        val locksPrivate = ReflectionTestUtils.getField(eventTransactionService, "locks") as Map<*, *>
        val transactionsPrivate = ReflectionTestUtils.getField(eventTransactionService, "transactions") as Map<*, *>
        val queuePrivate = ReflectionTestUtils.getField(eventTransactionService, "queue") as Queue<*>

        Assertions.assertTrue(locksPrivate.isEmpty())
        Assertions.assertTrue(transactionsPrivate.isEmpty())
        Assertions.assertTrue(queuePrivate.isEmpty())
    }

    private fun provideMultipleValuesSettings(): Stream<Arguments> {
        return Stream.of(
            Arguments.of(1, 0, 1),
            Arguments.of(1, 1, 100),
            Arguments.of(1, 1, 1000),
            Arguments.of(100, 0, 1),
            Arguments.of(100, 1, 100),
            Arguments.of(1000, 1, 100),
            Arguments.of(10, 900, 1000)
        )
    }

    private fun generateTransaction(txId: String, eventCount: Int) =
        Transaction(txId, eventCount, emptyMap(), KafkaMetadata(0, 0, 1))

    private fun generateEvents(txId: String, eventCount: Int) =
        Array(eventCount) {
            TableEvent(
                "c",
                null,
                null,
                Source("version", "connection", "name", 1, null, "db", "schema", "table", null),
                null,
                TransactionMetadata(txId, it, it),
                KafkaMetadata(1, 0, 1)
            )
        }
}
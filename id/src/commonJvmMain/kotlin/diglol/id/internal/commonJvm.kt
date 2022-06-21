package diglol.id.internal

import diglol.crypto.random.nextInt
import java.time.Instant
import java.util.concurrent.atomic.AtomicInteger

private val nextCounter: AtomicInteger = AtomicInteger(nextInt())

actual fun epochSeconds(): Long = Instant.now().epochSecond

actual fun counter(): Int = nextCounter.getAndIncrement()

package diglol.id

import diglol.crypto.random.nextInt
import java.time.Instant
import java.util.concurrent.atomic.AtomicInteger

private val nextCounter: AtomicInteger = AtomicInteger(nextInt())

internal actual fun epochSeconds(): Long = Instant.now().epochSecond

internal actual fun counter(): Int = nextCounter.getAndIncrement()

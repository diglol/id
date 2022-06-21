package diglol.id

import diglol.crypto.rand.nextInt
import java.util.UUID
import java.util.concurrent.atomic.AtomicInteger

private val nextCounter: AtomicInteger = AtomicInteger(nextInt(Int.MAX_VALUE))

actual fun counter(): Int = nextCounter.getAndIncrement()
val xxx = UUID.randomUUID()

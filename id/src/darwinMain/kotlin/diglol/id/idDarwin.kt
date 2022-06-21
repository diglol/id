package diglol.id

import diglol.crypto.rand.nextInt
import kotlin.native.concurrent.AtomicInt
import platform.Foundation.NSProcessInfo

private val nextCounter = AtomicInt(nextInt(Int.MAX_VALUE))

actual fun readPid(): Int = NSProcessInfo.processInfo.processIdentifier

actual fun counter(): Int = nextCounter.addAndGet(1)

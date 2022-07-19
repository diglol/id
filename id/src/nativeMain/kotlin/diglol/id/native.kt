package diglol.id

import diglol.crypto.random.nextInt
import kotlin.native.concurrent.AtomicInt
import kotlinx.cinterop.alloc
import kotlinx.cinterop.convert
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import platform.posix.CLOCK_REALTIME
import platform.posix.clock_gettime
import platform.posix.getpid
import platform.posix.timespec

private val nextCounter = AtomicInt(nextInt(Int.MAX_VALUE))

internal actual fun epochSeconds(): Long {
  memScoped {
    val current = alloc<timespec>()
    clock_gettime(CLOCK_REALTIME.convert(), current.ptr)
    return current.tv_sec.convert()
  }
}

internal actual fun readPid(): Int = getpid()

internal actual fun counter(): Int = nextCounter.addAndGet(1)


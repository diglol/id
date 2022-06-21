package diglol.id.internal

import diglol.crypto.random.nextInt
import kotlin.js.Date

private var nextCounter = nextInt(Int.MAX_VALUE)

actual fun epochSeconds(): Long = (Date().getTime() / 1000).toLong()

actual fun readPid(): Int = nextInt(Int.MAX_VALUE)

actual fun counter(): Int {
  if (nextCounter == Int.MAX_VALUE) {
    nextCounter = Int.MIN_VALUE
    return nextCounter
  }
  nextCounter += 1
  return nextCounter
}

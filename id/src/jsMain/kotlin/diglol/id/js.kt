package diglol.id

import diglol.crypto.random.nextInt
import kotlin.js.Date

private var nextCounter = nextInt(Int.MAX_VALUE)

internal actual fun epochSeconds(): Long = (Date().getTime() / 1000).toLong()

internal actual fun readPid(): Int = nextInt(Int.MAX_VALUE)

internal actual fun counter(): Int {
  if (nextCounter == Int.MAX_VALUE) {
    nextCounter = Int.MIN_VALUE
    return nextCounter
  }
  nextCounter += 1
  return nextCounter
}

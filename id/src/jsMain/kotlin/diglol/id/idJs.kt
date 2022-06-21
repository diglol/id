package diglol.id

import diglol.crypto.rand.nextInt

private var nextCounter = nextInt(Int.MAX_VALUE)

actual fun readPid(): Int = nextInt(Int.MAX_VALUE)

actual fun counter(): Int {
  if (nextCounter == Int.MAX_VALUE) {
    nextCounter = Int.MIN_VALUE
    return nextCounter
  }
  nextCounter += 1
  return nextCounter
}

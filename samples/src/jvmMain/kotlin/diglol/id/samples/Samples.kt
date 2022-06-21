@file:JvmName("Samples")

package diglol.id.samples

import diglol.id.Id

fun main() {
  val deviceId = Id.generate()
  println(deviceId)
}

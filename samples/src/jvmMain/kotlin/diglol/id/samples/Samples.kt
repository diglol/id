@file:JvmName("Samples")

package diglol.id.samples

import diglol.id.Id

fun main() {
  DeviceId.load(".diglol") // Init load
  println(DeviceId.get())

  println(Id.generate())
}

package diglol.id

import java.lang.management.ManagementFactory

internal actual fun readPid(): Int =
  ManagementFactory.getRuntimeMXBean().name.split("@").first().toInt()

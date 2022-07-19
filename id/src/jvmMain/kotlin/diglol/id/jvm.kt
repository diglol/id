package diglol.id

import java.lang.management.ManagementFactory

private val pid = ManagementFactory.getRuntimeMXBean().name.split("@").first().toInt()

internal actual fun readPid(): Int = pid

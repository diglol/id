package diglol.id

import java.lang.management.ManagementFactory

private val pid = ManagementFactory.getRuntimeMXBean().name.split("@").first().toInt()

actual fun readPid(): Int = pid

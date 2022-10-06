package diglol.id.samples

import diglol.id.Id
import diglol.id.Id.Companion.toId
import java.io.IOException
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.createDirectory
import kotlin.io.path.deleteExisting
import kotlin.io.path.exists
import kotlin.io.path.isDirectory
import kotlin.io.path.pathString
import kotlin.io.path.readBytes
import kotlin.io.path.writeBytes

object DeviceId {
  private const val deviceIdKey = "device_id"

  private val userHome = System.getProperty("user.home")
  private var deviceId: Id = Id.empty

  fun load(path: String) {
    val rootPath = Path(userHome, path)
    val deviceIdPath = Path(rootPath.pathString, deviceIdKey)
    var _deviceId: Id? = null
    if (!rootPath.forceCreateDirectory()) {
      _deviceId = deviceIdPath.readDeviceId()
    }
    if (_deviceId == null || _deviceId.isEmpty()) {
      _deviceId = Id.generate()
      deviceIdPath.writeDeviceId(_deviceId)
    } else {
      Id.machine = _deviceId.machine
    }
    deviceId = _deviceId
  }

  fun get(): Id = deviceId
}

private fun Path.forceCreateDirectory(): Boolean {
  if (exists()) {
    if (isDirectory()) {
      return false
    } else {
      deleteExisting()
    }
  }
  createDirectory()
  return true
}

private fun Path.writeDeviceId(id: Id) {
  if (exists() && isDirectory()) {
    deleteExisting()
  }
  try {
    writeBytes(id.bytes)
  } catch (e: IOException) {
    e.printStackTrace()
  }
}

private fun Path.readDeviceId(): Id? {
  if (exists()) {
    if (isDirectory()) {
      deleteExisting()
    } else {
      try {
        return readBytes().toId()
      } catch (e: IOException) {
        e.printStackTrace()
      }
    }
  }
  return null
}

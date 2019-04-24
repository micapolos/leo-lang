package leo.java.lang

import leo.base.utf8String
import leo.binary.byteArray
import leo.java.io.byteSeq

fun exec(vararg command: String): String {
	val runtime = Runtime.getRuntime()
	val process = runtime.exec(command)
	val string = process.inputStream.byteSeq.byteArray.utf8String
	val exitCode = process.waitFor()
	if (exitCode != 0) error("exec($command) = $exitCode")
	else return string
}

fun sttyPrivateMode() {
	val sttyConfig = exec("sh", "-c", "stty -g < /dev/tty").trim()
	exec("sh", "-c", "stty -icanon min 1 < /dev/tty")
	exec("sh", "-c", "stty -echo < /dev/tty")
	Runtime.getRuntime().addShutdownHook(object : Thread() {
		override fun start() {
			exec("sh", "-c", "stty $sttyConfig < /dev/tty")
		}
	})
}

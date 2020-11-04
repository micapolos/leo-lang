package leo.java.lang

fun execExpectingExitCode(expectedExitCode: Int, vararg command: String): String {
	val runtime = Runtime.getRuntime()
	val process = runtime.exec(command)
	val string = process.inputStream.reader().readText()
	val exitCode = process.waitFor()
	if (exitCode != expectedExitCode) error("exec(${command.contentToString()}) = $exitCode\n${process.errorStream.reader().readText()}")
	else return string
}

fun exec(vararg command: String): String =
	execExpectingExitCode(0, *command)

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

fun sendMail(to: String, subject: String, message: String) {
	val runtime = Runtime.getRuntime()
	val process = runtime.exec(arrayOf("sendmail", to))
	val writer = process.outputStream.bufferedWriter()
	writer.write("From: micapolos@gmail.com\n")
	writer.write("Subject: $subject\n")
	writer.write("\n$message\n.\n")
	writer.flush()
	val exitCode = process.waitFor()
	if (exitCode != 0) error("error")
}

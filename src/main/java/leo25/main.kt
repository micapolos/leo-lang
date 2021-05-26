package leo25

import leo.base.print
import leo.java.io.file
import leo.java.io.inString
import java.io.File

fun main(args: Array<String>) {
	val string =
		if (args.isEmpty()) inString
		else try {
			File(args[0]).readText()
		} catch (e: Exception) {
			use(args[0], *args.copyOfRange(1, args.size)).path.file.readText()
		}
	string.interpret.print
}
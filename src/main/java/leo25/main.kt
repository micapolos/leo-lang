package leo25

import leo.base.print
import leo.java.io.file
import leo.java.io.inString
import leo13.linkOrNull
import leo13.stack
import java.io.File

fun main(args: Array<String>) {
	val string =
		if (args.isEmpty()) inString
		else try {
			Use(stack(*args).linkOrNull!!).path.file.readText()
		} catch (e: Exception) {
			File(args[0]).readText()
		}
	string.interpret.print
}
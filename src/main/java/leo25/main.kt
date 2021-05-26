package leo25

import leo.base.print
import leo.java.io.file
import leo.java.io.inString
import leo13.linkOrNull
import leo13.stack

fun main(args: Array<String>) {
	val string =
		if (args.isEmpty()) inString
		else Use(stack(*args).linkOrNull!!).path.file.readText()
	string.interpret.print
}
package leo25

import leo.base.print
import leo.java.io.file
import leo.java.io.inString
import java.io.File

fun main(args: Array<String>) {
	try {
		readText(args).interpret
	} catch (e: ValueError) {
		value("parser" fieldTo e.value).errorValue.string
	}.print
}

fun readText(args: Array<String>) =
	if (args.isEmpty()) inString
	else try {
		File(args[0]).readText()
	} catch (e: Exception) {
		use(args[0], *args.copyOfRange(1, args.size)).path.file.readText()
	}
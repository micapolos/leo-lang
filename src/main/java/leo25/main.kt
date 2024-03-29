package leo25

import leo.base.print
import leo.java.io.file
import leo.java.io.inString
import leo25.natives.nativeEnvironment
import leo25.natives.path
import leo25.parser.scriptOrThrow
import java.io.File

fun main(args: Array<String>) {
	try {
		nativeEnvironment.interpret(readText(args).scriptOrThrow)
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
package leo14

import leo.ansi
import leo.bellChar
import leo.clear
import leo.home
import leo.java.lang.sttyPrivateMode
import leo13.linkOrNull
import leo13.push
import leo13.stack
import leo14.typed.compiler.CharCompiler
import leo14.typed.compiler.emptyCharCompiler
import leo14.typed.compiler.indentColorString
import leo14.typed.compiler.put
import java.io.InputStreamReader

val errorTriggerCount = 7

fun main() {
	sttyPrivateMode()
	var stack = stack<CharCompiler>()
	var charCompiler = emptyCharCompiler
	var errorToPrint: Throwable? = null
	var errorCount = 0
	val reader = InputStreamReader(System.`in`)
	while (true) {
		print(charCompiler)
		errorToPrint?.run {
			println("ERROR")
			printStackTrace()
		}
		val char = reader.read()
		if (char == -1) break
		if (char == 127) {
			if (errorToPrint != null) errorToPrint = null
			else stack.linkOrNull?.let { link ->
				charCompiler = link.value
				stack = link.stack
			}
		} else if (errorToPrint == null) try {
			val newCharCompiler = charCompiler.put(char.toChar())
			stack = stack.push(charCompiler)
			charCompiler = newCharCompiler
			errorCount = errorTriggerCount
		} catch (e: RuntimeException) {
			print(bellChar)
			if (errorCount-- < 0) errorToPrint = e
		}
	}
}

fun print(charCompiler: CharCompiler) {
	print("${ansi.clear}${ansi.home}")
//	println("context> ${charLeo.leo.promptString}")
	print("${charCompiler.indentColorString}")
}
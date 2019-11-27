package leo14

import leo.ansi
import leo.bellChar
import leo.clear
import leo.home
import leo.java.lang.sttyPrivateMode
import leo14.typed.compiler.CharCompiler
import leo14.typed.compiler.indentColorString
import leo14.typed.compiler.js.emptyCharCompiler
import leo14.typed.compiler.put
import java.io.InputStreamReader

val errorTriggerCount = 7

fun main() = run(emptyCharCompiler)

fun <T> run(compiler: CharCompiler<T>) {
	sttyPrivateMode()
	val undoableCompilerVariable = variable(undoable(compiler))
	var errorToPrint: Throwable? = null
	var errorCount = 0
	val reader = InputStreamReader(System.`in`)
	while (true) {
		print(undoableCompilerVariable.current.lastDone)
		errorToPrint?.run {
			println("ERROR")
			printStackTrace()
		}
		val char = reader.read()
		if (char == -1) break
		if (char == 127) {
			if (errorToPrint != null) errorToPrint = null
			else undoableCompilerVariable.update { undoIfPossible }
		} else if (errorToPrint == null) try {
			undoableCompilerVariable.update {
				doIt {
					put(char.toChar()).apply {
						// Pre-fetch string for error detection
						// TODO: This solution sucks, do it properly.
						indentColorString
					}
				}
			}
			errorCount = errorTriggerCount
		} catch (e: RuntimeException) {
			print(bellChar)
			if (errorCount-- < 0) errorToPrint = e
		}
	}
}

fun <T> print(charCompiler: CharCompiler<T>) {
	print("${ansi.clear}${ansi.home}")
//	println("context> ${charLeo.leo.promptString}")
	print("${charCompiler.indentColorString}")
}
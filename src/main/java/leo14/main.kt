package leo14

import leo.ansi
import leo.bellChar
import leo.clear
import leo.home
import leo.java.lang.sttyPrivateMode
import leo14.typed.compiler.CharCompiler
import leo14.typed.compiler.indentColorString
import leo14.typed.compiler.natives.emptyCharCompiler
import leo14.typed.compiler.put
import java.io.InputStreamReader

val errorTriggerCount = 7

fun main() = run(emptyCharCompiler)

fun run(compiler: CharCompiler) {
	sttyPrivateMode()
	val undoableCompilerVariable = variable(undoable(compiler))
	var errorToPrint: Throwable? = null
	var errorCount = 0
	val reader = InputStreamReader(System.`in`)
	var printDebug = false
	while (true) {
		print(undoableCompilerVariable.current.lastDone)
		errorToPrint?.run {
			println("ERROR")
			printStackTrace()
			println(undoableCompilerVariable.current.lastDone.toString())
		}
		if (printDebug) {
			println(undoableCompilerVariable.current.lastDone)
			printDebug = false
		}
		val char = reader.read()
		if (char == -1) break
		if (char == 127) {
			if (errorToPrint != null) errorToPrint = null
			else undoableCompilerVariable.update { undoIfPossible }
		} else if (char == 27) {
			printDebug = true
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

fun print(charCompiler: CharCompiler) {
	print("${ansi.clear}${ansi.home}")
	print("${charCompiler.indentColorString}")
}
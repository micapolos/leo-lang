package leo14

import leo.ansi
import leo.bellChar
import leo.clear
import leo.home
import leo.java.lang.sttyPrivateMode
import leo14.typed.compiler.natives.emptyCompiler
import leo14.typed.evaluator.evaluator
import java.io.InputStreamReader

val errorTriggerCount = 7

fun main() = run(emptyCompiler.evaluator.tokenReader.charReader)

fun run(reader: CharReader) {
	sttyPrivateMode()
	val undoableCharReaderVariable = variable(undoable(reader))
	var errorToPrint: Throwable? = null
	var errorCount = 0
	val reader = InputStreamReader(System.`in`)
	var printDebug = false
	while (true) {
		print(undoableCharReaderVariable.current.lastDone)
		errorToPrint?.run {
			println("ERROR")
			printStackTrace()
			println(undoableCharReaderVariable.current.lastDone.toString())
		}
		if (printDebug) {
			println(undoableCharReaderVariable.current.lastDone)
			printDebug = false
		}
		val char = reader.read()
		if (char == -1) break
		if (char == 127) {
			if (errorToPrint != null) errorToPrint = null
			else undoableCharReaderVariable.update { undoIfPossible }
		} else if (char == 27) {
			printDebug = true
		} else if (errorToPrint == null) try {
			undoableCharReaderVariable.update {
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

fun print(charReader: CharReader) {
	print("${ansi.clear}${ansi.home}")
	print("${charReader.indentColorString}")
}
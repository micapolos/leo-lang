package leo14

import leo.ansi
import leo.base.runIf
import leo.bellChar
import leo.clear
import leo.home
import leo.java.lang.sttyPrivateMode
import leo14.reader.*
import leo14.typed.compiler.compiler
import leo14.typed.compiler.js.emptyContext
import leo14.typed.compiler.js.stdScript
import leo14.typed.compiler.memory
import leo14.typed.compiler.parse
import leo14.typed.compiler.preludeMemory
import leo14.untyped.emptyTokenizer
import leo14.untyped.stringCharReducer
import java.io.InputStreamReader

val errorTriggerCount = 7
val prelude = true
val memory = if (prelude) emptyContext.preludeMemory() else memory()
val untyped = true

fun main() {
	if (untyped)
		run(emptyTokenizer.stringCharReducer)
	else
		run(emptyContext.compiler(memory).runIf(prelude) { parse(stdScript) }.tokenReader.charReader)
}

fun run(charReader: CharReader) {
	run(charReader.reducer.mapState { indentColorString })
}

fun run(reducer: Reducer<String, Char>) {
	sttyPrivateMode()
	val undoableReducerVariable = variable(undoable(reducer))
	var errorToPrint: Throwable? = null
	var errorCount = 0
	val reader = InputStreamReader(System.`in`)
	var printDebug = false
	while (true) {
		print(undoableReducerVariable.current.lastDone)
		errorToPrint?.run {
			println("ERROR")
			printStackTrace()
			println(undoableReducerVariable.current.lastDone.toString())
		}
		if (printDebug) {
			println("[DEBUG]")
			println(undoableReducerVariable.current.lastDone)
			printDebug = false
		}
		val char = reader.read()
		if (char == -1 || char == 4) break
		else if (char == 127) {
			if (errorToPrint != null) errorToPrint = null
			else undoableReducerVariable.update { undoIfPossible }
		} else if (char == 27) {
			printDebug = true
		} else if (errorToPrint == null) try {
			undoableReducerVariable.update {
				doIt {
					reduce(char.toChar()).apply {
						// Pre-fetch string for error detection
						// TODO: This solution sucks, do it properly.
						state
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

fun print(reducer: Reducer<String, Char>) {
	print("${ansi.clear}${ansi.home}")
	print(reducer.state)
}

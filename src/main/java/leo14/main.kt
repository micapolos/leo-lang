package leo14

import leo.*
import leo.base.runIf
import leo.java.lang.sttyPrivateMode
import leo14.reader.*
import leo14.typed.compiler.compiler
import leo14.typed.compiler.js.emptyContext
import leo14.typed.compiler.js.stdScript
import leo14.typed.compiler.memory
import leo14.typed.compiler.parse
import leo14.typed.compiler.preludeMemory
import leo14.untyped.dsl2.library.prelude
import leo14.untyped.dsl2.read
import leo14.untyped.stringCharReducer
import leo14.untyped.typed.stringCharReducer
import leo16.stringCharReducer
import java.io.InputStreamReader

val useLeo16 = true
val errorTriggerCount = 7
val importPrelude = true
val memory = if (importPrelude) emptyContext.preludeMemory() else memory()
val untyped = true
val untypedTyped = true

fun main() {
	if (useLeo16)
		run(leo16.emptyEvaluator.stringCharReducer)
	else if (untyped)
		if (untypedTyped) {
			run(leo14.untyped.typed.emptyReader.stringCharReducer)
		} else {
			run(leo14.untyped.emptyReader.runIf(importPrelude) { read(prelude) }.stringCharReducer)
		}
	else
		run(emptyContext.compiler(memory).runIf(importPrelude) { parse(stdScript) }.tokenReader.charReader)
}

fun run(charReader: CharReader) {
	run(charReader.reducer.mapState { indentColorString })
}

fun run(reducer: Reducer<String, Char>) {
	sttyPrivateMode()
	val undoableReducerVariable = variable(undoable(reducer))
	var errorToPrint: Throwable? = null
	var assertionError: AssertionError? = null
	var errorCount = 0
	val reader = InputStreamReader(System.`in`)
	var printDebug = false
	while (true) {
		print(undoableReducerVariable.current.lastDone)
		assertionError?.run {
			print("${ansi.clear}${ansi.home}")
			print(ansi.red)
			println(message)
			print(ansi.reset)
		}
		errorToPrint?.run {
			println("[ERROR]")
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
			if (errorToPrint != null || assertionError != null) {
				errorToPrint = null
				assertionError = null
			} else undoableReducerVariable.update { undoIfPossible }
		} else if (char == 27) {
			printDebug = true
		} else if (errorToPrint == null && assertionError == null) try {
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
		} catch (e: AssertionError) {
			print(bellChar)
			assertionError = e
		} catch (e: Throwable) {
			print(bellChar)
			if (errorCount-- < 0) errorToPrint = e
		}
	}
}

fun print(reducer: Reducer<String, Char>) {
	printLeo(reducer.state)
}

fun printLeo(string: String) {
	print("${ansi.clear}${ansi.home}")
	print(string)
}

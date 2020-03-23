package leo14.untyped

import leo.base.runIf
import leo13.fold
import leo13.reverse
import leo14.Script
import leo14.tokenStack

data class Function(
	val context: Context,
	val script: Script,
	val recursive: Boolean)

fun function(context: Context, script: Script, recursive: Boolean = false) =
	Function(context, script, recursive)

fun function(script: Script, recursive: Boolean = false) = function(context(), script, recursive)

fun Function.apply(given: Program): Program =
	try {
		context
			.runIf(recursive) { push(recurseRule) }
			.push(given.givenRule)
			.resolver()
			.reader
			.fold(script.tokenStack.reverse) { write(it)!! }
			.run { this as UnquotedReader }
			.unquoted
			.resolver
			.program
	} catch (stackOverflowError: StackOverflowError) {
		stackOverflowErrorProgram
	}

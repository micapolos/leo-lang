package leo14.untyped

import leo13.fold
import leo13.reverse
import leo14.Script
import leo14.tokenStack

data class Function(
	val context: Context,
	val script: Script)

fun function(context: Context, script: Script) = Function(context, script)

fun function(script: Script) = function(context(), script)

fun Function.apply(param: Program): Program =
	context
		.push(program(value(this)).thisRule)
		.resolver(param)
		.reader
		.fold(script.tokenStack.reverse) { write(it)!! }
		.run { this as UnquotedReader }
		.unquoted
		.resolver
		.program

package leo14.untyped

import leo13.fold
import leo13.reverse
import leo14.Script
import leo14.tokenStack

data class Function(
	val scope: Scope,
	val script: Script)

fun function(scope: Scope, script: Script) =
	Function(scope, script)

fun function(script: Script) = function(scope(), script)

fun Function.apply(given: Thunk): Thunk =
	scope
		.push(given.givenRule)
		.resolver()
		.reader
		.fold(script.tokenStack.reverse) { write(it)!! }
		.run { this as UnquotedReader }
		.unquoted
		.resolver
		.thunk

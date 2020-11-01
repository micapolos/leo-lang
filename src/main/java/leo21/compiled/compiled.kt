package leo21.compiled

import leo21.typed.LineTyped
import leo21.typed.Typed
import leo21.typed.getOrNull
import leo21.typed.plus

data class Compiled(
	val scope: Scope,
	val body: Typed,
	val isEmpty: Boolean
)

fun Compiled.plusOrNull(name: String): Compiled? =
	if (isEmpty) scope.resolveOrNull(name)?.let { setBody(it) }
	else body.getOrNull(name)?.let { setBody(it) }

fun Compiled.plus(typed: LineTyped): Compiled =
	setBody(scope.resolve(body.plus(typed)))

fun Compiled.setBody(body: Typed) = copy(body = body, isEmpty = false)
val Compiled.typed: Typed get() = body.push(scope)
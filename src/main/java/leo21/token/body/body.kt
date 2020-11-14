package leo21.token.body

import leo21.compiled.Compiled
import leo21.compiled.LineCompiled
import leo21.compiled.compiled
import leo21.compiled.plus
import leo21.compiled.resolve

data class Body(
	val bindings: Bindings,
	val compiled: Compiled
)

fun Bindings.asBody(compiled: Compiled) = Body(this, compiled)
val emptyBody = emptyBindings.asBody(compiled())

fun Body.plus(lineCompiled: LineCompiled): Body =
	set(compiled.plus(lineCompiled)).resolve

fun Body.set(compiled: Compiled): Body =
	copy(compiled = compiled)

val Body.begin: Body
	get() =
		copy(compiled = compiled())

val Body.beginDo: Body
	get() =
		bindings
			.plus(compiled.asGiven.asBinding)
			.asBody(compiled())

val Body.resolve: Body
	get() =
		copy(compiled = bindings.resolveOrNull(compiled) ?: compiled.resolve)
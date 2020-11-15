package leo21.token.body

import leo21.compiled.Compiled
import leo21.compiled.LineCompiled
import leo21.compiled.compiled
import leo21.compiled.plus
import leo21.compiled.resolve

data class Body(
	val module: Module,
	val compiled: Compiled
)

fun Module.asBody(compiled: Compiled) = Body(this, compiled)
val emptyBody = emptyModule.asBody(compiled())

fun Body.plus(lineCompiled: LineCompiled): Body =
	set(compiled.plus(lineCompiled)).resolve

fun Body.set(compiled: Compiled): Body =
	copy(compiled = compiled)

val Body.begin: Body
	get() =
		copy(compiled = compiled())

val Body.beginDo: Body
	get() =
		module
			.begin(compiled.type.asGiven)
			.asBody(compiled())

val Body.resolve: Body
	get() =
		copy(compiled = module.resolveOrNull(compiled) ?: compiled.resolve)

val Body.wrapCompiled: Compiled
	get() =
		compiled.wrap(module)

fun Body.plus(definitions: Definitions) =
	Body(
		module.plus(definitions),
		compiled.wrap(definitions))
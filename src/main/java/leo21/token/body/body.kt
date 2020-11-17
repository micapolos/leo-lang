package leo21.token.body

import leo21.compiled.Compiled
import leo21.compiled.LineCompiled
import leo21.compiled.compiled
import leo21.compiled.do_
import leo21.compiled.plus
import leo21.compiled.resolve
import leo21.token.type.compiler.cast

data class Body(
	val module: Module,
	val compiled: Compiled
)

fun Module.body(compiled: Compiled) = Body(this, compiled)
val emptyBody = emptyModule.body(compiled())

fun Body.plus(lineCompiled: LineCompiled): Body =
	set(compiled.plus(module.lines.cast(lineCompiled))).resolve

fun Body.set(compiled: Compiled): Body =
	copy(compiled = compiled)

val Body.begin: Body
	get() =
		copy(compiled = compiled())

val Body.beginDo: Body
	get() =
		module
			.begin(compiled.type.given)
			.body(compiled())

val Body.resolve: Body
	get() =
		copy(compiled = module.resolveOrNull(compiled) ?: compiled.resolve)

val Body.wrapCompiled: Compiled
	get() =
		compiled.wrap(module)

fun Body.plus(module: Module) =
	Body(
		module.plus(module),
		compiled.wrap(module.definitions))

fun Body.do_(body: Body): Body =
	set(compiled.do_(body.wrapCompiled))
package leo21.token.body

import leo14.ScriptLine
import leo14.Scriptable
import leo14.lineTo
import leo14.script
import leo15.dsl.*
import leo21.compiled.Compiled
import leo21.compiled.LineCompiled
import leo21.compiled.apply
import leo21.compiled.compiled
import leo21.compiled.do_
import leo21.compiled.plus
import leo21.compiled.resolve
import leo21.definition.Definition
import leo21.token.type.compiler.cast
import leo21.type.isEmpty

data class Body(
	val module: Module,
	val compiled: Compiled
) : Scriptable() {
	override fun toString() = super.toString()
	override val reflectScriptLine: ScriptLine
		get() = "body" lineTo script(module.reflectScriptLine, compiled.reflectScriptLine)
}

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

fun Body.plus(rhs: Module) =
	Body(
		module.plus(rhs),
		compiled)

fun Body.plus(definition: Definition) =
	if (!compiled.type.isEmpty) leo14.error { not { empty } }
	else Body(
		module.plus(definition),
		compiled)

fun Body.do_(rhs: Compiled): Body =
	set(compiled.do_(rhs))

fun Body.apply(rhs: Compiled): Body =
	set(compiled.apply(rhs))

package leo13.script

import leo.base.notNullIf
import leo13.*
import leo9.*

data class Compiled(val metable: Metable, val typed: Typed) : Scriptable() {
	override fun toString() = super.toString()
	override val asScriptLine = "compiled" lineTo script(metable.asScriptLine, typed.asScriptLine)
}

data class CompiledLine(val name: String, val rhs: Compiled) : Scriptable() {
	override fun toString() = super.toString()
	override val asScriptLine = "line" lineTo script(name lineTo script("to" lineTo script(rhs.asScriptLine)))
}

data class CompiledOpener(val lhs: Compiled, val opening: Opening) : Scriptable() {
	override fun toString() = super.toString()
	override val asScriptLine
		get() = "opener" lineTo script(lhs.asScriptLine, opening.asScriptLine)
}

data class CompiledOpeners(val stack: Stack<CompiledOpener>) : Scriptable() {
	override fun toString() = super.toString()
	override val asScriptLine
		get() = stack.asScriptLine("openers") { asScriptLine }
}

data class CompiledHead(val openers: CompiledOpeners, val compiled: Compiled) : Scriptable() {
	override fun toString() = super.toString()
	override val asScriptLine
		get() = "head" lineTo script(openers.asScriptLine, compiled.asScriptLine)
}

// --- constructors

fun compiled() = compiled(metable(), typed())
fun compiled(metable: Metable, typed: Typed) = Compiled(metable, typed)
fun compiled(typed: Typed) = Compiled(metable(context(), false), typed)
fun compiledOpeners(stack: Stack<CompiledOpener>) = CompiledOpeners(stack)
fun compiledOpeners(vararg openers: CompiledOpener) = CompiledOpeners(stack(*openers))
fun head(openers: CompiledOpeners, compiled: Compiled) = CompiledHead(openers, compiled)
fun head(typed: Typed) = head(compiledOpeners(), compiled(metable(), typed))
infix fun Compiled.openerTo(opening: Opening) = CompiledOpener(this, opening)
infix fun String.lineTo(rhs: Compiled) = CompiledLine(this, rhs)

val Context.metable get() = metable(this, false)
val Metable.compiled get() = compiled(this, typed())
val Compiled.head get() = head(compiledOpeners(), this)

fun CompiledOpeners.push(opener: CompiledOpener) = compiledOpeners(stack.push(opener))
val CompiledHead.completedCompiledOrNull get() = notNullIf(openers.stack.isEmpty) { compiled }

// --- plus

fun CompiledHead.plus(token: Token): CompiledHead? =
	when (token) {
		is OpeningToken -> plus(token.opening)
		is ClosingToken -> plus(token.closing)
	}

fun CompiledHead.plus(opening: Opening): CompiledHead =
	head(
		openers.push(compiled openerTo opening),
		compiled(
			compiled.metable.setMeta(!compiled.metable.isMeta && opening.name == "meta"),
			typed()))

fun CompiledHead.plus(closing: Closing): CompiledHead? =
	openers.stack.linkOrNull?.let { openerStackLink ->
		openerStackLink.value.let { opener ->
			opener.lhs.push(opener.opening.name lineTo compiled.typed)?.let { compiled ->
				head(compiledOpeners(openerStackLink.stack), compiled)
			}
		}
	}

fun Compiled.push(typedLine: TypedLine): Compiled? =
	if (head.compiled.metable.isMeta) append(typedLine)
	else when (typedLine.name) {
		"exists" -> resolveExists(typedLine.rhs)
		"gives" -> resolveGives(typedLine.rhs)
		"of" -> resolveOf(typedLine.rhs)
		else -> resolve(typedLine)
	}

fun Compiled.resolveExists(rhsTyped: Typed): Compiled? =
	typed.type.staticScriptOrNull?.type?.let { type ->
		rhsTyped.type.staticScriptOrNull?.let { rhsScript ->
			notNullIf(rhsScript.isEmpty) {
				compiled(metable.context.plus(type).metable, typed())
			}
		}
	}

fun Compiled.resolveGives(rhsTyped: Typed): Compiled? =
	typed.type.staticScriptOrNull?.type?.let { parameterType ->
		rhsTyped.type.staticScriptOrNull?.let { bodyScript ->
			metable.context.bind(parameterType).compile(bodyScript)?.let { bodyTyped ->
				compiled(metable.context.plus(function(parameterType, bodyTyped)).metable, typed())
			}
		}
	}

fun Compiled.resolveOf(rhsTyped: Typed): Compiled? =
	rhsTyped
		.type
		.staticScriptOrNull
		?.let { ofScript ->
			ofScript.type.let { ofScriptType ->
				notNullIf(ofScriptType.contains(this.typed.type)) {
					compiled(metable, typed.expr of ofScriptType)
				}
			}
		}

fun Compiled.resolve(typedLine: TypedLine): Compiled =
	append(typedLine)

fun Compiled.append(typedLine: TypedLine): Compiled =
	compiled(metable, typed.plus(typedLine))

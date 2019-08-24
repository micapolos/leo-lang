package leo13.script

import leo.base.notNullIf
import leo13.*
import leo9.*

data class Compiled(val context: Context, val typed: Typed) : Scriptable() {
	override fun toString() = super.toString()
	override val asScriptLine = "compiled" lineTo script(context.asScriptLine, typed.asScriptLine)
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

fun compiled() = compiled(context(), typed())
fun compiled(context: Context, typed: Typed) = Compiled(context, typed)
fun compiled(typed: Typed) = Compiled(context(), typed)
fun compiledOpeners(stack: Stack<CompiledOpener>) = CompiledOpeners(stack)
fun compiledOpeners(vararg openers: CompiledOpener) = CompiledOpeners(stack(*openers))
fun head(openers: CompiledOpeners, compiled: Compiled) = CompiledHead(openers, compiled)
fun head(typed: Typed) = head(compiledOpeners(), compiled(context(), typed))
infix fun Compiled.openerTo(opening: Opening) = CompiledOpener(this, opening)
infix fun String.lineTo(rhs: Compiled) = CompiledLine(this, rhs)

val Context.compiled get() = compiled(this, typed())
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
	head(openers.push(compiled openerTo opening), compiled(compiled.context, typed()))

fun CompiledHead.plus(closing: Closing): CompiledHead? =
	openers.stack.linkOrNull?.let { openerStackLink ->
		openerStackLink.value.let { opener ->
			opener.lhs.push(opener.opening.name lineTo compiled.typed)?.let { compiled ->
				head(compiledOpeners(openerStackLink.stack), compiled)
			}
		}
	}

fun Compiled.push(typedLine: TypedLine): Compiled? =
	when (typedLine.name) {
		"exists" -> pushExists(typedLine.rhs)
		"gives" -> pushGives(typedLine.rhs)
		"of" -> pushOf(typedLine.rhs)
		else -> resolve(typedLine)
	}

fun Compiled.pushExists(rhsTyped: Typed): Compiled? =
	typed.type.staticScriptOrNull?.type?.let { type ->
		rhsTyped.type.staticScriptOrNull?.let { rhsScript ->
			notNullIf(rhsScript.isEmpty) {
				compiled(context.plus(type), typed())
			}
		}
	}

fun Compiled.pushGives(rhsTyped: Typed): Compiled? =
	typed.type.staticScriptOrNull?.type?.let { parameterType ->
		rhsTyped.type.staticScriptOrNull?.let { bodyScript ->
			context.bind(parameterType).compile(bodyScript)?.let { bodyTyped ->
				compiled(context.plus(function(parameterType, bodyTyped)), typed())
			}
		}
	}

fun Compiled.pushOf(rhsTyped: Typed): Compiled? =
	rhsTyped
		.type
		.staticScriptOrNull
		?.let { ofScript ->
			ofScript.type.let { ofScriptType ->
				notNullIf(ofScriptType.contains(this.typed.type)) {
					compiled(context, typed.expr of ofScriptType)
				}
			}
		}

fun Compiled.resolve(typedLine: TypedLine): Compiled =
	append(typedLine)

fun Compiled.append(typedLine: TypedLine): Compiled =
	compiled(context, typed.plus(typedLine))

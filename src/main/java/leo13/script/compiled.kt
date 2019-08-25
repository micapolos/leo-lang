package leo13.script

import leo.base.fold
import leo.base.ifOrNull
import leo.base.notNullIf
import leo13.*
import leo13.Script
import leo9.*

data class Metable(
	val compiled: Compiled,
	val isMeta: Boolean) : AsScriptLine() {
	override fun toString() = super.toString()
	override val asScriptLine = "metable" lineTo script(
		compiled.asScriptLine,
		"meta" lineTo script(isMeta.toString()))
}

data class Compiled(
	val context: Context,
	val typed: Typed) : AsScriptLine() {
	override fun toString() = super.toString()
	override val asScriptLine = "compiled" lineTo script(context.asScriptLine, typed.asScriptLine)
}

data class CompiledLine(
	val name: String,
	val rhs: Metable) : AsScriptLine() {
	override fun toString() = super.toString()
	override val asScriptLine = "line" lineTo script(name lineTo script("to" lineTo script(rhs.asScriptLine)))
}

data class CompiledOpener(
	val lhs: Metable,
	val opening: Opening) : AsScriptLine() {
	override fun toString() = super.toString()
	override val asScriptLine
		get() = "opener" lineTo script(lhs.asScriptLine, opening.asScriptLine)
}

data class CompiledOpeners(
	val stack: Stack<CompiledOpener>) : AsScriptLine() {
	override fun toString() = super.toString()
	override val asScriptLine
		get() = stack.asScriptLine("openers") { asScriptLine }
}

data class CompiledHead(
	val openers: CompiledOpeners,
	val metable: Metable) : AsScriptLine() {
	override fun toString() = super.toString()
	override val asScriptLine
		get() = "head" lineTo script(openers.asScriptLine, metable.asScriptLine)
}

// --- constructors

fun metable(compiled: Compiled, isMeta: Boolean) = Metable(compiled, isMeta)
fun metable(compiled: Compiled) = metable(compiled, false)
fun metable() = metable(compiled())
fun Metable.setMeta(isMeta: Boolean) = metable(compiled, isMeta)

fun compiled(context: Context, typed: Typed) = Compiled(context, typed)
fun compiled(typed: Typed) = Compiled(context(), typed)
fun compiled() = compiled(context(), typed())

fun compiledOpeners(stack: Stack<CompiledOpener>) = CompiledOpeners(stack)
fun compiledOpeners(vararg openers: CompiledOpener) = CompiledOpeners(stack(*openers))
fun head(openers: CompiledOpeners, metable: Metable) = CompiledHead(openers, metable)
fun head(typed: Typed) = head(compiledOpeners(), metable(compiled(typed)))
fun opener(metable: Metable, opening: Opening) = metable openerTo opening
infix fun Metable.openerTo(opening: Opening) = CompiledOpener(this, opening)
infix fun String.lineTo(rhs: Metable) = CompiledLine(this, rhs)

val Context.compiled get() = compiled(this, typed())
val Compiled.metable get() = metable(this)
val Metable.head get() = head(compiledOpeners(), this)

fun CompiledOpeners.push(opener: CompiledOpener) = compiledOpeners(stack.push(opener))
val CompiledHead.completedCompiledOrNull: Compiled? get() = notNullIf(openers.stack.isEmpty) { metable.compiled }

// --- plus

fun CompiledHead.plus(token: Token): CompiledHead? =
	when (token) {
		is OpeningToken -> plus(token.opening)
		is ClosingToken -> plus(token.closing)
	}

fun CompiledHead.plus(opening: Opening): CompiledHead =
	if (!metable.isMeta && opening.name == "meta") head(openers, metable.setMeta(true))
	else head(
		openers.push(metable openerTo opening),
		metable(compiled(metable.compiled.context, typed())))

fun CompiledHead.plus(closing: Closing): CompiledHead? =
	if (metable.isMeta) head(openers, metable.setMeta(false))
	else openers.stack.linkOrNull?.let { openerStackLink ->
		openerStackLink.value.let { opener ->
			opener.lhs.push(opener.opening.name lineTo metable.compiled.typed)?.let { compiled ->
				head(compiledOpeners(openerStackLink.stack), metable(compiled, openerStackLink.value.lhs.isMeta))
			}
		}
	}

fun Metable.push(typedLine: TypedLine): Compiled? =
	if (isMeta) compiled.append(typedLine)
	else compiled.push(typedLine)

fun Compiled.push(typedLine: TypedLine): Compiled? =
	when (typedLine.name) {
		"exists" -> pushExists(typedLine.rhs)
		"gives" -> pushGives(typedLine.rhs)
		"line" -> pushLine(typedLine.rhs)
		"of" -> pushOf(typedLine.rhs)
		"previous" -> pushPrevious(typedLine.rhs)
		"switch" -> pushSwitch(typedLine.rhs)
		else -> pushOther(typedLine)
	}

fun Compiled.pushExists(rhsTyped: Typed): Compiled? =
	typed.type.staticScriptOrNull?.typeOrNull?.let { type ->
		rhsTyped.type.staticScriptOrNull?.let { rhsScript ->
			notNullIf(rhsScript.isEmpty) {
				compiled(context.plus(type), typed())
			}
		}
	}

fun Compiled.pushGives(rhsTyped: Typed): Compiled? =
	typed.type.staticScriptOrNull?.typeOrNull?.let { parameterType ->
		rhsTyped.type.staticScriptOrNull?.let { bodyScript ->
			context.bind(parameterType).typedOrNull(bodyScript)?.let { bodyTyped ->
				compiled(context.plus(function(parameterType, bodyTyped)), typed())
			}
		}
	}

fun Compiled.pushOf(rhsTyped: Typed): Compiled? =
	rhsTyped
		.type
		.staticScriptOrNull
		?.let { ofScript ->
			ofScript.typeOrNull?.let { ofScriptType ->
				notNullIf(ofScriptType.contains(this.typed.type)) {
					compiled(typed.expr of ofScriptType)
				}
			}
		}

fun Compiled.pushPrevious(rhsTyped: Typed): Compiled? =
	ifOrNull(rhsTyped.expr.isEmpty) {
		typed.previousOrNull?.let { set(it) }
	}

fun Compiled.pushLine(rhsTyped: Typed): Compiled? =
	ifOrNull(rhsTyped.expr.isEmpty) {
		typed.lineOrNull?.let { set(it) }
	}

fun Compiled.pushSwitch(rhsTyped: Typed): Compiled? =
	rhsTyped.type.staticScriptOrNull?.let { rhsStaticScript ->
		pushSwitch(rhsStaticScript)
	}

fun Compiled.pushSwitch(rhsScript: Script): Compiled? =
	("switch" lineTo rhsScript)
		.switchOrNull
		?.let { switch ->
			typed.type.onlyChoiceOrNull?.let { choice ->
				switch
					.choiceMatchOrNull(choice)
					?.eitherMatchStack
					?.mapOrNull { caseTypedOrNull(this) }
					?.typedSwitch
					?.switchTypedOrNull
					?.let { switchTyped ->
						set(
							typed(
								typed.expr.plus(op(switchTyped.switch)),
								switchTyped.type))
					}
			}
		}

fun Compiled.caseTypedOrNull(eitherMatch: EitherMatch): CaseTyped? =
	set(typed(expr(), eitherMatch.either.type))
		.typedOrNull(eitherMatch.script)
		?.let { rhsTyped -> typed(eitherMatch.either.name caseTo rhsTyped.expr, rhsTyped.type) }

fun Compiled.typedOrNull(script: Script): Typed? =
	metable
		.head
		.compiler
		.fold(script.tokenSeq) { push(it) }
		.successHeadOrNull
		?.completedCompiledOrNull
		?.typed

fun Compiled.pushOther(typedLine: TypedLine): Compiled =
	null
		?: pushGetOrNull(typedLine)
		?: append(typedLine)

fun Compiled.pushGetOrNull(typedLine: TypedLine): Compiled? =
	ifOrNull(typedLine.rhs.expr.isEmpty) {
		typed.accessOrNull(typedLine.name)?.let { set(it) }
	}

fun Compiled.append(typedLine: TypedLine): Compiled =
	compiled(typed.plus(typedLine))

fun Compiled.set(typed: Typed): Compiled =
	copy(typed = typed)

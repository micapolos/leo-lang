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
	head(
		openers.push(metable openerTo opening),
		metable(
			compiled(metable.compiled.context, typed()),
			!metable.isMeta && opening.name == "meta"))

fun CompiledHead.plus(closing: Closing): CompiledHead? =
	openers.stack.linkOrNull?.let { openerStackLink ->
		openerStackLink.value.let { opener ->
			opener.lhs.push(opener.opening.name lineTo metable.compiled.typed)?.let { compiled ->
				head(compiledOpeners(openerStackLink.stack), metable(compiled))
			}
		}
	}

fun Metable.push(typedLine: TypedLine): Compiled? =
	if (isMeta) compiled.append(typedLine)
	else compiled.push(typedLine)

fun Compiled.push(typedLine: TypedLine): Compiled? =
	when (typedLine.name) {
		"exists" -> resolveExists(typedLine.rhs)
		"gives" -> resolveGives(typedLine.rhs)
		"line" -> resolveLine(typedLine.rhs)
		"of" -> resolveOf(typedLine.rhs)
		"previous" -> resolvePrevious(typedLine.rhs)
		"switch" -> resolveSwitch(typedLine.rhs)
		else -> resolve(typedLine)
	}

fun Compiled.resolveExists(rhsTyped: Typed): Compiled? =
	typed.type.staticScriptOrNull?.typeOrNull?.let { type ->
		rhsTyped.type.staticScriptOrNull?.let { rhsScript ->
			notNullIf(rhsScript.isEmpty) {
				compiled(context.plus(type), typed())
			}
		}
	}

fun Compiled.resolveGives(rhsTyped: Typed): Compiled? =
	typed.type.staticScriptOrNull?.typeOrNull?.let { parameterType ->
		rhsTyped.type.staticScriptOrNull?.let { bodyScript ->
			context.bind(parameterType).typedOrNull(bodyScript)?.let { bodyTyped ->
				compiled(context.plus(function(parameterType, bodyTyped)), typed())
			}
		}
	}

fun Compiled.resolveOf(rhsTyped: Typed): Compiled? =
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

fun Compiled.resolvePrevious(rhsTyped: Typed): Compiled? =
	ifOrNull(rhsTyped.expr.isEmpty) {
		typed.previousOrNull?.let { set(it) }
	}

fun Compiled.resolveLine(rhsTyped: Typed): Compiled? =
	ifOrNull(rhsTyped.expr.isEmpty) {
		typed.lineOrNull?.let { set(it) }
	}

fun Compiled.resolveSwitch(rhsTyped: Typed): Compiled? =
	rhsTyped.type.staticScriptOrNull?.let { rhsStaticScript ->
		resolveSwitch(rhsStaticScript)
	}

fun Compiled.resolveSwitch(rhsScript: Script): Compiled? =
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

fun Compiled.resolve(typedLine: TypedLine): Compiled =
	null
		?: resolveGetOrNull(typedLine)
		?: append(typedLine)

fun Compiled.resolveGetOrNull(typedLine: TypedLine): Compiled? =
	ifOrNull(typedLine.rhs.expr.isEmpty) {
		typed.accessOrNull(typedLine.name)?.let { set(it) }
	}

fun Compiled.append(typedLine: TypedLine): Compiled =
	compiled(typed.plus(typedLine))

fun Compiled.set(typed: Typed): Compiled =
	copy(typed = typed)

package leo21.token.type.compiler

import leo.base.notNullIf
import leo.base.runIf
import leo13.firstEither
import leo13.map
import leo14.BeginToken
import leo14.EndToken
import leo14.LiteralToken
import leo14.ScriptLine
import leo14.Scriptable
import leo14.Token
import leo14.anyOptionalReflectScriptLine
import leo14.error
import leo14.lineTo
import leo14.script
import leo15.dsl.*
import leo19.script
import leo21.token.processor.ArrowCompilerProcessor
import leo21.token.processor.ChoiceCompilerProcessor
import leo21.token.processor.Processor
import leo21.token.processor.TypeCompilerProcessor
import leo21.token.processor.TypeRecurseCompilerProcessor
import leo21.token.processor.processor
import leo21.type.Line
import leo21.type.Type
import leo21.type.choice
import leo21.type.isEmpty
import leo21.type.line
import leo21.type.lineTo
import leo21.type.numberLine
import leo21.type.plus
import leo21.type.recurse
import leo21.type.recursive
import leo21.type.stringLine
import leo21.type.type

data class TypeCompiler(
	val parentOrNull: TypeParent?,
	val lines: Lines,
	val type: Type,
	val autoEnd: Boolean = false
) : Scriptable() {
	override fun toString() = super.toString()
	override val reflectScriptLine: ScriptLine
		get() = "compiler" lineTo script(
			parentOrNull.anyOptionalReflectScriptLine("parent"),
			lines.reflectScriptLine,
			type.reflectScriptLine,
			"autoend" lineTo autoEnd.script)
}

val emptyTypeCompiler = TypeCompiler(null, emptyLines, type())

fun TypeCompiler.plus(token: Token): Processor =
	when (token) {
		is LiteralToken -> error { not { expected { literal } } }
		is BeginToken -> plusBegin(token.begin.string)
		is EndToken ->
			if (autoEnd) error { not { expected { end } } }
			else end
	}

fun TypeCompiler.plusBegin(name: String): Processor =
	when (name) {
		"choice" ->
			if (type.isEmpty)
				ChoiceCompilerProcessor(
					ChoiceCompiler(
						TypeCompilerChoiceParent(this),
						lines,
						choice()))
			else error { not { expected { word { choice } } } }
		"function" -> ArrowCompilerProcessor(
			ArrowCompiler(
				TypeCompilerArrowParent(this),
				lines,
				type().firstEither()))
		"recursive" ->
			TypeCompilerProcessor(
				TypeCompiler(
					RecursiveTypeParent(this),
					lines,
					type()))
		"recurse" ->
			TypeRecurseCompilerProcessor(TypeRecurseCompiler(this))
		else -> TypeCompilerProcessor(
			TypeCompiler(
				TypeNameTypeParent(this, name),
				lines,
				type()))
	}

fun TypeCompiler.plus(name: String, rhs: Type): Processor =
	plus(lines.resolve(name compiledLineTo rhs))

fun TypeCompiler.plus(line: Line): Processor =
	process(type.plus(line))

fun TypeCompiler.set(type: Type): TypeCompiler =
	copy(type = type)

fun TypeCompiler.process(type: Type): Processor =
	set(type).process

fun TypeCompiler.plusRecursive(type: Type): Processor =
	process(type.lineStack.map { line(recursive(this)) }.type)

infix fun String.compiledLineTo(rhs: Type): Line =
	when (this) {
		"number" -> notNullIf(rhs == type()) { numberLine }
		"text" -> notNullIf(rhs == type()) { stringLine }
		else -> null
	} ?: this lineTo rhs

val TypeCompiler.process: Processor
	get() =
		processor.runIf(autoEnd) { end }

val TypeCompiler.end: Processor
	get() =
		parentOrNull!!.plus(type)

val TypeCompiler.plusRecurse: TypeCompiler
	get() =
		copy(type = type.plus(line(recurse(0))))
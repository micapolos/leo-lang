package leo14.untyped.typed.lambda

import leo.base.fold
import leo.base.ifOrNull
import leo.base.notNullIf
import leo14.*
import leo14.lambda2.*
import leo14.untyped.leoString
import leo14.untyped.typed.*
import java.math.BigDecimal

data class Compiled(val type: Type, val term: Term) {
	override fun toString() = script(
		"compiled" lineTo script(
			"type" lineTo type.script,
			"term" lineTo term.script)).leoString
}

data class CompiledLink(val lhs: Compiled, val line: CompiledLine)
data class CompiledLine(val typeLine: TypeLine, val term: Term)
data class CompiledField(val typeField: TypeField, val term: Term)

fun Type.compiled(term: Term) = Compiled(this, term)
infix fun Compiled.linkTo(line: CompiledLine) = CompiledLink(this, line)
infix fun TypeLine.compiled(term: Term) = CompiledLine(this, term)
infix fun TypeField.compiled(term: Term) = CompiledField(this, term)
infix fun String.lineTo(compiled: Compiled): CompiledLine =
	this.lineTo(compiled.type).compiled(compiled.term)

val emptyCompiled = emptyType.compiled(nil)

val Any?.nativeCompiledLine: CompiledLine get() = valueTerm.nativeCompiledLine
val Any?.nativeCompiled: Compiled get() = valueTerm.nativeCompiled

val Term.nativeCompiledLine: CompiledLine get() = nativeTypeLine.compiled(this)
val Term.nativeCompiled: Compiled get() = compiled(nativeCompiledLine)

val String.compiledLine: CompiledLine get() = textTypeLine.compiled(valueTerm)
val String.compiled: Compiled get() = compiled(compiledLine)

val Int.compiledLine: CompiledLine get() = bigDecimal.compiledLine
val Int.compiled: Compiled get() = bigDecimal.compiled

val BigDecimal.compiledLine: CompiledLine get() = numberTypeLine.compiled(valueTerm)
val BigDecimal.compiled: Compiled get() = compiled(compiledLine)

fun Type.does(type: Type, f: Compiled.() -> Compiled): Compiled =
	functionTo(type).type.compiled(fn(type.compiled(at(0)).f().term))

fun Compiled.invokeOrNull(compiled: Compiled): Compiled? =
	type.functionOrNull?.let { typeFunction ->
		notNullIf(typeFunction.from == compiled.type) {
			typeFunction.to.compiled(term.invoke(compiled.term))
		}
	}

val Literal.compiled
	get() = when (this) {
		is StringLiteral -> string.compiled
		is NumberLiteral -> number.bigDecimal.compiled
	}

fun Compiled.plus(line: CompiledLine): Compiled =
	type.plus(line.typeLine).compiled(pair.invoke(term).invoke(line.term))

fun compiled(vararg lines: CompiledLine): Compiled =
	emptyCompiled.fold(lines) { plus(it) }

fun compiled(name: String): Compiled =
	compiled(name lineTo emptyCompiled)

fun Compiled.matchEmpty(fn: () -> Compiled?): Compiled? =
	ifOrNull(type.isEmpty, fn)

fun Compiled.updateOrNull(fn: (Compiled) -> Compiled?): Compiled? =
	fn(type.compiled(at(0)))?.let { updated ->
		updated.type.compiled(fn(updated.term).invoke(term))
	}

val Compiled.linkOrNull: CompiledLink?
	get() =
		type.linkOrNull?.let { typeLink ->
			typeLink.lhs.compiled(term.invoke(first)) linkTo typeLink.line.compiled(term.invoke(second))
		}

val CompiledLink.onlyLineOrNull: CompiledLine?
	get() =
		notNullIf(lhs.type.isEmpty) { line }

val CompiledLine.fieldOrNull: CompiledField?
	get() =
		typeLine.fieldOrNull?.let { typeField ->
			typeField.compiled(term)
		}

val CompiledField.rhs: Compiled?
	get() =
		typeField.rhs.compiled(term)

fun Compiled.matchLink(fn: Compiled.(CompiledLine) -> Compiled?): Compiled? =
	updateOrNull {
		linkOrNull?.let { link ->
			link.lhs.fn(link.line)
		}
	}

fun CompiledLine.match(name: String, fn: (Compiled) -> Compiled?): Compiled? =
	typeLine.fieldOrNull?.let { field ->
		ifOrNull(field.name == name) {
			fn(field.rhs.compiled(term))
		}
	}

fun CompiledField.match(name: String, fn: (Compiled) -> Compiled?): Compiled? =
	ifOrNull(typeField.name == name) {
		fn(typeField.rhs.compiled(term))
	}

fun Compiled.matchInfix(name: String, fn: Compiled.(Compiled) -> Compiled?): Compiled? =
	matchLink { line ->
		line.match(name) { rhs ->
			fn(rhs)
		}
	}

fun Compiled.matchPrefix(name: String, fn: Compiled.() -> Compiled?): Compiled? =
	matchInfix(name) { rhs ->
		matchEmpty {
			rhs.fn()
		}
	}

fun Compiled.matchName(fn: String.() -> Compiled?): Compiled? =
	matchLink { line ->
		matchEmpty {
			line.typeLine.fieldOrNull?.let { field ->
				ifOrNull(field.rhs.isEmpty) {
					field.name.fn()
				}
			}
		}
	}

fun CompiledLine.matchText(fn: Term.() -> Compiled?): Compiled? =
	ifOrNull(typeLine == textTypeLine) {
		term.fn()
	}

fun CompiledLine.matchNumber(fn: Term.() -> Compiled?): Compiled? =
	ifOrNull(typeLine == numberTypeLine) {
		term.fn()
	}

fun CompiledLine.matchNative(fn: Term.() -> Compiled?): Compiled? =
	ifOrNull(typeLine == nativeTypeLine) {
		term.fn()
	}

fun Compiled.matchLine(fn: CompiledLine.() -> Compiled?): Compiled? =
	matchLink { rhs ->
		matchEmpty {
			rhs.fn()
		}
	}

fun Compiled.matchText(fn: Term.() -> Compiled?): Compiled? =
	matchLine {
		matchText(fn)
	}

fun Compiled.matchNumber(fn: Term.() -> Compiled?): Compiled? =
	matchLine {
		matchNumber(fn)
	}

fun Compiled.matchNative(fn: Term.() -> Compiled?): Compiled? =
	matchLine {
		matchNative(fn)
	}

fun CompiledLine.matchName(fn: (String) -> Compiled?): Compiled? =
	fieldOrNull?.matchName(fn)

fun CompiledField.matchName(fn: (String) -> Compiled?): Compiled? =
	ifOrNull(typeField.rhs.isEmpty) {
		fn(typeField.name)
	}

val Compiled.eval: Compiled
	get() =
		type.compiled(term.eval)

fun Compiled.updateTerm(fn: Term.() -> Term): Compiled =
	copy(term = term.fn())

fun Compiled.make(name: String): Compiled? =
	compiled(name lineTo this)
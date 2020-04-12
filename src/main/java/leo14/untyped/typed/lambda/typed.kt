package leo14.untyped.typed.lambda

import leo.base.fold
import leo.base.ifOrNull
import leo.base.notNullIf
import leo14.*
import leo14.lambda2.*
import leo14.untyped.leoString
import leo14.untyped.typed.*
import java.math.BigDecimal

data class Typed(val type: Type, val term: Term) {
	override fun toString() = script(
		"typed" lineTo script(
			"type" lineTo type.script,
			"term" lineTo term.script)).leoString
}

data class TypedLink(val lhs: Typed, val line: TypedLine)
data class TypedLine(val typeLine: TypeLine, val term: Term)
data class TypedField(val typeField: TypeField, val term: Term)

fun Type.typed(term: Term) = Typed(this, term)
infix fun Typed.linkTo(line: TypedLine) = TypedLink(this, line)
infix fun TypeLine.typed(term: Term) = TypedLine(this, term)
infix fun TypeField.typed(term: Term) = TypedField(this, term)
infix fun String.lineTo(typed: Typed): TypedLine =
	this.lineTo(typed.type).typed(typed.term)

val emptyTyped = emptyType.typed(nil)

val Any?.nativeTypedLine: TypedLine get() = valueTerm.nativeTypedLine
val Any?.nativeTyped: Typed get() = valueTerm.nativeTyped

val Term.nativeTypedLine: TypedLine get() = nativeTypeLine.typed(this)
val Term.nativeTyped: Typed get() = typed(nativeTypedLine)

val String.typedLine: TypedLine get() = textTypeLine.typed(valueTerm)
val String.typed: Typed get() = typed(typedLine)

val Int.typedLine: TypedLine get() = bigDecimal.typedLine
val Int.typed: Typed get() = bigDecimal.typed

val BigDecimal.typedLine: TypedLine get() = numberTypeLine.typed(valueTerm)
val BigDecimal.typed: Typed get() = typed(typedLine)

fun Type.does(type: Type, f: Typed.() -> Typed): Typed =
	functionTo(type).type.typed(fn(type.typed(at(0)).f().term))

fun Typed.invokeOrNull(typed: Typed): Typed? =
	type.functionOrNull?.let { typeFunction ->
		notNullIf(typeFunction.from == typed.type) {
			typeFunction.to.typed(term.invoke(typed.term))
		}
	}

val Literal.typed
	get() = when (this) {
		is StringLiteral -> string.typed
		is NumberLiteral -> number.bigDecimal.typed
	}

fun Typed.plus(line: TypedLine): Typed =
	type.plus(line.typeLine).typed(pair.invoke(term).invoke(line.term))

fun typed(vararg lines: TypedLine): Typed =
	emptyTyped.fold(lines) { plus(it) }

fun typed(name: String): Typed =
	typed(name lineTo emptyTyped)

fun Typed.matchEmpty(fn: () -> Typed?): Typed? =
	ifOrNull(type.isEmpty, fn)

fun Typed.updateOrNull(fn: (Typed) -> Typed?): Typed? =
	fn(type.typed(at(0)))?.let { updated ->
		updated.type.typed(fn(updated.term).invoke(term))
	}

val Typed.linkOrNull: TypedLink?
	get() =
		type.linkOrNull?.let { typeLink ->
			typeLink.lhs.typed(term.invoke(first)) linkTo typeLink.line.typed(term.invoke(second))
		}

val TypedLink.onlyLineOrNull: TypedLine?
	get() =
		notNullIf(lhs.type.isEmpty) { line }

val TypedLine.fieldOrNull: TypedField?
	get() =
		typeLine.fieldOrNull?.let { typeField ->
			typeField.typed(term)
		}

val TypedField.rhs: Typed?
	get() =
		typeField.rhs.typed(term)

fun Typed.matchLink(fn: Typed.(TypedLine) -> Typed?): Typed? =
	updateOrNull {
		linkOrNull?.let { link ->
			link.lhs.fn(link.line)
		}
	}

fun TypedLine.match(name: String, fn: (Typed) -> Typed?): Typed? =
	typeLine.fieldOrNull?.let { field ->
		ifOrNull(field.name == name) {
			fn(field.rhs.typed(term))
		}
	}

fun TypedField.match(name: String, fn: (Typed) -> Typed?): Typed? =
	ifOrNull(typeField.name == name) {
		fn(typeField.rhs.typed(term))
	}

fun Typed.matchInfix(name: String, fn: Typed.(Typed) -> Typed?): Typed? =
	matchLink { line ->
		line.match(name) { rhs ->
			fn(rhs)
		}
	}

fun Typed.matchPrefix(name: String, fn: Typed.() -> Typed?): Typed? =
	matchInfix(name) { rhs ->
		matchEmpty {
			rhs.fn()
		}
	}

fun Typed.matchName(fn: String.() -> Typed?): Typed? =
	matchLink { line ->
		matchEmpty {
			line.typeLine.fieldOrNull?.let { field ->
				ifOrNull(field.rhs.isEmpty) {
					field.name.fn()
				}
			}
		}
	}

fun TypedLine.matchText(fn: Term.() -> Typed?): Typed? =
	ifOrNull(typeLine == textTypeLine) {
		term.fn()
	}

fun TypedLine.matchNumber(fn: Term.() -> Typed?): Typed? =
	ifOrNull(typeLine == numberTypeLine) {
		term.fn()
	}

fun TypedLine.matchNative(fn: Term.() -> Typed?): Typed? =
	ifOrNull(typeLine == nativeTypeLine) {
		term.fn()
	}

fun Typed.matchLine(fn: TypedLine.() -> Typed?): Typed? =
	matchLink { rhs ->
		matchEmpty {
			rhs.fn()
		}
	}

fun Typed.matchText(fn: Term.() -> Typed?): Typed? =
	matchLine {
		matchText(fn)
	}

fun Typed.matchNumber(fn: Term.() -> Typed?): Typed? =
	matchLine {
		matchNumber(fn)
	}

fun Typed.matchNative(fn: Term.() -> Typed?): Typed? =
	matchLine {
		matchNative(fn)
	}

fun TypedLine.matchName(fn: (String) -> Typed?): Typed? =
	fieldOrNull?.matchName(fn)

fun TypedField.matchName(fn: (String) -> Typed?): Typed? =
	ifOrNull(typeField.rhs.isEmpty) {
		fn(typeField.name)
	}

val Typed.eval: Typed
	get() =
		type.typed(term.eval)

fun Typed.updateTerm(fn: Term.() -> Term): Typed =
	copy(term = term.fn())

fun Typed.make(name: String): Typed? =
	typed(name lineTo this)
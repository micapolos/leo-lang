package leo14.untyped.typed.lambda

import leo.base.fold
import leo.base.ifOrNull
import leo.base.map
import leo.base.notNullIf
import leo14.*
import leo14.lambda2.*
import leo14.untyped.leoString
import leo14.untyped.numberName
import leo14.untyped.textName
import leo14.untyped.typed.*
import java.math.BigDecimal

data class Typed(val type: Type, val term: Term) {
	override fun toString() = reflectScriptLine.leoString
}

data class TypedLink(val lhs: Typed, val line: TypedLine) {
	override fun toString() = lhs.plus(line).reflectScriptLine.leoString
}

data class TypedLine(val typeLine: TypeLine, val term: Term)
data class TypedField(val typeField: TypeField, val term: Term)

val Typed.reflectScriptLine: ScriptLine
	get() =
		"typed" lineTo script(
			"type" lineTo type.script,
			"term" lineTo term.script)

fun Type.typed(term: Term) = Typed(this, term)
infix fun Typed.linkTo(line: TypedLine) = TypedLink(this, line)
infix fun TypeLine.typed(term: Term) = TypedLine(this, term)
infix fun TypeField.typed(term: Term) = TypedField(this, term)
infix fun String.lineTo(typed: Typed): TypedLine =
	this.lineTo(typed.type).typed(typed.term)

val emptyTyped = emptyType.typed(nil)

val Any?.valueJavaTypedLine: TypedLine get() = valueTerm.javaTypedLine
val Any?.valueJavaTyped: Typed get() = valueTerm.javaTyped

val Term.javaTypedLine: TypedLine get() = javaTypeLine.typed(this)
val Term.javaTyped: Typed get() = typed(javaTypedLine)

val String.typedLine: TypedLine get() = textName lineTo valueJavaTyped
val String.typed: Typed get() = typed(typedLine)

val Int.typedLine: TypedLine get() = bigDecimal.typedLine
val Int.typed: Typed get() = bigDecimal.typed

val BigDecimal.typedLine: TypedLine get() = numberName lineTo valueJavaTyped
val BigDecimal.typed: Typed get() = typed(typedLine)

fun Type.does(type: Type, f: Typed.() -> Typed): Typed =
	functionTo(type).type.typed(fn(type.typed(at(0)).f().term))

fun Typed.invokeOrNull(typed: Typed): Typed? =
	type.functionOrNull?.let { typeFunction ->
		notNullIf(typeFunction.from == typed.type) {
			typeFunction.to.typed(term.invoke(typed.term))
		}
	}

val Literal.typedLine: TypedLine
	get() = when (this) {
		is StringLiteral -> string.typedLine
		is NumberLiteral -> number.bigDecimal.typedLine
	}

val Literal.typed: Typed
	get() =
		typed(typedLine)

val Literal.staticTypedLine: TypedLine
	get() =
		LiteralTypeLine(this).typed(nil)

val Literal.staticTyped: Typed
	get() =
		type.typed(nil)

fun Typed.plus(line: TypedLine): Typed =
	type.plus(line.typeLine).typed(
		if (type.isEmpty) line.term
		else pair.invoke(term).invoke(line.term))

fun typed(vararg lines: TypedLine): Typed =
	emptyTyped.fold(lines) { plus(it) }

fun typed(name: String): Typed =
	typed(name lineTo emptyTyped)

fun Typed.matchEmpty(fn: () -> Typed?): Typed? =
	ifOrNull(type.isEmpty, fn)

val Typed.linkOrNull: TypedLink?
	get() =
		type.linkOrNull?.let { typeLink ->
			if (typeLink.lhs.isEmpty) emptyTyped linkTo typeLink.line.typed(term)
			else typeLink.lhs.typed(term.invoke(first)) linkTo typeLink.line.typed(term.invoke(second))
		}

val TypedLink.onlyLineOrNull: TypedLine?
	get() =
		notNullIf(lhs.type.isEmpty) { line }

val TypedLine.fieldOrNull: TypedField?
	get() =
		typeLine.fieldOrNull?.typed(term)

val TypedField.rhs: Typed
	get() =
		typeField.rhs.typed(term)

fun Typed.matchLink(fn: Typed.(TypedLine) -> Typed?): Typed? =
	linkOrNull?.let { link ->
		link.lhs.fn(link.line)
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

fun Typed.matchInfixOrPrefix(name: String, fn: Typed.(Typed) -> Typed?): Typed? =
	matchInfix(name) { rhs ->
		fn(rhs)
	} ?: matchPrefix(name) {
		emptyTyped.fn(this)
	}

fun Typed.matchPrefix(name: String, fn: Typed.() -> Typed?): Typed? =
	matchInfix(name) { rhs ->
		matchEmpty {
			rhs.fn()
		}
	}

fun Typed.matchPrefix(fn: String.(Typed) -> Typed?): Typed? =
	matchLink { line ->
		matchEmpty {
			line.fieldOrNull?.let { field ->
				field.typeField.name.fn(field.rhs)
			}
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
	match(textName) { rhs ->
		rhs.matchJava {
			fn()
		}
	}

fun TypedLine.matchNumber(fn: Term.() -> Typed?): Typed? =
	match(numberName) { rhs ->
		rhs.matchJava {
			fn()
		}
	}

fun TypedLine.matchJava(fn: Term.() -> Typed?): Typed? =
	ifOrNull(typeLine is JavaTypeLine) {
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

fun Typed.matchJava(fn: Term.() -> Typed?): Typed? =
	matchLine {
		matchJava(fn)
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

val Typed.staticScriptOrNull: Script?
	get() =
		type.staticScriptOrNull

val Typed.staticTypeOrNull: Type?
	get() =
		staticScriptOrNull?.type

val Typed.withFnTerm
	get() =
		type.typed(fn(term))

fun Typed.repeatingOrNull(typeLine: TypeLine): Typed? =
	if (type.isEmpty) javaType.typed(nil)
	else linkOrNull?.let { link ->
		ifOrNull(link.line.typeLine == typeLine) {
			link.lhs.repeatingOrNull(typeLine)?.let { typedLhs ->
				typedLhs.type.typed(fn { lhs ->
					fn { rhs ->
						lhs.plus(rhs)
					}
				}.invoke(typedLhs.term).invoke(link.line.term))
			}
		}
	}

inline fun <reified T> Typed.javaArrayOrNull(typeLine: TypeLine): Typed? =
	repeatingOrNull(typeLine)?.let { repeating ->
		javaType.typed(repeating.term.apply {
			repeatingTermSeq.map { value as T }.toList().toTypedArray().valueTerm
		})
	}

fun Typed.set(term: Term): Typed =
	type.typed(term)
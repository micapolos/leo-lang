package leo15

import leo.base.fold
import leo.base.ifOrNull
import leo.base.notNullIf
import leo14.Literal
import leo14.NumberLiteral
import leo14.StringLiteral
import leo14.untyped.leoString
import leo15.lambda.nil

data class TypeFunction(val from: Type, val to: Type)
data class TypeAlternative(val lhs: Type, val rhs: Type)

data class TypeRepeating(val type: Type)
data class TypeRecursive(val type: Type)
data class TypeOr(val type: Type)

sealed class Type {
	override fun toString(): String = script.leoString
}

object EmptyType : Type()
object AnythingType : Type()
object NothingType : Type()
data class LinkType(val link: TypeLink) : Type() {
	override fun toString() = super.toString()
}

data class AlternativeType(val alternative: TypeAlternative) : Type() {
	override fun toString() = super.toString()
}

data class FunctionType(val function: TypeFunction) : Type() {
	override fun toString() = super.toString()
}

data class RepeatingType(val repeating: TypeRepeating) : Type() {
	override fun toString() = super.toString()
}

data class RecursiveType(val recursive: TypeRecursive) : Type() {
	override fun toString() = super.toString()
}

object RecurseType : Type()

data class TypeLink(val lhs: Type, val line: TypeLine)

sealed class TypeLine
data class LiteralTypeLine(val literal: Literal) : TypeLine()
data class FieldTypeLine(val field: TypeField) : TypeLine()
object JavaTypeLine : TypeLine()

data class TypeField(val name: String, val rhs: Type)

// === constructors ===

infix fun Type.functionTo(type: Type) = TypeFunction(this, type)
val Type.or get() = TypeOr(this)
val Type.recursive get() = TypeRecursive(this)
val Type.repeating get() = TypeRepeating(this)
val emptyType: Type = EmptyType
val anythingType: Type = AnythingType
val nothingType: Type = NothingType
val recurseType: Type = RecurseType
val TypeAlternative.type: Type get() = AlternativeType(this)
val TypeFunction.type: Type get() = FunctionType(this)
val TypeRecursive.toType: Type get() = RecursiveType(this)
val TypeRepeating.toType: Type get() = RepeatingType(this)
val TypeLink.type: Type get() = LinkType(this)
infix fun Type.linkTo(line: TypeLine) = TypeLink(this, line)
fun Type.plus(line: TypeLine) = linkTo(line).type
fun Type.plus(field: TypeField) = plus(field.line)
fun Type.plus(name: String) = plus(name fieldTo emptyType)
val Literal.typeLine: TypeLine get() = LiteralTypeLine(this)
val Literal.type: Type get() = type(typeLine)
val TypeField.line: TypeLine get() = FieldTypeLine(this)
val javaTypeLine: TypeLine = JavaTypeLine
infix fun String.fieldTo(type: Type) = TypeField(this, type)
infix fun String.lineTo(type: Type) = fieldTo(type).line
operator fun String.invoke(type: Type) = lineTo(type)
val Type.isEmpty: Boolean get() = this is EmptyType
infix fun Type.alternativeTo(rhs: Type) = TypeAlternative(this, rhs)
fun Type.or(rhs: Type): Type = alternativeTo(rhs).type
fun type(vararg lines: TypeLine): Type = emptyType.fold(lines) { plus(it) }
fun type(name: String): Type = type(name lineTo emptyType)

val Type.linkOrNull: TypeLink? get() = (this as? LinkType)?.link
val Type.functionOrNull: TypeFunction? get() = (this as? FunctionType)?.function
val Type.repeatingOrNull: TypeRepeating? get() = (this as? RepeatingType)?.repeating
val TypeLine.fieldOrNull: TypeField? get() = (this as? FieldTypeLine)?.field
val TypeLink.onlyLineOrNull: TypeLine? get() = notNullIf(lhs.isEmpty) { line }

val javaType = emptyType.plus(javaTypeLine)
val textTypeLine get() = textName lineTo javaType
val numberTypeLine get() = numberName lineTo javaType
val textType = type(textTypeLine)
val numberType = type(numberTypeLine)

val Literal.valueTypeLine: TypeLine
	get() =
		when (this) {
			is StringLiteral -> textTypeLine
			is NumberLiteral -> numberTypeLine
		}

fun <R : Any> Type.matchEmpty(fn: () -> R?): R? =
	ifOrNull(isEmpty) { fn() }

fun <R : Any> Type.matchInfix(name: String, fn: Type.(Type) -> R?): R? =
	linkOrNull?.let { link ->
		link.line.fieldOrNull?.let { field ->
			ifOrNull(field.name == name) {
				link.lhs.fn(field.rhs)
			}
		}
	}

fun <R : Any> Type.matchPrefix(name: String, fn: Type.() -> R?): R? =
	matchInfix(name) { rhs ->
		matchEmpty {
			rhs.fn()
		}
	}

fun <R : Any> Type.match(name: String, fn: () -> R?): R? =
	matchInfix(name) { rhs ->
		matchEmpty {
			rhs.matchEmpty(fn)
		}
	}

fun <R : Any> Type.matchName(fn: String.() -> R?): R? =
	linkOrNull?.let { link ->
		link.line.fieldOrNull?.let { field ->
			ifOrNull(field.rhs.isEmpty) {
				field.name.fn()
			}
		}
	}

fun <R : Any> Type.matchNumber(fn: () -> R?): R? =
	ifOrNull(this == numberType) { fn() }

fun <R : Any> Type.matchText(fn: () -> R?): R? =
	ifOrNull(this == textType) { fn() }

fun <R : Any> Type.matchJava(fn: () -> R?): R? =
	ifOrNull(this == javaType) { fn() }

fun <R : Any> Type.matchFunction(fn: (TypeFunction) -> R?): R? =
	functionOrNull?.let(fn)

fun <R : Any> Type.matchRepeating(fn: Type.() -> R?): R? =
	repeatingOrNull?.let { fn(it.type) }

fun <R : Any> Type.matchLine(fn: TypeLine.() -> R?): R? =
	linkOrNull?.onlyLineOrNull?.let(fn)

fun <R : Any> Type.matchStatic(fn: Type.() -> R?): R? =
	staticOrNull?.let(fn)

fun <R : Any> Type.matchList(fn: Type.() -> R?): R? =
	matchPrefix(listName) {
		matchRepeating {
			fn()
		}
	}

val Type.staticOrNull: Type?
	get() =
		// TODO: Could it be made simpler, without involving scope?
		typed(nil).script.type

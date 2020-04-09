package leo14.untyped.typed

import leo.base.fold
import leo.base.ifOrNull
import leo.base.notNullIf
import leo14.Literal
import leo14.NumberLiteral
import leo14.StringLiteral
import leo14.untyped.listName

data class TypeFunction(val from: Type, val to: Type)
data class TypeAlternative(val lhs: Type, val rhs: Type)

data class TypeRepeating(val type: Type)
data class TypeRecursive(val type: Type)
data class TypeOr(val type: Type)

sealed class Type
object EmptyType : Type()
object AnythingType : Type()
object NothingType : Type()
data class LinkType(val link: TypeLink) : Type()
data class AlternativeType(val alternative: TypeAlternative) : Type()
data class FunctionType(val function: TypeFunction) : Type()
data class RepeatingType(val repeating: TypeRepeating) : Type()
data class RecursiveType(val recursive: TypeRecursive) : Type()
object RecurseType : Type()

data class TypeLink(val lhs: Type, val line: TypeLine)

sealed class TypeLine
data class LiteralTypeLine(val literal: Literal) : TypeLine()
data class FieldTypeLine(val field: TypeField) : TypeLine()
object NativeTypeLine : TypeLine()
object NumberTypeLine : TypeLine()
object TextTypeLine : TypeLine()

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
val TypeField.line: TypeLine get() = FieldTypeLine(this)
val nativeTypeLine: TypeLine = NativeTypeLine
val numberTypeLine: TypeLine = NumberTypeLine
val textTypeLine: TypeLine = TextTypeLine
infix fun String.fieldTo(type: Type) = TypeField(this, type)
infix fun String.lineTo(type: Type) = fieldTo(type).line
operator fun String.invoke(type: Type) = lineTo(type)
val Type.isEmpty: Boolean get() = this is EmptyType
infix fun Type.alternativeTo(rhs: Type) = TypeAlternative(this, rhs)
fun Type.or(rhs: Type): Type = alternativeTo(rhs).type
fun type(vararg lines: TypeLine): Type = emptyType.fold(lines) { plus(it) }

val Type.linkOrNull: TypeLink? get() = (this as? LinkType)?.link
val Type.functionOrNull: TypeFunction? get() = (this as? FunctionType)?.function
val Type.repeatingOrNull: TypeRepeating? get() = (this as? RepeatingType)?.repeating
val TypeLine.fieldOrNull: TypeField? get() = (this as? FieldTypeLine)?.field
val TypeLink.onlyLineOrNull: TypeLine? get() = notNullIf(lhs.isEmpty) { line }

val textType = emptyType.plus(textTypeLine)
val numberType = emptyType.plus(numberTypeLine)
val nativeType = emptyType.plus(nativeTypeLine)

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

fun <R : Any> Type.matchNumber(fn: () -> R?): R? =
	ifOrNull(this == numberType) { fn() }

fun <R : Any> Type.matchText(fn: () -> R?): R? =
	ifOrNull(this == textType) { fn() }

fun <R : Any> Type.matchNative(fn: () -> R?): R? =
	ifOrNull(this == nativeType) { fn() }

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
		emptyScope.script(typed(null)).type


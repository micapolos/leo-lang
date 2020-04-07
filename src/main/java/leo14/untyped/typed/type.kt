package leo14.untyped.typed

import leo14.Literal
import leo14.NumberLiteral
import leo14.StringLiteral

data class TypeFunction(val from: Type, val to: Type)
data class TypeAlternative(val lhs: Type, val rhs: Type)

data class TypeRecursive(val type: Type)
data class TypeOr(val type: Type)

sealed class Type
object EmptyType : Type()
data class LinkType(val link: TypeLink) : Type()
data class AlternativeType(val alternative: TypeAlternative) : Type()
data class FunctionType(val function: TypeFunction) : Type()
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
val emptyType: Type = EmptyType
val recurseType: Type = RecurseType
val TypeAlternative.type: Type get() = AlternativeType(this)
val TypeFunction.type: Type get() = FunctionType(this)
val TypeRecursive.toType: Type get() = RecursiveType(this)
val TypeLink.type: Type get() = LinkType(this)
infix fun Type.linkTo(line: TypeLine) = TypeLink(this, line)
fun Type.plus(line: TypeLine) = linkTo(line).type
fun Type.plus(field: TypeField) = plus(field.line)
fun Type.plus(name: String) = plus(name fieldTo emptyType)
val Literal.staticTypeLine: TypeLine get() = LiteralTypeLine(this)
val TypeField.line: TypeLine get() = FieldTypeLine(this)
val nativeTypeLine: TypeLine = NativeTypeLine
val numberTypeLine: TypeLine = NumberTypeLine
val textTypeLine: TypeLine = TextTypeLine
infix fun String.fieldTo(type: Type) = TypeField(this, type)
infix fun String.lineTo(type: Type) = fieldTo(type).line
val Type.isEmpty: Boolean get() = this is EmptyType
infix fun Type.alternativeTo(rhs: Type) = TypeAlternative(this, rhs)
fun Type.or(rhs: Type): Type = alternativeTo(rhs).type

val textType = emptyType.plus(textTypeLine)
val numberType = emptyType.plus(numberTypeLine)
val nativeType = emptyType.plus(nativeTypeLine)

val Literal.typeLine: TypeLine
	get() =
		when (this) {
			is StringLiteral -> textTypeLine
			is NumberLiteral -> numberTypeLine
		}

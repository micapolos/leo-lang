package leo14.untyped.typed

import leo14.*

data class TypeFunction(val from: Type, val to: Type)

data class ScriptStatic(val script: Script)
data class TypeRecursive(val type: Type)

sealed class Type
data class StaticType(val static: ScriptStatic) : Type()
data class LinkType(val link: TypeLink) : Type()
data class FunctionType(val function: TypeFunction) : Type()
data class RecursiveType(val recursive: TypeRecursive) : Type()
object RecurseType : Type()

data class TypeLink(val lhs: Type, val line: TypeLine)

sealed class Enum
object EmptyEnum : Enum()
data class LinkEnum(val link: EnumLink) : Enum()
data class EnumLink(val lhs: Enum, val scriptLine: ScriptLine)

sealed class Choice
object EmptyChoice : Choice()
data class LinkChoice(val link: ChoiceLink) : Choice()

data class ChoiceLink(val lhs: Choice, val line: TypeLine)

sealed class TypeLine
data class LiteralTypeLine(val literal: Literal) : TypeLine()
data class FieldTypeLine(val field: TypeField) : TypeLine()
data class EnumTypeLine(val enum: Enum) : TypeLine()
data class ChoiceTypeLine(val choice: Choice) : TypeLine()
object NativeTypeLine : TypeLine()
object NumberTypeLine : TypeLine()
object TextTypeLine : TypeLine()

data class TypeField(val name: String, val rhs: Type)

// === constructors ===

infix fun Type.functionTo(type: Type) = TypeFunction(this, type)
val Script.static get() = ScriptStatic(this)
val Type.recursive get() = TypeRecursive(this)
val emptyType: Type = script().static.type
val recurseType: Type = RecurseType
val ScriptStatic.type: Type get() = StaticType(this)
val TypeFunction.type: Type get() = FunctionType(this)
val TypeRecursive.toType: Type get() = RecursiveType(this)
val TypeLink.type: Type get() = LinkType(this)
infix fun Type.linkTo(line: TypeLine) = TypeLink(this, line)
fun Type.plus(line: TypeLine) = linkTo(line).type
fun Type.plus(field: TypeField) = plus(field.line)
fun Type.plus(name: String) = plus(name fieldTo emptyType)
val emptyEnum: Enum = EmptyEnum
val EnumLink.enum: Enum get() = LinkEnum(this)
fun Enum.linkTo(line: ScriptLine) = EnumLink(this, line)
fun Enum.plus(line: ScriptLine) = linkTo(line).enum
val emptyChoice: Choice = EmptyChoice
val ChoiceLink.choice: Choice get() = LinkChoice(this)
infix fun Choice.linkTo(line: TypeLine) = ChoiceLink(this, line)
fun Choice.plus(line: TypeLine) = linkTo(line).choice
val TypeLine.choice get() = emptyChoice.plus(this)
val Literal.staticTypeLine: TypeLine get() = LiteralTypeLine(this)
val TypeField.line: TypeLine get() = FieldTypeLine(this)
val Choice.line: TypeLine get() = ChoiceTypeLine(this)
val Enum.line: TypeLine get() = EnumTypeLine(this)
val nativeTypeLine: TypeLine = NativeTypeLine
val numberTypeLine: TypeLine = NumberTypeLine
val textTypeLine: TypeLine = TextTypeLine
infix fun String.fieldTo(type: Type) = TypeField(this, type)
infix fun String.lineTo(type: Type) = fieldTo(type).line
val Type.isEmpty: Boolean get() = (this is StaticType) && static.script.isEmpty

val textType2 = emptyType.plus(textTypeLine)
val numberType2 = emptyType.plus(numberTypeLine)
val nativeType2 = emptyType.plus(nativeTypeLine)

val Literal.typeLine: TypeLine
	get() =
		when (this) {
			is StringLiteral -> textTypeLine
			is NumberLiteral -> numberTypeLine
		}

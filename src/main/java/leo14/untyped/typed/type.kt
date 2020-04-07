package leo14.untyped.typed

import leo14.Script
import leo14.ScriptLine
import leo14.script

data class ScriptStatic(val script: Script)
data class TypeRecursive(val type: Type)

sealed class Type
data class StaticType(val static: ScriptStatic) : Type()
data class LinkType(val link: TypeLink) : Type()
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
data class FieldTypeLine(val field: TypeField) : TypeLine()
data class EnumTypeLine(val enum: Enum) : TypeLine()
data class ChoiceTypeLine(val choice: Choice) : TypeLine()
object NativeTypeLine : TypeLine()
object NumberTypeLine : TypeLine()
object TextTypeLine : TypeLine()

data class TypeField(val name: String, val rhs: Type)

// === constructors ===

val Script.static get() = ScriptStatic(this)
val Type.recursive get() = TypeRecursive(this)
val emptyType: Type = script().static.type
val resurseType: Type = RecurseType
val ScriptStatic.type: Type get() = StaticType(this)
val TypeRecursive.type: Type get() = RecursiveType(this)
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
val TypeField.line: TypeLine get() = FieldTypeLine(this)
val Choice.line: TypeLine get() = ChoiceTypeLine(this)
val Enum.line: TypeLine get() = EnumTypeLine(this)
val nativeTypeLine: TypeLine = NativeTypeLine
val numberTypeLine: TypeLine = NumberTypeLine
val textTypeLine: TypeLine = TextTypeLine
infix fun String.fieldTo(type: Type) = TypeField(this, type)

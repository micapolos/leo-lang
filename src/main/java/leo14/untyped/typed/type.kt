package leo14.untyped.typed

import leo14.Literal

sealed class Type
object EmptyType : Type()
data class LinkType(val link: TypeLink) : Type()

data class TypeLink(val lhs: Type, val choice: Choice)

sealed class Choice
object EmptyChoice : Choice()
data class LinkChoice(val link: ChoiceLink) : Choice()

data class ChoiceLink(val lhs: Choice, val line: TypeLine)

sealed class TypeLine
data class LiteralTypeLine(val literal: Literal) : TypeLine()
data class FieldTypeLine(val field: TypeField) : TypeLine()
object NativeTypeLine : TypeLine()
object NumberTypeLine : TypeLine()
object TextTypeLine : TypeLine()

data class TypeField(val name: String, val rhs: Type)

// constructors

val emptyType: Type = EmptyType
val TypeLink.type: Type get() = LinkType(this)
fun Type.linkTo(choice: Choice) = TypeLink(this, choice)
fun Type.plus(choice: Choice) = linkTo(choice).type
fun Type.plus(line: TypeLine) = plus(line.choice)
fun Type.plus(field: TypeField) = plus(field.line)
val emptyChoice: Choice = EmptyChoice
val ChoiceLink.choice: Choice get() = LinkChoice(this)
fun Choice.linkTo(line: TypeLine) = ChoiceLink(this, line)
fun Choice.plus(line: TypeLine) = linkTo(line).choice
val TypeLine.choice get() = emptyChoice.plus(this)
val Literal.line: TypeLine get() = LiteralTypeLine(this)
val TypeField.line: TypeLine get() = FieldTypeLine(this)
val nativeTypeLine: TypeLine = NativeTypeLine
val numberTypeLine: TypeLine = NumberTypeLine
val textTypeLine: TypeLine = TextTypeLine
infix fun String.fieldTo(type: Type) = TypeField(this, type)

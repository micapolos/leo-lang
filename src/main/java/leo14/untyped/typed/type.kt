package leo14.untyped.typed

import leo.base.Empty
import leo.base.empty
import leo14.Literal

sealed class Type
data class EmptyType(val empty: Empty) : Type()
data class LinkType(val link: TypeLink) : Type()

data class TypeLink(val lhs: Type, val choice: Choice)

sealed class Choice
data class EmptyChoice(val empty: Empty) : Choice()
data class LinkChoice(val link: ChoiceLink) : Choice()

data class ChoiceLink(val lhs: Choice, val line: TypeLine)

sealed class TypeLine
data class LiteralTypeLine(val literal: Literal) : TypeLine()
data class NativeTypeLine(val native: Native) : TypeLine()
data class FieldTypeLine(val field: TypeField) : TypeLine()

data class TypeField(val name: String, val rhs: Type)
object Native

// constructors

val Empty.type: Type get() = EmptyType(this)
val TypeLink.type: Type get() = LinkType(this)
fun Type.linkTo(choice: Choice) = TypeLink(this, choice)
fun Type.plus(choice: Choice) = linkTo(choice).type
fun Type.plus(line: TypeLine) = plus(line.choice)
fun Type.plus(field: TypeField) = plus(field.line)
val Empty.choice: Choice get() = EmptyChoice(this)
val ChoiceLink.choice: Choice get() = LinkChoice(this)
fun Choice.linkTo(line: TypeLine) = ChoiceLink(this, line)
fun Choice.plus(line: TypeLine) = linkTo(line).choice
val TypeLine.choice get() = empty.choice.plus(this)
val Literal.line: TypeLine get() = LiteralTypeLine(this)
val Native.line: TypeLine get() = NativeTypeLine(this)
val TypeField.line: TypeLine get() = FieldTypeLine(this)
infix fun String.fieldTo(type: Type) = TypeField(this, type)
val native = Native

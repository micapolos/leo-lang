package leo13.js

import leo.base.fold

sealed class Type

object EmptyType : Type()
data class LinkType(val link: TypeLink) : Type()

data class TypeLink(val type: Type, val line: TypeLine)

data class TypeField(val string: String, val type: Type)

sealed class TypeLine
object NumberTypeLine : TypeLine()
object StringTypeLine : TypeLine()
object NativeTypeLine : TypeLine()
data class FieldTypeLine(val field: TypeField) : TypeLine()
data class ArrowTypeLine(val arrow: Arrow) : TypeLine()

infix fun Type.linkTo(line: TypeLine) = TypeLink(this, line)
infix fun String.fieldTo(type: Type) = TypeField(this, type)

val emptyType: Type = EmptyType
val numberLine: TypeLine = NumberTypeLine
val stringLine: TypeLine = StringTypeLine
val nativeLine: TypeLine = NativeTypeLine
fun line(field: TypeField): TypeLine = FieldTypeLine(field)
fun line(arrow: Arrow): TypeLine = ArrowTypeLine(arrow)

fun Type.plus(line: TypeLine): Type = LinkType(this linkTo line)
fun type(vararg lines: TypeLine): Type = emptyType.fold(lines) { plus(it) }
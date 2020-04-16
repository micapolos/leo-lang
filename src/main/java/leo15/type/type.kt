package leo15.type

import leo.base.*
import leo14.Literal
import leo14.NumberLiteral
import leo14.StringLiteral
import leo15.*

data class Arrow(val lhs: Type, val rhs: Type)

data class Repeating(val choice: Choice)
data class Recursive(val type: Type)

sealed class Type {
	override fun toString(): String = script.string
}

object EmptyType : Type()

data class LinkType(val link: TypeLink) : Type() {
	override fun toString() = super.toString()
}

data class RepeatingType(val repeating: Repeating) : Type() {
	override fun toString() = super.toString()
}

data class RecursiveType(val recursive: Recursive) : Type() {
	override fun toString() = super.toString()
}

object RecurseType : Type()

data class TypeLink(val lhs: Type, val choice: Choice)

sealed class TypeLine
data class LiteralTypeLine(val literal: Literal) : TypeLine()
data class FieldTypeLine(val field: TypeField) : TypeLine()
data class ArrowTypeLine(val arrow: Arrow) : TypeLine()
object JavaTypeLine : TypeLine()

data class TypeField(val name: String, val rhs: Type)

sealed class Choice
data class LineChoice(val line: TypeLine) : Choice()
data class LinkChoice(val link: ChoiceLink) : Choice()
data class ChoiceLink(val lhs: Choice, val line: TypeLine)

// === constructors ===

infix fun Type.functionTo(type: Type) = Arrow(this, type)
val Type.recursive get() = Recursive(this)
val Choice.repeating get() = Repeating(this)
val emptyType: Type = EmptyType
val recurseType: Type = RecurseType
val Recursive.toType: Type get() = RecursiveType(this)
val Repeating.toType: Type get() = RepeatingType(this)
val TypeLink.type: Type get() = LinkType(this)
infix fun Type.linkTo(choice: Choice) = TypeLink(this, choice)
infix fun Type.linkTo(line: TypeLine) = linkTo(line.choice)
fun Type.plus(choice: Choice) = linkTo(choice).type
fun Type.plus(line: TypeLine) = linkTo(line).type
fun Type.plus(field: TypeField) = plus(field.line)
fun Type.plus(name: String) = plus(name fieldTo emptyType)
val Literal.typeLine: TypeLine get() = LiteralTypeLine(this)
val Literal.type: Type get() = type(typeLine)
val TypeField.line: TypeLine get() = FieldTypeLine(this)
val Arrow.line: TypeLine get() = ArrowTypeLine(this)
val javaTypeLine: TypeLine = JavaTypeLine
infix fun String.fieldTo(type: Type) = TypeField(this, type)
infix fun String.lineTo(type: Type) = fieldTo(type).line
operator fun String.invoke(type: Type) = lineTo(type)
val Type.isEmpty: Boolean get() = this is EmptyType
fun type(vararg choices: Choice): Type = emptyType.fold(choices) { plus(it) }
fun type(line: TypeLine, vararg lines: TypeLine): Type = emptyType.plus(line).fold(lines) { plus(it) }
fun type(name: String, vararg names: String): Type = type(name lineTo emptyType).fold(names) { plus(it) }
val Choice.type: Type get() = emptyType.plus(this)

val TypeLine.choice: Choice get() = LineChoice(this)
val ChoiceLink.choice: Choice get() = LinkChoice(this)
infix fun Choice.linkTo(line: TypeLine) = ChoiceLink(this, line)
operator fun Choice.plus(line: TypeLine): Choice = linkTo(line).choice
fun choice(line: TypeLine, vararg lines: TypeLine): Choice = line.choice.fold(lines) { plus(it) }

val Type.linkOrNull: TypeLink? get() = (this as? LinkType)?.link
val Type.recursiveOrNull: Recursive? get() = (this as? RecursiveType)?.recursive
val Type.isRecurse: Boolean get() = this is RecurseType
val Type.repeatingOrNull: Repeating? get() = (this as? RepeatingType)?.repeating
val Choice.linkOrNull: ChoiceLink? get() = (this as? LinkChoice)?.link
val Choice.onlyLineOrNull: TypeLine? get() = (this as? LineChoice)?.line
val TypeLine.arrowOrNull: Arrow? get() = (this as? ArrowTypeLine)?.arrow
val TypeLine.fieldOrNull: TypeField? get() = (this as? FieldTypeLine)?.field
val TypeLine.literalOrNull: Literal? get() = (this as? LiteralTypeLine)?.literal
val TypeLine.isJava: Boolean get() = this is JavaTypeLine
val TypeLink.onlyChoiceOrNull: Choice? get() = notNullIf(lhs.isEmpty) { choice }
val TypeLink.onlyLineOrNull: TypeLine? get() = onlyChoiceOrNull?.onlyLineOrNull

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

val TypeLine.name: String
	get() =
		when (this) {
			is LiteralTypeLine -> literal.name
			is FieldTypeLine -> field.name
			is ArrowTypeLine -> functionName
			JavaTypeLine -> javaName
		}

val Literal.name: String
	get() =
		when (this) {
			is StringLiteral -> textName
			is NumberLiteral -> numberName
		}

val Choice.lineSeq: Seq<TypeLine> get() = seq { linkOrNull?.lineSeqNode }
val ChoiceLink.lineSeqNode: SeqNode<TypeLine> get() = line then lhs.lineSeq

val Choice.lineCount: Int get() = lineSeq.count()

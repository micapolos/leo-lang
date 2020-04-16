package leo15.type

import leo.base.applyOrNull
import leo.base.fold
import leo.base.notNullIf
import leo13.EmptyStack
import leo13.LinkStack
import leo13.Stack
import leo14.ScriptLine
import leo14.bigDecimal
import leo14.lineTo
import leo14.plus
import leo15.*
import leo15.lambda.*

data class Typed(val term: Term, val type: Type) {
	override fun toString() = reflectScriptLine.string
}

data class TypedChoice(val term: Term, val choice: Choice)
data class TypedLine(val term: Term, val typeLine: TypeLine)
data class TypedField(val term: Term, val field: TypeField)
data class TypedLink(val lhs: Typed, val choice: TypedChoice)
data class TypedArrow(val term: Term, val function: Arrow)

data class Match(val typeLineStack: Stack<TypeLine>, val term: Term, val typeOrNull: Type?)
data class Case(val name: String, val typed: Typed)

val Typed.reflectScriptLine: ScriptLine
	get() =
		typedName lineTo term.script.plus(ofName lineTo type.script)

infix fun Term.of(type: Type) = Typed(this, type)
infix fun Term.of(choice: Choice) = TypedChoice(this, choice)
infix fun Term.of(line: TypeLine) = TypedLine(this, line)
infix fun Term.of(field: TypeField) = TypedField(this, field)
infix fun Term.of(arrow: Arrow) = TypedArrow(this, arrow)
infix fun Typed.linkTo(choice: TypedChoice) = TypedLink(this, choice)

val emptyTyped: Typed = idTerm of emptyType

val Any?.javaTypedLine: TypedLine get() = valueTerm.of(javaTypeLine)
val Any?.javaTyped: Typed get() = typed(javaTypedLine)

val Int.typedLine: TypedLine get() = numberName lineTo bigDecimal.javaTyped
val Int.typed: Typed get() = typed(typedLine)

val String.typedLine: TypedLine get() = textName lineTo javaTyped
val String.typed: Typed get() = typed(typedLine)

fun Typed.plus(typed: TypedChoice): Typed =
	plusTerm(term, type.isStatic, typed.term, typed.choice.isStatic) of type.plus(typed.choice)

fun Typed.plus(typed: TypedLine): Typed = plus(typed.choice)
fun Typed.plus(name: String): Typed = plus(name lineTo emptyTyped)

val Typed.linkOrNull: TypedLink?
	get() = type.linkOrNull?.let { typeLink ->
		term.unplus(typeLink.lhs.isStatic, typeLink.choice.isStatic).let { termPair ->
			termPair.first.of(typeLink.lhs) linkTo termPair.second.of(typeLink.choice)
		}
	}

val TypedLink.typed: Typed get() = lhs.plus(choice)

infix fun String.lineTo(typed: Typed): TypedLine =
	typed.term of lineTo(typed.type)

val TypedField.rhs: Typed get() = term.of(field.rhs)
val TypedLine.fieldOrNull: TypedField? get() = typeLine.fieldOrNull?.let { term.of(it) }
val TypedLine.arrowOrNull: TypedArrow? get() = typeLine.arrowOrNull?.let { term.of(it) }
val TypedLine.rhsOrNull: Typed? get() = fieldOrNull?.rhs
val Typed.onlyChoiceOrNull: TypedChoice?
	get() =
		linkOrNull?.run { notNullIf(lhs.type.isEmpty) { choice } }
val Typed.onlyLineOrNull: TypedLine? get() = onlyChoiceOrNull?.onlyLineOrNull
val Typed.rhsOrNull: Typed? get() = onlyLineOrNull?.fieldOrNull?.rhs
val TypedChoice.onlyLineOrNull: TypedLine?
	get() =
		choice.onlyLineOrNull?.let { typeLine ->
			term of typeLine
		}

fun typed(line: TypedLine, vararg lines: TypedLine): Typed =
	emptyTyped.plus(line).fold(lines) { plus(it) }

fun typed(name: String, vararg names: String): Typed =
	emptyTyped.plus(name).fold(names) { plus(it) }

fun TypedLine.of(choice: Choice): TypedChoice? =
	of(choice, 0, null)

val TypedLine.choice: TypedChoice
	get() =
		term of typeLine.choice

fun TypedLine.of(choice: Choice, size: Int, indexOrNull: Int?): TypedChoice? =
	when (choice) {
		is LineChoice -> indexOrNull?.let { index -> choiceTerm(size, index, term) of choice }
		is LinkChoice -> of(choice.link, size, indexOrNull)
	}

fun TypedLine.of(link: ChoiceLink, size: Int, indexOrNull: Int?): TypedChoice? =
	of(link.lhs, size.inc(), if (indexOrNull == null && typeLine == link.line) 0 else indexOrNull)

fun Match.matchPlus(case: Case): Match? =
	when (typeLineStack) {
		is EmptyStack -> null
		is LinkStack ->
			if (typeLineStack.link.value.name != case.name) null
			else if (typeOrNull != null && case.typed.type != typeOrNull) null
			else Match(typeLineStack.link.stack, term.invoke(case.typed.term), case.typed.type)
	}

val Match.typedOrNull: Typed?
	get() =
		when (typeLineStack) {
			is EmptyStack -> term.applyOrNull(typeOrNull) { of(it) }
			is LinkStack -> null
		}

val Typed.eval: Typed
	get() =
		term.eval of type
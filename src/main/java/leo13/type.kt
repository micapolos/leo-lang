package leo13

import leo.base.*

sealed class Type
data class EmptyType(val empty: Empty) : Type()
data class LinkType(val link: TypeLink) : Type()

data class TypeLink(val lhs: Type, val line: TypeLine)

sealed class TypeLine
data class EmptyTypeLine(val empty: Empty) : TypeLine()
data class LinkTypeLine(val link: TypeLineLink) : TypeLine()

data class TypeLineLink(val lhs: TypeLine, val choice: TypeChoice)
data class TypeChoice(val name: String, val rhs: Type)

val Type.isEmpty get() = (this is EmptyType)
val TypeLine.isEmpty get() = (this is EmptyTypeLine)

// --- constructors

fun type(empty: Empty): Type = EmptyType(empty)
fun type(link: TypeLink): Type = LinkType(link)

fun choice(empty: Empty): TypeLine = EmptyTypeLine(empty)
fun choice(link: TypeLineLink): TypeLine = LinkTypeLine(link)

fun link(lhs: Type, line: TypeLine) = TypeLink(lhs, line)
fun link(lhs: TypeLine, line: TypeChoice) = TypeLineLink(lhs, line)
infix fun String.caseTo(rhs: Type) = TypeChoice(this, rhs)

fun Type.plus(line: TypeLine): Type = type(link(this, line))
fun type(vararg lines: TypeLine) = type(empty).fold(lines) { plus(it) }

fun TypeLine.plus(choice: TypeChoice) = choice(link(this, choice))
fun choice(vararg choices: TypeChoice) = choice(empty).fold(choices) { plus(it) }

// --- parse

val Script.type
	get() =
		when (this) {
			is EmptyScript -> type(empty)
			is LinkScript -> type(link.typeLink)
		}

val ScriptLink.typeLink: TypeLink
	get() =
		eitherTypeLinkOrNull ?: exactTypeLink

val ScriptLink.eitherTypeLinkOrNull: TypeLink?
	get() =
		eitherTypeLineLinkOrNull?.let { choiceLink ->
			link(type(empty), choice(choiceLink))
		}

val Script.eitherLineOrNull: TypeLine?
	get() =
		when (this) {
			is EmptyScript -> choice(empty)
			is LinkScript -> link.eitherTypeLineLinkOrNull?.let(::choice)
		}

val ScriptLink.eitherTypeLineLinkOrNull: TypeLineLink?
	get() =
		lhs.eitherLineOrNull?.let { choice ->
			line.eitherChoiceOrNull?.let { case ->
				link(choice, case)
			}
		}

val ScriptLine.eitherChoiceOrNull: TypeChoice?
	get() =
		if (name == "either" && rhs is LinkScript && rhs.link.lhs.isEmpty) rhs.link.line.typeLine
		else null

val ScriptLink.exactTypeLink: TypeLink
	get() =
		link(lhs.type, line.typeChoice)

val ScriptLine.typeChoice
	get() =
		choice(typeLine)

val ScriptLine.typeLine: TypeChoice
	get() =
		name caseTo rhs.type

// --- value -> script

fun Type.script(value: Value): Script =
	when (this) {
		is EmptyType -> script(value.empty)
		is LinkType -> script(link.scriptLink(value.link))
	}

fun TypeLink.scriptLink(link: ValueLink): ScriptLink =
	link(lhs.script(link.lhs), line.scriptLine(link.line))

fun TypeLine.scriptLine(valueLine: ValueLine): ScriptLine =
	when (this) {
		is EmptyTypeLine -> fail()
		is LinkTypeLine -> link.scriptLine(valueLine)
	}

fun TypeLineLink.scriptLine(valueLine: ValueLine): ScriptLine =
	if (valueLine.int == 0) choice.scriptLine(valueLine.rhs)
	else lhs.scriptLine(valueLine.int.dec() lineTo valueLine.rhs)

fun TypeChoice.scriptLine(value: Value): ScriptLine =
	name lineTo rhs.script(value)

// --- script -> value

fun Type.value(script: Script) =
	when (this) {
		is EmptyType -> value(script.emptyOrNull!!)
		is LinkType -> value(link.valueLink(script.linkOrNull!!))
	}

fun TypeLink.valueLink(scriptLink: ScriptLink): ValueLink =
	link(lhs.value(scriptLink.lhs), line.valueLine(scriptLink.line, 0))

fun TypeLine.valueLine(scriptLine: ScriptLine, int: Int): ValueLine =
	when (this) {
		is EmptyTypeLine -> fail()
		is LinkTypeLine -> link.valueLine(scriptLine, int)
	}

fun TypeLineLink.valueLine(scriptLine: ScriptLine, int: Int): ValueLine =
	choice.valueLineOrNull(scriptLine, int) ?: lhs.valueLine(scriptLine, int.inc())

fun TypeChoice.valueLineOrNull(scriptLine: ScriptLine, int: Int): ValueLine? =
	notNullIf(name == scriptLine.name) {
		int lineTo rhs.value(scriptLine.rhs)
	}
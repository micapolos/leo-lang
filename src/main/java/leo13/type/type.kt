package leo13.type

import leo.base.Empty
import leo.base.empty
import leo.base.failIfOr
import leo.base.fold
import leo13.LeoObject
import leo13.script.*

val typeName = "type"
val emptyName = "empty"

sealed class Type : LeoObject() {
	override fun toString() = scriptableLine.toString()
	override val scriptableName get() = typeName
	override val scriptableBody get() = typeScriptableBody
	abstract val typeScriptableName: String
	abstract val typeScriptableBody: Script
}

data class EmptyType(val empty: Empty) : Type() {
	override fun toString() = super.toString()
	override val typeScriptableName get() = emptyName
	override val typeScriptableBody get() = script()
}

data class LinkType(val link: TypeLink) : Type() {
	override fun toString() = super.toString()
	override val typeScriptableName get() = link.scriptableName
	override val typeScriptableBody get() = link.scriptableBody
}

data class ChoiceType(val choice: Choice) : Type() {
	override fun toString() = super.toString()
	override val typeScriptableName get() = choice.scriptableName
	override val typeScriptableBody get() = choice.scriptableBody
}

data class ArrowType(val arrow: TypeArrow) : Type() {
	override fun toString() = super.toString()
	override val typeScriptableName get() = arrow.scriptableName
	override val typeScriptableBody get() = arrow.scriptableBody
}

// --- constructors

fun type(empty: Empty): Type = EmptyType(empty)
fun type(link: TypeLink): Type = LinkType(link)
fun type(choice: Choice): Type = ChoiceType(choice)
fun type(arrow: TypeArrow): Type = ArrowType(arrow)

fun type(vararg lines: TypeLine): Type = type(empty).fold(lines) { plus(it) }
fun type(name: String): Type = type(name lineTo type())

fun Type.plus(line: TypeLine): Type = type(link(this, line))

val Type.isEmpty: Boolean get() = this is EmptyType
val Type.linkOrNull: TypeLink? get() = (this as? LinkType)?.link
val Type.onlyLineOrNull: TypeLine? get() = linkOrNull?.onlyLineOrNull

val Script.type: Type
	get() =
		type().fold(lineSeq) { plus(it.typeLine) }

val ScriptLine.typeLine: TypeLine
	get() =
		name lineTo rhs.type

fun Type.contains(type: Type): Boolean =
	when (this) {
		is EmptyType -> type is EmptyType
		is ChoiceType -> choice.contains(type)
		is ArrowType -> type is ArrowType && arrow == type.arrow
		is LinkType -> type is LinkType && link.contains(type.link)
	}

val ScriptLine.unsafeType: Type
	get() =
		failIfOr(name != typeName) {
			rhs.unsafeType
		}

val Script.unsafeType: Type
	get() =
		type().fold(lineSeq) { unsafePlus(it) }

fun Type.unsafePlus(scriptLine: ScriptLine): Type =
	when (this) {
		is EmptyType -> plus(scriptLine.unsafeTypeLine)
		is LinkType -> link.unsafePlusType(scriptLine)
		is ChoiceType -> choice.unsafePlusType(scriptLine)
		is ArrowType -> arrow.unsafePlusType(scriptLine)
	}

fun TypeLink.unsafePlusType(scriptLine: ScriptLine): Type =
	when {
		scriptLine.name == "or" && lhs is EmptyType -> type(uncheckedChoice(choiceNode(line.case), scriptLine.rhs.unsafeCase))
		scriptLine.name == "to" -> type(arrow(type(this), scriptLine.rhs.unsafeType))
		else -> type(plus(scriptLine.unsafeTypeLine))
	}

fun Choice.unsafePlusType(scriptLine: ScriptLine): Type =
	if (scriptLine.name == "or") type(unsafePlus(scriptLine.rhs.unsafeCase))
	else type(this).plus(scriptLine.unsafeTypeLine)

fun TypeArrow.unsafePlusType(scriptLine: ScriptLine): Type =
	if (scriptLine.name == "to") type(arrow(type(this), scriptLine.rhs.unsafeType))
	else type(this).plus(scriptLine.unsafeTypeLine)

val Type.previousOrNull: Type?
	get() = when (this) {
		is EmptyType -> null
		is LinkType -> link.lhs
		is ChoiceType -> type()
		is ArrowType -> type()
	}

val Type.lineOrNull: Type?
	get() = when (this) {
		is EmptyType -> null
		is LinkType -> type(link.line)
		is ChoiceType -> this
		is ArrowType -> this
	}

val Type.rhsThunkOrNull: TypeRhs?
	get() = linkOrNull?.line?.rhs

val Type.rhsOrNull: Type?
	get() = linkOrNull?.line?.rhs?.typeOrNull

// === type to script

val Type.unsafeStaticScript: Script
	get() = when (this) {
		is EmptyType -> script()
		is LinkType -> link.unsafeStaticScriptLink.script
		is ChoiceType -> error("non-static")
		is ArrowType -> error("non-static")
	}

val TypeLine.unsafeStaticScriptLine: ScriptLine
	get() =
		name lineTo rhs.unsafeStaticScript

val TypeLink.unsafeStaticScriptLink: ScriptLink
	get() =
		link(lhs.unsafeStaticScript, line.unsafeStaticScriptLine)

val Type.unsafeArrow get() = (this as ArrowType).arrow

val Type.choiceOrNull get() = (this as? ChoiceType)?.choice

fun Type.lineOrNull(name: String): Type? =
	linkOrNull?.let { link ->
		if (link.line.name == name) type(link.line)
		else link.lhs.lineOrNull(name)
	}

fun Type.getOrNull(name: String): Type? =
	linkOrNull?.line?.rhs?.typeOrNull?.lineOrNull(name)
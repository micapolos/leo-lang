package leo13.type

import leo.base.*
import leo13.script.*

sealed class Type : Scriptable() {
	override fun toString() = scriptableLine.toString()
	override val scriptableName get() = "type"
	override val scriptableBody get() = typeScriptableBody
	abstract val typeScriptableName: String
	abstract val typeScriptableBody: Script
}

data class EmptyType(val empty: Empty) : Type() {
	override fun toString() = super.toString()
	override val typeScriptableName get() = "empty"
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
		failIfOr(name != "type") {
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

//val Type.asCustomScript: Script
//	get() =
//		(choiceOrNull?.scriptableLine?.script ?: leo13.script.script())
//			.fold(lineStack.reverse) { plus(it.asScriptLine) }
//
//val TypeLine.asRawScriptLine
//	get() =
//		name lineTo rhs.scriptableBody
//
//val TypeLine.asScriptLine
//	get() =
//		if (name == "or" && rhs.onlyLineOrNull != null) "meta" lineTo leo13.script.script(asRawScriptLine)
//		else asRawScriptLine
//
//val Type.isEmpty get() = choiceOrNull == null && lineStack.isEmpty

val Type.previousOrNull: Type?
	get() = when (this) {
		is EmptyType -> null
		is LinkType -> link.lhs
		is ChoiceType -> type()
		is ArrowType -> arrow.rhs.previousOrNull
	}

val Type.lineOrNull: Type?
	get() = when (this) {
		is EmptyType -> null
		is LinkType -> type(link.line)
		is ChoiceType -> this
		is ArrowType -> arrow.rhs.lineOrNull
	}

//val Type.onlyLineOrNull
//	get() =
//		ifOrNull(choiceOrNull == null) {
//			lineStack.onlyOrNull
//		}
//
//val Type.onlyChoiceOrNull
//	get() =
//		ifOrNull(lineStack.isEmpty) {
//			choiceOrNull
//		}
//
//// --- typeOrNull
//
//val ScriptLine.typeOrNull: Type?
//	get() =
//		ifOrNull(name == "type") {
//			rhs.typeOrNull
//		}
//
//val Script.typeOrNull: Type?
//	get() =
//		lineStack.reverse.let { reverseLineStack ->
//			when (reverseLineStack) {
//				is EmptyStack -> type()
//				is LinkStack ->
//					reverseLineStack.link.value.choiceOrNull.let { choiceOrNull ->
//						if (choiceOrNull == null) plainTypeOrNull
//						else reverseLineStack.link.stack.reverse.script.plainTypeOrNull?.let { plainTypeOrNull ->
//							Type(choiceOrNull, plainTypeOrNull.lineStack)
//						}
//					}
//			}
//		}
//
//val Script.plainTypeOrNull: Type?
//	get() =
//		if (lineStack.isEmpty) type()
//		else asStackOrNull { typeLineOrNull }?.let { lineStack ->
//			Type(null, lineStack)
//		}
//
//val ScriptLine.typeLineOrNull: TypeLine?
//	get() =
//		rhs.typeOrNull?.let { type -> name lineTo type }
//
//val String.unsafeType get() = unsafeScript.typeOrNull!!
//
//// --- exact type
//
//val Script.exactType: Type
//	get() = type().fold(lineStack.reverse) { plus(it.exactTypeLine) }
//
//val ScriptLine.exactTypeLine
//	get() = name lineTo rhs.exactType
//
//// --- type matches script
//
//fun Type.matches(script: Script): Boolean =
//	contains(script.exactType)
//
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

//val TypeLink.type
//	get() =
//		lhs.plus(line)

// --- rhsOrNull

fun Type.rhsOrNull(name: String): Type? =
	when (this) {
		is EmptyType -> null
		is LinkType -> link.rhsOrNull(name)
		is ChoiceType -> null
		is ArrowType -> arrow.rhs.rhsOrNull(name)
	}

fun TypeLink.rhsOrNull(name: String): Type? =
	if (line.name == name) line.rhs
	else lhs.rhsOrNull(name)

// --- accessOrNull

fun Type.accessOrNull(name: String): Type? =
	when (this) {
		is EmptyType -> null
		is LinkType -> link.accessTypeOrNull(name)
		is ChoiceType -> null
		is ArrowType -> arrow.rhs.accessOrNull(name)
	}

fun TypeLink.accessTypeOrNull(name: String): Type? =
	ifOrNull(lhs is EmptyType) {
		line.rhs.rhsOrNull(name)?.let { rhs ->
			type(name lineTo rhs)
		}
	}

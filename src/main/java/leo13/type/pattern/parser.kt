package leo13.type.pattern

import leo.base.failIfOr
import leo.base.fold
import leo13.script.Script
import leo13.script.ScriptLine
import leo13.script.lineSeq
import leo13.script.onlyLineOrNull

val ScriptLine.unsafeType: Type
	get() =
		failIfOr(name != "type") {
			rhs.unsafeType
		}

val Script.unsafeType: Type
	get() =
		type().fold(lineSeq) { unsafePlus(it) }

val ScriptLine.unsafeTypeLine: TypeLine
	get() =
		name lineTo rhs.unsafeType

val Script.unsafeCase: Case
	get() =
		onlyLineOrNull!!.unsafeCase

val ScriptLine.unsafeCase: Case
	get() =
		name caseTo rhs.unsafeType

fun Type.unsafePlus(scriptLine: ScriptLine): Type =
	when (this) {
		is EmptyType -> plus(scriptLine.unsafeTypeLine)
		is LinkType -> link.unsafePlusType(scriptLine)
		is ChoiceType -> choice.unsafePlusType(scriptLine)
		is FunctionType -> arrow.unsafePlusType(scriptLine)
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
	if (scriptLine.name == "to") type(type(this) arrowTo scriptLine.rhs.unsafeType)
	else type(this).plus(scriptLine.unsafeTypeLine)
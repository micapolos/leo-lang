package leo13.type.pattern

import leo.base.ifOrNull
import leo.base.orNullFold
import leo13.script.Script
import leo13.script.ScriptLine
import leo13.script.lineSeq
import leo13.script.onlyLineOrNull

val Script.typeOrNull: Type?
	get() =
		type().orNullFold(lineSeq) { plusOrNull(it) }

val ScriptLine.typeOrNull: Type?
	get() =
		ifOrNull(name == "type") {
			rhs.typeOrNull
		}

val ScriptLine.typeLineOrNull: TypeLine?
	get() =
		rhs.typeOrNull?.let { rhs -> name lineTo rhs }

val Script.caseOrNull: Case?
	get() =
		onlyLineOrNull?.caseOrNull

val ScriptLine.caseOrNull: Case?
	get() =
		rhs.typeOrNull?.let { rhs ->
			name caseTo rhs
		}

fun Type.plusOrNull(scriptLine: ScriptLine): Type? =
	when (this) {
		is EmptyType -> scriptLine.typeLineOrNull?.let { plus(it) }
		is LinkType -> link.typePlusOrNull(scriptLine)
		is ChoiceType -> choice.typePlusOrNull(scriptLine)
		is FunctionType -> arrow.typePlusOrNull(scriptLine)
	}

fun TypeLink.typePlusOrNull(scriptLine: ScriptLine): Type? =
	when {
		scriptLine.name == "or" && lhs is EmptyType -> scriptLine.rhs.caseOrNull?.let { type(choice(choiceNode(line.case), it)) }
		scriptLine.name == "to" -> scriptLine.rhs.typeOrNull?.let { type(arrow(type(this), it)) }
		else -> scriptLine.typeLineOrNull?.let { type(plus(it)) }
	}

fun Choice.typePlusOrNull(scriptLine: ScriptLine): Type? =
	if (scriptLine.name == "or") scriptLine.rhs.caseOrNull?.let { type(plus(it)) }
	else scriptLine.typeLineOrNull?.let { type(this).plus(it) }

fun TypeArrow.typePlusOrNull(scriptLine: ScriptLine): Type? =
	if (scriptLine.name == "to") scriptLine.rhs.typeOrNull?.let { type(type(this) arrowTo it) }
	else scriptLine.typeLineOrNull?.let { type(this).plus(it) }
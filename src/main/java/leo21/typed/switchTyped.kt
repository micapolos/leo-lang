package leo21.typed

import leo.base.notNullOrError
import leo13.Stack
import leo13.fold
import leo13.isEmpty
import leo13.linkOrNull
import leo13.push
import leo13.reverse
import leo13.stack
import leo14.lambda.Term
import leo14.lambda.invoke
import leo21.prim.Prim
import leo21.type.Line
import leo21.type.Type
import leo21.type.name
import leo21.type.type

data class SwitchTyped(
	val term: Term<Prim>,
	val remainingLineStack: Stack<Line>,
	val termStack: Stack<Term<Prim>>,
	val typeOrNull: Type?
)

val ChoiceTyped.switchTyped: SwitchTyped
	get() =
		SwitchTyped(termOrNull!!, choice.lineStack.reverse, stack(), null)

fun SwitchTyped.case(name: String, typed: ArrowTyped): SwitchTyped =
	remainingLineStack.linkOrNull.notNullOrError("exhausted").let { link ->
		if (link.value.name != name) error("case mismatch")
		else if (type(link.value) != typed.arrow.lhs) error("arrow lhs illegal")
		else SwitchTyped(
			term,
			link.stack,
			termStack.push(typed.term),
			if (typeOrNull == null) typed.arrow.rhs
			else if (typeOrNull != typed.arrow.rhs) error("type mismatch")
			else typeOrNull)
	}

val SwitchTyped.end: Typed
	get() =
		if (!remainingLineStack.isEmpty) error("not exaustive")
		else Typed(
			term.fold(termStack) { invoke(it) },
			typeOrNull.notNullOrError("impossible"))

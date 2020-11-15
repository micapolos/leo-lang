package leo21.compiled

import leo.base.notNullOrError
import leo13.Stack
import leo13.fold
import leo13.isEmpty
import leo13.linkOrNull
import leo13.map
import leo13.push
import leo13.reverse
import leo13.stack
import leo14.Script
import leo14.lambda.Term
import leo14.lambda.invoke
import leo21.prim.Prim
import leo21.type.Line
import leo21.type.Type
import leo21.type.name
import leo21.type.script
import leo21.type.type

data class SwitchCompiled(
	val term: Term<Prim>,
	val remainingLineStack: Stack<Line>,
	val termStack: Stack<Term<Prim>>,
	val typeOrNull: Type?
)

val ChoiceCompiled.switchCompiled: SwitchCompiled
	get() =
		SwitchCompiled(termOrNull!!, choice.lineStack.reverse, stack(), null)

fun SwitchCompiled.case(name: String, compiled: ArrowCompiled): SwitchCompiled =
	remainingLineStack.linkOrNull.notNullOrError("exhausted").let { link ->
		if (link.value.name != name) error("case mismatch")
		else if (type(link.value) != compiled.arrow.lhs) error("arrow lhs illegal")
		else SwitchCompiled(
			term,
			link.stack,
			termStack.push(compiled.term),
			if (typeOrNull == null) compiled.arrow.rhs
			else if (typeOrNull != compiled.arrow.rhs) error("type mismatch")
			else typeOrNull)
	}

val SwitchCompiled.end: Compiled
	get() =
		if (!remainingLineStack.isEmpty) error("not exaustive")
		else Compiled(
			term.fold(termStack) { invoke(it) },
			typeOrNull.notNullOrError("impossible"))

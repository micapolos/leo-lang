package leo21.compiled

import leo.base.fold
import leo.base.notNullOrError
import leo13.Stack
import leo13.fold
import leo13.isEmpty
import leo13.linkOrNull
import leo13.push
import leo13.reverse
import leo13.stack
import leo14.lambda.Term
import leo14.lambda.abstraction
import leo14.lambda.invoke
import leo21.prim.Prim
import leo21.type.Line
import leo21.type.Type
import leo21.type.matches
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
	plus(name caseTo { compiled.term.of(compiled.arrow.rhs) })

val SwitchCompiled.end: Compiled
	get() =
		if (!remainingLineStack.isEmpty) error("not exhausted")
		else Compiled(
			term.fold(termStack) { invoke(it) },
			typeOrNull.notNullOrError("impossible"))

fun SwitchCompiled.plus(case: Case): SwitchCompiled =
	remainingLineStack.linkOrNull.notNullOrError("exhausted").let { link ->
		if (!link.value.matches(case.name)) error("case mismatch")
		else case.fn(compiledArg(0, type(link.value))).let { compiled ->
			type(link.value).arrowTo(compiled).let { compiled ->
				if (type(link.value) != compiled.arrow.lhs) error("arrow lhs illegal")
				else SwitchCompiled(
					term,
					link.stack,
					termStack.push(compiled.term),
					if (typeOrNull == null) compiled.arrow.rhs
					else if (typeOrNull != compiled.arrow.rhs) error("type mismatch")
					else typeOrNull)
			}
		}
	}

fun Compiled.switch(case: Case, vararg cases: Case): Compiled =
	switch.plus(case).fold(cases) { plus(it) }.end
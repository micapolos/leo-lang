package leo32.interpreter

import leo.base.Empty
import leo.base.fold
import leo32.base.Effect
import leo32.base.apply
import leo32.base.effect
import leo32.runtime.*

data class Evaluator(
	val scope: Scope,
	val term: Term)

val Empty.evaluator get() =
	Evaluator(scope, term)

fun Evaluator.eval(field: TermField): Evaluator =
	field
		.map { eval(this).term }
		.let { mappedField ->
			term.plus(mappedField).let { mappedTerm ->
				this
					.invoke(macro(mappedTerm))
					.apply { invoke(parameter(it)) }
			}
		}

fun Evaluator.eval(term: Term): Evaluator =
	fold(term.fieldSeq) { eval(it) }

fun Evaluator.invoke(macro: Macro): Effect<Evaluator, Term> =
	effect(macro.eval)

fun Evaluator.invoke(parameter: Parameter): Evaluator =
	copy(term = scope.invoke(parameter.term))

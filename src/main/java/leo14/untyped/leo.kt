package leo14.untyped

import leo14.Begin
import leo14.End
import leo14.Literal

data class Leo(val parentOrNull: LeoParent?, val evaluator: Evaluator)
data class LeoParent(val leo: Leo, val begin: Begin)

fun LeoParent?.leo(evaluator: Evaluator) = Leo(this, evaluator)
fun Leo.parent(begin: Begin) = LeoParent(this, begin)

fun Leo.write(begin: Begin): Leo? =
	parent(begin).let { parent ->
		when (begin.string) {
			quoteName ->
				parent.leo(evaluator.environment.quote.evaluator(value()))
			unquoteName ->
				evaluator.environment.unquoteOrNull?.let { unquoted ->
					parent.leo(unquoted.evaluator(value()))
				}
			else ->
				parent.leo(evaluator.environment.evaluator(value()))
		}
	}

fun Leo.write(literal: Literal): Leo =
	copy(evaluator = evaluator.write(line(literal)))

fun Leo.write(end: End): Leo? =
	parentOrNull?.let { parent ->
		when (parent.begin.string) {
			quoteName ->
				when (parent.leo.evaluator.environment) {
					is ContextEnvironment -> parent.leo.update { write(appendName lineTo evaluator.value) }
					is QuotedEnvironment -> parent.leo.update { write(quoteName lineTo evaluator.value) }
				}
			unquoteName -> parent.leo.update { write(unquoteName lineTo evaluator.value) }
			else -> parent.leo.update { write(parent.begin.string lineTo evaluator.value) }
		}
	}

fun Leo.update(fn: Evaluator.() -> Evaluator): Leo =
	copy(evaluator = evaluator.fn())
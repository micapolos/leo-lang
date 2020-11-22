package leo21.evaluated

import leo.base.notNullIf
import leo21.type.matches

fun Evaluated.accessOrNull(name: String): Evaluated? =
	linkOrNull?.let { link ->
		link.head.accessOrNull(name) ?: link.tail.accessOrNull(name)
	}

fun ChoiceEvaluated.accessOrNull(name: String): Evaluated? =
	switch(
		{ choiceEvaluated -> choiceEvaluated.accessOrNull(name) },
		{ lineEvaluated ->
			notNullIf(lineEvaluated.line.matches(name)) {
				evaluated(lineEvaluated)
			}
		})

fun LineEvaluated.accessOrNull(name: String): Evaluated? =
	choiceOrNull?.accessOrNull(name)
		?: notNullIf(line.matches(name)) {
			evaluated(this)
		}

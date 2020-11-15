package leo21.evaluator

import leo.base.notNullIf
import leo21.type.name
import leo21.type.type

fun Evaluated.accessOrNull(name: String): Evaluated? =
	switch(
		{ it.accessOrNull(name) },
		{ it.accessOrNull(name) })

fun StructEvaluated.accessOrNull(name: String): Evaluated? =
	linkOrNull?.let { link ->
		link.head.accessOrNull(name) ?: link.tail.accessOrNull(name)
	}

fun ChoiceEvaluated.accessOrNull(name: String): Evaluated? =
	switch(
		{ choiceEvaluated -> choiceEvaluated.accessOrNull(name) },
		{ lineEvaluated ->
			notNullIf(lineEvaluated.line.name == name) {
				evaluated(lineEvaluated)
			}
		})

fun LineEvaluated.accessOrNull(name: String): Evaluated? =
	notNullIf(line.name == name) {
		value of type(line)
	}

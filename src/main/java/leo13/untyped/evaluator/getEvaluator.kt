package leo13.untyped.evaluator

import leo.base.notNullIf
import leo13.untyped.TokenReader

data class GetEvaluator(val parentEvaluator: Evaluator, val nameOrNull: String?): TokenReader {
	override fun begin(name: String) =
		notNullIf(nameOrNull == null) {
			set(name).nameEvaluator
		}

	override val end get() =
		nameOrNull?.let {
			parentEvaluator.plusGet(it)
		}
}

val Evaluator.getEvaluator get() = GetEvaluator(this, null)

fun GetEvaluator.set(name: String) = copy(nameOrNull = name)
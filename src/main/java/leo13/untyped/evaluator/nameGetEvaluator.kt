package leo13.untyped.evaluator

import leo13.untyped.TokenReader

data class NameGetEvaluator(val parentGetEvaluator: GetEvaluator): TokenReader {
	override fun begin(name: String) = null
	override val end get() = parentGetEvaluator
}

val GetEvaluator.nameEvaluator get() = NameGetEvaluator(this)

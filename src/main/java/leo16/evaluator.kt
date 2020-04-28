package leo16

import leo.base.notNullIf
import leo.base.nullOf
import leo.base.runIfNotNull
import leo15.thisName

data class Evaluator(val parentOrNull: EvaluatorParent?, val script: Script)
data class EvaluatorParent(val evaluator: Evaluator, val word: String)

fun EvaluatorParent?.evaluator(script: Script) = Evaluator(this, script)
fun Evaluator.parent(word: String) = EvaluatorParent(this, word)
val emptyEvaluator = nullOf<EvaluatorParent>().evaluator(script())

operator fun Evaluator.plus(token: Token) =
	when (token) {
		is BeginToken -> begin(token.word)
		EndToken -> end!!
	}

fun Evaluator.begin(word: String): Evaluator =
	parent(word).evaluator(script())

val Evaluator.end: Evaluator?
	get() =
		parentOrNull?.endEvaluator(script)

fun EvaluatorParent.endEvaluator(script: Script): Evaluator =
	evaluator.plus(word(script))

operator fun Evaluator.plus(sentence: Sentence): Evaluator =
	parentOrNull.evaluator(script + sentence).normalizeAndApply

val Evaluator.normalizeAndApply: Evaluator
	get() =
		parentOrNull.evaluator(script.normalize).apply.resolveThis

val Evaluator.apply: Evaluator
	get() =
		null
			?: applyStatic
			?: this

val Evaluator.applyStatic: Evaluator?
	get() =
		parentOrNull.runIfNotNull(script.apply) { evaluator(it) }

val Evaluator.applyThis: Evaluator?
	get() =
		notNullIf(script == script(thisName())) {
			parentOrNull.evaluator(script(thisName(rootScript)))
		}

val Evaluator.resolveThis: Evaluator
	get() =
		applyThis ?: this

val Evaluator.rootScript: Script
	get() =
		parentOrNull?.rootScript(script) ?: script

fun EvaluatorParent.rootScript(script: Script): Script =
	evaluator.script + word(script)

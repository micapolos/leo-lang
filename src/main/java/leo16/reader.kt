package leo16

import leo.base.nullOf
import leo16.names.*

data class Reader(val parentOrNull: ReaderParent?, val evaluator: Evaluator) {
	override fun toString() = asSentence.toString()
	val asSentence
		get(): Sentence = _reader(
			parentOrNull?.asSentence ?: _parent(_none()),
			evaluator.asField)
}

data class ReaderParent(val reader: Reader, val word: String) {
	override fun toString() = asSentence.toString()
	val asSentence get(): Sentence = _parent(reader.asSentence, _word(word.sentence))
}

fun ReaderParent?.reader(evaluator: Evaluator) = Reader(this, evaluator)
fun Reader.parent(word: String) = ReaderParent(this, word)
val Evaluator.reader get() = nullOf<ReaderParent>().reader(this)

// TODO: Wrong
fun Reader.begin(word: String) = parent(word).reader(nullOf<EvaluatorParent>().evaluator(evaluator.evaluated, Mode.QUOTE))
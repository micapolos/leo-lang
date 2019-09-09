package leo13

import leo.base.fold
import leo.base.ifNull

data class Evaluator(
	val context: Context,
	val script: ValueScript) {
	override fun toString() = sentenceLine.toString()
}

fun evaluator(context: Context = context(), script: ValueScript = valueScript()) =
	Evaluator(context, script)

fun Evaluator.set(script: ValueScript): Evaluator =
	copy(script = script)

fun Evaluator.plusError(word: Word): Evaluator =
	set(
		script(
			value(
				sentence(
					errorWord lineTo sentence(
						sentenceLine,
						plusWord lineTo sentence(word))))))

fun Evaluator.plusError(line: ValueLine): Evaluator =
	set(
		script(
			value(
				sentence(
					errorWord lineTo sentence(
						sentenceLine,
						plusWord lineTo sentence(line.sentenceLine))))))

fun Evaluator.plus(script: SentenceScript): Evaluator =
	fold(script.lineSeq) { plus(it) }

fun Evaluator.plus(sentence: Sentence): Evaluator =
	fold(sentence.scriptLineSeq) { plus(it) }

fun Evaluator.plus(line: SentenceScriptLine): Evaluator =
	if (line.script.sentenceOrNull == null) plus(line.word)
	else plus(line.word lineTo line.script.sentenceOrNull)

fun Evaluator.plus(line: SentenceLine): Evaluator =
	context
		.evaluate(line.sentence)
		.let { script ->
			if (script.valueOrNull == null) plus(line.word)
			else plus(line.word lineTo script.valueOrNull)
		}

fun Evaluator.plus(link: SentenceLink): Evaluator =
	plus(link.sentence).plus(link.line)

fun Evaluator.plus(word: Word): Evaluator =
	if (hasError) this
	else if (script.valueOrNull != null) plusError(word)
	else set(script(value(sentence(word))))

fun Evaluator.plus(line: ValueLine): Evaluator =
	if (hasError) this
	else when (line.word) {
		setWord -> plusSetOrNull(line.value) ?: plusError(line)
		else -> plusOther(line)
	}

fun Evaluator.plusSetOrNull(newValue: Value): Evaluator? =
	script
		.valueOrNull
		?.let { value ->
			newValue
				.pattern
				.lineOrNull
				?.let { newPatternLine ->
					value
						.pattern
						.setOrNull(newPatternLine)
						?.let { setPattern ->
							set(
								script(
									value
										.sentence
										.setOrNull(newValue.sentence.lineOrNull!!)!!
										.valueOf(setPattern)))
						}
				}
		}

fun Evaluator.plusOther(line: ValueLine): Evaluator =
	null
		?: plusGetOrNull(line)
		?: append(line)

fun Evaluator.plusGetOrNull(line: ValueLine): Evaluator? =
	script.valueOrNull.ifNull {
		line.value.pattern.getOrNull(line.word)?.let { getPattern ->
			set(script(line.value.sentence.getOrNull(line.word)!!.valueOf(getPattern)))
		}
	}

fun Evaluator.append(line: ValueLine): Evaluator =
	set(script.plus(line))

val Evaluator.sentenceLine: SentenceLine
	get() =
		evaluatorWord lineTo sentence(script.sentenceLine)

val Evaluator.hasError
	get() : Boolean =
		script.valueOrNull?.sentence?.lineOrNull?.word == errorWord
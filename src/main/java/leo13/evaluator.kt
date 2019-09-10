package leo13

import leo.base.fold
import leo.base.ifNull
import leo.base.notNullIf

data class Evaluator(
	val context: Context,
	val option: ValueOption) {
	override fun toString() = sentenceLine.toString()
}

fun evaluator(context: Context = context(), option: ValueOption = valueOption()) =
	Evaluator(context, option)

fun Evaluator.set(option: ValueOption): Evaluator =
	copy(option = option)

fun Evaluator.plusError(word: Word): Evaluator =
	set(
		option(
			value(
				sentence(
					errorWord lineTo sentence(
						sentenceLine,
						plusWord lineTo sentence(word))))))

fun Evaluator.plusError(line: SentenceLine): Evaluator =
	set(
		option(
			value(
				sentence(
					errorWord lineTo sentence(
						sentenceLine,
						plusWord lineTo sentence(line))))))

fun Evaluator.plusError(line: ValueLine): Evaluator =
	set(
		option(
			value(
				sentence(
					errorWord lineTo sentence(
						sentenceLine,
						plusWord lineTo sentence(line.sentenceLine))))))

fun Evaluator.plus(script: SentenceOption): Evaluator =
	fold(script.lineSeq) { plus(it) }

fun Evaluator.plus(sentence: Sentence): Evaluator =
	fold(sentence.optionLineSeq) { plus(it) }

fun Evaluator.plus(line: SentenceOptionLine): Evaluator =
	if (line.option.sentenceOrNull == null) plus(line.word)
	else plus(line.word lineTo line.option.sentenceOrNull)

fun Evaluator.plus(line: SentenceLine): Evaluator =
	when (line.word) {
		ofWord -> plusOfOrNull(line.sentence) ?: plusError(line)
		else -> plusEvaluated(line)
	}

fun Evaluator.plusEvaluated(line: SentenceLine): Evaluator =
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
	else if (option.valueOrNull != null) plusError(word)
	else set(option(value(sentence(word))))

fun Evaluator.plus(line: ValueLine): Evaluator =
	if (hasError) this
	else when (line.word) {
		setWord -> plusSetOrNull(line.value) ?: plusError(line)
		evaluatorWord -> plusEvaluatorOrNull(line.value) ?: plusError(line)
		else -> plusOther(line)
	}

fun Evaluator.plusEvaluatorOrNull(value: Value): Evaluator? =
	notNullIf(option.valueOrNull == null) {
		set(option(value)).run {
			set(option(value(sentence(sentenceLine))))
		}
	}

fun Evaluator.plusOfOrNull(sentence: Sentence): Evaluator? =
	option.valueOrNull?.let { value ->
		sentence.failableBodyPattern.orNull?.let { pattern ->
			value.castOrNull(pattern)?.let { castValue ->
				set(option(castValue))
			}
		}
	}

fun Evaluator.plusSetOrNull(newValue: Value): Evaluator? =
	option
		.valueOrNull
		?.let { value ->
			newValue
				.pattern.let { pattern ->
				value
					.pattern
					.setOrNull(pattern)
					?.let { setPattern ->
						set(
							option(
								value
									.sentence
									.setOrNull(newValue.sentence)!!
									.valueOf(setPattern)))
					}
			}
		}

fun Evaluator.plusOther(line: ValueLine): Evaluator =
	null
		?: plusGetOrNull(line)
		?: append(line)

fun Evaluator.plusGetOrNull(line: ValueLine): Evaluator? =
	option.valueOrNull.ifNull {
		line.value.pattern.getOrNull(line.word)?.let { getPattern ->
			set(option(line.value.sentence.getOrNull(line.word)!!.valueOf(getPattern)))
		}
	}

fun Evaluator.append(line: ValueLine): Evaluator =
	set(option.plus(line))

val Evaluator.sentenceLine: SentenceLine
	get() =
		evaluatorWord lineTo sentence(option.sentenceLine)

val Evaluator.hasError
	get() : Boolean =
		option.valueOrNull?.sentence?.lineOrNull?.word == errorWord
package leo13

sealed class PatternStart

data class WordChoicePatternStart(val choice: WordChoice) : PatternStart()
data class LineChoicePatternStart(val choice: LineChoice) : PatternStart()
data class AnyPatternStart(val any: SentenceAny) : PatternStart()
data class ArrowPatternStart(val arrow: PatternArrow) : PatternStart()

fun start(choice: WordChoice): PatternStart = WordChoicePatternStart(choice)
fun start(choice: LineChoice): PatternStart = LineChoicePatternStart(choice)
fun start(any: SentenceAny): PatternStart = AnyPatternStart(any)
fun start(arrow: PatternArrow): PatternStart = ArrowPatternStart(arrow)

val patternStartSentenceWriter: SentenceLineWriter<PatternStart> =
	TODO()
//	sealedWriter(
//		startWord,
//		wordChoiceSentenceWriter,
//		lineChoiceSentenceWriter,
//		sentenceAnySentenceWriter,
//		patternArrowSentenceWriter
//	) { fn1, fn2, fn3, fn4 ->
//		when (this) {
//			is WordChoicePatternStart -> choice.fn1()
//			is LineChoicePatternStart -> choice.fn2()
//			is AnyPatternStart -> any.fn3()
//			is ArrowPatternStart -> arrow.fn4()
//		}
//	}

fun PatternStart.matches(start: SentenceStart): Boolean =
	when (this) {
		is WordChoicePatternStart -> start is WordSentenceStart && choice.matches(start.word)
		is LineChoicePatternStart -> start is LineSentenceStart && choice.matches(start.line)
		is AnyPatternStart -> true
		is ArrowPatternStart -> false
	}

fun PatternStart.contains(pattern: PatternStart): Boolean =
	when (this) {
		is WordChoicePatternStart -> pattern is WordChoicePatternStart && choice.contains(pattern.choice)
		is LineChoicePatternStart -> pattern is LineChoicePatternStart && choice.contains(pattern.choice)
		is AnyPatternStart -> true
		is ArrowPatternStart -> pattern is ArrowPatternStart && arrow.contains(pattern.arrow)
	}

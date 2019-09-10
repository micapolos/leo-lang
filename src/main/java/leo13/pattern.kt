package leo13

import leo.base.fold

sealed class Pattern {
	override fun toString() = patternSentenceWriter.toString(this)
}

data class StartPattern(val start: PatternStart) : Pattern() {
	override fun toString() = super.toString()
}

data class LinkPattern(val link: PatternLink) : Pattern() {
	override fun toString() = super.toString()
}

fun pattern(start: PatternStart): Pattern = StartPattern(start)
fun pattern(link: PatternLink): Pattern = LinkPattern(link)

fun pattern(word: Word): Pattern = pattern(start(choice(either(word))))
fun pattern(line: PatternLine): Pattern = pattern(start(choice(either(line))))
fun pattern(choice: WordChoice): Pattern = pattern(start(choice))
fun pattern(choice: LineChoice): Pattern = pattern(start(choice))
fun pattern(any: SentenceAny): Pattern = pattern(start(any))
fun pattern(arrow: PatternArrow): Pattern = pattern(start(arrow))

fun Pattern.plus(line: PatternLine): Pattern = pattern(linkTo(line))

fun pattern(word: Word, vararg lines: PatternLine) = pattern(word).fold(lines) { plus(it) }
fun pattern(line: PatternLine, vararg lines: PatternLine) = pattern(line).fold(lines) { plus(it) }
fun pattern(choice: WordChoice, vararg lines: PatternLine) = pattern(choice).fold(lines) { plus(it) }
fun pattern(choice: LineChoice, vararg lines: PatternLine) = pattern(choice).fold(lines) { plus(it) }
fun pattern(any: SentenceAny, vararg lines: PatternLine) = pattern(any).fold(lines) { plus(it) }
fun pattern(arrow: PatternArrow, vararg lines: PatternLine) = pattern(arrow).fold(lines) { plus(it) }

val patternSentenceWriter =
	recursiveWriter<Pattern> {
		sealedWriter(
			patternWord,
			patternStartSentenceWriter,
			patternLinkSentenceWriter
		) { fn1, fn2 ->
			when (this) {
				is StartPattern -> start.fn1()
				is LinkPattern -> link.fn2()
			}
		}
	}

fun Pattern.matches(sentence: Sentence): Boolean =
	when (this) {
		is StartPattern -> sentence is StartSentence && start.matches(sentence.start)
		is LinkPattern -> sentence is LinkSentence && link.matches(sentence.link)
	}

fun pattern(sentence: Sentence): Pattern =
	when (sentence) {
		is StartSentence -> pattern(sentence.start)
		is LinkSentence -> pattern(patternLink(sentence.link))
	}

fun pattern(start: SentenceStart): Pattern =
	when (start) {
		is WordSentenceStart -> pattern(start.word)
		is LineSentenceStart -> pattern(patternLine(start.line))
	}

fun Pattern.contains(pattern: Pattern): Boolean =
	when (this) {
		is StartPattern -> pattern is StartPattern && start.contains(pattern.start)
		is LinkPattern -> pattern is LinkPattern && link == pattern.link
	}
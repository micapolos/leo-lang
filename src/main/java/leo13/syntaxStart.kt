package leo13

sealed class SyntaxStart

data class WordSyntaxStart(val syntax: WordSyntax) : SyntaxStart()
data class LineSyntaxStart(val line: SyntaxLine) : SyntaxStart()
data class FunctionSyntaxStart(val function: SyntaxFunction) : SyntaxStart()
data class GivenSyntaxStart(val syntax: GivenSyntax) : SyntaxStart()

fun start(syntax: WordSyntax): SyntaxStart = WordSyntaxStart(syntax)
fun start(line: SyntaxLine): SyntaxStart = LineSyntaxStart(line)
fun start(function: SyntaxFunction): SyntaxStart = FunctionSyntaxStart(function)
fun start(syntax: GivenSyntax): SyntaxStart = GivenSyntaxStart(syntax)

val SyntaxStart.sentenceStart: SentenceStart
	get() =
		when (this) {
			is WordSyntaxStart -> start(syntax.word)
			is LineSyntaxStart -> start(line.sentenceLine)
			is FunctionSyntaxStart -> start(function.sentenceLine)
			is GivenSyntaxStart -> syntax.sentenceStart
		}
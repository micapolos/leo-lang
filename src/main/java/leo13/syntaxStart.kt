package leo13

sealed class SyntaxStart

data class WordSyntaxStart(val word: Word) : SyntaxStart()
data class LineSyntaxStart(val line: SyntaxLine) : SyntaxStart()
data class FunctionSyntaxStart(val function: SyntaxFunction) : SyntaxStart()
data class GivenSyntaxStart(val given: SyntaxGiven) : SyntaxStart()
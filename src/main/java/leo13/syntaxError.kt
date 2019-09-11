package leo13

sealed class SyntaxError

data class WordMismatchSyntaxError(val mismatch: Mismatch<Word>) : SyntaxError()
object OnlyWordExpectedSyntaxError : SyntaxError()
data class WordSwitchSyntaxError(val error: WordSwitchError) : SyntaxError()
data class LineSwitchSyntaxError(val error: LineSwitchError) : SyntaxError()

fun syntaxError(mismatch: Mismatch<Word>): SyntaxError = WordMismatchSyntaxError(mismatch)
val onlyWordExpectedSyntaxError: SyntaxError = OnlyWordExpectedSyntaxError
fun syntaxError(error: WordSwitchError): SyntaxError = WordSwitchSyntaxError(error)
fun syntaxError(error: LineSwitchError): SyntaxError = LineSwitchSyntaxError(error)

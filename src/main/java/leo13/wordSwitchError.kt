package leo13

sealed class WordSwitchError

data class DuplicateWordSwitchError(val duplicate: CaseWordDuplicate) : WordSwitchError()
data class WordMismatchWordSwitchError(val mismatch: Mismatch<Word>) : WordSwitchError()

fun wordSwitchError(duplicate: CaseWordDuplicate): WordSwitchError = DuplicateWordSwitchError(duplicate)
fun wordSwitchError(mismatch: Mismatch<Word>): WordSwitchError = WordMismatchWordSwitchError(mismatch)

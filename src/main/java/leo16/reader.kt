package leo16

sealed class Reader

object VoidReader : Reader()
data class EvaluatorReader(val evaluator: Evaluator) : Reader()
data class LibrarianReader(val librarian: Librarian) : Reader()

val voidReader: Reader = VoidReader
val Evaluator.reader: Reader get() = EvaluatorReader(this)
val Librarian.reader: Reader get() = LibrarianReader(this)


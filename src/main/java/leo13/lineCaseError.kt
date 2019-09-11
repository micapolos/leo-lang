package leo13

sealed class LineCaseError

data class SyntaxLineCaseError(val syntaxError: SyntaxError) : LineCaseError()
data class EmptyLineCaseError(val empty: Empty) : LineCaseError()

fun lineCaseError(error: SyntaxError): LineCaseError = SyntaxLineCaseError(error)
fun lineCaseError(empty: Empty): LineCaseError = EmptyLineCaseError(empty)

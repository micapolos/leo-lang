package leo13

sealed class LineSwitchError

data class DuplicateLineSwitchError(val duplicate: CaseWordDuplicate) : LineSwitchError()
data class CaseLineSwitchError(val lineCaseError: LineCaseError) : LineSwitchError()

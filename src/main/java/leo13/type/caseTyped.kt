package leo13.type

import leo13.value.Case

data class CaseTyped(val case: Case, val pattern: Pattern)

fun typed(case: Case, pattern: Pattern) = CaseTyped(case, pattern)

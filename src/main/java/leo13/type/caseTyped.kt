package leo13.type

import leo13.value.Case

data class CaseTyped(val case: Case, val type: Type)

fun typed(case: Case, type: Type) = CaseTyped(case, type)

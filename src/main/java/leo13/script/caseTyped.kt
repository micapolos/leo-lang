package leo13.script

import leo13.type.Type

data class CaseTyped(val case: Case, val type: Type)

fun typed(case: Case, type: Type) = CaseTyped(case, type)

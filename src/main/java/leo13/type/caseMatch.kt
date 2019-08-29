package leo13.type

import leo13.script.Script

data class CaseMatch(val case: Case, val script: Script)

fun match(case: Case, script: Script) = CaseMatch(case, script)

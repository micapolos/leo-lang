package leo22.term

import leo22.dsl.*

fun X.functionApply(value: X): X =
	scope.scopePush(value).scopeValue(term)
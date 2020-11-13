package leo22.term

import leo22.dsl.*

val functionDef = function(scope(), term())

fun X.functionApply(value: X): X =
	scope.scopePush(value).scopeValue(term)
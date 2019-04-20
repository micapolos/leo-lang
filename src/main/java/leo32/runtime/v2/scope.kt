package leo32.runtime.v2

import leo.base.Empty

data class Scope(
	val typeDictionary: Dict<Script, Script>,
	val bodyDictionary: Dict<Script, Script>)

val Empty.scope
	get() =
		Scope(scriptDict(), scriptDict())
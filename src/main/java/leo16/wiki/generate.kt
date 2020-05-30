package leo16.wiki

import leo.base.print
import leo13.array
import leo13.map
import leo16.Definition
import leo16.Dictionary
import leo16.Value
import leo16.invoke
import leo16.loadedOrNull
import leo16.names.*
import leo16.patternValueOrNull
import leo16.value

val Value.generate
	get() =
		loadedOrNull!!.scope.exportDictionary.wikiString

val Dictionary.wikiString: String
	get() =
		definitionStack.map { wikiEntryString }.array.joinToString("\n")

val Definition.wikiEntryString: String
	get() =
		"```\n${patternValueOrNull!!}\n```\n"

fun main() {
	value(_base()).generate.print
}
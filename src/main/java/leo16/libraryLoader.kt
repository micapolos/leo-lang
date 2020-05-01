package leo16

import leo16.library.libraryFnMap

val libraryMap = mutableMapOf<Sentence, Library>()

val Sentence.library: Library
	get() =
		libraryMap.computeIfAbsent(this) { libraryFnMap[it]!!.invoke() }

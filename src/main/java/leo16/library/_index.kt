package leo16.library

import leo15.*
import leo16.*

private fun Field.dictionary(fn: () -> Value) =
	value(dictionaryName.invoke(this)).pattern to fn

// TODO: Use reflection instead of hard-coded index.
val valueFunMap = mapOf(
	coreName().dictionary { core },
	pingName().dictionary { ping },
	reflectionName().dictionary { reflection },
	intName().dictionary { int },
	numberName().dictionary { number },
	baseName().dictionary { base },
	bitName().dictionary { bit },
	listName().dictionary { list },
	textName().dictionary { text }
)

package leo16.library

import leo15.*
import leo16.*

private fun Field.library(fn: () -> Value) =
	value(libraryName.invoke(this)).pattern to fn

// TODO: Use reflection instead of hard-coded index.
val valueFunMap = mapOf(
	pingName().library { ping },
	reflectionName().library { reflection },
	intName().library { int },
	numberName().library { number },
	baseName().library { base },
	bitName().library { bit },
	listName().library { list },
	textName().library { text }
)

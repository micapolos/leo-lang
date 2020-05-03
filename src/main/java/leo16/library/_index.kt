package leo16.library

import leo15.*
import leo16.pattern

// TODO: Use reflection instead of hard-coded index.
val valueFunMap = mapOf(
	pingName.pattern to { ping },
	baseName.pattern to { base },
	bitName.pattern to { bit },
	listName.pattern to { list },
	textName.pattern to { text }
)

package leo16.library

import leo15.baseName
import leo15.bitName
import leo15.listName
import leo15.pingName
import leo16.pattern

val valueFunMap = mapOf(
	pingName.pattern to { ping },
	baseName.pattern to { base },
	bitName.pattern to { bit },
	listName.pattern to { list }
)

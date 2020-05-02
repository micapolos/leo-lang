package leo16.library

import leo15.baseName
import leo15.bitName
import leo15.listName
import leo15.pingName
import leo16.invoke

val valueFunMap = mapOf(
	pingName() to { ping },
	baseName() to { base },
	bitName() to { bit },
	listName() to { list }
)

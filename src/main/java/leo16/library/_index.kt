package leo16.library

import leo15.baseName
import leo15.bitName
import leo15.listName
import leo15.pingName
import leo16.invoke
import leo16.pattern
import leo16.value

val valueFunMap = mapOf(
	value(pingName(value())).pattern to { ping },
	value(baseName(value())).pattern to { base },
	value(bitName(value())).pattern to { bit },
	value(listName(value())).pattern to { list }
)

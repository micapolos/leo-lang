package leo16.library

import leo15.bitName
import leo15.listName
import leo16.invoke

val libraryFnMap = mapOf(
	bitName() to { bitLibrary },
	listName() to { listLibrary }
)

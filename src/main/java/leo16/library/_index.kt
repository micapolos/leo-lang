package leo16.library

import leo15.baseName
import leo15.bitName
import leo15.listName
import leo16.invoke

val libraryFnMap = mapOf(
	baseName() to { baseLibrary },
	bitName() to { bitLibrary },
	listName() to { listLibrary }
)

package leo16

import leo15.booleanName
import leo15.falseName
import leo15.trueName

val Boolean.field: Field
	get() =
		booleanName(if (this) trueName() else falseName())

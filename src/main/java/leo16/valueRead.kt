package leo16

import leo13.linkOrNull
import leo13.mapOrNull
import leo13.push
import leo16.names.*

val Field.read: Field
	get() =
		null
			?: stackFieldOrNull
			?: this

val Field.stackFieldOrNull: Field?
	get() =
		matchPrefix(_stack) { rhs ->
			rhs
				.fieldStack
				.mapOrNull { matchPrefix(_item) { it } }
				?.linkOrNull
				?.run { stack.push(value) }
				?.valueField
		}

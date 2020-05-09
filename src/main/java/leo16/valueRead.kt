package leo16

import leo13.linkOrNull
import leo13.mapOrNull
import leo13.push
import leo16.names.*

val Field.read: Field
	get() =
		null
			?: listFieldOrNull
			?: this

val Field.listFieldOrNull: Field?
	get() =
		matchPrefix(_list) { rhs ->
			rhs
				.fieldStack
				.mapOrNull { matchPrefix(_item) { it } }
				?.linkOrNull
				?.run { stack.push(value) }
				?.listField
		}

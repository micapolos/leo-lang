package lambda.lib

import lambda.Term

private var nextId = zeroNat

val genId: Term
	get() {
		val id = nextId
		nextId = nextId.natInc
		return id
	}

package lambda.lib.typed

import lambda.Term
import lambda.lib.*

private var nextType = 0.i32Term

val genType: Term
	get() {
		val id = nextType
		nextType = nextType.i32Int.inc().apply { if (this < 0) error("id overflow") }.i32Term
		return id
	}

val Term.typedOf get() = pairTo
val Term.typedValue get() = pairAt0
val Term.typedType get() = pairAt1

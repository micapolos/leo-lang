package lambda.v2

val nothing = oneOf(1, 2)(id)
val just = oneOf(2, 2)

val Term.forNothingOrJust get() = this

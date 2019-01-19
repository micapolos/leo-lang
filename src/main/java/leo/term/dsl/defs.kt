package leo.term.dsl

val _bit = _script(
	bit(),
	gives(
		either(zero()),
		either(one())))

val _naturalNumber = _script(
	natural(number()),
	gives(
		either(natural(number(zero()))),
		either(
			natural(number()),
			plus(one()))))

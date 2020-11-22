package leo21.type

val Type.try_: Type
	get() =
		type(
			"try" lineTo type(
				line(
					choice(
						"success" lineTo this,
						"failure" lineTo type()))))
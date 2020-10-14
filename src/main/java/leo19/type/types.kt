package leo19.type

val void = type()

val bitType =
	type(
		"bit" fieldTo choice(
			"zero" caseTo void,
			"one" caseTo void))

val booleanType =
	type(
		"boolean" fieldTo choice(
			"true" caseTo void,
			"false" caseTo void))

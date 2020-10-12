package leo19.type

val void = struct()

val bitType =
	struct(
		"bit" fieldTo choice(
			"zero" caseTo void,
			"one" caseTo void))

val booleanType =
	struct(
		"boolean" fieldTo choice(
			"true" caseTo void,
			"false" caseTo void))

package leo14.untyped.typed

val bitType =
	type("bit" lineTo type("zero").or(type("one")))

val booleanType =
	type("boolean" lineTo type("false").or(type("true")))

val Type.listType
	get() =
		type("list" lineTo this.repeating.toType)

val byteType =
	type("byte" lineTo type(
		"first" lineTo bitType,
		"second" lineTo bitType,
		"third" lineTo bitType,
		"fourth" lineTo bitType,
		"fifth" lineTo bitType,
		"sixth" lineTo bitType,
		"seventh" lineTo bitType,
		"eight" lineTo bitType))

val intType =
	type("int" lineTo type(
		"first" lineTo byteType,
		"second" lineTo byteType,
		"third" lineTo byteType,
		"fourth" lineTo byteType))

val byteArrayType =
	type("byte" lineTo byteType.listType)

val stringType =
	type("text" lineTo type("utf8" lineTo byteArrayType))
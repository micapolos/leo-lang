package leo32

fun Appendable.field(key: String, value: Appendable.() -> Appendable) =
	this
		.append(key)
		.append('(')
		.value()
		.append(')')
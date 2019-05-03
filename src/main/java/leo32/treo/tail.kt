package leo32.treo

data class Tail(val chainOrNull: Chain?)

fun tail(chainOrNull: Chain?) = Tail(chainOrNull)

fun tail(vararg values: Value): Tail =
	values.foldRight(tail(null)) { value, tail ->
		tail(chain(head(value), tail))
	}

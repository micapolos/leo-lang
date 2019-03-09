package leo.base

data class Indent(val tabCount: Int)

val Int.indent
	get() =
		Indent(this)

fun Appendable.append(indent: Indent) =
	iterate(indent.tabCount) { append('\t') }

fun <R> R.increased(indent: Indent, fn: R.(Indent) -> R): R =
	fn(Indent(indent.tabCount + 1))

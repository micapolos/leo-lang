package leo.base

data class Indent(val tabCount: Int)

val Int.indent
	get() =
		Indent(this)

fun Appendable.append(indent: Indent) =
	iterate(indent.tabCount) { append('\t') }

fun Appendable.append(indent: Indent, string: String) =
	iterate(indent.tabCount) { append(string) }

fun <R> R.indented(indent: Indent, fn: R.(Indent) -> R): R =
	fn(Indent(indent.tabCount + 1))

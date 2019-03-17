package leo32

import leo.base.iterate

data class Print(
	val appendable: Appendable,
	val indentInt: Int)

val Appendable.print
	get() =
		Print(this, 0)

fun Print.string(string: String) =
	copy(appendable = appendable.append(string))

fun Print.indented(body: Print.() -> Print) =
	copy(indentInt = indentInt + 1)
		.body()
		.copy(indentInt = indentInt)

fun Print.simple(name: String, body: Print.() -> Print) =
	string(name).string(" ").body()

fun Print.complex(name: String, body: Print.() -> Print) =
	this
		.string(name)
		.indented { indent.body() }

fun Print.ptr(ptr: Ptr) =
	string("0x").string(ptr.toString(16))

val Print.indent
	get() =
		string("\n").iterate(indentInt) { string("\t") }

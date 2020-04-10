package leo14.untyped.typed

import leo14.*

data class Reader(val parentOrNull: ReaderParent?, val compiler: Compiler)
data class ReaderParent(val reader: Reader, val begin: Begin)

fun ReaderParent?.reader(compiler: Compiler) = Reader(this, compiler)
fun Reader.parent(begin: Begin) = ReaderParent(this, begin)

val nullReaderParent: ReaderParent? = null
val emptyReader = nullReaderParent.reader(emptyCompiler)

fun Reader.plus(token: Token): Reader =
	when (token) {
		is LiteralToken -> updateCompiler { plus(token.literal) }
		is BeginToken -> parent(token.begin).reader(compiler.clear)
		is EndToken -> parentOrNull!!.endReader(compiler)
	}

fun ReaderParent.endReader(compiler: Compiler): Reader =
	reader.updateCompiler { plus(begin, compiler) }

fun Reader.updateCompiler(fn: Compiler.() -> Compiler): Reader =
	copy(compiler = compiler.fn())

val Reader.fragment: Fragment
	get() =
		parentOrNull?.fragmentParent.fragment(compiler.script)

val ReaderParent.fragmentParent: FragmentParent
	get() =
		reader.fragment.parent(begin)
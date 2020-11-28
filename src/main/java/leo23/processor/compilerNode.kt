package leo23.processor

import leo14.BeginToken
import leo14.EndToken
import leo14.Fragment
import leo14.FragmentParent
import leo14.LiteralToken
import leo14.Token
import leo14.begin
import leo14.fragment
import leo14.parent
import leo23.compiler.Compiler
import leo23.compiler.begin
import leo23.compiler.emptyCompiler
import leo23.compiler.plus
import leo23.compiler.printScript
import leo23.typed.term.Compiled
import leo23.typed.term.compiled
import leo23.typed.term.struct

sealed class CompilerParent
data class NameCompilerParent(val compilerNode: CompilerNode, val name: String) : CompilerParent()

data class CompilerNode(
	val parentOrNull: NameCompilerParent?,
	val compiler: Compiler
)

val emptyCompilerNode: CompilerNode get() = CompilerNode(null, emptyCompiler)

fun CompilerNode.plus(token: Token): Processor =
	when (token) {
		is LiteralToken -> CompilerProcessor(plus(compiled(token.literal)))
		is BeginToken -> CompilerProcessor(begin(token.begin.string))
		is EndToken -> parentOrNull!!.end(compiler)
	}

fun CompilerNode.plus(compiled: Compiled): CompilerNode =
	copy(compiler = compiler.plus(compiled))

fun CompilerNode.begin(name: String): CompilerNode =
	CompilerNode(NameCompilerParent(this, name), compiler.begin)

fun CompilerParent.end(compiler: Compiler): Processor =
	when (this) {
		is NameCompilerParent -> CompilerProcessor(compilerNode.plus(name struct compiler.stackCompiled))
	}

val CompilerNode.printFragment: Fragment
	get() =
		parentOrNull?.printFragmentParent.fragment(compiler.printScript)

val CompilerParent.printFragmentParent: FragmentParent
	get() =
		when (this) {
			is NameCompilerParent -> compilerNode.printFragment.parent(begin(name))
		}
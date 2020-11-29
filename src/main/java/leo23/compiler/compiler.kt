package leo23.compiler

import leo13.array
import leo14.Script
import leo23.term.tuple
import leo23.type.fields
import leo23.type.struct
import leo23.typed.of
import leo23.typed.term.Compiled
import leo23.typed.term.StackCompiled
import leo23.typed.term.emptyStackCompiled
import leo23.typed.term.normalize
import leo23.typed.term.printScript
import leo23.typed.term.push

data class Compiler(
	val context: Context,
	val stackCompiled: StackCompiled
)

val emptyCompiler get() = Compiler(emptyContext, emptyStackCompiled)

fun Compiler.plus(compiled: Compiled): Compiler =
	set(context.resolve(stackCompiled.push(compiled)))

fun Compiler.set(stackCompiled: StackCompiled): Compiler =
	copy(stackCompiled = stackCompiled)

val Compiler.begin: Compiler
	get() =
		Compiler(context.begin, emptyStackCompiled)

val Compiler.beginDo: Compiler
	get() =
		Compiler(context.beginDo(stackCompiled), emptyStackCompiled)

fun Compiler.compiled(name: String): Compiled =
	tuple(*stackCompiled.v.array).of(name struct fields(*stackCompiled.t.array))

val Compiler.printScript: Script
	get() =
		stackCompiled.printScript
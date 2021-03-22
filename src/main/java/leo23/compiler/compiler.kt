package leo23.compiler

import leo.base.fold
import leo.base.reverse
import leo13.array
import leo14.Literal
import leo14.Script
import leo14.ScriptField
import leo14.ScriptLine
import leo14.lineSeq
import leo23.compiler.binding.Given
import leo23.compiler.binding.GivenBinding
import leo23.term.tuple
import leo23.type.fields
import leo23.type.struct
import leo23.typed.of
import leo23.typed.term.Compiled
import leo23.typed.term.StackCompiled
import leo23.typed.term.compiled
import leo23.typed.term.emptyStackCompiled
import leo23.typed.term.printScript
import leo23.typed.term.push
import leo23.typed.term.struct

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

fun Compiler.plus(scriptLine: ScriptLine): Compiler =
	TODO()

fun Compiler.plus(scriptField: ScriptField): Compiler =
	when (scriptField.string) {
		"do" -> plusDo(scriptField.rhs)
		"function" -> plusFunction(scriptField.rhs)
		else -> TODO()
//		"apply" -> plusApply(scriptField.rhs)
//		"define" -> plusDefine(scriptField.rhs)
//		else -> plus(stackCompiled.v.of(scriptField.string struct fields(*stackCompiled.t.array)))
	}

fun Compiler.plusDo(script: Script): Compiler =
	set(context.beginDo(stackCompiled).stackCompiled(script))

fun Compiler.plusFunction(script: Script): Compiler =
	TODO()

fun Compiler.plus(literal: Literal): Compiler =
	plus(compiled(literal))

fun Compiler.plus(script: Script): Compiler =
	fold(script.lineSeq.reverse) { plus(it) }

fun Context.stackCompiled(script: Script): StackCompiled =
	Compiler(this, emptyStackCompiled).plus(script).stackCompiled

package leo23.processor

import leo14.Begin
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
import leo23.compiler.beginDo
import leo23.compiler.emptyCompiler
import leo23.compiler.plus
import leo23.compiler.printScript
import leo23.compiler.set
import leo23.typed.term.ApplyCompiledCommand
import leo23.typed.term.BeginCompiledCommand
import leo23.typed.term.Compiled
import leo23.typed.term.CompiledCommand
import leo23.typed.term.DefineCompiledCommand
import leo23.typed.term.DoCompiledCommand
import leo23.typed.term.FunctionCompiled
import leo23.typed.term.FunctionCompiledCommand
import leo23.typed.term.RepeatCompiledCommand
import leo23.typed.term.StackCompiled
import leo23.typed.term.SwitchCompiledCommand
import leo23.typed.term.apply
import leo23.typed.term.command
import leo23.typed.term.compiled
import leo23.typed.term.stack
import leo23.typed.term.struct

sealed class CompilerParent
data class BeginCompilerParent(val compilerNode: CompilerNode, val begin: Begin) : CompilerParent()
data class DoCompilerParent(val compilerNode: CompilerNode) : CompilerParent()
data class ApplyCompilerParent(val compilerNode: CompilerNode, val functionCompiled: FunctionCompiled) : CompilerParent()

data class CompilerNode(
	val parentOrNull: CompilerParent?,
	val compiler: Compiler
)

val emptyCompilerNode: CompilerNode get() = CompilerNode(null, emptyCompiler)

fun CompilerNode.plus(token: Token): Processor =
	when (token) {
		is LiteralToken -> CompilerProcessor(plus(compiled(token.literal)))
		is BeginToken -> plus(compiler.stackCompiled.command(token.begin.string))
		is EndToken -> parentOrNull!!.end(compiler)
	}

fun CompilerNode.plus(command: CompiledCommand): Processor =
	when (command) {
		is BeginCompiledCommand ->
			CompilerProcessor(
				CompilerNode(
					BeginCompilerParent(this, command.begin),
					compiler.begin))
		is DoCompiledCommand ->
			CompilerProcessor(
				CompilerNode(
					DoCompilerParent(this),
					compiler.beginDo))
		FunctionCompiledCommand -> TODO()
		is ApplyCompiledCommand ->
			CompilerProcessor(
				CompilerNode(
					ApplyCompilerParent(this, command.functionCompiled),
					compiler.begin))
		is SwitchCompiledCommand -> TODO()
		DefineCompiledCommand -> TODO()
		is RepeatCompiledCommand -> TODO()
	}

fun CompilerNode.plus(compiled: Compiled): CompilerNode =
	copy(compiler = compiler.plus(compiled))

fun CompilerNode.set(stackCompiled: StackCompiled): CompilerNode =
	copy(compiler = compiler.set(stackCompiled))

fun CompilerParent.end(compiler: Compiler): Processor =
	when (this) {
		is BeginCompilerParent -> CompilerProcessor(compilerNode.plus(begin.string struct compiler.stackCompiled))
		is DoCompilerParent -> CompilerProcessor(compilerNode.set(compiler.stackCompiled))
		is ApplyCompilerParent -> CompilerProcessor(compilerNode.set(functionCompiled.apply(compiler.stackCompiled).stack))
	}

val CompilerNode.printFragment: Fragment
	get() =
		parentOrNull?.printFragmentParent.fragment(compiler.printScript)

val CompilerParent.printFragmentParent: FragmentParent
	get() =
		when (this) {
			is BeginCompilerParent -> compilerNode.printFragment.parent(begin)
			is DoCompilerParent -> compilerNode.printFragment.parent(begin("do"))
			is ApplyCompilerParent -> compilerNode.printFragment.parent(begin("function"))
		}
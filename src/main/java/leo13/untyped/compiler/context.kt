package leo13.untyped.compiler

import leo.base.fold
import leo13.script.*
import leo13.trace
import leo13.untyped.caseName
import leo13.untyped.compileName
import leo13.untyped.pattern.Choice
import leo13.untyped.pattern.Either
import leo13.untyped.pattern.PatternArrow
import leo13.with
import leo9.Stack

data class Context(val arrowStack: Stack<PatternArrow>)

fun Context.compile(script: Script): Compiled =
	compiler(this, compiled())
		.fold(script.lineSeq) { plus(it) }
		.compiled

fun Context.compile(line: ScriptLine): CompiledLine =
	compile(line.rhs).let { line.name lineTo it }

fun Context.compileSwitch(choice: Choice, script: Script): CompiledSwitch =
	TODO()
//	compile(switchName) {
//		CompiledSwitch(
//			zip(choice.eitherStack.reverse, script.lineStack.reverse)
//				.map {
//					if (first == null || second == null) compileError(caseName lineTo script(mismatchName))
//					else compileCase(first!!, second!!)
//				})
//	}

fun Context.compileCase(either: Either, line: ScriptLine): Case =
	trace {
		compileName lineTo script(caseName lineTo script(line))
	}.with {
		line.tracedRhs(caseName) {
			tracedOnlyLine {
				tracedRhs(either.name) {
					TODO()
				}
			}
		}
	}

fun Context.compileSet(rhs: Script): CompiledSet =
	TODO()
//	compile(setName) {
//		rhs.lineStack.map { compile(this) }
//	}

fun Script.compileWith(context: Context) =
	context.compile(this)
package leo16

import leo.base.print
import leo14.untyped.leoString
import leo15.dsl.*

fun Evaluator.read(f: F): Evaluator =
	tokenReducer.read(f).reduced

fun compile_(f: F) = emptyEvaluator.read(f).evaluated
fun value_(f: F) = compile_(f).value
fun evaluate_(f: F) = value_(f).printed.script
fun read_(f: F) = emptyEvaluator.copy(mode = Mode.QUOTE).read(f).evaluated.value.printed.script
fun print_(f: F) = evaluate_(f).leoString.print
fun run_(f: F) = Unit.also { compile_(f) }
fun leo_(f: F) = print_ { base.import; f() }
fun library_(f: F) = print_(f)

package leo16

import leo.base.print
import leo15.dsl.*

fun Evaluator.read(f: F): Evaluator =
	tokenReducer.read(f).state

fun compile_(f: F) = emptyEvaluator.read(f).compiled
fun value_(f: F) = compile_(f).value
fun evaluate_(f: F) = value_(f).script
fun read_(f: F) = emptyEvaluator.copy(isMeta = true).read(f).compiled.value.script
fun print_(f: F) = evaluate_(f).print
fun run_(f: F) = Unit.also { compile_(f) }
fun library_(f: F) = value_ { library { f() } }.libraryOrNull!!

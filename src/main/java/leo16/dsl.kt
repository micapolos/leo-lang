package leo16

import leo.base.print
import leo15.dsl.*

fun Compiler.read(f: F): Compiler =
	tokenReducer.read(f).state

fun compile_(f: F) = emptyCompiler.read(f).compiled
fun evaluate_(f: F) = compile_(f).value
fun print_(f: F) = evaluate_(f).script.print
fun run_(f: F) = Unit.also { compile_(f) }

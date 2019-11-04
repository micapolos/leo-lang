package leo14.typed

import leo14.Compiler
import leo14.Number
import leo14.Ret

data class Compiled<T>(
	val context: Context<T>,
	val typed: Typed<T>)

fun <T> Compiled<T>.compiler(string: String, ret: Ret<Compiled<T>>): Compiler =
	context.compiler(string, typed, ret)

fun <T> Compiled<T>.compilerNative(string: String, ret: Ret<Compiled<T>>): Compiler =
	if (!typed.isEmpty) error("string not expected")
	else ret(Compiled(context, context.compileString(string) of nativeType))

fun <T> Compiled<T>.compilerNative(number: Number, ret: Ret<Compiled<T>>): Compiler =
	if (!typed.isEmpty) error("number not expected")
	else ret(Compiled(context, context.compileNumber(number) of nativeType))

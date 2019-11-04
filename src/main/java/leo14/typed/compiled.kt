package leo14.typed

import leo14.Compiler
import leo14.Literal
import leo14.Ret

data class Compiled<T>(
	val context: Context<T>,
	val typed: Typed<T>)

fun <T> Compiled<T>.compiler(string: String, ret: Ret<Compiled<T>>): Compiler =
	context.compiler(string, typed, ret)

fun <T> Compiled<T>.compilerNative(literal: Literal, ret: Ret<Typed<T>>): Compiler =
	if (!typed.isEmpty) error("literal not expected")
	else ret(context.literalTerm(literal) of nativeType)

infix fun <T> Context<T>.with(typed: Typed<T>): Compiled<T> =
	Compiled(this, typed)

fun <T> Compiled<T>.resolvePlus(string: String, rhs: Compiled<T>): Compiled<T> =
	copy(typed = typed.resolvePlus(string, rhs.typed))
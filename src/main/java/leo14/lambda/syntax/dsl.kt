package leo14.lambda.syntax

fun <T> native(native: T): Syntax<T> = NativeSyntax(native)
fun <T> fn(fn: (Syntax<T>) -> Syntax<T>): Syntax<T> = FunctionSyntax(fn)
fun <T> fn2(fn: (Syntax<T>, Syntax<T>) -> Syntax<T>): Syntax<T> = fn { a -> fn { b -> fn(a, b) } }
operator fun <T> Syntax<T>.invoke(rhs: Syntax<T>): Syntax<T> = InvokeSyntax(this, rhs)

infix fun <T> Syntax<T>.pairTo(rhs: Syntax<T>) =
	fn<T> { lhs -> fn { rhs -> fn { f -> f.invoke(lhs).invoke(rhs) } } }.invoke(this).invoke(rhs)

val <T> Syntax<T>.pairFirst: Syntax<T> get() = invoke(fn { lhs -> fn { rhs -> lhs } })
val <T> Syntax<T>.pairSecond: Syntax<T> get() = invoke(fn { lhs -> fn { rhs -> rhs } })

val <T> Syntax<T>.eitherFirst: Syntax<T>
	get() =
		fn<T> { value -> fn { firstFn -> fn { secondFn -> firstFn.invoke(value) } } }.invoke(this)
val <T> Syntax<T>.eitherSecond: Syntax<T>
	get() =
		fn<T> { value -> fn { firstFn -> fn { secondFn -> secondFn.invoke(value) } } }.invoke(this)

fun <T> boolean(boolean: Boolean): Syntax<T> =
	if (boolean) fn<T> { it }.eitherFirst
	else fn<T> { it }.eitherSecond

fun <T> Syntax<T>.eitherSwitch(firstFn: (Syntax<T>) -> Syntax<T>, secondFn: (Syntax<T>) -> Syntax<T>): Syntax<T> =
	invoke(fn(firstFn)).invoke(fn(secondFn))

fun <T> Syntax<T>.eitherSwitch(first: Syntax<T>, second: Syntax<T>): Syntax<T> =
	eitherSwitch({ first }, { second })

fun <T> fix(): Syntax<T> =
	fn { g ->
		fn<T> { f ->
			g.invoke(fn { v ->
				f.invoke(f).invoke(v)
			})
		}.invoke(fn { f ->
			g.invoke(fn { v ->
				f.invoke(f).invoke(v)
			})
		})
	}

fun <T> recFn(f: (Syntax<T>, Syntax<T>) -> Syntax<T>): Syntax<T> =
	fix<T>().invoke(fn2(f))

@file:Suppress("unused")

package leo32

sealed class Op<T> {
	abstract fun invoke(scope: Scope<T>): Scope<T>
}

data class LogOp<T>(val log: Log) : Op<T>() {
	override fun invoke(scope: Scope<T>) =
		scope.invoke(log)
}

data class PushOp<T>(val push: Push) : Op<T>() {
	override fun invoke(scope: Scope<T>) =
		scope.push(push.bit)
}

data class PopOp<T>(val pop: Pop) : Op<T>() {
	override fun invoke(scope: Scope<T>) =
		scope.pop
}

data class NandOp<T>(val nand: Nand) : Op<T>() {
	override fun invoke(scope: Scope<T>) =
		scope.nand
}

data class SeqOp<T>(
	val firstOp: Op<T>,
	val secondOp: Op<T>) : Op<T>() {
	override fun invoke(scope: Scope<T>) =
		firstOp.invoke(scope).let {
			secondOp.invoke(it)
		}
}

data class OutOp<T>(val out: Out) : Op<T>() {
	override fun invoke(scope: Scope<T>) =
		scope.out
}

fun <T> Log.op(): Op<T> = LogOp(this)
fun <T> Push.op(): Op<T> = PushOp(this)
fun <T> Pop.op(): Op<T> = PopOp(this)
fun <T> Nand.op(): Op<T> = NandOp(this)
fun <T> Out.op(): Op<T> = OutOp(this)
fun <T> Op<T>.then(op: Op<T>) = SeqOp(this, op)

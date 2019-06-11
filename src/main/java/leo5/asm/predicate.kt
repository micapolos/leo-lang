package leo5.asm

sealed class Predicate

data class ZeroPredicate(val zero: Zero) : Predicate()
data class NotZeroPredicate(val notZero: NotZero) : Predicate()
data class PositivePredicate(val positive: Positive) : Predicate()
data class NegativePredicate(val negative: Negative) : Predicate()

fun predicate(zero: Zero): Predicate = ZeroPredicate(zero)
fun predicate(notZero: NotZero): Predicate = NotZeroPredicate(notZero)
fun predicate(positive: Positive): Predicate = PositivePredicate(positive)
fun predicate(negative: Negative): Predicate = NegativePredicate(negative)

fun Predicate.test(int: Int) = when (this) {
	is ZeroPredicate -> zero.test(int)
	is NotZeroPredicate -> notZero.test(int)
	is PositivePredicate -> positive.test(int)
	is NegativePredicate -> negative.test(int)
}

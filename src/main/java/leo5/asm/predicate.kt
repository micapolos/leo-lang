package leo5.asm

sealed class Predicate

data class IsZeroPredicate(val isZero: IsZero) : Predicate()
data class IsNotZeroPredicate(val isNotZero: IsNotZero) : Predicate()
data class IsPositivePredicate(val isPositive: IsPositive) : Predicate()
data class IsNegativePredicate(val isNegative: IsNegative) : Predicate()

fun predicate(isZero: IsZero): Predicate = IsZeroPredicate(isZero)
fun predicate(isNotZero: IsNotZero): Predicate = IsNotZeroPredicate(isNotZero)
fun predicate(isPositive: IsPositive): Predicate = IsPositivePredicate(isPositive)
fun predicate(isNegative: IsNegative): Predicate = IsNegativePredicate(isNegative)

fun Predicate.test(int: Int) = when (this) {
	is IsZeroPredicate -> isZero.test(int)
	is IsNotZeroPredicate -> isNotZero.test(int)
	is IsPositivePredicate -> isPositive.test(int)
	is IsNegativePredicate -> isNegative.test(int)
}

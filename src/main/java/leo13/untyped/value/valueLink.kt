package leo13.untyped.value

data class ValueLink(val lhsValue: Value, val rhsItem: ValueItem)

infix fun Value.linkTo(rhs: ValueItem) = ValueLink(this, rhs)

val ValueLink.value get() = lhsValue.plus(rhsItem)

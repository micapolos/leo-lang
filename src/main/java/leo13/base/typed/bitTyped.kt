package leo13.base.typed

import leo.binary.*
import leo13.base.Typed
import leo13.base.type.*

val zeroType: Type<Zero> = type("zero", body(struct(zero)))
val oneType: Type<One> = type("one", body(struct(one)))

val bitType: Type<Bit> = type(
	"bit",
	body(
		choice(
			case(zeroType) { bit },
			case(oneType) { bit }) {
			when (this) {
				is ZeroBit -> zeroType to zero
				is OneBit -> oneType to one
			}
		}))

data class BitTyped(val bit: Bit) : Typed<Bit>() {
	override fun toString() = super.toString()
	override val type = bitType
}
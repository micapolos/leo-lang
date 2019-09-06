package leo13.script.reflect

import leo.binary.*

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

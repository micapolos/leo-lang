package leo13.script.reflect

import leo.binary.*

val zeroType: Type<Zero> = type("zero", body(struct(zero)))
val oneType: Type<One> = type("one", body(struct(one)))
val trueType: Type<Boolean> = type("true", body(struct(true)))
val falseType: Type<Boolean> = type("false", body(struct(false)))

val booleanType: Type<Boolean> =
	type(
		"boolean",
		body(
			choice(
				case(trueType) { true },
				case(falseType) { false }) {
				if (this) trueType to true
				else falseType to false
			}))

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

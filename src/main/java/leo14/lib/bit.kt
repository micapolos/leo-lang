package leo14.lib

import leo14.typed.choice
import leo14.typed.lineTo
import leo14.typed.type

val bitTypeLine =
	"bit" lineTo type(choice("zero", "one"))

class Bit(term: Term) : Obj(term) {
	override val typeLine = bitTypeLine
}

val Zero.bit get() = Bit(oneOf(1, 2, this))
val One.bit get() = Bit(oneOf(2, 2, this))
val bit0 = zero.bit
val bit1 = one.bit
val Int.bit get() = if (and(1) == 0) bit0 else bit1
val Bit.int get() = if (this == bit0) 0 else 1

fun <R : Obj> Bit.switch(make: Term.() -> R, fn0: Zero.() -> R, fn1: One.() -> R): R =
	term.switch(::Zero, ::One, make, fn0, fn1)

val Bit.negate
	get() =
		switch(::Bit, { bit1 }, { bit0 })

fun Bit.and(bit: Bit): Bit =
	switch(::Bit,
		{ bit.switch(::Bit, { bit0 }, { bit0 }) },
		{ bit.switch(::Bit, { bit0 }, { bit1 }) })

fun Bit.or(bit: Bit): Bit =
	switch(::Bit,
		{ bit.switch(::Bit, { bit0 }, { bit1 }) },
		{ bit.switch(::Bit, { bit1 }, { bit1 }) })

fun Bit.xor(bit: Bit): Bit =
	switch(::Bit,
		{ bit.switch(::Bit, { bit0 }, { bit1 }) },
		{ bit.switch(::Bit, { bit1 }, { bit0 }) })

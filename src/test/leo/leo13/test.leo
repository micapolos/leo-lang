define
	bit has
		either: zero
		either: one

define
	negate: bit
	gives: given negate bit
		if: zero: bit: one
		if: one: bit: zero

bit: zero
negate
compiler

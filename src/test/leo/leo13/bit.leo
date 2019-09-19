define: bit has: zero or: one

define
	negate: bit
	gives: given negate bit switch
		case: zero: bit: one
		case: one: bit: zero

define
	bit
	and: bit
	gives: given bit switch
		case: zero: bit: zero
		case: one: given and bit

define
	bit
	xor: bit
	gives: given bit switch
		case: zero: given xor bit
		case: one: given xor bit negate

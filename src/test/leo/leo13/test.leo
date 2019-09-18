define
	bit has: choice
		either: zero
		either: one

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
	or: bit
	gives: given bit switch
		case: zero: given or bit
		case: one: bit: one

bit: zero
or: bit: zero
negate

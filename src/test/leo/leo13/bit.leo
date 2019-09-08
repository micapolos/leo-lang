define
	negate: bit: choice
		option: zero
		option: one
	gives: given negate bit switch
		case: zero: bit: zero
		case: one: bit: one
define
	bit: choice
		option: zero
		option: one
	and: bit: choice
		option: zero
		option: one
	gives: given bit switch
		case: zero: bit: zero
		case: one: given and bit
define
	bit: choice
		option: zero
		option: one
	or: bit: choice
		option: zero
		option: one
	gives: given bit switch
		case: zero: given or bit
		case: one: bit: one
define
	bit: choice
		option: zero
		option: one
	xor: bit: choice
		option: zero
		option: one
	gives: given bit switch
		case: zero: given xor bit
		case: one: given xor bit negate

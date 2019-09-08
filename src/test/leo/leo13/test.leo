define
	negate: bit: choice
		option: zero
		option: one
	gives: given negate bit switch
		case: zero: bit: one
		case: one: bit: zero
negate: bit: zero

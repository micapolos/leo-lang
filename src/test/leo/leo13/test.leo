function
	bit: choice
		either: zero
		either: one
	negate
	gives: given negate bit switch
		case: zero
			bit: one
			of: bit: choice
				either: zero
				either: one
		case: zero
			bit: one
			of: bit: choice
				either: zero
				either: one
apply
	bit: zero
	of: bit: choice
		either: zero
		either: one
	negate

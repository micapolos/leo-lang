define
	bit has
		either: zero
		either: one

	bit negate
	gives: given bit switch
		zero: bit: one
		one: bit: zero

	bit and: bit
	gives: given bit switch
		zero: bit: zero
		one: given and bit

	bit or: bit
	gives: given bit switch
		zero: given or bit
		one: bit: one

	bit xor: bit
	gives: given bit switch
		zero: given xor bit
		one: given xor bit negate

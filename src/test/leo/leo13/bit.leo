define: bit has
	either: zero
	either: one

define: bit negate gives
	given bit switch
		zero: bit: one
		one: bit: zero

define
	bit and: bit
	gives: given bit switch
		zero: bit: zero
		one: given and bit

define
	bit or: bit
	gives: given bit switch
		zero: given or bit
		one: bit: one

define
	bit xor: bit
	gives: given bit switch
		zero: given xor bit
		one: given xor bit negate

define
	bit has: options: zero one

	bit negate
	gives: given bit match
		zero: bit: one
		one: bit: zero

	bit and: bit
	gives: given bit match
		zero: bit: zero
		one: given and bit

	bit or: bit
	gives: given bit match
		zero: given or bit
		one: bit: one

	bit xor: bit
	gives: given bit match
		zero: given xor bit
		one: given xor bit negate

circle: bit: zero
set: bit: one

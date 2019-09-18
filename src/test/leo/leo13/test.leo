define: bit has: choice
	either: zero
	either: one
function
	bit
	and: bit
	gives: given bit switch
		case: zero: bit: zero
		case: one: given and bit
apply
	bit: zero
	and: bit: zero
compiler

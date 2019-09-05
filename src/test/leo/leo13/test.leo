define
	any bit negate
	gives: given negate bit switch
		zero gives: one bit
		one gives: zero bit
define
	any bit
	and: any bit
	gives: given bit switch
		zero gives: zero bit
		one gives: given and bit
define
	any bit
	or: any bit
	gives: given bit switch
		zero gives: given or bit
		one gives: one bit
zero bit
or: zero bit

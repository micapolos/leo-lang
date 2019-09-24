define
	natural: number
	contains: options
		zero
		next: containing: natural: number

	natural: number
	plus: natural: number: zero
	gives: given natural number

	natural: number
	plus: next: natural: number
	gives
		next: given natural number
		plus: given next natural number

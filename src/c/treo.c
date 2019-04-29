enum treo_kind {
	treo_leaf, treo_select, treo_branch
};

struct treo {
	enum treo_kind kind;
};

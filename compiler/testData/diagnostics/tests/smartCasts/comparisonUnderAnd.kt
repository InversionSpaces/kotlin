// RUN_PIPELINE_TILL: FRONTEND
fun foo(x : String?, y : String?) {
    if (y != null && x == y) {
        // Both not null
        <!DEBUG_INFO_SMARTCAST!>x<!>.length
        <!DEBUG_INFO_SMARTCAST!>y<!>.length
    }
    else {
        x<!UNSAFE_CALL!>.<!>length
        y<!UNSAFE_CALL!>.<!>length
    }
    if (y != null || x == <!DEBUG_INFO_CONSTANT!>y<!>) {
        x<!UNSAFE_CALL!>.<!>length
        y<!UNSAFE_CALL!>.<!>length
    }
    else {
        // y == null but x != y
        <!DEBUG_INFO_SMARTCAST!>x<!>.length
        y<!UNSAFE_CALL!>.<!>length
    }
    if (y == null && x != <!DEBUG_INFO_CONSTANT!>y<!>) {
        // y == null but x != y
        <!DEBUG_INFO_SMARTCAST!>x<!>.length
        y<!UNSAFE_CALL!>.<!>length
    }
    else {
        x<!UNSAFE_CALL!>.<!>length
        y<!UNSAFE_CALL!>.<!>length
    }
    if (y == null || x != y) {
        x<!UNSAFE_CALL!>.<!>length
        y<!UNSAFE_CALL!>.<!>length
    }
    else {
        // Both not null
        <!DEBUG_INFO_SMARTCAST!>x<!>.length
        <!DEBUG_INFO_SMARTCAST!>y<!>.length
    }
}

/* GENERATED_FIR_TAGS: andExpression, disjunctionExpression, equalityExpression, functionDeclaration, ifExpression,
nullableType, smartcast */

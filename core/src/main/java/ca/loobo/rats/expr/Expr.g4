grammar Expr;


expr:   ID ('.' ID)* '(' exprList? ')'    # Invoke
    |   expr ('=='|'!=') expr   # Compare
    |   ID                      # Var
    |   ('-')* NUMBER           # Number
    |   '"' str '"'     	    # String
    |   ('>'|'>='|'<'|'<='|'=='|'!=') NUMBER ('.' NUMBER)* #SingleCompare
    ;

exprList : expr (',' WS* expr WS* )* ;   // arg list
str: (ID|NUMBER|SYMBOL|'!'|' '|'.'|'*'|','|'-'|'\\\\'|'\\"')*;

ID  :   LETTER (LETTER | [0-9])* ;

fragment
LETTER : [a-zA-Z] ;

NUMBER : [0-9]+ ;

SYMBOL : [~`\!@#$%^&*()_+\\|}{[\]?><,\./:\\='"];

WS  :   [ \t\n\r]+ -> skip ;

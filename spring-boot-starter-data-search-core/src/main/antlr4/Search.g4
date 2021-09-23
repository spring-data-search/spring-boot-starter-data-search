grammar Search;

// parser

input
    : search EOF
    ;

search
    : left=search logicalOp=(AND | OR) right=search #opSearch
    | LPAREN search RPAREN #prioritySearch
    | criteria #atomSearch
    ;

criteria
    : key op value
	| key
    ;

key
    : IDENTIFIER
	| N_IDENTIFIER
    ;

value
    : IDENTIFIER
    | STRING
    | ENCODED_STRING
    | NUMBER
    | BOOL
    ;

op
    : EQ
    | NE
    | GT
    | GE
    | LT
    | LE
    ;

// lexer

fragment DoubleStringCharacter
    : ~["\\\r\n]
    | '\\' EscapeSequence
    | LineContinuation
    ;

fragment SingleStringCharacter
    : ~['\\\r\n]
    | '\\' EscapeSequence
    | LineContinuation
    ;

fragment EscapeSequence
    : CharacterEscapeSequence
    | HexEscapeSequence
    | UnicodeEscapeSequence
    ;

fragment CharacterEscapeSequence
    : SingleEscapeCharacter
    | NonEscapeCharacter
    ;

fragment HexEscapeSequence
    : 'x' HexDigit HexDigit
    ;
 
fragment UnicodeEscapeSequence
    : 'u' HexDigit HexDigit HexDigit HexDigit
    ;

fragment SingleEscapeCharacter
    : ['"\\bfnrtv]
    ;

fragment NonEscapeCharacter
    : ~['"\\bfnrtv0-9xu\r\n]
    ;

fragment EscapeCharacter
    : SingleEscapeCharacter
    | DecimalDigit
    | [xu]
    ;

fragment LineContinuation
    : '\\' LineTerminatorSequence
    ;

fragment LineTerminatorSequence
    : '\r\n'
    | LineTerminator
    ;

fragment DecimalDigit
    : [0-9]
    ;

fragment HexDigit
    : [0-9a-fA-F]
    ;

fragment OctalDigit
    : [0-7]
    ;

BOOL
     : 'true'
     | 'TRUE'
     | 'false'
     | 'FALSE'
     ;

STRING
    : '"' DoubleStringCharacter* '"'
    | '\'' SingleStringCharacter* '\''
    ;

NUMBER
    : ('0' .. '9') + ('.' ('0' .. '9') +)?
    | ('-') + ('0' .. '9') + ('.' ('0' .. '9') +)?
    ;

LPAREN
    : '('
    ;

RPAREN
    : ')'
    ;

AND
    : 'AND'
    | 'and'
    ;

OR
    : 'OR'
    | 'or'
    ;

EQ
    : ':'
    ;

NE
    : '!:'
    ;

GT
    : '>'
    ;

GE
    : '>:'
    ;

LT
    : '<'
    ;

LE
    : '<:'
    ;

IDENTIFIER
    : [A-Za-z0-9.]+
    ;

N_IDENTIFIER
    : '!'[A-Za-z0-9.]+
    ;

ENCODED_STRING
    : ~([ :<>!()])+
    ;

LineTerminator
    : [\r\n\u2028\u2029] -> channel(HIDDEN)
    ;

WS
    : [ \t\r\n]+ -> skip
    ;

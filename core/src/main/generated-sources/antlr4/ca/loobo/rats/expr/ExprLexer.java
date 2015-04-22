// Generated from Expr.g4 by ANTLR 4.3
package ca.loobo.rats.expr;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class ExprLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.3", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__16=1, T__15=2, T__14=3, T__13=4, T__12=5, T__11=6, T__10=7, T__9=8, 
		T__8=9, T__7=10, T__6=11, T__5=12, T__4=13, T__3=14, T__2=15, T__1=16, 
		T__0=17, ID=18, NUMBER=19, SYMBOL=20, WS=21;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] tokenNames = {
		"'\\u0000'", "'\\u0001'", "'\\u0002'", "'\\u0003'", "'\\u0004'", "'\\u0005'", 
		"'\\u0006'", "'\\u0007'", "'\b'", "'\t'", "'\n'", "'\\u000B'", "'\f'", 
		"'\r'", "'\\u000E'", "'\\u000F'", "'\\u0010'", "'\\u0011'", "'\\u0012'", 
		"'\\u0013'", "'\\u0014'", "'\\u0015'"
	};
	public static final String[] ruleNames = {
		"T__16", "T__15", "T__14", "T__13", "T__12", "T__11", "T__10", "T__9", 
		"T__8", "T__7", "T__6", "T__5", "T__4", "T__3", "T__2", "T__1", "T__0", 
		"ID", "LETTER", "NUMBER", "SYMBOL", "WS"
	};


	public ExprLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "Expr.g4"; }

	@Override
	public String[] getTokenNames() { return tokenNames; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2\27o\b\1\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\3\2\3\2\3\3\3\3\3\4"+
		"\3\4\3\5\3\5\3\6\3\6\3\7\3\7\3\b\3\b\3\t\3\t\3\t\3\n\3\n\3\n\3\13\3\13"+
		"\3\13\3\f\3\f\3\r\3\r\3\16\3\16\3\16\3\17\3\17\3\17\3\20\3\20\3\20\3\21"+
		"\3\21\3\22\3\22\3\23\3\23\3\23\7\23[\n\23\f\23\16\23^\13\23\3\24\3\24"+
		"\3\25\6\25c\n\25\r\25\16\25d\3\26\3\26\3\27\6\27j\n\27\r\27\16\27k\3\27"+
		"\3\27\2\2\30\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31\16"+
		"\33\17\35\20\37\21!\22#\23%\24\'\2)\25+\26-\27\3\2\6\3\2\62;\4\2C\\c|"+
		"\b\2#.\60\61<<>B]b}\u0080\5\2\13\f\17\17\"\"q\2\3\3\2\2\2\2\5\3\2\2\2"+
		"\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3"+
		"\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2"+
		"\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2)\3\2\2"+
		"\2\2+\3\2\2\2\2-\3\2\2\2\3/\3\2\2\2\5\61\3\2\2\2\7\63\3\2\2\2\t\65\3\2"+
		"\2\2\13\67\3\2\2\2\r9\3\2\2\2\17;\3\2\2\2\21=\3\2\2\2\23@\3\2\2\2\25C"+
		"\3\2\2\2\27F\3\2\2\2\31H\3\2\2\2\33J\3\2\2\2\35M\3\2\2\2\37P\3\2\2\2!"+
		"S\3\2\2\2#U\3\2\2\2%W\3\2\2\2\'_\3\2\2\2)b\3\2\2\2+f\3\2\2\2-i\3\2\2\2"+
		"/\60\7+\2\2\60\4\3\2\2\2\61\62\7\60\2\2\62\6\3\2\2\2\63\64\7.\2\2\64\b"+
		"\3\2\2\2\65\66\7/\2\2\66\n\3\2\2\2\678\7,\2\28\f\3\2\2\29:\7*\2\2:\16"+
		"\3\2\2\2;<\7>\2\2<\20\3\2\2\2=>\7#\2\2>?\7?\2\2?\22\3\2\2\2@A\7^\2\2A"+
		"B\7$\2\2B\24\3\2\2\2CD\7>\2\2DE\7?\2\2E\26\3\2\2\2FG\7\"\2\2G\30\3\2\2"+
		"\2HI\7@\2\2I\32\3\2\2\2JK\7^\2\2KL\7^\2\2L\34\3\2\2\2MN\7?\2\2NO\7?\2"+
		"\2O\36\3\2\2\2PQ\7@\2\2QR\7?\2\2R \3\2\2\2ST\7$\2\2T\"\3\2\2\2UV\7#\2"+
		"\2V$\3\2\2\2W\\\5\'\24\2X[\5\'\24\2Y[\t\2\2\2ZX\3\2\2\2ZY\3\2\2\2[^\3"+
		"\2\2\2\\Z\3\2\2\2\\]\3\2\2\2]&\3\2\2\2^\\\3\2\2\2_`\t\3\2\2`(\3\2\2\2"+
		"ac\t\2\2\2ba\3\2\2\2cd\3\2\2\2db\3\2\2\2de\3\2\2\2e*\3\2\2\2fg\t\4\2\2"+
		"g,\3\2\2\2hj\t\5\2\2ih\3\2\2\2jk\3\2\2\2ki\3\2\2\2kl\3\2\2\2lm\3\2\2\2"+
		"mn\b\27\2\2n.\3\2\2\2\7\2Z\\dk\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}
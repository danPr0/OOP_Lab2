package util;

import parser.strategy.DOMParseAndSaveStrategy;
import parser.strategy.ParseAndSaveStrategy;
import parser.strategy.SAXParseAndSaveStrategy;
import parser.strategy.STAXParseAndSaveStrategy;
import util.schedule_props.StudentsRange;

public enum ParserName {
    DOMParser("DOM Parser") {
        @Override
        public ParseAndSaveStrategy getStrategy() {
            return new DOMParseAndSaveStrategy();
        }
    },
    SAXParser("SAX Parser") {
        @Override
        public ParseAndSaveStrategy getStrategy() {
            return new SAXParseAndSaveStrategy();
        }
    },
    STAXParser("STAX Parser") {
        @Override
        public ParseAndSaveStrategy getStrategy() {
            return new STAXParseAndSaveStrategy();
        }
    };

    private final String value;

    ParserName(String value) {
        this.value = value;
    }

    public abstract ParseAndSaveStrategy getStrategy();

    public static ParserName getByValue(String value) {
        for (ParserName parserName : ParserName.values())
            if (parserName.value.equals(value))
                return parserName;
        throw new IllegalArgumentException();
    }

    @Override
    public String toString() {
        return value;
    }
}

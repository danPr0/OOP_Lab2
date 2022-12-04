package util;

import org.apache.commons.lang3.tuple.Pair;
import util.schedule_props.StudentsRange;

import java.util.List;
import java.util.Locale;

public enum ScheduleAttribute {
    author {
        @Override
        public boolean filter(Object value, String filter) {
            String[] clueWords = filter.trim().toLowerCase(Locale.ROOT).split(" ");
            for (String word : clueWords)
                if (!((String) value).toLowerCase(Locale.ROOT).contains(word))
                    return false;
            return true;
        }
    },
    faculty {
        @Override
        public boolean filter(Object value, String filter) {
            return ((String) value).toLowerCase(Locale.ROOT).contains(filter.trim().toLowerCase(Locale.ROOT));
        }
    },
    department {
        @Override
        public boolean filter(Object value, String filter) {
            return ((String) value).toLowerCase(Locale.ROOT).contains(filter.trim().toLowerCase(Locale.ROOT));
        }
    },
    audiences {
        @Override
        public boolean filter(Object value, String filter) {
            List<String> filters = List.of((filter).replace(" ", "").split(","));
            for (String curFilter : filters) {
                boolean accepted = false;
                for (Pair<String, String> audience : (List<Pair<String, String>>) value) {
                    if (curFilter.equals("") || audience.getLeft().equals(curFilter))
                        accepted = true;
                }

                if (!accepted)
                    return false;
            }
            return true;
        }
    },
    curriculum {
        @Override
        public boolean filter(Object value, String filter) {
            return true;
        }
    },
    students {
        @Override
        public boolean filter(Object value, String filter) {
            if (filter.equals(""))
                return true;

            int quantity = (int) value;
            StudentsRange range = StudentsRange.getByValue(filter);

            return quantity >= range.getMin() && quantity <= range.getMax();
        }
    };

    public abstract boolean filter(Object value, String filter);
}

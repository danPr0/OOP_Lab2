package util.schedule_props;

import util.ParserName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum StudentsRange {
    range1 ("0-50") {
        @Override
        public int getMin() {
            return 0;
        }

        @Override
        public int getMax() {
            return 50;
        }
    },
    range2 ("51-100") {
        @Override
        public int getMin() {
            return 51;
        }

        @Override
        public int getMax() {
            return 100;
        }
    },
    range3 ("101-150") {
        @Override
        public int getMin() {
            return 101;
        }

        @Override
        public int getMax() {
            return 150;
        }
    },
    range4 ("150+") {
        @Override
        public int getMin() {
            return 150;
        }

        @Override
        public int getMax() {
            return Integer.MAX_VALUE;
        }
    };

    public abstract int getMin();
    public abstract int getMax();

    private String value;

    StudentsRange(String value) {
        this.value = value;
    }

    public static String[] getValues() {
        List<String> values = new ArrayList<>();
        Arrays.stream(StudentsRange.values()).toList().forEach(v -> values.add(v.getValue()));

        return values.toArray(String[]::new);
    }

    public static StudentsRange getByValue(String value) {
        for (StudentsRange studentsRange : StudentsRange.values())
            if (studentsRange.value.equals(value))
                return studentsRange;
        return null;
    }

    public static int getMaxNoOfStudents() {
        return 200;
    }

    public String getValue() {
        return value;
    }
}

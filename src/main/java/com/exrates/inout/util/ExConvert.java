package com.exrates.inout.util;


import java.math.BigDecimal;

public class ExConvert {

    private ExConvert() {
    }

    public static BigDecimal fromWei(String number, ExConvert.Unit unit) {
        return fromWei(new BigDecimal(number), unit);
    }

    private static BigDecimal fromWei(BigDecimal number, ExConvert.Unit unit) {
        return number.divide(unit.getWeiFactor());
    }

    public static BigDecimal toWei(BigDecimal number, ExConvert.Unit unit) {
        return number.multiply(unit.getWeiFactor());
    }

    public enum Unit {
        WEI("wei", 0),
        KWEI("kwei", 3),
        MWEI("mwei", 6),
        AIWEI("aiwei", 8),
        GWEI("gwei", 9),
        SZABO("szabo", 12),
        FINNEY("finney", 15),
        ETHER("ether", 18),
        KETHER("kether", 21),
        METHER("mether", 24),
        GETHER("gether", 27);

        private String name;
        private BigDecimal weiFactor;

        Unit(String name, int factor) {
            this.name = name;
            this.weiFactor = BigDecimal.TEN.pow(factor);
        }

        public BigDecimal getWeiFactor() {
            return this.weiFactor;
        }

        public String toString() {
            return this.name;
        }

        public static ExConvert.Unit fromString(String name) {
            if (name != null) {
                for (Unit unit : values()) {
                    if (name.equalsIgnoreCase(unit.name)) {
                        return unit;
                    }
                }
            }
            return valueOf(name);
        }
    }
}

package com.exrates.inout.service.ethereum;


import java.math.BigDecimal;

public class ExConvert {

    private ExConvert() {
    }

    static BigDecimal fromWei(String number, ExConvert.Unit unit) {
        return fromWei(new BigDecimal(number), unit);
    }

    private static BigDecimal fromWei(BigDecimal number, ExConvert.Unit unit) {
        return number.divide(unit.getWeiFactor());
    }

    public static BigDecimal toWei(String number, ExConvert.Unit unit) {
        return toWei(new BigDecimal(number), unit);
    }

    static BigDecimal toWei(BigDecimal number, ExConvert.Unit unit) {
        return number.multiply(unit.getWeiFactor());
    }

    public static enum Unit {
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
                ExConvert.Unit[] var1 = values();
                int var2 = var1.length;

                for (Unit unit : var1) {
                    if (name.equalsIgnoreCase(unit.name)) {
                        return unit;
                    }
                }
            }
            return valueOf(name);
        }
    }
}

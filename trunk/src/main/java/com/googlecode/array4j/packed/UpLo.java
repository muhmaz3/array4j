package com.googlecode.array4j.packed;

public enum UpLo {
    LO {
        @Override
        public String toString() {
            return "L";
        }
    },
    UP {
        @Override
        public String toString() {
            return "U";
        }
    };
}

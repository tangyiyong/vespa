// Copyright 2017 Yahoo Holdings. Licensed under the terms of the Apache 2.0 license. See LICENSE in the project root.
package com.yahoo.slime;

/**
 * Helper class for inserting values into an ObjectValue.
 * For justification read Inserter documentation.
 **/
final class ObjectInserter implements Inserter {
    private Cursor target;
    private int symbol;
    public final ObjectInserter adjust(Cursor c, int sym) {
        target = c;
        symbol = sym;
        return this;
    }
    public final Cursor insertNIX()                { return target.setNix(symbol); }
    public final Cursor insertBOOL(boolean value)  { return target.setBool(symbol, value); }
    public final Cursor insertLONG(long value)     { return target.setLong(symbol, value); }
    public final Cursor insertDOUBLE(double value) { return target.setDouble(symbol, value); }
    public final Cursor insertSTRING(String value) { return target.setString(symbol, value); }
    public final Cursor insertSTRING(byte[] utf8)  { return target.setString(symbol, utf8); }
    public final Cursor insertDATA(byte[] value)   { return target.setData(symbol, value); }
    public final Cursor insertARRAY()              { return target.setArray(symbol); }
    public final Cursor insertOBJECT()             { return target.setObject(symbol); }
}

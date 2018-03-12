package com.util.plcconn;  
public enum DataType {
	INT16(1),
    UINT16(2),
    DINT32(3),
    HEX32(4),
    REAL32(5),
    BIN16(6),
    CHAR8(7),
    BYTE8(8);
	
	private int index ;
	private DataType(int index ){
        this.index = index ;
    }
	
    public int getIndex() {
        return index;
    }
    public void setIndex(int index) {
        this.index = index;
    }
}
  

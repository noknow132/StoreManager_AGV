package com.util.plcconn;  
public enum PlcMemory {
	DI(1),
    DQ(2),
    MR(3),
    DR(4);
	
	private int index ;
	private PlcMemory(int index ){
        this.index = index ;
    }
	
    public int getIndex() {
        return index;
    }
    public void setIndex(int index) {
        this.index = index;
    }
}
  

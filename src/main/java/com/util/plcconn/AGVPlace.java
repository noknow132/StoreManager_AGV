package com.util.plcconn;  
public enum AGVPlace {
	
	// 上左1 area=0,num=1
	// 上左2 area=0,num=2
	// 上左3 area=0,num=4
	// 上左4 area=0,num=8
	// 上左5 area=0,num=16
	
	// 下左1 area=1,num=1
	// 下左2 area=1,num=2
	// 下左3 area=1,num=4
	// 下左4 area=1,num=8
	// 下左5 area=1,num=16
	// 下左6 area=1,num=32
	// 下左7 area=1,num=64
	// 下左8 area=1,num=128
	// 下左9 area=2,num=1
	// 下左10 area=2,num=2
	
	//停止  area不变,num=0
	Upleft1start(0,1),
	Upleft2start(0,2),
	Upleft3start(0,4),
	Upleft4start(0,8),
	Upleft5start(0,16),
	
	Upleftstop(0,0),
	
	Downleft1start(1,1),
	Downleft2start(1,2),
	Downleft3start(1,4),
	Downleft4start(1,8),
	Downleft5start(1,16),
	Downleft6start(1,32),
	Downleft7start(1,64),
	Downleft8start(1,128),
	Downleft9start(2,1),
	Downleft10start(2,2),
	
	Downleft1stop(1,0),
	Downleft2stop(2,0);
	
	
	private int area ;
	private int num;
	private AGVPlace(int area,int num){
		this.area = area ;
		this.num = num ;
    }
	
    public int getArea() {
        return area;
    }
    public void setArea(int area) {
        this.area = area;
    }
    
    public int getNum() {
        return num;
    }
    public void setNum(int num) {
        this.num = num;
    }
}
  

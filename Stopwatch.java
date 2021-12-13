class Stopwatch{
    private boolean isRunning;
    private int time;
    
    public Stopwatch(){
        isRunning = false;
        time = 0;
    }
    
    public Stopwatch(boolean isRunning, int time){
        this.isRunning = isRunning;
        this.time = time;
    }
    public void start(){
        isRunning = true;
    }
    
    public void update(){
        if(isRunning)
            time++;
    }
    
    public void stop(){
        isRunning = false;
    }
    
    public void reset(){
        isRunning = false;
        time = 0;
    }
    
    public int getTime(){
        return time;
    }
}
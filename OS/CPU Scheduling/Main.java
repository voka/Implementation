import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

class Algorithm{
    public String algoName;
    public double avgWaitTime;
    public double avgTurnAroundTime;
    public List<Result> executionResults;

    public Algorithm(String name) {
        this.algoName = name;
        this.avgWaitTime = 0.0f;
        this.avgTurnAroundTime = 0.0f;
        this.executionResults = new ArrayList<>();
    }

    public void execution(PriorityQueue<Process> readyQueue){};

    public void calculateAVG(int size){
        for(int i=0;i<executionResults.size();++i){
            this.avgWaitTime += executionResults.get(i).waitTime;
            this.avgTurnAroundTime += executionResults.get(i).turnAroundTime;
        }
        this.avgWaitTime /= size;
        this.avgTurnAroundTime /= size;
    }

    public void printResult(){
        System.out.println(this.algoName);
        for(Result cur : executionResults){
            System.out.println(cur.toString());
        }
        System.out.printf("Avg -> WaitTime : %f, TurnAroundTime : %f\n\n", avgWaitTime , avgTurnAroundTime);
    };
}



class Result{
    int id; // process id;
    int waitTime; // 큐에 도착해서 기다린 시간 
    int turnAroundTime; // 큐에 도착해서 실행 종료까지 걸린 시간

    public Result(int id, int waitTime, int turnAroundTime) {
        this.id = id;
        this.waitTime = waitTime;
        this.turnAroundTime = turnAroundTime;
    }
    @java.lang.Override
    public String toString(){
        return String.format("id : %d, wait : %d, turn : %d",this.id, this.waitTime, this.turnAroundTime);
    }
}

class Process{
    int id;
    int arrivalTime;
    int cpuTime;

    public Process(int id, int arrivalTime, int cpuTime) {
        this.id = id;
        this.arrivalTime = arrivalTime;
        this.cpuTime = cpuTime;
    }
}

class FCFSProcess extends Process implements Comparable<FCFSProcess>{

    public FCFSProcess(int id, int arrivalTime, int cpuTime) {
        super(id, arrivalTime, cpuTime);
    }
    @Override
    public int compareTo(FCFSProcess o){
        
        if(this.arrivalTime > o.arrivalTime){
            return 1;
        }
        else{
            return -1;
        }
    }

}

class SJFProcess extends Process implements Comparable<SJFProcess>{

    public SJFProcess(int id, int arrivalTime, int cpuTime) {
        super(id, arrivalTime, cpuTime);
    }
    @Override
    public int compareTo(SJFProcess o){
         if(this.arrivalTime > o.arrivalTime){
            return 1;
        }
        else{
            if(this.cpuTime > o.cpuTime){
                return 1;
            }
            else if(this.cpuTime <  o.cpuTime){
                return -1;
            }
            return 0;
        }
    }

}

class SRTProcess extends Process implements Comparable<SRTProcess>{

    public int rid;
    public SRTProcess(int id, int arrivalTime, int cpuTime, int rid) {
        super(id, arrivalTime, cpuTime);
        this.rid = rid;
    }
    @Override
    public int compareTo(SRTProcess o){
        if(this.cpuTime > o.cpuTime){
            return 1;
        }
        else if(this.cpuTime < o.cpuTime){
            return -1;
        }
        return 0;
    }

}

class RRProcess extends Process{
    public int rid;
    public RRProcess(int id, int arrivalTime, int cpuTime, int rid) {
        super(id, arrivalTime, cpuTime);
        this.rid = rid;
    }
}

class HRNProcess extends Process implements Comparable<HRNProcess>{
    public int rid;
    public int waitTime;
    public HRNProcess(int id, int arrivalTime, int cpuTime, int rid) {
        super(id, arrivalTime, cpuTime);
        this.rid = rid;
        this.waitTime = 0;
    }
    @Override
    public int compareTo(HRNProcess o){ // 응답 비율 계산해서 우선순위로 사용함.
        double responseRatioA = (this.waitTime + this.cpuTime) / this.cpuTime;
        double responseRatioB = (o.waitTime + o.cpuTime) / o.cpuTime;
        System.out.printf("a-id : %d, b-id : %d, responseRatioA : %f, responseRatioB : %f\n", this.id, o.id, responseRatioA, responseRatioA );
        if(responseRatioA < responseRatioB){
            return 1;
        }
        else if(responseRatioA > responseRatioB){
            return -1;
        }
        return 0;
    }
}

class FCFS extends Algorithm{

    public FCFS() {
        super("FCFS");
    }

    @java.lang.Override
    public void execution(PriorityQueue<Process> readyQueue) {
        int time = 0;
        int size = readyQueue.size();//queue 크기 확인

        while(!readyQueue.isEmpty()){
            if(readyQueue.peek().arrivalTime <= time){ // 도착 시간이 되었다면 
                Process cur = readyQueue.poll(); // 꺼내기
                int waitTime = time - cur.arrivalTime; // 기다린 시간 계산
                int turnAroundTime = waitTime + cur.cpuTime; // 도착 ~ 실행 종료까지의 시간 계산
                Result result = new Result(cur.id, waitTime, turnAroundTime); 
                time += cur.cpuTime; // 시간에 cpu 사용 시간 더하기
                this.executionResults.add(result);
            }
            time++; // 시간 진행
        }
        calculateAVG(size);
    }
}

class SJF extends Algorithm {
    public SJF(){
        super("SJF");
    }
    @java.lang.Override
    public void execution(PriorityQueue<Process> readyQueue) {
        int time = 0;
        int size = readyQueue.size();//queue 크기 확인

        while(!readyQueue.isEmpty()){
            if(readyQueue.peek().arrivalTime <= time){ // 도착 시간이 되었다면 
                Process cur = readyQueue.poll(); // 꺼내기
                int waitTime = time - cur.arrivalTime; // 기다린 시간 계산
                int turnAroundTime = waitTime + cur.cpuTime; // 도착 ~ 실행 종료까지의 시간 계산
                Result result = new Result(cur.id, waitTime, turnAroundTime); 
                time += cur.cpuTime; // 시간에 cpu 사용 시간 더하기
                this.executionResults.add(result);
            }
            time++; // 시간 진행
        }
        calculateAVG(size);
    }
}

class SRT extends Algorithm { // 선점형 알고리즘 -> 다시 우선순위 큐에 넣어주자 

    public SRT(){
        super("SRT");
    }
    public void addWaitTime(int rid, boolean[] end){
        for(int i=0;i<executionResults.size();++i){
            if(i == rid) continue; // cpu를 사용중인 process를 제외하고
            if(!end[i]) executionResults.get(i).waitTime += 1; // 종료된 프로세스도 제외하고 대기시간 증가시키기
        }
    }

    public void processJob(PriorityQueue<SRTProcess> jobQueue, boolean[] end){
        SRTProcess cur = jobQueue.poll();
        cur.cpuTime -= 1;// cpu 사용시간을 1 줄인다.
        executionResults.get(cur.rid).turnAroundTime += 1;// 실행시간을 1 늘려준다.
        if(cur.cpuTime == 0){ // 작업이 종료되었다면
            int waitTime = executionResults.get(cur.rid).waitTime;
            executionResults.get(cur.rid).turnAroundTime += waitTime; // 실행시간에 대기시간을 더해준다.
            end[cur.rid] = true; // 종료 표시 
        }
        else jobQueue.add(cur); // 종료되지 않았다면 작업큐에 다시 넣는다.
        addWaitTime(cur.rid,end); // 대기큐에 있는 프로세스들 대기시간 증가 시키기
    }

    @java.lang.Override
    public void execution(PriorityQueue<Process> readyQueue) {
        int time = 0;
        int size = readyQueue.size();// queue 크기 확인
        boolean[] end = new boolean[size]; // process 종료 여부 
        PriorityQueue<SRTProcess> jobQueue = new PriorityQueue<>();
        while(!readyQueue.isEmpty()){
            if(readyQueue.peek().arrivalTime <= time){ // 도착 시간이 되었다면 
                Process cur = readyQueue.poll(); // 꺼내기
                SRTProcess scur = new SRTProcess(cur.id, cur.arrivalTime, cur.cpuTime, executionResults.size());
                jobQueue.add(scur);
                Result result = new Result(cur.id, 0, 0);
                executionResults.add(result);
            }
            if(!jobQueue.isEmpty()){ // jobQueue가 비어있지 않을때
                processJob(jobQueue,end);
            }
            time++; // 시간 진행
        }
        // job큐가 비어있지 않다면 비어있을때 까지 실행
        while(!jobQueue.isEmpty()){ 
            processJob(jobQueue,end);
        }
        
        calculateAVG(size);
    }
}
class RR extends Algorithm{

    public int timeQuota;

    public RR(int quota) {
        super("Round Robin");
        this.timeQuota = quota;
    }

    public void addWaitTime(int rid, boolean[] end, int plus){
        for(int i=0;i<executionResults.size();++i){
            if(i == rid) continue; // cpu를 사용중인 process를 제외하고
            if(!end[i]) executionResults.get(i).waitTime += plus; // 종료된 프로세스도 제외하고 대기시간 증가시키기
        }
    }

    public int processJob(Queue<RRProcess> jobQueue, boolean[] end){
        RRProcess cur = jobQueue.poll();
        System.out.printf("id : %d\n",cur.id);
        int useTime = this.timeQuota; // 기본 할당량 설정
        if(cur.cpuTime < this.timeQuota){ // 기본 할당량 보다 cpu 사용시간이 작게 남은 경우 작은 시간을 골라준다.
            useTime = cur.cpuTime;
        }
        cur.cpuTime -= useTime;// cpu 사용시간을 min(할당량, 남은 작업시간)만큼 줄인다.
        executionResults.get(cur.rid).turnAroundTime += useTime;// 실행시간을 늘려준다.
        if(cur.cpuTime == 0){ // 작업이 종료되었다면
            int waitTime = executionResults.get(cur.rid).waitTime;
            executionResults.get(cur.rid).turnAroundTime += waitTime; // 실행시간에 대기시간을 더해준다.
            end[cur.rid] = true; // 종료 표시 
        }
        else jobQueue.add(cur); // 종료되지 않았다면 작업큐에 다시 넣는다.
        addWaitTime(cur.rid,end,useTime); // 대기큐에 있는 프로세스들 대기시간 증가 시키기\
        return useTime;
    }
    public RRProcess makeDefault(){
        return new RRProcess(-1, -1, -1, -1);
    }

    @java.lang.Override
    public void execution(PriorityQueue<Process> readyQueue) {
        int time = 0, preTime = 0;
        int useTime = 0; // 이전 작업에 걸린 시간 
        int size = readyQueue.size();// queue 크기 확인
        boolean[] end = new boolean[size]; // process 종료 여부 
        RRProcess lastJob = makeDefault(); // 마지막으로 작업하고 cpu 시간이 남아서 다시 queue에 넣어야 하는 프로세스
        Queue<RRProcess> jobQueue = new LinkedList<>();
        while(!readyQueue.isEmpty()){
            if(readyQueue.peek().arrivalTime <= time){ // 도착 시간이 되었다면 
                Process cur = readyQueue.poll(); // 꺼내기
                RRProcess rcur = new RRProcess(cur.id, cur.arrivalTime, cur.cpuTime, executionResults.size());
                jobQueue.add(rcur);
                Result result = new Result(cur.id, 0, 0);
                if(lastJob.id != -1){ //이미 작업 중인 process가 존재하는 경우 대기시간을 증가시킨다.
                    result.waitTime += (useTime - (time - preTime)); // cpu 사용 시간 - (cpu 사용 시작으로 부터 지난 시간);
                }
                executionResults.add(result);
            }
            if((!jobQueue.isEmpty()) && (time - preTime >= useTime)){ // jobQueue가 비어있지 않고, 이전 프로세스의 작업시간이 지난 경우
                preTime = time; // 현재 시간 저장
                //이전 작업이 다시 작업 큐에 들어가야 한다면
                if(lastJob.id != -1){
                    jobQueue.add(lastJob);
                    lastJob = makeDefault();
                }
                RRProcess cur = jobQueue.poll();
                System.out.printf("id : %d\n",cur.id);
                useTime = this.timeQuota; // 기본 할당량 설정
                if(cur.cpuTime < this.timeQuota){ // 기본 할당량 보다 cpu 사용시간이 작게 남은 경우 작은 시간을 골라준다.
                    useTime = cur.cpuTime;
                }
                cur.cpuTime -= useTime;// cpu 사용시간을 min(할당량, 남은 작업시간)만큼 줄인다.
                executionResults.get(cur.rid).turnAroundTime += useTime;// 실행시간을 늘려준다.
                if(cur.cpuTime == 0){ // 작업이 종료되었다면
                    int waitTime = executionResults.get(cur.rid).waitTime;
                    executionResults.get(cur.rid).turnAroundTime += waitTime; // 실행시간에 대기시간을 더해준다.
                    end[cur.rid] = true; // 종료 표시 
                }
                else {
                    lastJob = cur; // 종료되지 않았다면 다음 작업때 대기 큐에 넣는다.
                }
                addWaitTime(cur.rid,end,useTime); // 대기큐에 있는 프로세스들 대기시간 증가 시키기
            }
            time++; // 시간 진행
        }
        // job큐가 비어있지 않다면 비어있을때 까지 실행
        while(!jobQueue.isEmpty()){ 
            processJob(jobQueue, end);
        }
        
        calculateAVG(size);
    }

}

class HRN extends Algorithm{

    public HRN() {
        super("Hightest Response Ratio Next");
    }

    public void addWaitTime(int rid, boolean[] end, int plus){
        for(int i=0;i<executionResults.size();++i){
            if(i == rid) continue; // cpu를 사용중인 process를 제외하고
            if(!end[i]) executionResults.get(i).waitTime += plus; // 종료된 프로세스도 제외하고 대기시간 증가시키기
        }
    }
    public void reCalculateResponseRatio(PriorityQueue<HRNProcess> jobQueue){
        List<HRNProcess> list = new ArrayList<>();
        while(!jobQueue.isEmpty()){
            HRNProcess hrnProcess = jobQueue.poll();
            list.add(hrnProcess);
        }
        for(int i=0;i<list.size();++i){
            int rid = list.get(i).rid;
            list.get(i).waitTime = executionResults.get(rid).waitTime; // 대기 시간 최신화
            jobQueue.add(list.get(i));
        }
    }
    public HRNProcess processJob(PriorityQueue<HRNProcess> jobQueue, boolean[] end){
        // 응답 비율 다시 계산
        reCalculateResponseRatio(jobQueue);
        HRNProcess cur = jobQueue.poll();
        executionResults.get(cur.rid).turnAroundTime += cur.cpuTime;// 실행시간을 늘려준다.
        // 작업 종료
        int waitTime = executionResults.get(cur.rid).waitTime;
        executionResults.get(cur.rid).turnAroundTime += waitTime; // 실행시간에 대기시간을 더해준다.
        end[cur.rid] = true; // 종료 표시 
        return cur;
    }

    @java.lang.Override
    public void execution(PriorityQueue<Process> readyQueue) {
        int time = 0;
        int useTime = 0;
        int curId = -1;
        int size = readyQueue.size();// queue 크기 확인
        boolean[] end = new boolean[size]; // process 종료 여부 
        PriorityQueue<HRNProcess> jobQueue = new PriorityQueue();
        while(!readyQueue.isEmpty()){
            if(readyQueue.peek().arrivalTime <= time){ // 도착 시간이 되었다면 
                Process cur = readyQueue.poll(); // 꺼내기
                HRNProcess rcur = new HRNProcess(cur.id, cur.arrivalTime, cur.cpuTime, executionResults.size());
                jobQueue.add(rcur);
                Result result = new Result(cur.id, 0, 0);
                executionResults.add(result);
            }
            if((!jobQueue.isEmpty()) && (useTime == 0)){ // jobQueue가 비어있지 않고, 이전 작업이 모두 끝났다면
                HRNProcess cur = processJob(jobQueue,end);
                useTime = cur.cpuTime-1;
                curId = cur.rid;
            }else{//이전 작업이 모두 끝나지 않았다면
                if(useTime > 0) useTime--;
                addWaitTime(curId, end, 1);
            }
            time++; // 시간 진행
            System.out.printf("Time : %d\n",time);
            printResult();
        }
        if(useTime > 1){
            addWaitTime(curId, end, useTime);
        }
        while(!jobQueue.isEmpty()){
            HRNProcess cur = processJob(jobQueue,end);
            addWaitTime(cur.rid, end, cur.cpuTime);
            printResult();
        }
        calculateAVG(size);
    }

}


public class Main {

    static PriorityQueue<Process> pq = new PriorityQueue<>();

    static void makeFCFSReadyQueue(){
        Process A = new FCFSProcess(1, 0, 6);
        Process B = new FCFSProcess(2, 1, 3);
        Process C = new FCFSProcess(3, 4, 2);
        Process D = new FCFSProcess(4, 6, 1);
        pq.add(A);
        pq.add(B);
        pq.add(C);
        pq.add(D);
    }

    static void makeSJFReadyQueue(){
        Process A = new SJFProcess(1, 1, 6);
        Process B = new SJFProcess(2, 2, 3);
        Process C = new SJFProcess(3, 3, 1);
        Process D = new SJFProcess(4, 4, 4);
        pq.add(A);
        pq.add(B);
        pq.add(C);
        pq.add(D);
    }
    
    public static void main(String[] args) {
        makeFCFSReadyQueue();
        FCFS fcfs = new FCFS();
        fcfs.execution(pq);
        fcfs.printResult();

        makeSJFReadyQueue();
        SJF sjf = new SJF();
        sjf.execution(pq);
        sjf.printResult();

        makeFCFSReadyQueue();
        SRT srt = new SRT();
        srt.execution(pq);
        srt.printResult();

        makeFCFSReadyQueue();
        RR rr = new RR(3);
        rr.execution(pq);
        rr.printResult();

        makeFCFSReadyQueue();
        HRN hrn = new HRN();
        hrn.execution(pq);
        hrn.printResult();
    }
}

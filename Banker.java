import java.util.ArrayList;
import java.util.Arrays;

public class Banker {
    public int[] available; // the available amount of each resource
    public int[][] maximum; // the maximum demand of each process
    public int[][] allocation; // the amount currently allocated to each process
    public int[][] need;
    public int numOfPros, numOfRes;
    ArrayList<Integer> waitingProcess;
    ArrayList<int[]> waitingRequest;
    boolean[] finish;

    public Banker(int numOfPros, int numOfRes) {
        this.numOfPros = numOfPros;
        this.numOfRes = numOfRes;
        finish = new boolean[numOfPros];
        waitingProcess = new ArrayList<>();
        waitingRequest = new ArrayList<>();

    }

    public boolean checkAllFinish(boolean[] finish) {
        for (boolean b : finish) {
            if (!b) {
                return false;
            }
        }
        return true;
    }

    public boolean safetyCheck() {
        boolean safe = false;
        int[] work = new int[numOfRes];
        ArrayList<Integer> seq = new ArrayList<>();
        Arrays.fill(finish, false);
        System.arraycopy(available, 0, work, 0, numOfRes);
        int i = 0;
        int previousProc = numOfPros;
        int currentProc = numOfPros;
        while (!checkAllFinish(finish)) {

            if (i == numOfPros) {

                if (currentProc < previousProc) {
                    previousProc = currentProc;
                } else {
                    break;
                }

                if (!checkAllFinish(finish)) {
                    i = 0;
                } else {
                    break;
                }
            }
            if (!finish[i]) {
                int count = 0;
                while (count < numOfRes) {
                    if (need[i][count] > work[count])
                        break;
                    count++;
                }
                // Finish its work
                if (count == numOfRes) {
                    finish[i] = true;
                    seq.add(i);
                    currentProc--;
                    for (int j = 0; j < work.length; j++) {
                        work[j] += allocation[i][j];
                    }
                } else {

                    safe = checkAllFinish(finish);

                }

            }
            i++;

            System.out.println("/////////////////////////");
            System.out.println("Processes State: ");
            System.out.println("Work: " + Arrays.toString(work));
            System.out.println("===========");
            getProcessState();
            System.out.println("===========");

        }
        String s = "";
        if (checkAllFinish(finish)) {
            s = "safe";
        } else
            s = "not safe";

        System.out.println("/////////////////////////");
        System.out.println("System is " + s);

        String str = "<";
        for (int k : seq) {
            str += "P" + k + ",";
        }
        str = str.substring(0, str.length() - 1);
        str += ">";

        System.out.println("The sequence: " + str);
        return checkAllFinish(finish);

    }

    public void request(int proNo, int[] request) {

        boolean check = false;
        for (int i = 0; i < request.length; i++) {
            if (request[i] > need[proNo][i]) {
                System.out.println("Maximum number exceeded");
                break;
            }
            if (request[i] > available[i]) {
                System.out.println("Waiting for resources");
                waitingProcess.add(proNo);
                waitingRequest.add(request);
                check = true;
                break;
            }
        }
        int[][] tempAllocation = new int[numOfPros][numOfRes], tempNeed = new int[numOfPros][numOfRes];
        int[] tempAvailable = new int[numOfRes];

        System.arraycopy(available, 0, tempAvailable, 0, numOfRes);
        for (int i = 0; i < numOfPros; i++) {
            for (int j = 0; j < numOfRes; j++) {
                tempAllocation[i][j] = allocation[i][j];
                tempNeed[i][j] = need[i][j];
            }
        }

        if (!check) {
            finish[proNo] = false;
            for (int j = 0; j < request.length; j++) {
                available[j] -= request[j];
                allocation[proNo][j] += request[j];
                need[proNo][j] -= request[j];
            }
        }
        if (safetyCheck()) {
            System.out.println("Request Approved");
            getAvailable();
            getAllocation();
            getNeed();

        } else {
            System.arraycopy(tempAvailable, 0, available, 0, numOfRes);
            for (int i = 0; i < numOfPros; i++) {
                for (int j = 0; j < numOfRes; j++) {
                    allocation[i][j] = tempAllocation[i][j];
                    need[i][j] = tempNeed[i][j];
                }
            }

            waitingProcess.add(proNo);
            waitingRequest.add(request);
        }


    }

    public void getAvailable() {

        System.out.println("Available: " + Arrays.toString(available));
        System.out.println("===========");


    }

    public void getAllocation() {

        System.out.println("Allocation");

        for (int i = 0; i < numOfPros; i++) {
            System.out.println(Arrays.toString(allocation[i]));
        }
        System.out.println("===========");

    }

    public void getMaximum() {

        System.out.println("Maximum");

        for (int i = 0; i < numOfPros; i++) {
            System.out.println(Arrays.toString(maximum[i]));
        }
        System.out.println("===========");
    }

    public void getNeed() {

        System.out.println("Need");
        for (int i = 0; i < numOfPros; i++) {
            System.out.println(Arrays.toString(need[i]));
        }
        System.out.println("===========");
    }

    public void getProcessState() {
        System.out.println("P# " + "  Finished");

        for (int i = 0; i < finish.length; i++) {
            System.out.println("P" + i + ": " + finish[i]);
        }

    }
}

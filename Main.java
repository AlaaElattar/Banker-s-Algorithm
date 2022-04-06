import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        System.out.println("Please consider that the order of processes will be P0,P1,P2,...etc.");


        System.out.print("Enter number of processes: ");
        int numOfPros = sc.nextInt();

        System.out.print("\nEnter number of resources: ");
        int numOfRes = sc.nextInt(); // length of available

        Banker banker = new Banker(numOfPros, numOfRes);

        banker.available = new int[numOfRes];
        banker.maximum = new int[numOfPros][numOfRes];
        banker.allocation = new int[numOfPros][numOfRes];
        banker.need = new int[numOfPros][numOfRes];

        System.out.print("\nEnter available matrix: ");
        for (int i = 0; i < banker.available.length; i++) {
            banker.available[i] = sc.nextInt();
        }
        System.out.println("Enter maximum matrix: ");

        for (int i = 0; i < numOfPros; i++) {

            for (int j = 0; j < numOfRes; j++) {

                banker.maximum[i][j] = sc.nextInt();
            }
        }

        System.out.println("Enter allocation matrix: ");

        for (int i = 0; i < numOfPros; i++) {

            for (int j = 0; j < numOfRes; j++) {
                banker.allocation[i][j] = sc.nextInt();
            }
        }


        for (int i = 0; i < numOfPros; i++) {
            for (int j = 0; j < numOfRes; j++) {
                System.out.print(banker.maximum[i][j] + " ");
            }
            System.out.println("");
        }

        //Test Case//
        /* allocation
        0 1 0
        2 0 0
        3 0 2
        2 1 1
        0 0 2
         */
        /* maximum
        7 5 3
        3 2 2
        9 0 2
        2 2 2
        4 3 3
         */

        for (int i = 0; i < numOfPros; i++) {
            for (int j = 0; j < numOfRes; j++) {
                banker.need[i][j] = banker.maximum[i][j] - banker.allocation[i][j];
            }
        }

        banker.safetyCheck();
        boolean quit = false;

        while (!quit) {

            System.out.println("Enter your choice (RQ - RL - Recover - Quit), process number, resources space separated: ");
            String input = sc.next();

            if (input.equals("RQ") || input.equals("RL")) {
                int processNo = sc.nextInt();
                int[] request = new int[numOfRes];
                for (int i = 0; i < numOfRes; i++) {
                    request[i] = sc.nextInt();
                }
                //test case
                //int processNo = 1;
                //int[] request = {1, 0, 2};

                switch (input) {
                    case "RQ":
                        banker.request(processNo, request);
                        break;
                    case "RL":
                        boolean RL = false;
                        while(RL != true) {
                            RL = banker.release (processNo,request);
                            if (RL)
                                break;
                            else {
                                processNo = sc.nextInt ();
                                for (int i = 0; i < numOfRes; i++) {
                                    request[i] = sc.nextInt();
                                }

                            }
                        }
                        break;
                }
            } else if (input.equalsIgnoreCase("Recover")) {
                banker.recovery ();
            } else if (input.equalsIgnoreCase("Quit")) {
                quit = true;

            } else {
                System.out.println("Wrong input");
            }
        }


    }
}

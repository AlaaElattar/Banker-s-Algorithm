import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Banker {
    public int[] available; // the available amount of each resource
    public int[][] maximum; // the maximum demand of each process
    public int[][] allocation; // the amount currently allocated to each process
    public int[][] need;
    public int numOfPros, numOfRes;
    ArrayList < Integer > waitingProcess;
    ArrayList < int[] > waitingRequest;
    boolean[] finish;

    public Banker ( int numOfPros , int numOfRes ) {
        this.numOfPros = numOfPros;
        this.numOfRes = numOfRes;
        finish = new boolean[ numOfPros ];
        waitingProcess = new ArrayList <> ( );
        waitingRequest = new ArrayList <> ( );

    }

    public boolean checkAllFinish ( boolean[] finish ) {
        for ( boolean b : finish ) {
            if ( ! b ) {
                return false;
            }
        }
        return true;
    }

    public boolean safetyCheck ( ) {
        boolean safe = false;
        int[] work = new int[ numOfRes ];
        ArrayList < Integer > seq = new ArrayList <> ( );
        Arrays.fill ( finish , false );
        System.arraycopy ( available , 0 , work , 0 , numOfRes );
        int i = 0;
        int previousProc = numOfPros;
        int currentProc = numOfPros;
        while ( ! checkAllFinish ( finish ) ) {

            if ( i == numOfPros ) {

                if ( currentProc < previousProc ) {
                    previousProc = currentProc;
                } else {
                    break;
                }

                if ( ! checkAllFinish ( finish ) ) {
                    i = 0;
                } else {
                    break;
                }
            }
            if ( ! finish[ i ] ) {
                int count = 0;
                while ( count < numOfRes ) {
                    if ( need[ i ][ count ] > work[ count ] )
                        break;
                    count++;
                }
                // Finish its work
                if ( count == numOfRes ) {
                    finish[ i ] = true;
                    seq.add ( i );
                    currentProc--;
                    for ( int j = 0 ; j < work.length ; j++ ) {
                        work[ j ] += allocation[ i ][ j ];
                    }
                } else {

                    safe = checkAllFinish ( finish );

                }

            }
            i++;

            System.out.println ( "/////////////////////////" );
            System.out.println ( "Processes State: " );
            System.out.println ( "Work: " + Arrays.toString ( work ) );
            System.out.println ( "===========" );
            getProcessState ( );
            System.out.println ( "===========" );

        }
        String s = "";
        if ( checkAllFinish ( finish ) ) {
            s = "safe";
        } else
            s = "not safe";

        System.out.println ( "/////////////////////////" );
        System.out.println ( "System is " + s );

        String str = "<";
        for ( int k : seq ) {
            str += "P" + k + ",";
        }
        str = str.substring ( 0 , str.length ( ) - 1 );
        str += ">";

        System.out.println ( "The sequence: " + str );
        return checkAllFinish ( finish );

    }

    public void request ( int proNo , int[] request ) {

        boolean check = false;
        for ( int i = 0 ; i < request.length ; i++ ) {
            if ( request[ i ] > need[ proNo ][ i ] ) {
                System.out.println ( "Maximum number exceeded" );
                break;
            }
            if ( request[ i ] > available[ i ] ) {
                System.out.println ( "Waiting for resources" );
                if(!waitingProcess.contains(proNo)){
                    waitingProcess.add ( proNo );
                    waitingRequest.add ( request );
                }
                check = true;
                break;
            }
        }
        int[][] tempAllocation = new int[ numOfPros ][ numOfRes ], tempNeed = new int[ numOfPros ][ numOfRes ];
        int[] tempAvailable = new int[ numOfRes ];

        System.arraycopy ( available , 0 , tempAvailable , 0 , numOfRes );
        for ( int i = 0 ; i < numOfPros ; i++ ) {
            for ( int j = 0 ; j < numOfRes ; j++ ) {
                tempAllocation[ i ][ j ] = allocation[ i ][ j ];
                tempNeed[ i ][ j ] = need[ i ][ j ];
            }
        }

        if ( ! check ) {
            finish[ proNo ] = false;
            for ( int j = 0 ; j < request.length ; j++ ) {
                available[ j ] -= request[ j ];
                allocation[ proNo ][ j ] += request[ j ];
                need[ proNo ][ j ] -= request[ j ];
            }
        }
        if ( safetyCheck ( ) ) {
            System.out.println ( "Request Approved" );
            if(waitingProcess.contains(proNo))
            {
                waitingProcess.remove(proNo);
                waitingRequest.remove(request);
            }
            getAvailable ( );
            getAllocation ( );
            getNeed ( );

        } else {

            System.out.println("Apply recovery algorithm? Y/N");

            Scanner input= new Scanner(System.in);
            String recover=input.next();
            if(recover.equalsIgnoreCase("Y")) recovery();
            else {
                System.arraycopy(tempAvailable, 0, available, 0, numOfRes);
                for (int i = 0; i < numOfPros; i++) {
                    for (int j = 0; j < numOfRes; j++) {
                        allocation[i][j] = tempAllocation[i][j];
                        need[i][j] = tempNeed[i][j];
                    }
                }
                if(!waitingProcess.contains(proNo)){
                    waitingProcess.add ( proNo );
                    waitingRequest.add ( request );
                }
            }
        }


    }

    public void getAvailable ( ) {

        System.out.println ( "Available: " + Arrays.toString ( available ) );
        System.out.println ( "===========" );


    }

    public void getAllocation ( ) {

        System.out.println ( "Allocation" );

        for ( int i = 0 ; i < numOfPros ; i++ ) {
            System.out.println ( Arrays.toString ( allocation[ i ] ) );
        }
        System.out.println ( "===========" );

    }

    public void getMaximum ( ) {

        System.out.println ( "Maximum" );

        for ( int i = 0 ; i < numOfPros ; i++ ) {
            System.out.println ( Arrays.toString ( maximum[ i ] ) );
        }
        System.out.println ( "===========" );
    }

    public void getNeed ( ) {

        System.out.println ( "Need" );
        for ( int i = 0 ; i < numOfPros ; i++ ) {
            System.out.println ( Arrays.toString ( need[ i ] ) );
        }
        System.out.println ( "===========" );
    }

    public void getProcessState ( ) {
        System.out.println ( "P# " + "  Finished" );

        for ( int i = 0 ; i < finish.length ; i++ ) {
            System.out.println ( "P" + i + ": " + finish[ i ] );
        }

    }

    public boolean release ( int numOfPros , int arr[] ) {
        System.out.println ( "Before" );
        this.getAllocation ( );
        this.getAvailable ( );

        boolean flag = true;
        for ( int i = 0 ; i < allocation[ numOfPros ].length ; i++ ) {
            if ( arr[ i ] > allocation[ numOfPros ][ i ] ) {
                flag = false;
                break;
            }
        }
        if ( flag ) {
            for ( int i = 0 ; i < allocation[ numOfPros ].length ; i++ ) {
                available[i] += arr[i];
                allocation[ numOfPros ][ i ] -= arr[ i ];

            }
            System.out.println ( "Resources were released successfully" );
            System.out.println ( "After released : " );
            this.getAllocation ( );
            this.getAvailable ( );
            return true;

        } else {
            System.out.println ( "Error, Please enter number of process & less number of resources" );
            return false;
        }
    }

    public void prevention ( int numOfpro ) {   //release for recovery
        System.out.println ( "Before" );
        this.getAllocation ( );
        this.getAvailable ( );
        for ( int i = 0 ; i < allocation[ 0 ].length ; i++ ) {
            available[ i ] += allocation[ numOfpro ][ i ];
            allocation[ numOfpro ][ i ] = 0;
        }
        System.out.println ( "After" );
        this.getAllocation ( );
        this.getAvailable ( );
    }

    public boolean zeroAllocationCheck()
    {
        for ( int i = 0 ; i < numOfPros ; i++ ) {
            for ( int j = 0 ; j < numOfRes ; j++ ) {
                if (allocation[ i ][ j ]!=0) return false;
            }
        }
        return true;
    }


    public void recovery ( ) {
        System.out.println ( "The victim to be released was chosen based on its maximum allocation " );
        while ( !safetyCheck ( ) || waitingProcess.size()!=0 ) {
            int max = - 1;
            int sum = 0;
            int idx = - 1;
            for ( int i = 0 ; i < allocation.length ; i++ ) {
                for ( int j = 0 ; j < allocation[ i ].length ; j++ ) {
                    sum += allocation[ i ][ j ];
                }
                if ( sum >= max ) {
                    max = sum;
                    idx = i;
                }
                sum=0;
            }
            System.out.println ( "The process of number " + idx + " will be released" );
            prevention ( idx );
            if(safetyCheck()){
                for (int i = 0; i < waitingProcess.size(); i++) {

                    this.request(waitingProcess.get(i), waitingRequest.get(i));
                }
            }
            if(zeroAllocationCheck())
            {
                System.out.println("Recovery Failed");
                break;
            }
        }
    }

}

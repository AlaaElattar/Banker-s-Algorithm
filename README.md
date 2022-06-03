# Banker's_Algorithm
**Resource allocation and deadlock avoidance algorithm that tests for safety for operating systems by simulating the allocation for predetermined maximum possible amounts of all resources, then makes safe state check to test for possible activities, before deciding whether allocation should be allowed to continue.**

## The system implements 4 main functions:
1. Request 
2. Release
3. Recover
4. Quit

**If any request led to deadlock, recovery algorithm will be applied by choosing a victim then force it to release resources. It will keep checking until system enters safe state again.**

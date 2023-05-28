import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

public class Scheduler
{

    private Assignment[] assignmentArray;
    private Integer[] C;// This array stores the index of the last compatible assignment for each assignment in above array.
    private Double[] max;  // Stores the value of optimal solutions for each assignment
    private ArrayList<Assignment> solutionDynamic;  // Stores appropriate assignments for optimal dynamic solution.
    private ArrayList<Assignment> solutionGreedy; // Stores appropriate assignments for optimal greedy solution.

    public Scheduler(Assignment[] assignmentArray) throws IllegalArgumentException
    {
        // Should be instantiated with an Assignment array
        // All the properties of this class will be initialized here
        if(assignmentArray.length == 0)
        {
            throw new IllegalArgumentException("Array is Empty"); // Empty json array will not be accepted
        }
        else
            {
            this.solutionDynamic = new ArrayList<Assignment>();
            this.solutionGreedy = new ArrayList<Assignment>();
            this.assignmentArray = assignmentArray;
            Arrays.sort(this.assignmentArray); // From now on,our assignment array is sorted by finish time of assignments.
            this.C = new Integer[this.assignmentArray.length];
            this.max = new Double[this.assignmentArray.length];
            calculateC(); //Calculation of the compatible items for each assignment.
            calculateMax(this.assignmentArray.length-1);
            findSolutionDynamic(this.assignmentArray.length-1);  // Solution will be in reverse order.
            Stack<Assignment> stack = new Stack<Assignment>();
            for(Assignment a : solutionDynamic)
            {
                stack.push(a);
            }
            int index = 0;
            while(!stack.isEmpty())
            {
                solutionDynamic.set(index,stack.pop()); // Reversing the solution by using the LIFO property of stack.
                index++;
            }
        }
    }

    private int binarySearch(int index)
    {
        //This function is used to find the first compatible assignment before the assignment which is at assignmentArray[index] position.
        if(index == 0)
        {   //First assignment can never have a compatible job.
            return -1;   //Means no compatible job.
        }
        String start = assignmentArray[index].getStartTime(); //We will use this while comparing start and finish times.
        int low = 0;
        int high = index-1;  //We search from the last compatible assignment before the specified assignment so high value is less than index.
        int lastCompatible = -1;
        while (true)
        {
            int mid = (low+high)/2;   //We use binary search instead of linear search.
            if(assignmentArray[mid].getFinishTime().compareTo(start) <= 0) //Means compatible but not enough.
            {
                lastCompatible = mid;
                low = mid+1; //We must continue searching to find the last compatible assignment.
            }
            else{
                high= mid-1; //We will continue searching on the left side of the mid.
            }
            if(low>high)
            {   //We do not need to search anymore.
                break;
            }
        }
        return lastCompatible;
    }
    private void calculateC() {
        //This function fills the C.
        for(int i=0;i<this.assignmentArray.length;i++){
            C[i] = binarySearch(i);
        }
    }
    public ArrayList<Assignment> scheduleDynamic() { return this.solutionDynamic;}
    private void findSolutionDynamic(int i)
    {
        if(i==-1)
        {   // Base case
            return;
        }
        System.out.println("findSolutionDynamic("+i+")");
        boolean control = (C[i] == -1);  // Control for staying in bound.
        if(!control) {
            if (assignmentArray[i].getWeight() + max[C[i]] >= max[i - 1])
            {
                /*If we combine the weight of the assignment i and maximum weights that can be produced until
                the last compatible assignment with i,we can decide if i is in our solution or not by comparing the result
                with the maximum weights that can be produced until assignment i(max[i-1]).*/

                System.out.println("Adding " + assignmentArray[i] + " to the dynamic schedule.");
                solutionDynamic.add(assignmentArray[i]);
                findSolutionDynamic(C[i]); // We must continue checking for only compatible assignments with i.

            } else
                {
                findSolutionDynamic(i - 1); //We must continue checking for remaining elements.
                }
        }
        else
            {
            if(i==0)
            {
                // If this function is called by 0 , we only need to add corresponding assignment to the solution.
                System.out.println("Adding " + assignmentArray[0] + " to the dynamic schedule.");
                solutionDynamic.add(assignmentArray[0]);
            }
            else
                {

                if (assignmentArray[i].getWeight() >= max[i - 1])  // Just to check.
                {
                    System.out.println("Adding " + assignmentArray[i] + " to the dynamic schedule.");
                    solutionDynamic.add(assignmentArray[i]);

                } else
                    {
                    findSolutionDynamic(i - 1); //We must continue checking for remaining elements.
                    }

                }
        }
    }
    private Double calculateMax(int i)
    {
        /*This function compares two different cases of each assignment object which is at position[index] in
          the assignment array.
          Case 1 : assignmentArray[i] is in the solution
          Case 2 : assignmentArray[i] is not in the solution
          Our function calculates both cases and choose the optimal one and store the solution in the max array.
         */
        boolean control = (i==0);   // If i==0,our console output will be changed.
        if(i==-1)
        {   //For first assignment,max solution will always be equal to its weight.
            return 0d;
        }
        if(max[i] != null)  // Means we have already calculated the solution and store it in max array.
        {
            /*Initially,all elements in the max array are null.However,after some calculations,we will
             start storing our results in the max array and we will not need to recalculate same values
             again.We will just return the stored one if it is already calculated.*/
            if(control)
            {
                System.out.println("calculateMax(0): Zero");
            }
            else
                {
                System.out.println("calculateMax("+i+"): Present");
                }
            return max[i];
        }
        if(control)
        {
            System.out.println("calculateMax(0): Zero");
        }
        else
            {
            System.out.println("calculateMax("+i+"): Prepare");
            }

        /*From this line,both cases will be evaluated recursively and for each assignment,
         the case which has optimal result will be assigned to the max array using bottom-up approach.
         */

        this.max[i] = Math.max(this.assignmentArray[i].getWeight() + calculateMax(this.C[i]),calculateMax(i-1));
        return this.max[i];
    }

    public ArrayList<Assignment> scheduleGreedy()
    {
        //According to the greedy approach,first element(index 0) will always be selected.
        int lastSelected = 0;
        ArrayList<Assignment> greedySchedule = new ArrayList<Assignment>();
        for(int i=0;i<assignmentArray.length;i++)
        {
            if(i==0)
            {
                System.out.println("Adding "+assignmentArray[i]+" to the greedy schedule.");
                greedySchedule.add(assignmentArray[0]); // No need to update lastSelected.It is already 0.
            }
            else
                {
                    // If they are compatible ....
                if(assignmentArray[i].getStartTime().compareTo(greedySchedule.get(lastSelected).getFinishTime()) >=0)
                {
                    System.out.println("Adding "+assignmentArray[i]+" to the greedy schedule.");
                    greedySchedule.add(assignmentArray[i]);
                    lastSelected++;  //Updating last selecting to use in next steps.
                }
            }
        }
        this.solutionGreedy = greedySchedule;
        return greedySchedule;
    }
}


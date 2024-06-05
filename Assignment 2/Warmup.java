

public class Warmup {
    public static int backtrackingSearch(int[] arr, int x, int forward, int back, Stack myStack) {
        int c=1;
        boolean isFound = false;
        for(int i=0;i<arr.length & !isFound;i++) {
        	if(arr[i] == x) {
        		return i;
        	}
        	myStack.push(arr[i]);
        	if(c==forward) {
        		c=1;
        		i=i+1-back;
        		for(int j=0;j<back & !myStack.isEmpty();j++)	
        			myStack.pop();
        	}
        	else {
        		c++;
        	}
        }
    	return -1;
    }

    public static int consistentBinSearch(int[] arr, int x, Stack myStack) {
    	int left=0, right=arr.length-1, mid=(left+right)/2;
    	while(left<=right) {
    		myStack.push(mid);
    		myStack.push(arr[mid]);
    		if(arr[mid]==x) {
    			return mid;
    		}
        	int inconsistencies = Consistency.isConsistent(arr);
    		if(inconsistencies !=0) {
    			for(int i=0;i<=inconsistencies & !myStack.isEmpty();i++){
    				mid=(int)myStack.pop();
    				myStack.pop();
    			}
    		}
    		if(arr[mid]<x)
    			left=mid+1;
    		else
    			right=mid-1;
    		mid=(left+right)/2;
    	}
    	return -1;
    }
}

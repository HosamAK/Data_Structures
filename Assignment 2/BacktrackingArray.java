
public class BacktrackingArray implements Array<Integer>, Backtrack {
	private Stack stack;
	private int[] arr;
	private int curr;//indication for the next available place

	// Do not change the constructor's signature
	public BacktrackingArray(Stack stack, int size) {
		this.stack = stack;
		arr = new int[size];
		curr = 0;
	}

	@Override
	public Integer get(int index) {
		if (index < 0 | index >= curr)
			throw new IllegalArgumentException("index out of bounds");
		return arr[index];
	}

	@Override
	public Integer search(int k) {
		if (curr > 0) {
			for (int i = 0; i < curr; i++)
				if (arr[i] == k)
					return i;
		}
		return -1;
	}

	@Override
	public void insert(Integer x) {
		if (curr >= arr.length)
			throw new IllegalArgumentException("array is full");
		//backtrack values
		arr[curr] = x;
		stack.push(arr[curr]);
		stack.push(curr);
		stack.push(1);// 1 is an indication that we inserted a number
		curr++;
	}

	@Override
	public void delete(Integer index) {
		if (index <= 0 | index >= curr)
			throw new IllegalArgumentException("index out of bounds");
		if (curr == 0)
			throw new IllegalArgumentException("array is empty");
		//backtrack values
		stack.push(arr[index]);
		stack.push(index);
		stack.push(-1);// -1 is an indication that we deleted a number
		if (index < curr - 1) {
			for (int i = index + 1; i < curr; i++)//sort the array after deleting the given value
				arr[i - 1] = arr[i];
		}
		curr--;
	}

	@Override
	public Integer minimum() {
		if (curr == 0)
			throw new IllegalArgumentException("array is empty");
		int min = 0;
		for (int i = 0; i < curr; i++)
			if (arr[i] < arr[min])
				min = i;
		return min;
	}

	@Override
	public Integer maximum() {
		if (curr == 0)
			throw new IllegalArgumentException("array is empty");
		int max = 0;
		for (int i = 0; i < curr; i++)
			if (arr[i] > arr[max])
				max = i;
		return max;
	}

	@Override
	public Integer successor(Integer index) {
		if (index < 0 | index >= curr)
			throw new IllegalArgumentException("index out of bounds");
		if (curr <= 0)
			throw new IllegalArgumentException("array is empty");
		int suc = -1;//temporary value
		for (int i = 0; i < curr & (suc == -1); i++)//search for the first bigger number of the wanted number
			if (arr[i] > arr[index])
				suc = i;
		if (suc == -1)//if we don't find a bigger number than the given number there is no successor
			throw new IllegalArgumentException("element does not exist");
		for (int i = suc; i < curr; i++)//search for the successor in the array
			if (arr[i] < arr[suc] & arr[i] > arr[index])
				suc = i;
		return suc;
	}

	@Override
	public Integer predecessor(Integer index) {
		if (index < 0 | index >= curr)
			throw new IllegalArgumentException("index out of bounds");
		if (curr <= 0)
			throw new IllegalArgumentException("array is empty");
		int pre = -1;//temporary value
		for (int i = 0; i < curr & (pre == -1); i++)//search for the first smaller number of the wanted number
			if (arr[i] < arr[index])
				pre = i;
		if (pre == -1)//if we don't find a smaller number than the given number there is no predecessor
			throw new IllegalArgumentException("element does not exist");

		for (int i = pre; i < curr; i++)//search for the predecessor in the array
			if (arr[i] > arr[pre] & arr[i] < arr[index])
				pre = i;
		return pre;
	}

	@Override
	public void backtrack() {
		if (!stack.isEmpty()) {
			int in = (int) stack.pop();
			int index = (int) stack.pop();
			int value = (int) stack.pop();
			if (in == 1) {//backtrack insertion
				curr--;
			} else {//backtrack deletion
				for (int i = curr; i > index; i--) {
					arr[i] = arr[i - 1];
				}
				arr[index] = value;
				curr++;
			}
		}
	}

	@Override
	public void retrack() {
		/////////////////////////////////////
		// Do not implement anything here! //
		/////////////////////////////////////
	}

	@Override
	public void print() {
		String output = "";
		for (int i = 0; i < curr; i++) {
			if (i == curr - 1)
				output += arr[i];
			else
				output += arr[i] + " ";
		}
		System.out.println(output);
	}
}

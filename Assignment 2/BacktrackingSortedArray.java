
public class BacktrackingSortedArray implements Array<Integer>, Backtrack {
	private Stack stack;
	public int[] arr; // This field is public for grading purposes. By coding conventions and best
						// practice it should be private.
	private int curr;//indication for the next available place

	// Do not change the constructor's signature
	public BacktrackingSortedArray(Stack stack, int size) {
		this.stack = stack;
		arr = new int[size];
		curr = 0;
	}

	@Override
	public Integer get(int index) {
		if (index < 0 & index >= curr)
			throw new IllegalArgumentException("index out of bounds");
		return arr[index];
	}

	@Override
	public Integer search(int k) {
		// binary search
		if (curr > 0) {
			int left = 0;
			int right = curr - 1;
			while (left <= right) {
				int mid = (left + right) / 2;
				if (arr[mid] == k)
					return mid;
				if (arr[mid] > k)
					right = mid - 1;
				else
					left = mid + 1;
			}
		}
		return -1;
	}

	@Override
	public void insert(Integer x) {
		if (curr >= arr.length)
			throw new IllegalArgumentException("array is full");
		int place;
		for (place = 0; place < curr; place++)//find the right place for the given value to insert
			if (arr[place] > x)
				break;
		for (int i = curr - 1; place <= i; i--) {//empty the place to insert the given value
			arr[i + 1] = arr[i];
		}
		//backtrack values
		arr[place] = x;
		stack.push(arr[place]);
		stack.push(place);
		stack.push(1);// 1 is an indication that we inserted a number
		curr++;
	}

	@Override
	public void delete(Integer index) {
		if (index < 0 | index >= curr)
			throw new IllegalArgumentException("index out of bounds");
		if (curr == 0)
			throw new IllegalArgumentException("array is empty");
		//backtrack values
		stack.push(arr[index]);
		stack.push(index);
		stack.push(-1);// -1 is an indication that we deleted a number
		if (index < curr - 1) {//sort the array after deleting the given value
			for (int i = index + 1; i < curr; i++)
				arr[i - 1] = arr[i];
		}
		curr--;
	}

	@Override
	public Integer minimum() {
		if (curr == 0)
			throw new IllegalArgumentException("element does not exist");
		return 0;
	}

	@Override
	public Integer maximum() {
		if (curr == 0)
			throw new IllegalArgumentException("element does not exist");
		return curr - 1;
	}

	@Override
	public Integer successor(Integer index) {
		if (curr <= 1 | index == curr - 1)
			throw new IllegalArgumentException("element does not exist");
		if (index < 0 | index >= curr)
			throw new IllegalArgumentException("index out of bounds");
		return index + 1;
	}

	@Override
	public Integer predecessor(Integer index) {
		if (curr <= 1 | index == 0)
			throw new IllegalArgumentException("element does not exist");
		if (index < 0 | index >= curr)
			throw new IllegalArgumentException("index out of bounds");
		return index - 1;
	}

	@Override
	public void backtrack() {
		if (!stack.isEmpty()) {
			int in = (int) stack.pop();
			int index = (int) stack.pop();
			int value = (int) stack.pop();
			if (in == 1) {//backtrack insertion
				for (int i = index; i < curr - 1; i++) {
					arr[i] = arr[i + 1];
				}
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


import java.util.NoSuchElementException;

public class BacktrackingBST implements Backtrack, ADTSet<BacktrackingBST.Node> {
	private Stack stack;
	private Stack redoStack;
	private BacktrackingBST.Node root = null;

	// Do not change the constructor's signature
	public BacktrackingBST(Stack stack, Stack redoStack) {
		this.stack = stack;
		this.redoStack = redoStack;
	}

	public Node getRoot() {
		if (root == null) {
			throw new NoSuchElementException("empty tree has no root");
		}
		return root;
	}

	public Node search(int k) {
		Node n = root;
		while (n != null) {//iterative search for the wanted node
			if ((int) n.getKey() == k)
				return n;
			else if ((int) n.getKey() > k)
				n = n.left;
			else
				n = n.right;
		}
		return null;
	}

	public void insert(Node node) {
		if (node == null || search(node.getKey()) != null)
			throw new IllegalArgumentException("can not insert null");
		if (root == null) {//when tree is empty the inserted node is the root
			root = node;
		} else {
			Node n = root;
			Node p = null;
			while (n != null) {//insert the value in the right place as a leaf
				p = n;
				if (node.getKey() < n.getKey())
					n = n.left;
				else
					n = n.right;
			}
			if (node.getKey() < p.getKey())
				p.left = node;
			else
				p.right = node;
			node.parent = p;
		}
		//backtrack values
		stack.push(node.parent);
		stack.push(node);
		stack.push(1);// 1 is an indication that we inserted a node
	}

	public void delete(Node node) {
		if (root == null | node == null)
			throw new NoSuchElementException("tree is empty");
		else if (search(node.getKey()) == null)
			throw new NoSuchElementException("element does not exist");

		// leaf node
		else if (node.right == null & node.left == null) {
			//backtrack values
			stack.push(node.parent);
			stack.push(node);
			// delete leaf node from parent
			if (node.parent.left != null && node.parent.left.getKey() == node.getKey()) {
				node.parent.left = null;
				stack.push("Left");//the deleted node is in the left subtree of the parent
			} else {
				node.parent.right = null;
				stack.push("Right");//the deleted node is in the right subtree of the parent
			}
			stack.push(-1);// -1 is an indication that we deleted a leaf node
		}
		// node with one child
		// right child
		else if (node.left == null & node.right != null) {
			//backtrack values
			stack.push(node.parent);
			stack.push(node.right);
			stack.push(node);
			if (node.parent.right != null && node.parent.right.getKey() == node.getKey()) {
				node.parent.right = node.right;
				stack.push("Right");//the deleted node is in the right subtree of the parent
			} else {
				node.parent.left = node.right;
				stack.push("Left");//the deleted node is in the left subtree of the parent
			}
			stack.push(-2);// -2 is an indication that we deleted a node with only right child
		}
		// left child
		else if (node.left != null & node.right == null) {
			//backtrack values
			stack.push(node.parent);
			stack.push(node.left);
			stack.push(node);
			if (node.parent.left != null && node.parent.left.getKey() == node.getKey()) {
				node.parent.left = node.left;
				stack.push("Left");//the deleted node is in the left subtree of the parent
			} else {
				node.parent.right = node.left;
				stack.push("Right");//the deleted node is in the right subtree of the parent
			}
			stack.push(-3);// -3 is an indication that we deleted a node with only left child
		}
		// node with two children
		else {
			Node suc = successor(node);
			delete(suc);
			//backtrack values
			stack.push(node.parent);
			stack.push(node);
			suc.parent = node.parent;
			suc.left = node.left;
			suc.right = node.right;
			if (node.parent == null) {
				root = suc;
				stack.push("root");
			} else if (node.parent.left != null && node.parent.left.getKey() == node.getKey()) {
				node.parent.left = suc;
				stack.push("Left");//the deleted node is in the left subtree of the parent
			} else if (node.parent.right != null && node.parent.right.getKey() == node.getKey()) {
				node.parent.right = suc;
				stack.push("Right");//the deleted node is in the right subtree of the parent
			}
			stack.push(-4);// -4 is an indication that we deleted a node with two children
		}
	}

	public Node minimum() {
		if (root == null)
			throw new NoSuchElementException("tree is empty");
		Node min = root;
		while (min.left != null)
			min = min.left;
		return min;
	}

	public Node maximum() {
		if (root == null)
			throw new NoSuchElementException("tree is empty");
		Node max = root;
		while (max.right != null)
			max = max.right;
		return max;
	}

	public Node successor(Node node) {
		if (node == null || search(node.getKey()) == null)
			throw new NoSuchElementException("successor does not exist");
		Node suc = node;
		if (suc.right != null) {// successor is the minimum in left subtree of given node
			suc = suc.right;
			while (suc.left != null)// search minimum node in left subtree
				suc = suc.left;
			return suc;
		}
		Node p = node.parent;
		while (suc.parent != null && suc.getKey() > p.getKey()) {// search successor in parents
			suc = p;
			p = p.parent;
		}
		if (suc.key == node.key)// successor does not exist
			throw new IllegalArgumentException("successor does not exist");
		return p;
	}

	public Node predecessor(Node node) {
		if (node == null || search(node.getKey()) == null)
			throw new NoSuchElementException("predecessor does not exist");
		Node pre = node;
		if (pre.left != null) {// predecessor is the maximum in right subtree of given node
			pre = pre.left;
			while (pre.right != null)// search maximum node in right subtree
				pre = pre.right;
			return pre;
		}
		Node p = node.parent;
		while (pre.parent != null && pre.getKey() < p.getKey()) {// search predecessor in parents
			pre = p;
			p = p.parent;
		}
		if (pre.getKey() == node.getKey())// successor does not exist
			throw new IllegalArgumentException("predecessor does not exist");
		return pre;
	}

	@Override
	public void backtrack() {
		if (!stack.isEmpty()) {
			int ind = (int) stack.pop();// the indication for the last delete or insert
			if (ind == 1) {//backtrack insertion
				Node node = (Node) stack.pop();
				Node nodepar = (Node) stack.pop();
				if (nodepar.left != null && nodepar.left.getKey() == node.getKey()) {
					nodepar.left = null;
				} else {
					nodepar.right = null;
				}
				//retrack values
				redoStack.push(node);
				redoStack.push(1);//1 is an indication that we backtrack insertion

			} else if (ind == -1) {//backtrack deletion of leaf node
				String s = (String) stack.pop();
				Node node = (Node) stack.pop();
				Node nodepar = (Node) stack.pop();
				if (s.equals("Left")) {
					nodepar.left = node;
				} else {
					nodepar.right = node;
				}
				//retrack values
				redoStack.push(node);
				redoStack.push(2);//2 is an indication that we backtrack deletion
			} else if (ind == -2) {//backtrack deletion of node that has only right child
				String s = (String) stack.pop();
				Node node = (Node) stack.pop();
				Node noder = (Node) stack.pop();
				Node nodepar = (Node) stack.pop();
				node.parent = nodepar;
				node.right = noder;
				noder.parent = node;
				if (s.equals("Right"))
					nodepar.right = node;
				else
					nodepar.left = node;
				//retrack values
				redoStack.push(node);
				redoStack.push(2);//2 is an indication that we backtrack deletion
			} else if (ind == -3) {//backtrack deletion of node that has only left child
				String s = (String) stack.pop();
				Node node = (Node) stack.pop();
				Node noder = (Node) stack.pop();
				Node nodepar = (Node) stack.pop();
				node.parent = nodepar;
				node.left = noder;
				noder.parent = node;
				if (s.equals("Right"))
					nodepar.right = node;
				else
					nodepar.left = node;
				//retrack values
				redoStack.push(node);
				redoStack.push(2);//2 is an indication that we backtrack deletion
			} else if (ind == -4) {//backtrack deletion of node that has both children
				String s = (String) stack.pop();
				Node node = (Node) stack.pop();
				Node nodepar = (Node) stack.pop();
				if (s.equals("root")) {
					root = node;
				} else if (s.equals("Left")) {
					nodepar.left = node;
				} else {
					nodepar.right = node;
				}
				//retrack values
				redoStack.push(node);
				redoStack.push(2);//2 is an indication that we backtrack deletion
				backtrack();//backtrack the deleted node
			}
		}
	}

	@Override
	public void retrack() {
		if (!redoStack.isEmpty()) {
			int op = (int) redoStack.pop();
			if (op == 1) {//retrack backtracked insertion
				Node node = (Node) redoStack.pop();
				delete(node);
			} else if (op == 2) {//retrack backtracked deletion
				Node node = (Node) redoStack.pop();
				insert(node);
			}
		}
	}

	public void printPreOrder() {
		if (root == null)
			return;
		String output = "";
		Stack s = new Stack();
		s.push(root);
		while (!s.isEmpty()) {
			Node n = (Node) s.pop();
			output += n.getKey() + " ";
			if (n.right != null)
				s.push(n.right);
			if (n.left != null)
				s.push(n.left);
		}
		output = output.substring(0, output.length() - 1);
		System.out.println(output);
	}

	@Override
	public void print() {
		printPreOrder();
	}
	
	public static class Node {
		// These fields are public for grading purposes. By coding conventions and best
		// practice they should be private.
		public BacktrackingBST.Node left;
		public BacktrackingBST.Node right;

		private BacktrackingBST.Node parent;
		private int key;
		private Object value;

		public Node(int key, Object value) {
			this.key = key;
			this.value = value;
		}

		public int getKey() {
			return key;
		}

		public Object getValue() {
			return value;
		}
	}
}

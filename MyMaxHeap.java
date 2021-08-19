
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class MyMaxHeap<K extends Comparable<K>,T>{

	protected MyBSTreeNode<K,T> root;
	private int size;

	public MyMaxHeap(){
		this.root = null;
		this.size = 0;
	}

	public int getSize() {
		return this.size;
	}

	public MyBSTreeNode<K,T> peek(){
		return this.root;
	}

	public boolean isEmpty() {
		if (this.root == null) {
			return true;
		}
		else return false;
	}

	public void insert(K newKey, T newPayload) {

		if (this.isEmpty() == true) { // the heap is empty
			MyBSTreeNode<K,T> newNode = new MyBSTreeNode<K,T>(newKey, newPayload);
			this.root = newNode;
			size++;
			return;
		}


		else { // the heap is not empty
			Stack<Integer> path = this.nextNullPath();

			MyBSTreeNode<K,T> current = this.root;

			while (path.isEmpty() == false) {

				if(path.peek() == 0) {
					if(current.getLeft() != null) {
						current.getLeft().setParent(current);
						current = current.getLeft();
					}
				}

				else {
					if(current.getRight() != null) {
						current.getRight().setParent(current);
						current = current.getRight();
					}
				}
			//	System.out.println(path.peek());
				path.pop();
			}

		//	System.out.println(current);
			MyBSTreeNode<K,T> newNode = new MyBSTreeNode<K,T>(newKey, newPayload);

			if (current.getLeft() != null) { //left exists, add right
				current.setRight(newNode);
				current.getRight().setParent(current);
				current = current.getRight();
			}
			else {
				current.setLeft(newNode);
				current.getLeft().setParent(current);
				current = current.getLeft();
			}

		//	System.out.println("The node just inserted is " +current);

		//	System.out.println("Its parent is " + current.getParent());


			//now that the node is in the heap, restore heap property

			MyBSTreeNode<K,T> temp = new MyBSTreeNode<K,T>();

			while(current.getParent() != null && current.getKey().compareTo(current.getParent().getKey()) > 0) {
				temp.setKey(current.getKey());
				temp.setPayload(current.getPayload()); //store current's info in temp

				current.setKey(current.getParent().getKey()); //set current to same info as its parent
				current.setPayload(current.getParent().getPayload());

				current.getParent().setKey(temp.getKey()); //set the parent to current's old info stored in temp
				current.getParent().setPayload(temp.getPayload());

				current = current.getParent(); // move up the heap

			}

			this.size++;
			return;
		}
	}

	public MyBSTreeNode<K,T> top(){

		if(this.isEmpty() == true) { //empty heap
			return null;
		}

		else if (this.size == 1) { // only root exists
			MyBSTreeNode<K,T> tempRoot = new MyBSTreeNode<K,T>(this.root.getKey(), this.root.getPayload());
			this.root = null;
			size--;
			return tempRoot;
		}

		else {
			MyBSTreeNode<K,T> tempRoot = new MyBSTreeNode<K,T>(this.root.getKey(), this.root.getPayload());
			Stack<Integer> path = this.lastNodePath();
			MyBSTreeNode<K,T> current = this.root;

			while (path.isEmpty() == false) {

				if(path.peek() == 0) {
					current = current.getLeft();
				}

				else {
					current = current.getRight();
				}
				path.pop();
			}

		//	System.out.println("The last node is " + current);

			//current is now equal to the node to be deleted.
			//put this data into the root

			this.root.setKey(current.getKey());
			this.root.setPayload(current.getPayload());

		//	System.out.println("The root node is now " + this.root);

			//sever all connections of current

			if(current.getParent().getLeft().getKey().compareTo(current.getKey()) == 0) { // was left child
				current.getParent().setLeft(null);
				current.setParent(null);
			}

			else { // was right child
				current.getParent().setRight(null);
				current.setParent(null);
			}

			//swap root down until you restore the heap ordering

			MyBSTreeNode<K,T> temp = new MyBSTreeNode<K,T>();
			MyBSTreeNode<K,T> current2 = this.root;

			while ( current2.getLeft() != null ) { //need to swap until max heap condition restored, trickle down heap

				if(current2.getRight() != null && current2.getLeft().getKey().compareTo(current2.getRight().getKey()) < 0
						&& current2.getKey().compareTo(current2.getRight().getKey()) < 0) { // left is less than right, swap right

					temp.setKey(current2.getKey()); // store current info in temp
					temp.setPayload(current2.getPayload());

					current2.setKey(current2.getRight().getKey()); //put right info in current
					current2.setPayload(current2.getRight().getPayload());

					current2.getRight().setKey(temp.getKey());//set right to old current from temp
					current2.getRight().setPayload(temp.getPayload());

					current2 = current2.getRight();
				}

				else if ( current2.getKey().compareTo(current2.getLeft().getKey()) < 0 ) { // swap left
					temp.setKey(current2.getKey()); // store current info in temp
					temp.setPayload(current2.getPayload());

					current2.setKey(current2.getLeft().getKey()); //put left info in current
					current2.setPayload(current2.getLeft().getPayload());

					current2.getLeft().setKey(temp.getKey()); //set left to old current from temp
					current2.getLeft().setPayload(temp.getPayload());

					current2 = current2.getLeft();

				}

				else {
					current2 = current2.getLeft();
				}
			}

			this.size--;
			return tempRoot;
		}
	}

	private Stack<Integer> lastNodePath() {

		Stack<Integer> s = new Stack<Integer>();

		int n = this.size;

		while (n > 1) {
			if (n % 2 == 0) {
				s.push(0); // go left
			}
			else {
				s.push(1); //go right
			}
			n = n/2;
		}

		return s;
	}

	private Stack<Integer> nextNullPath() {

		Stack<Integer> s = new Stack<Integer>();

		int n = this.size + 1;

		while (n > 1) {
			if (n % 2 == 0) {
				s.push(0); // go left
			}
			else {
				s.push(1); //go right
			}
			n = n/2;
		}

		return s;
	}

	public void pathCheck() {
		Stack<Integer> last = this.lastNodePath();
		Stack<Integer> next = this.nextNullPath();

		System.out.print("Last ");
		while (last.isEmpty() == false) {
			System.out.print(last.peek());
			last.pop();
		}

		System.out.print("  ");

		System.out.print("Next ");
		while (next.isEmpty() == false) {
			System.out.print(next.peek());
			next.pop();
		}

		return;
	}

	public void levelOrder() {
		levelOrderRecursive(this.root);
		System.out.println();
	}

	private void levelOrderRecursive(MyBSTreeNode<K,T> node) {
		if (node != null) {

			Queue<MyBSTreeNode<K,T>> q = new LinkedList<MyBSTreeNode<K,T>>();

			q.add(node);

			while(q.isEmpty() == false) {
				MyBSTreeNode<K,T> current = q.remove();
				if (current != null) {
					System.out.print(current.getKey() + " ");
					q.add(current.getLeft());
					q.add(current.getRight());
				}
			}
		}
	}
}

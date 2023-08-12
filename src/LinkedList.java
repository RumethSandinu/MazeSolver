class LinkedList {
    Node head;

    public void insert(String data) {
        Node new_node = new Node(data);
        if (head == null) {
            head = new_node;
        } else {
            Node last = head;
            while (last.next != null) {
                last = last.next;
            }
            last.next = new_node;
        }
    }
    public void removeFromFront() {
        if (head == null) {
            System.out.println("\u001B[31m[E]\u001B[39m Linked list is empty.Cannot remove");
            return;
        }

        head = head.next;
    }

    
}


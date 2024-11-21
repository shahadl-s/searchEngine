public class BSTIndex implements InvertedIndex {
    private BSTNode root;

    private static class BSTNode {
        String key;
        String[] documents;
        BSTNode left, right;

        public BSTNode(String key, String documentId) {
            this.key = key;
            this.documents = new String[100];
            this.documents[0] = documentId;
        }

        public void addDocument(String documentId) {
            for (int i = 0; i < documents.length; i++) {
                if (documents[i] == null) {
                    documents[i] = documentId;
                    return;
                }
            }
        }

        public String[] getDocuments() {
            return documents;
        }
    }

    @Override
    public void add(String term, String documentId) {
        root = addRecursive(root, term, documentId);
    }

    private BSTNode addRecursive(BSTNode node, String term, String documentId) {
        if (node == null) {
            return new BSTNode(term, documentId);
        }
        if (term.compareTo(node.key) < 0) {
            node.left = addRecursive(node.left, term, documentId);
        } else if (term.compareTo(node.key) > 0) {
            node.right = addRecursive(node.right, term, documentId);
        } else {
            node.addDocument(documentId);
        }
        return node;
    }

    @Override
    public String[] getDocuments(String term) {
        BSTNode node = search(root, term);
        return node != null ? node.getDocuments() : new String[0];
    }

    private BSTNode search(BSTNode node, String term) {
        if (node == null || node.key.equals(term)) {
            return node;
        }
        return term.compareTo(node.key) < 0 ? search(node.left, term) : search(node.right, term);
    }

    @Override
    public void display() {
        displayInOrder(root);
    }

    private void displayInOrder(BSTNode node) {
        if (node != null) {
            displayInOrder(node.left);
            System.out.println(node.key + ": " + String.join(", ", node.getDocuments()));
            displayInOrder(node.right);
        }
    }
}

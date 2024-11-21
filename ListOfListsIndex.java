public class ListOfListsIndex implements InvertedIndex {
    private String[][] invertedIndex;

    public ListOfListsIndex() {
        invertedIndex = new String[1000][2]; // Fixed size for simplicity
    }

    @Override
    public void add(String term, String documentId) {
        for (int i = 0; i < invertedIndex.length; i++) {
            if (invertedIndex[i][0] == null) {
                invertedIndex[i][0] = term;
                invertedIndex[i][1] = documentId;
                return;
            } else if (invertedIndex[i][0].equals(term)) {
                if (!invertedIndex[i][1].contains(documentId)) {
                    invertedIndex[i][1] += "," + documentId;
                }
                return;
            }
        }
    }

    @Override
    public String[] getDocuments(String term) {
        for (int i = 0; i < invertedIndex.length; i++) {
            if (invertedIndex[i][0] != null && invertedIndex[i][0].equals(term)) {
                return invertedIndex[i][1].split(",");
            }
        }
        return new String[0];
    }

    @Override
    public void display() {
        System.out.println("Inverted Index (List of Lists):");
        for (int i = 0; i < invertedIndex.length; i++) {
            if (invertedIndex[i][0] != null) {
                System.out.println(invertedIndex[i][0] + ": " + invertedIndex[i][1]);
            }
        }
    }
}

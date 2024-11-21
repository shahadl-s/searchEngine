public interface InvertedIndex {
    void add(String term, String documentId);
    String[] getDocuments(String term);
    void display();
}

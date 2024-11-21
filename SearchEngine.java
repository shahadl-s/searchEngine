import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class SearchEngine {
    private InvertedIndex invertedIndex;
    private String[] stopWords;

    public SearchEngine(boolean useListOfLists) {
        this.invertedIndex = useListOfLists ? new ListOfListsIndex() : new BSTIndex();
        this.stopWords = new String[]{
                "the", "is", "and", "a", "an", "of", "to", "in", "that", "it"
        };
    }

    private boolean isStopWord(String word) {
        for (String stopWord : stopWords) {
            if (stopWord.equals(word)) {
                return true;
            }
        }
        return false;
    }

    public void loadData(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            br.readLine(); // Skip header
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",", 2);
                if (parts.length < 2) continue;

                String documentId = parts[0].trim();
                String content = parts[1].trim().toLowerCase().replaceAll("[^a-zA-Z0-9\\s]", "");
                String[] words = content.split("\\s+");

                for (String word : words) {
                    if (!isStopWord(word) && !word.isEmpty()) {
                        invertedIndex.add(word, documentId);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading data: " + e.getMessage());
        }
    }

    /**
     * Process a ranked query and calculate scores for each document.
     */
    public DocumentScore[] processRankedQuery(String query) {
        String[] queryTerms = query.toLowerCase().split("\\s+");
        DocumentScore[] scores = new DocumentScore[1000];
        int scoreIndex = 0;

        for (String term : queryTerms) {
            if (isStopWord(term) || term.isEmpty()) continue;

            String[] documents = invertedIndex.getDocuments(term);
            for (String docId : documents) {
                if (docId == null) continue;

                // Update score or add a new score entry
                boolean found = false;
                for (int i = 0; i < scoreIndex; i++) {
                    if (scores[i].documentId.equals(docId)) {
                        scores[i].score++;
                        found = true;
                        break;
                    }
                }

                if (!found) {
                    scores[scoreIndex++] = new DocumentScore(docId, 1);
                }
            }
        }

        // Trim and sort scores
        DocumentScore[] result = new DocumentScore[scoreIndex];
        System.arraycopy(scores, 0, result, 0, scoreIndex);
        sortScores(result);
        return result;
    }

    /**
     * Sort the documents by their scores in descending order.
     */
    private void sortScores(DocumentScore[] scores) {
        for (int i = 0; i < scores.length - 1; i++) {
            for (int j = 0; j < scores.length - i - 1; j++) {
                if (scores[j].score < scores[j + 1].score) {
                    DocumentScore temp = scores[j];
                    scores[j] = scores[j + 1];
                    scores[j + 1] = temp;
                }
            }
        }
    }

    public void displayInvertedIndex() {
        invertedIndex.display();
    }

    /**
     * DocumentScore class to hold document ID and its score.
     */
    public static class DocumentScore {
        String documentId;
        int score;

        public DocumentScore(String documentId, int score) {
            this.documentId = documentId;
            this.score = score;
        }

        @Override
        public String toString() {
            return "DocumentID: " + documentId + ", Score: " + score;
        }
    }
}

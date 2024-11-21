import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SearchEngineGUI {

    private JPanel panel1;
    private JTextField searchIn;        // Word entered by user
    private JTextArea searchOut;       // Output display for search results
    private JButton searchButton;      // Button to trigger search
    private JComboBox<String> modeSelect; // Dropdown to select List of Lists or BST
    private SearchEngine searchEngine; // SearchEngine instance

    public SearchEngineGUI() {
        // Initialize the GUI components
        JFrame frame = new JFrame("Search Engine");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        panel1 = new JPanel(new BorderLayout());

        // Search Input Field
        searchIn = new JTextField();
        searchIn.setFont(new Font("Arial", Font.PLAIN, 18));
        panel1.add(searchIn, BorderLayout.NORTH);

        // Search Output Area
        searchOut = new JTextArea();
        searchOut.setFont(new Font("Arial", Font.PLAIN, 16));
        searchOut.setEditable(false);
        panel1.add(new JScrollPane(searchOut), BorderLayout.CENTER);

        // Mode Selector Dropdown
        modeSelect = new JComboBox<>(new String[]{"List of Lists", "BST"});
        modeSelect.setFont(new Font("Arial", Font.PLAIN, 16));
        modeSelect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                initializeSearchEngine(); // Switch between implementations
            }
        });

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(searchIn, BorderLayout.CENTER);
        topPanel.add(modeSelect, BorderLayout.EAST);
        panel1.add(topPanel, BorderLayout.NORTH);

        // Search Button
        searchButton = new JButton("Search");
        searchButton.setFont(new Font("Arial", Font.PLAIN, 18));
        panel1.add(searchButton, BorderLayout.SOUTH);
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performSearch();
            }
        });

        frame.add(panel1);
        frame.setVisible(true);

        // Initialize default search engine (List of Lists)
        initializeSearchEngine();
    }

    /**
     * Initialize the search engine based on the selected mode.
     */
    private void initializeSearchEngine() {
        String mode = (String) modeSelect.getSelectedItem();
        if ("List of Lists".equals(mode)) {
            searchEngine = new SearchEngine(true); // Use List of Lists
        } else if ("BST".equals(mode)) {
            searchEngine = new SearchEngine(false); // Use BST
        }
        searchEngine.loadData("C:\\Users\\sauds\\Downloads\\dataset.csv");
        searchOut.setText("Initialized with " + mode + " implementation.");
    }

    /**
     * Perform the search based on the query entered by the user.
     */
    private void performSearch() {
        String query = searchIn.getText().trim();
        if (!query.isEmpty()) {
            // Call processRankedQuery and handle DocumentScore[]
            SearchEngine.DocumentScore[] results = searchEngine.processRankedQuery(query);
            displayResults(results); // Updated to handle DocumentScore[]
        } else {
            searchOut.setText("Please enter a search query.");
        }
    }

    private void displayResults(SearchEngine.DocumentScore[] results) {
        StringBuilder output = new StringBuilder("Search Results (Ranked):\n");
        if (results.length == 0) {
            output.append("No results found.");
        } else {
            for (SearchEngine.DocumentScore result : results) {
                output.append("Document ID: ").append(result.documentId)
                        .append(", Score: ").append(result.score).append("\n");
            }
        }
        searchOut.setText(output.toString());
    }


    public static void main(String[] args) {
        new SearchEngineGUI();
    }
}

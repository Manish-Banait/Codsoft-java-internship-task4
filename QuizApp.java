import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

// Main class for the Quiz Application
public class QuizApp extends JFrame implements ActionListener {
    private List<Question> questions; // List to hold quiz questions
    private int currentQuestionIndex; // Index to keep track of the current question
    private int score; // User's score
    private JLabel questionLabel; // Label to display questions
    private JRadioButton[] options; // Radio buttons for multiple-choice options
    private ButtonGroup optionsGroup; // Group for radio buttons
    private JButton submitButton; // Button to submit answer
    private JLabel timerLabel; // Label to display timer
    private Timer timer; // Timer for each question
    private int timeRemaining; // Time remaining for the current question

    // Constructor to set up the quiz application
    public QuizApp() {
        questions = new ArrayList<>(); // Initialize the list of questions
        score = 0; // Initialize the score
        currentQuestionIndex = 0; // Start with the first question
        timeRemaining = 10; // Set time limit per question

        // Set up the GUI
        setTitle("Quiz Application");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(6, 1));

        questionLabel = new JLabel(""); // Initialize the question label
        add(questionLabel);

        options = new JRadioButton[4]; // Array for 4 options
        optionsGroup = new ButtonGroup(); // Group for radio buttons
        for (int i = 0; i < 4; i++) {
            options[i] = new JRadioButton();
            optionsGroup.add(options[i]);
            add(options[i]);
        }

        submitButton = new JButton("Submit"); // Initialize the submit button
        submitButton.addActionListener(this);
        add(submitButton);

        timerLabel = new JLabel("Time Remaining: 10 seconds"); // Initialize the timer label
        add(timerLabel);

        loadQuestions(); // Load questions
        displayQuestion(); // Display the first question
        startTimer(); // Start the timer
    }

    // Method to load quiz questions
    private void loadQuestions() {
        questions.add(new Question("What is the capital of France?", new String[]{"Paris", "Rome", "Berlin", "London"}, "Paris"));
        questions.add(new Question("What is the square root of 64?", new String[]{"6", "7", "8", "9"}, "8"));
        questions.add(new Question("What is the largest ocean on Earth?", new String[]{"Atlantic", "Indian", "Arctic", "Pacific"}, "Pacific"));
        questions.add(new Question("What is the chemical symbol for Gold?", new String[]{"Au", "Ag", "Fe", "Pb"}, "Au"));
        questions.add(new Question("How many continents are there?", new String[]{"5", "6", "7", "8"}, "7"));
    }

    // Method to display the current question
    private void displayQuestion() {
        if (currentQuestionIndex < questions.size()) {
            Question q = questions.get(currentQuestionIndex);
            questionLabel.setText(q.getQuestion());

            String[] opts = q.getOptions();
            for (int i = 0; i < 4; i++) {
                options[i].setText(opts[i]);
            }
            optionsGroup.clearSelection();
            timeRemaining = 10;
            timerLabel.setText("Time Remaining: 10 seconds");
        } else {
            showResults(); // Show results if no more questions
        }
    }

    // Method to start the timer for each question
    private void startTimer() {
        timer = new Timer(1000, new ActionListener() { // 1000 ms = 1 second
            @Override
            public void actionPerformed(ActionEvent e) {
                timeRemaining--;
                timerLabel.setText("Time Remaining: " + timeRemaining + " seconds");
                if (timeRemaining <= 0) {
                    timer.stop();
                    handleSubmit();
                }
            }
        });
        timer.start();
    }

    // Method to handle answer submission
    private void handleSubmit() {
        Question currentQuestion = questions.get(currentQuestionIndex);
        String selectedOption = getSelectedOption();

        if (selectedOption != null && selectedOption.equals(currentQuestion.getCorrectAnswer())) {
            score++; // Increment score if the answer is correct
        }

        currentQuestionIndex++; // Move to the next question
        displayQuestion(); // Display the next question
        timer.restart(); // Restart the timer
    }

    // Helper method to get the selected option
    private String getSelectedOption() {
        for (JRadioButton option : options) {
            if (option.isSelected()) {
                return option.getText();
            }
        }
        return null;
    }

    // Action handler for submit button
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == submitButton) {
            timer.stop();
            handleSubmit();
        }
    }

    // Method to show the final results
    private void showResults() {
        JOptionPane.showMessageDialog(this, "Quiz Over!\nYour Score: " + score + "/" + questions.size());
        System.exit(0);
    }

    // Main method to launch the application
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new QuizApp().setVisible(true);
            }
        });
    }
}

// Class to represent a quiz question
class Question {
    private String question; // Question text
    private String[] options; // Options for the question
    private String correctAnswer; // Correct answer for the question

    // Constructor to initialize a question
    public Question(String question, String[] options, String correctAnswer) {
        this.question = question;
        this.options = options;
        this.correctAnswer = correctAnswer;
    }

    // Getter for question text
    public String getQuestion() {
        return question;
    }

    // Getter for options
    public String[] getOptions() {
        return options;
    }

    // Getter for correct answer
    public String getCorrectAnswer() {
        return correctAnswer;
    }
}

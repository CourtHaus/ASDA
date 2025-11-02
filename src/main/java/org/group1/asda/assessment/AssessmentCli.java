package org.group1.asda.assessment;

import java.util.Locale;
import java.util.Scanner;

/**
 * Console (CLI) runner for the 50-question Adolescent AQ.
 * Usage: run the application with --cli to start this mode.
 */
public class AssessmentCli {
    public static int run() {
        Scanner scanner = new Scanner(System.in);
        scanner.useLocale(Locale.US);

        // 1) Welcome and overview
        displayWelcomeMessage();

        // 2) Instructions (read first)
        displayInstructions(scanner);

        // 3) Consent (directly after instructions, before first question)
        if (!getConsentToProceed(scanner)) {
            System.out.println("Assessment cancelled. Thank you for your time.");
            return 0;
        }

        // 4) Conduct questionnaire with back/quit/progress
        AQAssessment assessment = new AQAssessment();
        boolean completed = conductQuestionnaire(scanner, assessment);
        if (!completed) {
            // User quit mid-way
            System.out.println("Assessment cancelled.");
            return 0;
        }

        // 5) Display results
        displayResults(assessment);
        return 0;
    }

    // --- Flow sections ---
    private static void displayWelcomeMessage() {
        System.out.println("Welcome to the Adolescent Autism Spectrum Quotient (AQ)");
        System.out.println();
        System.out.println("IMPORTANT DISCLAIMER:");
        System.out.println("This is the official AQ screening questionnaire developed by");
        System.out.println("S. Baron-Cohen et al. (2006) for identifying autism spectrum traits.");
        System.out.println("It is NOT a diagnostic instrument.");
        System.out.println("Only qualified healthcare professionals can provide a formal diagnosis.");
        System.out.println();
        System.out.println("This assessment consists of 50 questions");
        System.out.println("and should take approximately 10-15 minutes to complete.");
        System.out.println();
        System.out.println("NOTE: Questions are written in third-person ('S/he...')");
        System.out.println("Please answer as if describing yourself.");
        System.out.println();
    }

    private static boolean getConsentToProceed(Scanner scanner) {
        System.out.print("Do you understand and wish to proceed? (y/n): ");
        String response = scanner.nextLine().trim().toLowerCase();
        while (!response.equals("y") && !response.equals("n") &&
               !response.equals("yes") && !response.equals("no")) {
            System.out.print("Please enter 'y' for yes or 'n' for no: ");
            response = scanner.nextLine().trim().toLowerCase();
        }
        return response.equals("y") || response.equals("yes");
    }

    private static void displayInstructions(Scanner scanner) {
        System.out.println();
        System.out.println("INSTRUCTIONS:");
        System.out.println("- Please answer each question honestly based on your typical experiences");
        System.out.println("- Select the response option that best describes you");
        System.out.println("- Enter the number corresponding to your choice");
        System.out.println("- You can type 'back' to return to the previous question");
        System.out.println("- Type 'quit' at any time to exit the assessment");
        System.out.println();
        System.out.println("Press Enter to begin...");
        scanner.nextLine();
        System.out.println();
    }

    private static boolean conductQuestionnaire(Scanner scanner, AQAssessment assessment) {
        int currentQuestionIndex = 0;
        final int total = assessment.getTotalQuestions();

        while (currentQuestionIndex < total) {
            AQQuestion currentQuestion = assessment.getQuestions().get(currentQuestionIndex);

            displayProgressIndicator(currentQuestionIndex + 1, total);
            displayQuestion(currentQuestion);

            String userInput = getUserInput(scanner);
            if (userInput.equalsIgnoreCase("quit")) {
                return false; // cancelled
            }
            if (userInput.equalsIgnoreCase("back")) {
                if (currentQuestionIndex > 0) {
                    currentQuestionIndex--;
                    System.out.println("Returning to previous question...\n");
                } else {
                    System.out.println("This is the first question. Cannot go back.\n");
                }
                continue;
            }

            // Numeric input
            try {
                int choice = Integer.parseInt(userInput);
                if (choice >= 1 && choice <= currentQuestion.getResponseOptions().length) {
                    assessment.recordResponse(currentQuestionIndex, choice);
                    currentQuestionIndex++;
                    System.out.println();
                } else {
                    System.out.println("Invalid choice. Please select a number between 1 and " +
                            currentQuestion.getResponseOptions().length + ".");
                    System.out.println();
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number, 'back', or 'quit'.");
                System.out.println();
            }
        }

        // Mark complete
        assessment.completeAssessment();
        System.out.println("Assessment completed successfully!");
        System.out.println();
        return true;
    }

    private static void displayQuestion(AQQuestion q) {
        System.out.println("Question " + q.getQuestionId() + ":");
        System.out.println(q.getQuestionText());
        System.out.println();
        String[] opts = q.getResponseOptions();
        for (int i = 0; i < opts.length; i++) {
            System.out.println((i + 1) + ". " + opts[i]);
        }
        System.out.println();
    }

    private static void displayProgressIndicator(int current, int total) {
        System.out.println("Progress: " + current + "/" + total);
        int progressLength = 30;
        int filledLength = (int) ((double) current / total * progressLength);
        System.out.print("[");
        for (int i = 0; i < progressLength; i++) {
            System.out.print(i < filledLength ? "=" : " ");
        }
        int pct = (int) ((double) current / total * 100);
        System.out.println("] " + pct + "%");
        System.out.println();
    }

    private static String getUserInput(Scanner scanner) {
        System.out.print("Your choice (or 'back'/'quit'): ");
        String s = scanner.nextLine();
        return s == null ? "" : s.trim();
    }

    private static void displayResults(AQAssessment assessment) {
        try {
            int totalScore = AQScoreCalculator.calculateTotalScore(assessment);
            AQScoreCalculator.CategoryScores categoryScores = AQScoreCalculator.calculateCategoryScores(assessment);
            AQResultInterpreter.AssessmentSummary result = AQResultInterpreter.interpret(totalScore, categoryScores);

            displayResultsHeader();
            displayScoreSummary(result);
            displayCategoryBreakdown(categoryScores);
            displayInterpretation(result);
            displayRecommendations(result);
            displayDisclaimer();
        } catch (Exception e) {
            System.err.println("Error calculating results: " + e.getMessage());
        }
    }

    private static void displayResultsHeader() {
        System.out.println("=================================================");
        System.out.println("           ASSESSMENT RESULTS");
        System.out.println("=================================================");
        System.out.println();
    }

    private static void displayScoreSummary(AQResultInterpreter.AssessmentSummary result) {
        System.out.println("AQ SCORE SUMMARY:");
        System.out.println("Total AQ Score: " + result.totalScore + "/50");
        System.out.println("Autism Spectrum Trait Level: " + result.riskLevel);
        System.out.println();

        System.out.println("REFERENCE INFORMATION:");
        System.out.println("- Most neurotypical individuals score 0-15");
        System.out.println("- Scores of 26+ may indicate autism spectrum traits");
        System.out.println("- Scores of 32+ are strongly associated with autism spectrum conditions");
        System.out.println();
    }

    private static void displayCategoryBreakdown(AQScoreCalculator.CategoryScores c) {
        System.out.println("AQ SUBSCALE BREAKDOWN:");
        System.out.println("Social Skills: " + c.getSocialSkillsScore() + "/10");
        System.out.println("Attention Switching: " + c.getAttentionSwitchingScore() + "/10");
        System.out.println("Attention to Detail: " + c.getAttentionToDetailScore() + "/10");
        System.out.println("Communication: " + c.getCommunicationScore() + "/10");
        System.out.println("Imagination: " + c.getImaginationScore() + "/10");
        System.out.println();

        System.out.println("DETAILED SUBSCALE ANALYSIS:");
        System.out.println(AQResultInterpreter.interpretCategories(c));
        System.out.println();
    }

    private static void displayInterpretation(AQResultInterpreter.AssessmentSummary result) {
        System.out.println("INTERPRETATION:");
        System.out.println(wrapText(result.overallInterpretation, 80));
        System.out.println();
    }

    private static void displayRecommendations(AQResultInterpreter.AssessmentSummary result) {
        System.out.println("RECOMMENDATIONS:");
        System.out.println(wrapText(result.recommendation, 80));
        System.out.println();
    }

    private static void displayDisclaimer() {
        System.out.println("=================================================");
        System.out.println("IMPORTANT REMINDER:");
        System.out.println("This screening tool is not a substitute for professional");
        System.out.println("diagnosis. If you have concerns about autism spectrum");
        System.out.println("differences, please consult with a qualified healthcare");
        System.out.println("professional for a comprehensive evaluation.");
        System.out.println("=================================================");
        System.out.println();
        System.out.println("Thank you for completing the assessment.");
    }

    private static String wrapText(String text, int lineLength) {
        if (text == null || text.length() <= lineLength) {
            return text;
        }
        StringBuilder wrapped = new StringBuilder();
        String[] words = text.split(" ");
        int currentLineLength = 0;
        for (String word : words) {
            if (currentLineLength + word.length() + (currentLineLength == 0 ? 0 : 1) > lineLength) {
                wrapped.append('\n');
                currentLineLength = 0;
            }
            if (currentLineLength > 0) {
                wrapped.append(' ');
                currentLineLength++;
            }
            wrapped.append(word);
            currentLineLength += word.length();
        }
        return wrapped.toString();
    }
}

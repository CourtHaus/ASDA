package org.group1.asda.assessment;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages the AQ (Autism Quotient) assessment questionnaire
 * Contains all questions and tracks user responses
 */
public class AQAssessment {
    private final List<AQQuestion> questions;
    private final List<Integer> userResponses; // 1-based choices
    private boolean completed;

    public AQAssessment() {
        this.questions = new ArrayList<>();
        this.userResponses = new ArrayList<>();
        this.completed = false;
        initializeQuestions();
    }

    public List<AQQuestion> getQuestions() { return questions; }
    public List<Integer> getUserResponses() { return userResponses; }
    public boolean isCompleted() { return completed; }
    public int getTotalQuestions() { return questions.size(); }
    public int getAnsweredQuestions() { return (int) userResponses.stream().filter(v -> v != null && v != 0).count(); }

    public void recordResponse(int questionIndex, int response) {
        if (questionIndex < 0 || questionIndex >= questions.size()) {
            throw new IllegalArgumentException("Invalid question index: " + questionIndex);
        }
        AQQuestion question = questions.get(questionIndex);
        if (!question.isValidChoice(response)) {
            throw new IllegalArgumentException("Invalid response choice: " + response);
        }
        while (userResponses.size() <= questionIndex) {
            userResponses.add(0);
        }
        userResponses.set(questionIndex, response);
    }

    public boolean areAllQuestionsAnswered() {
        return userResponses.size() == questions.size() && userResponses.stream().noneMatch(v -> v == null || v == 0);
    }

    public void completeAssessment() {
        if (!areAllQuestionsAnswered()) {
            throw new IllegalStateException("Cannot complete assessment: not all questions answered");
        }
        this.completed = true;
    }

    private void initializeQuestions() {
        String[] responseOptions = new String[]{
                "Definitely Agree",
                "Slightly Agree",
                "Slightly Disagree",
                "Definitely Disagree"
        };
        // The score arrays are encoded per original spec: some reversed
        questions.add(new AQQuestion(1, "S/he prefers to do things with others rather than on her/his own.", responseOptions, new int[]{0,0,1,1}));
        questions.add(new AQQuestion(2, "S/he prefers to do things the same way over and over again.", responseOptions, new int[]{1,1,0,0}));
        questions.add(new AQQuestion(3, "If s/he tries to imagine something, s/he finds it very easy to create a picture in her/his mind.", responseOptions, new int[]{0,0,1,1}));
        questions.add(new AQQuestion(4, "S/he frequently gets so strongly absorbed in one thing that s/he loses sight of other things.", responseOptions, new int[]{1,1,0,0}));
        questions.add(new AQQuestion(5, "S/he often notices small sounds when others do not.", responseOptions, new int[]{1,1,0,0}));
        questions.add(new AQQuestion(6, "S/he usually notices car number plates or similar strings of information.", responseOptions, new int[]{1,1,0,0}));
        questions.add(new AQQuestion(7, "Other people frequently tell her/him that what s/he has said is impolite, even though s/he thinks it is polite.", responseOptions, new int[]{1,1,0,0}));
        questions.add(new AQQuestion(8, "When s/he is reading a story, s/he can easily imagine what the characters might look like.", responseOptions, new int[]{0,0,1,1}));
        questions.add(new AQQuestion(9, "S/he is fascinated by dates.", responseOptions, new int[]{1,1,0,0}));
        questions.add(new AQQuestion(10, "In a social group, s/he can easily keep track of several different people's conversations.", responseOptions, new int[]{0,0,1,1}));
        questions.add(new AQQuestion(11, "S/he finds social situations easy.", responseOptions, new int[]{0,0,1,1}));
        questions.add(new AQQuestion(12, "S/he tends to notice details that others do not.", responseOptions, new int[]{1,1,0,0}));
        questions.add(new AQQuestion(13, "S/he would rather go to a library than a party.", responseOptions, new int[]{1,1,0,0}));
        questions.add(new AQQuestion(14, "S/he finds making up stories easy.", responseOptions, new int[]{0,0,1,1}));
        questions.add(new AQQuestion(15, "S/he finds her/himself drawn more strongly to people than to things.", responseOptions, new int[]{0,0,1,1}));
        questions.add(new AQQuestion(16, "S/he tends to have very strong interests, which s/he gets upset about if s/he can't pursue.", responseOptions, new int[]{1,1,0,0}));
        questions.add(new AQQuestion(17, "S/he enjoys social chit-chat.", responseOptions, new int[]{0,0,1,1}));
        questions.add(new AQQuestion(18, "When s/he talks, it isn't always easy for others to get a word in edgeways.", responseOptions, new int[]{1,1,0,0}));
        questions.add(new AQQuestion(19, "S/he is fascinated by numbers.", responseOptions, new int[]{1,1,0,0}));
        questions.add(new AQQuestion(20, "When s/he is reading a story, s/he finds it difficult to work out the characters' intentions.", responseOptions, new int[]{1,1,0,0}));
        questions.add(new AQQuestion(21, "S/he doesn't particularly enjoy reading fiction.", responseOptions, new int[]{1,1,0,0}));
        questions.add(new AQQuestion(22, "S/he finds it hard to make new friends.", responseOptions, new int[]{1,1,0,0}));
        questions.add(new AQQuestion(23, "S/he notices patterns in things all the time.", responseOptions, new int[]{1,1,0,0}));
        questions.add(new AQQuestion(24, "S/he would rather go to the theatre than a museum.", responseOptions, new int[]{0,0,1,1}));
        questions.add(new AQQuestion(25, "It does not upset him/her if his/her daily routine is disturbed.", responseOptions, new int[]{0,0,1,1}));
        questions.add(new AQQuestion(26, "S/he frequently finds that s/he doesn't know how to keep a conversation going.", responseOptions, new int[]{1,1,0,0}));
        questions.add(new AQQuestion(27, "S/he finds it easy to \"read between the lines\" when someone is talking to her/him.", responseOptions, new int[]{0,0,1,1}));
        questions.add(new AQQuestion(28, "S/he usually concentrates more on the whole picture, rather than the small details.", responseOptions, new int[]{0,0,1,1}));
        questions.add(new AQQuestion(29, "S/he is not very good at remembering phone numbers.", responseOptions, new int[]{0,0,1,1}));
        questions.add(new AQQuestion(30, "S/he doesn't usually notice small changes in a situation, or a person's appearance.", responseOptions, new int[]{0,0,1,1}));
        questions.add(new AQQuestion(31, "S/he knows how to tell if someone listening to him/her is getting bored.", responseOptions, new int[]{0,0,1,1}));
        questions.add(new AQQuestion(32, "S/he finds it easy to do more than one thing at once.", responseOptions, new int[]{0,0,1,1}));
        questions.add(new AQQuestion(33, "When s/he talks on the phone, s/he is not sure when it's her/his turn to speak.", responseOptions, new int[]{1,1,0,0}));
        questions.add(new AQQuestion(34, "S/he enjoys doing things spontaneously.", responseOptions, new int[]{0,0,1,1}));
        questions.add(new AQQuestion(35, "S/he is often the last to understand the point of a joke.", responseOptions, new int[]{1,1,0,0}));
        questions.add(new AQQuestion(36, "S/he finds it easy to work out what someone is thinking or feeling just by looking at their face.", responseOptions, new int[]{0,0,1,1}));
        questions.add(new AQQuestion(37, "If there is an interruption, s/he can switch back to what s/he was doing very quickly.", responseOptions, new int[]{0,0,1,1}));
        questions.add(new AQQuestion(38, "S/he is good at social chit-chat.", responseOptions, new int[]{0,0,1,1}));
        questions.add(new AQQuestion(39, "People often tell her/him that s/he keeps going on and on about the same thing.", responseOptions, new int[]{1,1,0,0}));
        questions.add(new AQQuestion(40, "When s/he was younger, s/he used to enjoy playing games involving pretending with other children.", responseOptions, new int[]{0,0,1,1}));
        questions.add(new AQQuestion(41, "S/he likes to collect information about categories of things (e.g. types of car, types of bird, types of train, types of plant, etc.).", responseOptions, new int[]{1,1,0,0}));
        questions.add(new AQQuestion(42, "S/he finds it difficult to imagine what it would be like to be someone else.", responseOptions, new int[]{1,1,0,0}));
        questions.add(new AQQuestion(43, "S/he likes to plan any activities s/he participates in carefully.", responseOptions, new int[]{1,1,0,0}));
        questions.add(new AQQuestion(44, "S/he enjoys social occasions.", responseOptions, new int[]{0,0,1,1}));
        questions.add(new AQQuestion(45, "S/he finds it difficult to work out people's intentions.", responseOptions, new int[]{1,1,0,0}));
        questions.add(new AQQuestion(46, "New situations make him/her anxious.", responseOptions, new int[]{1,1,0,0}));
        questions.add(new AQQuestion(47, "S/he enjoys meeting new people.", responseOptions, new int[]{0,0,1,1}));
        questions.add(new AQQuestion(48, "S/he is a good diplomat.", responseOptions, new int[]{0,0,1,1}));
        questions.add(new AQQuestion(49, "S/he is not very good at remembering people's date of birth.", responseOptions, new int[]{0,0,1,1}));
        questions.add(new AQQuestion(50, "S/he finds it very easy to play games with children that involve pretending.", responseOptions, new int[]{0,0,1,1}));
    }
}

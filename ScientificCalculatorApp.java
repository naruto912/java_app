import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Stack;

public class ScientificCalculatorApp {
    private JFrame frame;
    private JTextField textField;
    
    public ScientificCalculatorApp() {
        // Create the main frame
        frame = new JFrame("Scientific Calculator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 500);
        frame.setLayout(new BorderLayout());

        // Create text field for input/output
        textField = new JTextField();
        textField.setFont(new Font("Arial", Font.PLAIN, 24));
        frame.add(textField, BorderLayout.NORTH);

        // Create a panel for buttons
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 4));

        // Add buttons
        String[] buttonLabels = {
            "7", "8", "9", "/",
            "4", "5", "6", "*",
            "1", "2", "3", "-",
            "0", ".", "=", "+",
            "sin", "cos", "tan", "log",
            "(", ")", "C", "√"
        };

        for (String label : buttonLabels) {
            JButton button = new JButton(label);
            button.setFont(new Font("Arial", Font.PLAIN, 18));
            button.addActionListener(new ButtonClickListener());
            panel.add(button);
        }

        frame.add(panel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private class ButtonClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            if (command.equals("=")) {
                calculateResult();
            } else if (command.equals("C")) {
                textField.setText("");
            } else {
                textField.setText(textField.getText() + command);
            }
        }
    }

    private void calculateResult() {
        try {
            String input = textField.getText();
            double result = evaluateExpression(input);
            textField.setText(String.valueOf(result));
        } catch (Exception e) {
            textField.setText("Error");
        }
    }

    private double evaluateExpression(String expression) {
        // Simple evaluation logic; for a full implementation, consider using a library or a more robust parser
        Stack<Double> values = new Stack<>();
        Stack<Character> operators = new Stack<>();

        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);

            if (Character.isDigit(c)) {
                StringBuilder sb = new StringBuilder();
                while (i < expression.length() && (Character.isDigit(expression.charAt(i)) || expression.charAt(i) == '.')) {
                    sb.append(expression.charAt(i++));
                }
                values.push(Double.parseDouble(sb.toString()));
                i--; // Decrement to adjust for the outer loop increment
            } else if (c == '+' || c == '-' || c == '*' || c == '/') {
                while (!operators.isEmpty() && precedence(operators.peek()) >= precedence(c)) {
                    values.push(applyOperation(operators.pop(), values.pop(), values.pop()));
                }
                operators.push(c);
            } else if (c == '(') {
                operators.push(c);
            } else if (c == ')') {
                while (operators.peek() != '(') {
                    values.push(applyOperation(operators.pop(), values.pop(), values.pop()));
                }
                operators.pop();
            } else if (c == 's') {
                i += 2; // Skip "sin"
                values.push(Math.sin(Math.toRadians(getNextValue(expression, i))));
                continue;
            } else if (c == 'c') {
                i += 2; // Skip "cos"
                values.push(Math.cos(Math.toRadians(getNextValue(expression, i))));
                continue;
            } else if (c == 't') {
                i += 2; // Skip "tan"
                values.push(Math.tan(Math.toRadians(getNextValue(expression, i))));
                continue;
            } else if (c == 'l') {
                i += 2; // Skip "log"
                values.push(Math.log10(getNextValue(expression, i)));
                continue;
            } else if (c == '√') {
                values.push(Math.sqrt(getNextValue(expression, i + 1)));
                continue;
            }
        }

        while (!operators.isEmpty()) {
            values.push(applyOperation(operators.pop(), values.pop(), values.pop()));
        }

        return values.pop();
    }

    private double getNextValue(String expression, int startIndex) {
        StringBuilder sb = new StringBuilder();
        while (startIndex < expression.length() && (Character.isDigit(expression.charAt(startIndex)) || expression.charAt(startIndex) == '.')) {
            sb.append(expression.charAt(startIndex++));
        }
        return Double.parseDouble(sb.toString());
    }

    private int precedence(char operator) {
        switch (operator) {
            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
                return 2;
            default:
                return -1;
        }
    }

    private double applyOperation(char operator, double b, double a) {
        switch (operator) {
            case '+':
                return a + b;
            case '-':
                return a - b;
            case '*':
                return a * b;
            case '/':
                if (b == 0) throw new UnsupportedOperationException("Cannot divide by zero");
                return a / b;
        }
        return 0;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ScientificCalculatorApp::new);
    }
}

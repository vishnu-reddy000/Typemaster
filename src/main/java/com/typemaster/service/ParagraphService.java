package com.typemaster.service;

import com.typemaster.dto.ParagraphDTO;
import com.typemaster.model.Paragraph;
import com.typemaster.repository.ParagraphRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Service handling business logic for typing practice paragraphs.
 */
@Service
public class ParagraphService {

    private final ParagraphRepository paragraphRepository;
    private final Random random = new Random();

    @Autowired
    public ParagraphService(ParagraphRepository paragraphRepository) {
        this.paragraphRepository = paragraphRepository;
    }

    /**
     * Retrieves practice material matched to mode (PARAGRAPH vs CODING), language, topic, and duration.
     */
    public ParagraphDTO getPracticeMaterial(Integer durationMinutes, String mode, String language, String topic) {
        int duration = (durationMinutes != null && durationMinutes > 0) ? durationMinutes : 1;
        String targetType = (mode != null && mode.equalsIgnoreCase("CODING")) ? "CODING" : "PARAGRAPH";

        if ("CODING".equals(targetType)) {
            String targetLang = (language != null && !language.trim().isEmpty()) ? language.trim().toUpperCase() : "JAVA";
            String targetTopic = (topic != null && !topic.trim().isEmpty() && !topic.equalsIgnoreCase("ALL")) ? topic.trim().toUpperCase() : null;

            List<Paragraph> matches;
            if (targetTopic != null) {
                matches = paragraphRepository.findByTypeAndLanguageAndTopic("CODING", targetLang, targetTopic);
                if (matches.isEmpty()) {
                    matches = paragraphRepository.findByTypeAndLanguage("CODING", targetLang);
                }
            } else {
                matches = paragraphRepository.findByTypeAndLanguage("CODING", targetLang);
            }

            if (matches.isEmpty()) {
                matches = paragraphRepository.findByTypeAndLanguage("CODING", "JAVA");
            }

            StringBuilder scaledCode = new StringBuilder();
            if (!matches.isEmpty()) {
                // Shuffle matching snippets to select distinct programs for every block
                List<Paragraph> pool = new ArrayList<>(matches);
                java.util.Collections.shuffle(pool, random);

                // If pool is smaller than required duration, supplement with all language snippets
                if (pool.size() < duration) {
                    List<Paragraph> langPool = paragraphRepository.findByTypeAndLanguage("CODING", targetLang);
                    java.util.Collections.shuffle(langPool, random);
                    for (Paragraph p : langPool) {
                        if (!pool.contains(p)) pool.add(p);
                    }
                }

                for (int i = 0; i < duration; i++) {
                    Paragraph snippet = pool.get(i % pool.size());
                    if (i > 0) scaledCode.append("\n\n// --- Program Block ").append(i + 1).append(" ---\n");
                    scaledCode.append(snippet.getContent());
                }
            } else {
                String baseCode = getFallbackCodeSnippet(targetLang, targetTopic);
                for (int i = 0; i < duration; i++) {
                    if (i > 0) scaledCode.append("\n\n// --- Program Block ").append(i + 1).append(" ---\n");
                    scaledCode.append(baseCode);
                }
            }

            return new ParagraphDTO(0L, scaledCode.toString(), duration, "CODING", targetLang, targetTopic != null ? targetTopic : "ALL");
        } else {
            // Strictly PARAGRAPH Mode
            List<Paragraph> matches = paragraphRepository.findByTypeAndDurationMinutes("PARAGRAPH", duration);

            if (matches.isEmpty()) {
                matches = paragraphRepository.findByType("PARAGRAPH");
            }

            if (matches.isEmpty()) {
                matches = paragraphRepository.findByDurationMinutes(duration);
            }

            if (matches.isEmpty()) {
                return new ParagraphDTO(0L, "The quick brown fox jumps over the lazy dog. Touch typing enables software developers to maintain an uninterrupted flow state and write code efficiently.", duration, "PARAGRAPH", "ENGLISH", "GENERAL");
            }

            List<Paragraph> pool = new ArrayList<>(matches);
            Collections.shuffle(pool, random);
            Paragraph selected = pool.get(0);
            return new ParagraphDTO(selected.getId(), selected.getContent(), selected.getDurationMinutes(), "PARAGRAPH", "ENGLISH", "GENERAL");
        }
    }

    private String getFallbackCodeSnippet(String language, String topic) {
        String lang = (language != null) ? language.toUpperCase() : "JAVA";
        String top = (topic != null) ? topic.toUpperCase() : "HELLO_WORLD";

        if ("PYTHON".equals(lang)) {
            if ("VARIABLES".equals(top)) return "user_age = 25\nprice = 99.99\nis_student = True\nname = \"TypeMaster\"\nprint(name, user_age)";
            if ("ARITHMETIC".equals(top)) return "a = 50\nb = 8\ntotal = a + b\ndiff = a - b\nproduct = a * b\nquotient = a / b";
            if ("TYPECASTING".equals(top)) return "num_str = \"125.75\"\nfloat_val = float(num_str)\nint_val = int(float_val)\nstr_val = str(int_val)";
            return "def main():\n    print(\"Hello, World!\")\n\nmain()";
        } else if ("JAVASCRIPT".equals(lang)) {
            if ("VARIABLES".equals(top)) return "let itemCount = 15;\nconst unitPrice = 29.99;\nlet isActive = true;\nconst itemName = \"Keyboard\";";
            if ("ARITHMETIC".equals(top)) return "const a = 30;\nconst b = 7;\nconst sum = a + b;\nconst product = a * b;\nconst modulo = a % b;";
            if ("TYPECASTING".equals(top)) return "const textNum = \"450.60\";\nconst parsedInt = parseInt(textNum, 10);\nconst numValue = Number(textNum);";
            return "function sayHello() {\n    console.log(\"Hello, World!\");\n}\nsayHello();";
        } else if ("CPP".equals(lang)) {
            if ("VARIABLES".equals(top)) return "int speed = 120;\ndouble acceleration = 9.81;\nchar group = 'C';\nbool status = true;";
            if ("ARITHMETIC".equals(top)) return "int valA = 64;\nint valB = 9;\nint sumVal = valA + valB;\nint prodVal = valA * valB;";
            if ("TYPECASTING".equals(top)) return "double temp = 98.6;\nint roundedTemp = static_cast<int>(temp);\nstring str = to_string(roundedTemp);";
            return "#include <iostream>\nusing namespace std;\n\nint main() {\n    cout << \"Hello, World!\" << endl;\n    return 0;\n}";
        } else { // JAVA
            if ("VARIABLES".equals(top)) return "int age = 25;\ndouble accountBalance = 1500.75;\nchar grade = 'A';\nboolean isEmployed = true;\nString name = \"TypeMaster\";";
            if ("ARITHMETIC".equals(top)) return "int num1 = 45;\nint num2 = 12;\nint sum = num1 + num2;\nint diff = num1 - num2;\nint prod = num1 * num2;";
            if ("TYPECASTING".equals(top)) return "double rawVal = 99.85;\nint castedInt = (int) rawVal;\nString strNum = \"250\";\nint parsedInt = Integer.parseInt(strNum);";
            return "public class HelloWorld {\n    public static void main(String[] args) {\n        System.out.println(\"Hello, World!\");\n    }\n}";
        }
    }

    /**
     * Retrieves a random paragraph matched to the requested duration in minutes.
     */
    public ParagraphDTO getRandomParagraphByDuration(Integer durationMinutes) {
        return getPracticeMaterial(durationMinutes, "PARAGRAPH", null, null);
    }

    /**
     * Saves a new paragraph to the database.
     */
    public Paragraph saveParagraph(String content, Integer durationMinutes) {
        Paragraph paragraph = new Paragraph(content, durationMinutes);
        return paragraphRepository.save(paragraph);
    }
}

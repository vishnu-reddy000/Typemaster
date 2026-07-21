package com.typemaster.config;

import com.typemaster.repository.ParagraphRepository;
import com.typemaster.model.Paragraph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Seeds initial typing practice paragraphs into the database on startup.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private final ParagraphRepository paragraphRepository;

    @Autowired
    public DataInitializer(ParagraphRepository paragraphRepository) {
        this.paragraphRepository = paragraphRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Fix any existing null type fields from previous database schema
        paragraphRepository.findAll().forEach(p -> {
            if (p.getType() == null) {
                p.setType("PARAGRAPH");
                p.setLanguage("ENGLISH");
                p.setTopic("GENERAL");
                paragraphRepository.save(p);
            }
        });

        if (paragraphRepository.count() == 0) {
            // 1 Minute Test Paragraphs
            paragraphRepository.save(new Paragraph(
                    "The quick brown fox jumps over the lazy dog. Programming is the art of telling a computer what to do through clear, logical, and structured instructions. Developing great typing speed requires regular practice, focus, and patience.", 1
            ));
            paragraphRepository.save(new Paragraph(
                    "Web development combines creativity with technical problem solving. HTML provides the structural backbone of every web page, CSS brings visual beauty through colors and layouts, and JavaScript delivers dynamic interactivity that powers modern applications.", 1
            ));

            // 3 Minute Test Paragraph
            paragraphRepository.save(new Paragraph(
                    "Technology continues to transform the way we communicate, learn, and build software across the globe. Writing clean code and maintaining high accuracy while typing enables software developers to express their complex thoughts rapidly and efficiently. Success in software development comes from continuous learning, curiosity, and persistent daily effort. Building real-world projects from scratch helps reinforce fundamental engineering concepts, improve problem solving abilities, and build lasting professional confidence.", 3
            ));

            // 5 Minute Test Paragraph
            paragraphRepository.save(new Paragraph(
                    "The evolution of modern computing has revolutionized every facet of human activity, from scientific research to creative arts and global communication networks. At the core of every software application lies a carefully constructed sequence of instructions written by developers who bridge the gap between human ideas and machine execution. Learning to write software is a journey of continuous discovery. Touch typing enables software engineers to maintain an uninterrupted flow state. When your fingers move across the keyboard effortlessly, your brain remains entirely focused on logical problem solving and architectural design.", 5
            ));

            // 7 Minute Test Paragraph
            paragraphRepository.save(new Paragraph(
                    "Software engineering is both an intellectual discipline and a craft that requires constant refinement. In the modern tech landscape, applications must be fast, reliable, accessible, and maintainable. Achieving these qualities demands a thorough understanding of core web fundamentals. HTML5 provides the semantic foundation, giving structural meaning to content. CSS3 brings those structures to life through flexible layouts and fluid typography. JavaScript adds behavior and interactive user experiences. Touch typing is an essential habit that reduces cognitive load.", 7
            ));

            // 9 Minute Test Paragraph
            paragraphRepository.save(new Paragraph(
                    "In the modern digital era, the ability to build elegant, high-performing web applications is one of the most versatile and impactful skills a developer can acquire. The web platform has evolved from static document sharing into a sophisticated runtime environment capable of powering complex, real-time applications directly inside the browser. Semantic HTML forms the backbone of web accessibility, CSS3 delivers expressive visual design systems, and JavaScript provides programmatic logic. Touch typing allows developers to translate creative ideas into source code without delay.", 9, "PARAGRAPH", "ENGLISH", "GENERAL"
            ));
        }

        if (paragraphRepository.findByType("CODING").size() < 40) {
            // Remove existing sparse coding rows to re-seed rich catalog
            List<Paragraph> oldCoding = paragraphRepository.findByType("CODING");
            paragraphRepository.deleteAll(oldCoding);

            // ==================== CODING SNIPPETS CATALOG (48 UNIQUE PROGRAMS) ====================

            // --- JAVA SNIPPETS ---
            // Java - Hello World
            paragraphRepository.save(new Paragraph(
                    "public class HelloWorld {\n    public static void main(String[] args) {\n        System.out.println(\"Hello, World!\");\n    }\n}",
                    1, "CODING", "JAVA", "HELLO_WORLD"
            ));
            paragraphRepository.save(new Paragraph(
                    "public class FormattedGreeting {\n    public static void main(String[] args) {\n        String appName = \"TypeMaster\";\n        System.out.printf(\"Welcome to %s Coding Practice!%n\", appName);\n    }\n}",
                    1, "CODING", "JAVA", "HELLO_WORLD"
            ));
            paragraphRepository.save(new Paragraph(
                    "public class MultiLineOutput {\n    public static void main(String[] args) {\n        System.out.println(\"Line 1: Compiling Java Application...\");\n        System.out.println(\"Line 2: Execution Finished Successfully.\");\n    }\n}",
                    1, "CODING", "JAVA", "HELLO_WORLD"
            ));

            // Java - Variables & Data Types
            paragraphRepository.save(new Paragraph(
                    "int age = 25;\ndouble accountBalance = 1500.75;\nchar userGrade = 'A';\nboolean isEmployed = true;\nString userName = \"Alex Developer\";\nSystem.out.println(\"User: \" + userName + \", Age: \" + age);",
                    1, "CODING", "JAVA", "VARIABLES"
            ));
            paragraphRepository.save(new Paragraph(
                    "long population = 8000000000L;\nfloat temperature = 36.6f;\nbyte maxLevel = 100;\nshort errorCode = 404;\nSystem.out.println(\"Status code: \" + errorCode);",
                    1, "CODING", "JAVA", "VARIABLES"
            ));
            paragraphRepository.save(new Paragraph(
                    "final double PI = 3.14159265359;\nString country = \"United States\";\nboolean isVerified = true;\nSystem.out.println(\"Constant PI: \" + PI);",
                    1, "CODING", "JAVA", "VARIABLES"
            ));

            // Java - Arithmetic Operations
            paragraphRepository.save(new Paragraph(
                    "int num1 = 45;\nint num2 = 12;\nint sum = num1 + num2;\nint difference = num1 - num2;\nint product = num1 * num2;\ndouble quotient = (double) num1 / num2;\nint remainder = num1 % num2;\nSystem.out.println(\"Sum: \" + sum + \", Product: \" + product);",
                    1, "CODING", "JAVA", "ARITHMETIC"
            ));
            paragraphRepository.save(new Paragraph(
                    "double principal = 1000.0;\ndouble rate = 0.05;\nint years = 3;\ndouble interest = principal * rate * years;\ndouble totalAmount = principal + interest;\nSystem.out.println(\"Total: \" + totalAmount);",
                    1, "CODING", "JAVA", "ARITHMETIC"
            ));
            paragraphRepository.save(new Paragraph(
                    "double radius = 7.5;\ndouble area = Math.PI * Math.pow(radius, 2);\ndouble perimeter = 2 * Math.PI * radius;\nSystem.out.println(\"Circle Area: \" + area);",
                    1, "CODING", "JAVA", "ARITHMETIC"
            ));

            // Java - Type Casting & Conversion
            paragraphRepository.save(new Paragraph(
                    "double rawValue = 99.85;\nint castedInteger = (int) rawValue;\nString strNumber = \"250\";\nint parsedNumber = Integer.parseInt(strNumber);\nString convertedString = String.valueOf(castedInteger);\nSystem.out.println(\"Casted int: \" + castedInteger + \", Parsed: \" + parsedNumber);",
                    1, "CODING", "JAVA", "TYPECASTING"
            ));
            paragraphRepository.save(new Paragraph(
                    "int score = 95;\nString scoreText = String.valueOf(score);\ndouble doubleVal = Double.parseDouble(\"12.99\");\nboolean boolVal = Boolean.parseBoolean(\"true\");\nSystem.out.println(\"Double: \" + doubleVal);",
                    1, "CODING", "JAVA", "TYPECASTING"
            ));
            paragraphRepository.save(new Paragraph(
                    "char initial = 'J';\nint asciiValue = (int) initial;\nchar nextChar = (char) (asciiValue + 1);\nSystem.out.println(\"Next char: \" + nextChar);",
                    1, "CODING", "JAVA", "TYPECASTING"
            ));

            // --- PYTHON SNIPPETS ---
            // Python - Hello World
            paragraphRepository.save(new Paragraph(
                    "def main():\n    print(\"Hello, World!\")\n\nif __name__ == \"__main__\":\n    main()",
                    1, "CODING", "PYTHON", "HELLO_WORLD"
            ));
            paragraphRepository.save(new Paragraph(
                    "user_name = \"Developer\"\nprint(f\"Hello {user_name}, welcome to Python Practice!\")",
                    1, "CODING", "PYTHON", "HELLO_WORLD"
            ));
            paragraphRepository.save(new Paragraph(
                    "print(\"==============================\")\nprint(\"  TYPEMASTER PYTHON UTILITY   \")\nprint(\"==============================\")",
                    1, "CODING", "PYTHON", "HELLO_WORLD"
            ));

            // Python - Variables & Data Types
            paragraphRepository.save(new Paragraph(
                    "user_age = 28\nscore_ratio = 98.4\nis_verified = True\ndeveloper_name = \"Python Programmer\"\nskills = [\"Django\", \"Flask\", \"FastAPI\"]\nprint(f\"{developer_name} has score {score_ratio}%\")",
                    1, "CODING", "PYTHON", "VARIABLES"
            ));
            paragraphRepository.save(new Paragraph(
                    "config = {\n    \"host\": \"localhost\",\n    \"port\": 8080,\n    \"debug\": False\n}\nprint(f\"Server listening on {config['host']}:{config['port']}\")",
                    1, "CODING", "PYTHON", "VARIABLES"
            ));
            paragraphRepository.save(new Paragraph(
                    "items_count = 42\naverage_rating = 4.85\nstatus_flags = (True, False, True)\nprint(f\"Items: {items_count}, Rating: {average_rating}\")",
                    1, "CODING", "PYTHON", "VARIABLES"
            ));

            // Python - Arithmetic Operations
            paragraphRepository.save(new Paragraph(
                    "x = 50\ny = 8\ntotal_sum = x + y\ndifference = x - y\nproduct = x * y\nfloat_div = x / y\nfloor_div = x // y\nremainder = x % y\npower = x ** 2",
                    1, "CODING", "PYTHON", "ARITHMETIC"
            ));
            paragraphRepository.save(new Paragraph(
                    "import math\n\nradius = 5.0\ncircle_area = math.pi * (radius ** 2)\ncircle_circumference = 2 * math.pi * radius\nprint(f\"Area: {circle_area:.2f}\")",
                    1, "CODING", "PYTHON", "ARITHMETIC"
            ));
            paragraphRepository.save(new Paragraph(
                    "base_price = 199.99\ntax_rate = 0.08\ndiscount = 15.0\nfinal_price = (base_price - discount) * (1 + tax_rate)\nprint(f\"Final Total: ${final_price:.2f}\")",
                    1, "CODING", "PYTHON", "ARITHMETIC"
            ));

            // Python - Type Casting & Conversion
            paragraphRepository.save(new Paragraph(
                    "input_str = \"125.75\"\nfloat_val = float(input_str)\nint_val = int(float_val)\nstr_converted = str(int_val)\nbool_val = bool(int_val)\nprint(\"Float:\", float_val, \"Int:\", int_val, \"String:\", str_converted)",
                    1, "CODING", "PYTHON", "TYPECASTING"
            ));
            paragraphRepository.save(new Paragraph(
                    "values = [\"10\", \"20\", \"30\"]\nint_list = [int(v) for v in values]\ntotal = sum(int_list)\nprint(f\"Sum of parsed ints: {total}\")",
                    1, "CODING", "PYTHON", "TYPECASTING"
            ));
            paragraphRepository.save(new Paragraph(
                    "hex_string = \"0x1A\"\ndecimal_val = int(hex_string, 16)\nbinary_str = bin(decimal_val)\nprint(f\"Dec: {decimal_val}, Bin: {binary_str}\")",
                    1, "CODING", "PYTHON", "TYPECASTING"
            ));

            // --- JAVASCRIPT SNIPPETS ---
            // JavaScript - Hello World
            paragraphRepository.save(new Paragraph(
                    "function sayHello() {\n    console.log(\"Hello, World!\");\n}\nsayHello();",
                    1, "CODING", "JAVASCRIPT", "HELLO_WORLD"
            ));
            paragraphRepository.save(new Paragraph(
                    "const showWelcome = (name) => {\n    console.log(`Hello ${name}, ready for JavaScript typing!`);\n};\nshowWelcome(\"Developer\");",
                    1, "CODING", "JAVASCRIPT", "HELLO_WORLD"
            ));
            paragraphRepository.save(new Paragraph(
                    "console.group(\"System Status\");\nconsole.log(\"Database: Connected\");\nconsole.log(\"Server: Running on port 8084\");\nconsole.groupEnd();",
                    1, "CODING", "JAVASCRIPT", "HELLO_WORLD"
            ));

            // JavaScript - Variables & Data Types
            paragraphRepository.save(new Paragraph(
                    "let itemCount = 15;\nconst unitPrice = 29.99;\nlet isActive = true;\nconst itemName = \"Mechanical Keyboard\";\nlet itemDetails = { name: itemName, price: unitPrice };\nconsole.log(itemDetails);",
                    1, "CODING", "JAVASCRIPT", "VARIABLES"
            ));
            paragraphRepository.save(new Paragraph(
                    "const userRoles = [\"ADMIN\", \"USER\", \"EDITOR\"];\nlet currentUser = { id: 101, role: userRoles[0] };\nconst MAX_ATTEMPTS = 5;\nconsole.log(`Logged in as ${currentUser.role}`);",
                    1, "CODING", "JAVASCRIPT", "VARIABLES"
            ));
            paragraphRepository.save(new Paragraph(
                    "let score = null;\nlet nickname;\nconst isNull = (score === null);\nconst isUndefined = (typeof nickname === \"undefined\");\nconsole.log(`IsNull: ${isNull}, IsUndefined: ${isUndefined}`);",
                    1, "CODING", "JAVASCRIPT", "VARIABLES"
            ));

            // JavaScript - Arithmetic Operations
            paragraphRepository.save(new Paragraph(
                    "const a = 30;\nconst b = 7;\nconst addition = a + b;\nconst subtraction = a - b;\nconst multiplication = a * b;\nconst division = a / b;\nconst modulo = a % b;\nconsole.log(`Sum: ${addition}, Modulo: ${modulo}`);",
                    1, "CODING", "JAVASCRIPT", "ARITHMETIC"
            ));
            paragraphRepository.save(new Paragraph(
                    "const billAmount = 250.00;\nconst tipPercent = 0.15;\nconst tipAmount = billAmount * tipPercent;\nconst totalBill = billAmount + tipAmount;\nconst perPerson = totalBill / 4;\nconsole.log(`Per Person: ${perPerson.toFixed(2)}`);",
                    1, "CODING", "JAVASCRIPT", "ARITHMETIC"
            ));
            paragraphRepository.save(new Paragraph(
                    "const sideA = 12;\nconst sideB = 16;\nconst hypotenuse = Math.sqrt(Math.pow(sideA, 2) + Math.pow(sideB, 2));\nconsole.log(`Hypotenuse: ${hypotenuse}`);",
                    1, "CODING", "JAVASCRIPT", "ARITHMETIC"
            ));

            // JavaScript - Type Casting & Conversion
            paragraphRepository.save(new Paragraph(
                    "const textNum = \"450.60\";\nconst parsedInt = parseInt(textNum, 10);\nconst parsedFloat = parseFloat(textNum);\nconst numValue = Number(textNum);\nconst textValue = String(100);\nconsole.log(typeof parsedInt, parsedInt, typeof textValue);",
                    1, "CODING", "JAVASCRIPT", "TYPECASTING"
            ));
            paragraphRepository.save(new Paragraph(
                    "const boolString = \"true\";\nconst isTrue = (boolString === \"true\");\nconst num = 123.456;\nconst formatted = num.toFixed(2);\nconsole.log(`Formatted: ${formatted}, IsTrue: ${isTrue}`);",
                    1, "CODING", "JAVASCRIPT", "TYPECASTING"
            ));
            paragraphRepository.save(new Paragraph(
                    "const rawJson = '{\"id\": 42, \"status\": \"active\"}';\nconst parsedObj = JSON.parse(rawJson);\nconst jsonString = JSON.stringify(parsedObj);\nconsole.log(`Parsed ID: ${parsedObj.id}`);",
                    1, "CODING", "JAVASCRIPT", "TYPECASTING"
            ));

            // --- C++ SNIPPETS ---
            // C++ - Hello World
            paragraphRepository.save(new Paragraph(
                    "#include <iostream>\nusing namespace std;\n\nint main() {\n    cout << \"Hello, World!\" << endl;\n    return 0;\n}",
                    1, "CODING", "CPP", "HELLO_WORLD"
            ));
            paragraphRepository.save(new Paragraph(
                    "#include <iostream>\nusing namespace std;\n\nvoid greetUser(string name) {\n    cout << \"Hello \" << name << \", welcome to C++!\" << endl;\n}\n\nint main() {\n    greetUser(\"Developer\");\n    return 0;\n}",
                    1, "CODING", "CPP", "HELLO_WORLD"
            ));
            paragraphRepository.save(new Paragraph(
                    "#include <iostream>\nusing namespace std;\n\nint main() {\n    cout << \"==============================\" << endl;\n    cout << \" C++ TYPEMASTER SYSTEM ONLINE\" << endl;\n    cout << \"==============================\" << endl;\n    return 0;\n}",
                    1, "CODING", "CPP", "HELLO_WORLD"
            ));

            // C++ - Variables & Data Types
            paragraphRepository.save(new Paragraph(
                    "int speed = 120;\ndouble acceleration = 9.81;\nchar codeGroup = 'C';\nbool engineStatus = true;\nstring carModel = \"TypeMaster GT\";\ncout << \"Model: \" << carModel << \", Speed: \" << speed << endl;",
                    1, "CODING", "CPP", "VARIABLES"
            ));
            paragraphRepository.save(new Paragraph(
                    "const double GRAVITY = 9.8;\nunsigned int distance = 5000;\nfloat duration = 12.5f;\nlong long totalBytes = 10737418240LL;\ncout << \"Bytes: \" << totalBytes << endl;",
                    1, "CODING", "CPP", "VARIABLES"
            ));
            paragraphRepository.save(new Paragraph(
                    "struct Student {\n    int id;\n    string name;\n    double gpa;\n};\nStudent s1 = {101, \"Alice\", 3.95};\ncout << \"Student: \" << s1.name << \", GPA: \" << s1.gpa << endl;",
                    1, "CODING", "CPP", "VARIABLES"
            ));

            // C++ - Arithmetic Operations
            paragraphRepository.save(new Paragraph(
                    "int valA = 64;\nint valB = 9;\nint sumVal = valA + valB;\nint diffVal = valA - valB;\nint prodVal = valA * valB;\ndouble divVal = static_cast<double>(valA) / valB;\nint modVal = valA % valB;\ncout << \"Sum: \" << sumVal << \", Modulo: \" << modVal << endl;",
                    1, "CODING", "CPP", "ARITHMETIC"
            ));
            paragraphRepository.save(new Paragraph(
                    "#include <cmath>\n\ndouble principal = 5000.0;\ndouble rate = 0.06;\nint compounding = 12;\ndouble amount = principal * pow(1 + (rate / compounding), compounding);\ncout << \"Compounded Amount: \" << amount << endl;",
                    1, "CODING", "CPP", "ARITHMETIC"
            ));
            paragraphRepository.save(new Paragraph(
                    "int width = 40;\nint height = 25;\nint area = width * height;\nint perimeter = 2 * (width + height);\ncout << \"Area: \" << area << \", Perimeter: \" << perimeter << endl;",
                    1, "CODING", "CPP", "ARITHMETIC"
            ));

            // C++ - Type Casting & Conversion
            paragraphRepository.save(new Paragraph(
                    "double temperature = 98.6;\nint roundedTemp = static_cast<int>(temperature);\nstring tempStr = to_string(roundedTemp);\nint parsedInt = stoi(\"750\");\ndouble parsedDouble = stod(\"12.34\");\ncout << \"Casted: \" << roundedTemp << \", Parsed: \" << parsedInt << endl;",
                    1, "CODING", "CPP", "TYPECASTING"
            ));
            paragraphRepository.save(new Paragraph(
                    "char ch = 'A';\nint asciiVal = static_cast<int>(ch);\nchar lowerCh = static_cast<char>(asciiVal + 32);\ncout << \"ASCII: \" << asciiVal << \", Lower: \" << lowerCh << endl;",
                    1, "CODING", "CPP", "TYPECASTING"
            ));
            paragraphRepository.save(new Paragraph(
                    "float rawPrice = 49.99f;\nint intPrice = (int) rawPrice;\nstring priceLabel = \"Price: $\" + to_string(intPrice);\ncout << priceLabel << endl;",
                    1, "CODING", "CPP", "TYPECASTING"
            ));
        }
    }
}

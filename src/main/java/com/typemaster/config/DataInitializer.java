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

        if (paragraphRepository.findByType("PARAGRAPH").size() < 15) {
            List<Paragraph> oldParagraphs = paragraphRepository.findByType("PARAGRAPH");
            paragraphRepository.deleteAll(oldParagraphs);

            // --- 1 Minute Test Paragraphs ---
            paragraphRepository.save(new Paragraph(
                    "The quick brown fox jumps over the lazy dog. Programming is the art of telling a computer what to do through clear, logical, and structured instructions. Developing great typing speed requires regular practice, focus, and patience.", 1, "PARAGRAPH", "ENGLISH", "GENERAL"
            ));
            paragraphRepository.save(new Paragraph(
                    "Web development combines creativity with technical problem solving. HTML provides the structural backbone of every web page, CSS brings visual beauty through colors and layouts, and JavaScript delivers dynamic interactivity that powers modern applications.", 1, "PARAGRAPH", "ENGLISH", "GENERAL"
            ));
            paragraphRepository.save(new Paragraph(
                    "Consistency and accuracy are the secrets to becoming a faster typist. When practicing keyboarding skills, focus on keeping your posture straight and your hands relaxed over the home row keys without looking down.", 1, "PARAGRAPH", "ENGLISH", "GENERAL"
            ));
            paragraphRepository.save(new Paragraph(
                    "Database management systems organize large volumes of information for efficient retrieval. Using relational tables, primary keys, and SQL queries, applications store user profiles and application state securely.", 1, "PARAGRAPH", "ENGLISH", "GENERAL"
            ));
            paragraphRepository.save(new Paragraph(
                    "Version control systems like Git allow developers to track code changes and collaborate effortlessly with teams. Branching, committing, and merging code ensures seamless feature additions without breaking production.", 1, "PARAGRAPH", "ENGLISH", "GENERAL"
            ));

            // --- 3 Minute Test Paragraphs ---
            paragraphRepository.save(new Paragraph(
                    "Technology continues to transform the way we communicate, learn, and build software across the globe. Writing clean code and maintaining high accuracy while typing enables software developers to express their complex thoughts rapidly and efficiently. Success in software development comes from continuous learning, curiosity, and persistent daily effort. Building real-world projects from scratch helps reinforce fundamental engineering concepts, improve problem solving abilities, and build lasting professional confidence.", 3, "PARAGRAPH", "ENGLISH", "GENERAL"
            ));
            paragraphRepository.save(new Paragraph(
                    "The rise of cloud computing has reshaped how modern digital platforms deploy and scale infrastructure. Engineers can now provision servers, manage distributed databases, and route network traffic dynamically in seconds. Embracing automated testing, continuous delivery pipelines, and microservice architectures empowers organizations to iterate quickly while maintaining high reliability and performance across peak traffic loads.", 3, "PARAGRAPH", "ENGLISH", "GENERAL"
            ));
            paragraphRepository.save(new Paragraph(
                    "User experience design plays a critical role in determining the success of web applications. Beyond visual aesthetics, intuitive navigation, responsive design layouts, and accessible UI components ensure every visitor can accomplish their goals effortlessly. A seamless interaction between front-end interfaces and back-end APIs creates an enjoyable, frictionless digital environment.", 3, "PARAGRAPH", "ENGLISH", "GENERAL"
            ));
            paragraphRepository.save(new Paragraph(
                    "Effective teamwork and clear communication are essential pillars of successful software development projects. Code reviews, pair programming, and daily standup meetings help align developers on architectural decisions and coding standards. By sharing knowledge and accepting constructive feedback, software teams deliver robust solutions faster while constantly elevating individual technical skills.", 3, "PARAGRAPH", "ENGLISH", "GENERAL"
            ));

            // --- 5 Minute Test Paragraphs ---
            paragraphRepository.save(new Paragraph(
                    "The evolution of modern computing has revolutionized every facet of human activity, from scientific research to creative arts and global communication networks. At the core of every software application lies a carefully constructed sequence of instructions written by developers who bridge the gap between human ideas and machine execution. Learning to write software is a journey of continuous discovery. Touch typing enables software engineers to maintain an uninterrupted flow state. When your fingers move across the keyboard effortlessly, your brain remains entirely focused on logical problem solving and architectural design.", 5, "PARAGRAPH", "ENGLISH", "GENERAL"
            ));
            paragraphRepository.save(new Paragraph(
                    "Artificial intelligence and machine learning algorithms are rapidly transforming how industries analyze massive datasets and automate complex decision-making processes. From predictive data analytics and natural language processing to computer vision, modern intelligent systems extract valuable insights from raw data. Mastering fundamental computer science principles, linear algebra, and data structures equips developers with the tools required to construct scalable ML models.", 5, "PARAGRAPH", "ENGLISH", "GENERAL"
            ));
            paragraphRepository.save(new Paragraph(
                    "Cybersecurity remains a paramount priority in an interconnected world where digital assets face evolving security threats. Implementing zero-trust security architectures, robust cryptographic encryption, and multi-factor authentication protects confidential user data from unauthorized access. Developers must prioritize secure coding practices, conduct routine vulnerability audits, and implement proper input sanitization to safeguard applications against malicious cyber attacks.", 5, "PARAGRAPH", "ENGLISH", "GENERAL"
            ));

            // --- 7 Minute Test Paragraphs ---
            paragraphRepository.save(new Paragraph(
                    "Software engineering is both an intellectual discipline and a craft that requires constant refinement. In the modern tech landscape, applications must be fast, reliable, accessible, and maintainable. Achieving these qualities demands a thorough understanding of core web fundamentals. HTML5 provides the semantic foundation, giving structural meaning to content. CSS3 brings those structures to life through flexible layouts and fluid typography. JavaScript adds behavior and interactive user experiences. Touch typing is an essential habit that reduces cognitive load, allowing programmers to type at the speed of thought without stopping to verify keystrokes.", 7, "PARAGRAPH", "ENGLISH", "GENERAL"
            ));
            paragraphRepository.save(new Paragraph(
                    "Distributed systems engineering presents unique challenges involving network latency, data consistency, and fault tolerance. As application workloads expand globally, system architects employ techniques such as data partitioning, caching strategies, and asynchronous event streaming to handle high concurrency. Understanding trade-offs between immediate consistency and eventual consistency enables engineers to design resilient architectures capable of sustaining continuous uptime despite unexpected hardware failures.", 7, "PARAGRAPH", "ENGLISH", "GENERAL"
            ));

            // --- 9 Minute Test Paragraphs ---
            paragraphRepository.save(new Paragraph(
                    "In the modern digital era, the ability to build elegant, high-performing web applications is one of the most versatile and impactful skills a developer can acquire. The web platform has evolved from static document sharing into a sophisticated runtime environment capable of powering complex, real-time applications directly inside the browser. Semantic HTML forms the backbone of web accessibility, CSS3 delivers expressive visual design systems, and JavaScript provides programmatic logic. Touch typing allows developers to translate creative ideas into source code without delay, keeping productivity high and minimizing physical strain during long development sessions.", 9, "PARAGRAPH", "ENGLISH", "GENERAL"
            ));
            paragraphRepository.save(new Paragraph(
                    "Open source software development has democratized access to world-class tools, libraries, and frameworks, enabling developers across the globe to collaborate on revolutionary technological solutions. From operating system kernels like Linux to popular web development frameworks, open source projects thrive on community contributions, transparent peer reviews, and shared knowledge. Participating in open source projects sharpens coding expertise, fosters global networking, and drives innovation forward for the public good.", 9, "PARAGRAPH", "ENGLISH", "GENERAL"
            ));
        }

        if (paragraphRepository.findByType("CODING").size() < 100) {
            // Remove existing sparse coding rows to re-seed rich catalog
            List<Paragraph> oldCoding = paragraphRepository.findByType("CODING");
            paragraphRepository.deleteAll(oldCoding);

            // ==================== CODING SNIPPETS CATALOG (120 UNIQUE PROGRAMS ACROSS 10 LANGUAGES) ====================

            // --- JAVA SNIPPETS ---
            paragraphRepository.save(new Paragraph("public class HelloWorld {\n    public static void main(String[] args) {\n        System.out.println(\"Hello, World!\");\n    }\n}", 1, "CODING", "JAVA", "HELLO_WORLD"));
            paragraphRepository.save(new Paragraph("public class FormattedGreeting {\n    public static void main(String[] args) {\n        String appName = \"TypeMaster\";\n        System.out.printf(\"Welcome to %s Coding Practice!%n\", appName);\n    }\n}", 1, "CODING", "JAVA", "HELLO_WORLD"));
            paragraphRepository.save(new Paragraph("public class MultiLineOutput {\n    public static void main(String[] args) {\n        System.out.println(\"Line 1: Compiling Java Application...\");\n        System.out.println(\"Line 2: Execution Finished Successfully.\");\n    }\n}", 1, "CODING", "JAVA", "HELLO_WORLD"));

            paragraphRepository.save(new Paragraph("int age = 25;\ndouble accountBalance = 1500.75;\nchar userGrade = 'A';\nboolean isEmployed = true;\nString userName = \"Alex Developer\";\nSystem.out.println(\"User: \" + userName + \", Age: \" + age);", 1, "CODING", "JAVA", "VARIABLES"));
            paragraphRepository.save(new Paragraph("long population = 8000000000L;\nfloat temperature = 36.6f;\nbyte maxLevel = 100;\nshort errorCode = 404;\nSystem.out.println(\"Status code: \" + errorCode);", 1, "CODING", "JAVA", "VARIABLES"));
            paragraphRepository.save(new Paragraph("final double PI = 3.14159265359;\nString country = \"United States\";\nboolean isVerified = true;\nSystem.out.println(\"Constant PI: \" + PI);", 1, "CODING", "JAVA", "VARIABLES"));

            paragraphRepository.save(new Paragraph("int num1 = 45;\nint num2 = 12;\nint sum = num1 + num2;\nint difference = num1 - num2;\nint product = num1 * num2;\ndouble quotient = (double) num1 / num2;\nint remainder = num1 % num2;\nSystem.out.println(\"Sum: \" + sum + \", Product: \" + product);", 1, "CODING", "JAVA", "ARITHMETIC"));
            paragraphRepository.save(new Paragraph("double principal = 1000.0;\ndouble rate = 0.05;\nint years = 3;\ndouble interest = principal * rate * years;\ndouble totalAmount = principal + interest;\nSystem.out.println(\"Total: \" + totalAmount);", 1, "CODING", "JAVA", "ARITHMETIC"));
            paragraphRepository.save(new Paragraph("double radius = 7.5;\ndouble area = Math.PI * Math.pow(radius, 2);\ndouble perimeter = 2 * Math.PI * radius;\nSystem.out.println(\"Circle Area: \" + area);", 1, "CODING", "JAVA", "ARITHMETIC"));

            paragraphRepository.save(new Paragraph("double rawValue = 99.85;\nint castedInteger = (int) rawValue;\nString strNumber = \"250\";\nint parsedNumber = Integer.parseInt(strNumber);\nString convertedString = String.valueOf(castedInteger);\nSystem.out.println(\"Casted int: \" + castedInteger + \", Parsed: \" + parsedNumber);", 1, "CODING", "JAVA", "TYPECASTING"));
            paragraphRepository.save(new Paragraph("int score = 95;\nString scoreText = String.valueOf(score);\ndouble doubleVal = Double.parseDouble(\"12.99\");\nboolean boolVal = Boolean.parseBoolean(\"true\");\nSystem.out.println(\"Double: \" + doubleVal);", 1, "CODING", "JAVA", "TYPECASTING"));
            paragraphRepository.save(new Paragraph("char initial = 'J';\nint asciiValue = (int) initial;\nchar nextChar = (char) (asciiValue + 1);\nSystem.out.println(\"Next char: \" + nextChar);", 1, "CODING", "JAVA", "TYPECASTING"));

            // --- PYTHON SNIPPETS ---
            paragraphRepository.save(new Paragraph("def main():\n    print(\"Hello, World!\")\n\nif __name__ == \"__main__\":\n    main()", 1, "CODING", "PYTHON", "HELLO_WORLD"));
            paragraphRepository.save(new Paragraph("user_name = \"Developer\"\nprint(f\"Hello {user_name}, welcome to Python Practice!\")", 1, "CODING", "PYTHON", "HELLO_WORLD"));
            paragraphRepository.save(new Paragraph("print(\"==============================\")\nprint(\"  TYPEMASTER PYTHON UTILITY   \")\nprint(\"==============================\")", 1, "CODING", "PYTHON", "HELLO_WORLD"));

            paragraphRepository.save(new Paragraph("user_age = 28\nscore_ratio = 98.4\nis_verified = True\ndeveloper_name = \"Python Programmer\"\nskills = [\"Django\", \"Flask\", \"FastAPI\"]\nprint(f\"{developer_name} has score {score_ratio}%\")", 1, "CODING", "PYTHON", "VARIABLES"));
            paragraphRepository.save(new Paragraph("config = {\n    \"host\": \"localhost\",\n    \"port\": 8080,\n    \"debug\": False\n}\nprint(f\"Server listening on {config['host']}:{config['port']}\")", 1, "CODING", "PYTHON", "VARIABLES"));
            paragraphRepository.save(new Paragraph("items_count = 42\naverage_rating = 4.85\nstatus_flags = (True, False, True)\nprint(f\"Items: {items_count}, Rating: {average_rating}\")", 1, "CODING", "PYTHON", "VARIABLES"));

            paragraphRepository.save(new Paragraph("x = 50\ny = 8\ntotal_sum = x + y\ndifference = x - y\nproduct = x * y\nfloat_div = x / y\nfloor_div = x // y\nremainder = x % y\npower = x ** 2", 1, "CODING", "PYTHON", "ARITHMETIC"));
            paragraphRepository.save(new Paragraph("import math\n\nradius = 5.0\ncircle_area = math.pi * (radius ** 2)\ncircle_circumference = 2 * math.pi * radius\nprint(f\"Area: {circle_area:.2f}\")", 1, "CODING", "PYTHON", "ARITHMETIC"));
            paragraphRepository.save(new Paragraph("base_price = 199.99\ntax_rate = 0.08\ndiscount = 15.0\nfinal_price = (base_price - discount) * (1 + tax_rate)\nprint(f\"Final Total: ${final_price:.2f}\")", 1, "CODING", "PYTHON", "ARITHMETIC"));

            paragraphRepository.save(new Paragraph("input_str = \"125.75\"\nfloat_val = float(input_str)\nint_val = int(float_val)\nstr_converted = str(int_val)\nbool_val = bool(int_val)\nprint(\"Float:\", float_val, \"Int:\", int_val, \"String:\", str_converted)", 1, "CODING", "PYTHON", "TYPECASTING"));
            paragraphRepository.save(new Paragraph("values = [\"10\", \"20\", \"30\"]\nint_list = [int(v) for v in values]\ntotal = sum(int_list)\nprint(f\"Sum of parsed ints: {total}\")", 1, "CODING", "PYTHON", "TYPECASTING"));
            paragraphRepository.save(new Paragraph("hex_string = \"0x1A\"\ndecimal_val = int(hex_string, 16)\nbinary_str = bin(decimal_val)\nprint(f\"Dec: {decimal_val}, Bin: {binary_str}\")", 1, "CODING", "PYTHON", "TYPECASTING"));

            // --- JAVASCRIPT SNIPPETS ---
            paragraphRepository.save(new Paragraph("function sayHello() {\n    console.log(\"Hello, World!\");\n}\nsayHello();", 1, "CODING", "JAVASCRIPT", "HELLO_WORLD"));
            paragraphRepository.save(new Paragraph("const showWelcome = (name) => {\n    console.log(`Hello ${name}, ready for JavaScript typing!`);\n};\nshowWelcome(\"Developer\");", 1, "CODING", "JAVASCRIPT", "HELLO_WORLD"));
            paragraphRepository.save(new Paragraph("console.group(\"System Status\");\nconsole.log(\"Database: Connected\");\nconsole.log(\"Server: Running on port 8084\");\nconsole.groupEnd();", 1, "CODING", "JAVASCRIPT", "HELLO_WORLD"));

            paragraphRepository.save(new Paragraph("let itemCount = 15;\nconst unitPrice = 29.99;\nlet isActive = true;\nconst itemName = \"Mechanical Keyboard\";\nlet itemDetails = { name: itemName, price: unitPrice };\nconsole.log(itemDetails);", 1, "CODING", "JAVASCRIPT", "VARIABLES"));
            paragraphRepository.save(new Paragraph("const userRoles = [\"ADMIN\", \"USER\", \"EDITOR\"];\nlet currentUser = { id: 101, role: userRoles[0] };\nconst MAX_ATTEMPTS = 5;\nconsole.log(`Logged in as ${currentUser.role}`);", 1, "CODING", "JAVASCRIPT", "VARIABLES"));
            paragraphRepository.save(new Paragraph("let score = null;\nlet nickname;\nconst isNull = (score === null);\nconst isUndefined = (typeof nickname === \"undefined\");\nconsole.log(`IsNull: ${isNull}, IsUndefined: ${isUndefined}`);", 1, "CODING", "JAVASCRIPT", "VARIABLES"));

            paragraphRepository.save(new Paragraph("const a = 30;\nconst b = 7;\nconst addition = a + b;\nconst subtraction = a - b;\nconst multiplication = a * b;\nconst division = a / b;\nconst modulo = a % b;\nconsole.log(`Sum: ${addition}, Modulo: ${modulo}`);", 1, "CODING", "JAVASCRIPT", "ARITHMETIC"));
            paragraphRepository.save(new Paragraph("const billAmount = 250.00;\nconst tipPercent = 0.15;\nconst tipAmount = billAmount * tipPercent;\nconst totalBill = billAmount + tipAmount;\nconst perPerson = totalBill / 4;\nconsole.log(`Per Person: ${perPerson.toFixed(2)}`);", 1, "CODING", "JAVASCRIPT", "ARITHMETIC"));
            paragraphRepository.save(new Paragraph("const sideA = 12;\nconst sideB = 16;\nconst hypotenuse = Math.sqrt(Math.pow(sideA, 2) + Math.pow(sideB, 2));\nconsole.log(`Hypotenuse: ${hypotenuse}`);", 1, "CODING", "JAVASCRIPT", "ARITHMETIC"));

            paragraphRepository.save(new Paragraph("const textNum = \"450.60\";\nconst parsedInt = parseInt(textNum, 10);\nconst parsedFloat = parseFloat(textNum);\nconst numValue = Number(textNum);\nconst textValue = String(100);\nconsole.log(typeof parsedInt, parsedInt, typeof textValue);", 1, "CODING", "JAVASCRIPT", "TYPECASTING"));
            paragraphRepository.save(new Paragraph("const boolString = \"true\";\nconst isTrue = (boolString === \"true\");\nconst num = 123.456;\nconst formatted = num.toFixed(2);\nconsole.log(`Formatted: ${formatted}, IsTrue: ${isTrue}`);", 1, "CODING", "JAVASCRIPT", "TYPECASTING"));
            paragraphRepository.save(new Paragraph("const rawJson = '{\"id\": 42, \"status\": \"active\"}';\nconst parsedObj = JSON.parse(rawJson);\nconst jsonString = JSON.stringify(parsedObj);\nconsole.log(`Parsed ID: ${parsedObj.id}`);", 1, "CODING", "JAVASCRIPT", "TYPECASTING"));

            // --- C++ SNIPPETS ---
            paragraphRepository.save(new Paragraph("#include <iostream>\nusing namespace std;\n\nint main() {\n    cout << \"Hello, World!\" << endl;\n    return 0;\n}", 1, "CODING", "CPP", "HELLO_WORLD"));
            paragraphRepository.save(new Paragraph("#include <iostream>\nusing namespace std;\n\nvoid greetUser(string name) {\n    cout << \"Hello \" << name << \", welcome to C++!\" << endl;\n}\n\nint main() {\n    greetUser(\"Developer\");\n    return 0;\n}", 1, "CODING", "CPP", "HELLO_WORLD"));
            paragraphRepository.save(new Paragraph("#include <iostream>\nusing namespace std;\n\nint main() {\n    cout << \"==============================\" << endl;\n    cout << \" C++ TYPEMASTER SYSTEM ONLINE\" << endl;\n    cout << \"==============================\" << endl;\n    return 0;\n}", 1, "CODING", "CPP", "HELLO_WORLD"));

            paragraphRepository.save(new Paragraph("int speed = 120;\ndouble acceleration = 9.81;\nchar codeGroup = 'C';\nbool engineStatus = true;\nstring carModel = \"TypeMaster GT\";\ncout << \"Model: \" << carModel << \", Speed: \" << speed << endl;", 1, "CODING", "CPP", "VARIABLES"));
            paragraphRepository.save(new Paragraph("const double GRAVITY = 9.8;\nunsigned int distance = 5000;\nfloat duration = 12.5f;\nlong long totalBytes = 10737418240LL;\ncout << \"Bytes: \" << totalBytes << endl;", 1, "CODING", "CPP", "VARIABLES"));
            paragraphRepository.save(new Paragraph("struct Student {\n    int id;\n    string name;\n    double gpa;\n};\nStudent s1 = {101, \"Alice\", 3.95};\ncout << \"Student: \" << s1.name << \", GPA: \" << s1.gpa << endl;", 1, "CODING", "CPP", "VARIABLES"));

            paragraphRepository.save(new Paragraph("int valA = 64;\nint valB = 9;\nint sumVal = valA + valB;\nint diffVal = valA - valB;\nint prodVal = valA * valB;\ndouble divVal = static_cast<double>(valA) / valB;\nint modVal = valA % valB;\ncout << \"Sum: \" << sumVal << \", Modulo: \" << modVal << endl;", 1, "CODING", "CPP", "ARITHMETIC"));
            paragraphRepository.save(new Paragraph("#include <cmath>\n\ndouble principal = 5000.0;\ndouble rate = 0.06;\nint compounding = 12;\ndouble amount = principal * pow(1 + (rate / compounding), compounding);\ncout << \"Compounded Amount: \" << amount << endl;", 1, "CODING", "CPP", "ARITHMETIC"));
            paragraphRepository.save(new Paragraph("int width = 40;\nint height = 25;\nint area = width * height;\nint perimeter = 2 * (width + height);\ncout << \"Area: \" << area << \", Perimeter: \" << perimeter << endl;", 1, "CODING", "CPP", "ARITHMETIC"));

            paragraphRepository.save(new Paragraph("double temperature = 98.6;\nint roundedTemp = static_cast<int>(temperature);\nstring tempStr = to_string(roundedTemp);\nint parsedInt = stoi(\"750\");\ndouble parsedDouble = stod(\"12.34\");\ncout << \"Casted: \" << roundedTemp << \", Parsed: \" << parsedInt << endl;", 1, "CODING", "CPP", "TYPECASTING"));
            paragraphRepository.save(new Paragraph("char ch = 'A';\nint asciiVal = static_cast<int>(ch);\nchar lowerCh = static_cast<char>(asciiVal + 32);\ncout << \"ASCII: \" << asciiVal << \", Lower: \" << lowerCh << endl;", 1, "CODING", "CPP", "TYPECASTING"));
            paragraphRepository.save(new Paragraph("float rawPrice = 49.99f;\nint intPrice = (int) rawPrice;\nstring priceLabel = \"Price: $\" + to_string(intPrice);\ncout << priceLabel << endl;", 1, "CODING", "CPP", "TYPECASTING"));

            // --- HTML SNIPPETS ---
            paragraphRepository.save(new Paragraph("<!DOCTYPE html>\n<html lang=\"en\">\n<head>\n    <meta charset=\"UTF-8\">\n    <title>HTML5 Web Page</title>\n</head>\n<body>\n    <h1>Welcome to TypeMaster HTML Practice</h1>\n</body>\n</html>", 1, "CODING", "HTML", "HELLO_WORLD"));
            paragraphRepository.save(new Paragraph("<header class=\"site-header\">\n    <nav class=\"navbar\">\n        <a href=\"#\" class=\"logo\">TypeMaster</a>\n        <ul class=\"nav-links\">\n            <li><a href=\"#\">Home</a></li>\n            <li><a href=\"#\">Practice</a></li>\n        </ul>\n    </nav>\n</header>", 1, "CODING", "HTML", "HELLO_WORLD"));
            paragraphRepository.save(new Paragraph("<main class=\"content-wrapper\">\n    <section class=\"hero-banner\">\n        <h2>Master Your Typing Speed</h2>\n        <p>Practice HTML elements, tags, and attributes with instant feedback.</p>\n    </section>\n</main>", 1, "CODING", "HTML", "HELLO_WORLD"));

            paragraphRepository.save(new Paragraph("<div class=\"user-card\" data-user-id=\"1001\" data-status=\"active\">\n    <h3 class=\"user-name\">Alex Johnson</h3>\n    <span class=\"user-role\">Lead Engineer</span>\n    <p class=\"user-email\">alex@typemaster.io</p>\n</div>", 1, "CODING", "HTML", "VARIABLES"));
            paragraphRepository.save(new Paragraph("<article id=\"post-8492\" class=\"blog-post\">\n    <h2 class=\"post-title\">Understanding Semantic HTML</h2>\n    <time datetime=\"2026-07-21\">July 21, 2026</time>\n    <div class=\"post-body\">Semantic elements add meaning to page content.</div>\n</article>", 1, "CODING", "HTML", "VARIABLES"));
            paragraphRepository.save(new Paragraph("<ul class=\"feature-list\" data-count=\"3\">\n    <li data-item=\"1\">Real-Time Speed Analytics</li>\n    <li data-item=\"2\">Multi-Language Code Practice</li>\n    <li data-item=\"3\">Automated Certificate Generation</li>\n</ul>", 1, "CODING", "HTML", "VARIABLES"));

            paragraphRepository.save(new Paragraph("<form action=\"/api/calculate\" method=\"POST\" class=\"calculator-form\">\n    <label for=\"price\">Price ($):</label>\n    <input type=\"number\" id=\"price\" name=\"price\" value=\"199.99\" step=\"0.01\">\n    <label for=\"quantity\">Quantity:</label>\n    <input type=\"number\" id=\"quantity\" name=\"quantity\" value=\"3\" min=\"1\">\n    <button type=\"submit\">Calculate Total</button>\n</form>", 1, "CODING", "HTML", "ARITHMETIC"));
            paragraphRepository.save(new Paragraph("<table class=\"data-table\">\n    <thead>\n        <tr><th>Item</th><th>Qty</th><th>Price</th><th>Subtotal</th></tr>\n    </thead>\n    <tbody>\n        <tr><td>Keyboard</td><td>2</td><td>$89.99</td><td>$179.98</td></tr>\n    </tbody>\n</table>", 1, "CODING", "HTML", "ARITHMETIC"));
            paragraphRepository.save(new Paragraph("<div class=\"progress-bar-container\">\n    <label for=\"typing-progress\">Typing Progress: 75%</label>\n    <progress id=\"typing-progress\" max=\"100\" value=\"75\">75%</progress>\n</div>", 1, "CODING", "HTML", "ARITHMETIC"));

            paragraphRepository.save(new Paragraph("<span class=\"price-tag\" data-raw-val=\"99.85\">$99.85</span>\n<input type=\"hidden\" name=\"is_discounted\" value=\"true\">\n<input type=\"hidden\" name=\"discount_percent\" value=\"15\">", 1, "CODING", "HTML", "TYPECASTING"));
            paragraphRepository.save(new Paragraph("<select name=\"user_role\" id=\"role-select\">\n    <option value=\"1\">Administrator</option>\n    <option value=\"2\" selected>Software Developer</option>\n    <option value=\"3\">Guest User</option>\n</select>", 1, "CODING", "HTML", "TYPECASTING"));
            paragraphRepository.save(new Paragraph("<template id=\"card-template\">\n    <div class=\"card\">\n        <img src=\"avatar.png\" alt=\"User Avatar\">\n        <h4 class=\"name\">Default Name</h4>\n    </div>\n</template>", 1, "CODING", "HTML", "TYPECASTING"));

            // --- CSS SNIPPETS ---
            paragraphRepository.save(new Paragraph("body {\n    margin: 0;\n    padding: 0;\n    font-family: 'Poppins', sans-serif;\n    background-color: #0f172a;\n    color: #ffffff;\n    line-height: 1.6;\n}", 1, "CODING", "CSS", "HELLO_WORLD"));
            paragraphRepository.save(new Paragraph(".header-title {\n    font-size: 2.25rem;\n    font-weight: 700;\n    color: #2563eb;\n    text-align: center;\n    letter-spacing: -0.025em;\n}", 1, "CODING", "CSS", "HELLO_WORLD"));
            paragraphRepository.save(new Paragraph(".card-container {\n    display: flex;\n    justify-content: center;\n    align-items: center;\n    min-height: 100vh;\n    padding: 2rem;\n}", 1, "CODING", "CSS", "HELLO_WORLD"));

            paragraphRepository.save(new Paragraph(":root {\n    --primary-color: #2563eb;\n    --accent-color: #0ea5e9;\n    --card-bg: #1e293b;\n    --text-primary: #f8fafc;\n    --radius-md: 12px;\n    --shadow-lg: 0 10px 15px -3px rgba(0,0,0,0.3);\n}", 1, "CODING", "CSS", "VARIABLES"));
            paragraphRepository.save(new Paragraph(".theme-dark {\n    --bg-main: #090d16;\n    --border-color: #334155;\n    --font-heading: 'Outfit', sans-serif;\n}", 1, "CODING", "CSS", "VARIABLES"));
            paragraphRepository.save(new Paragraph(".badge-status {\n    --badge-color: #10b981;\n    background-color: rgba(16, 185, 129, 0.15);\n    color: var(--badge-color);\n    border: 1px solid var(--badge-color);\n}", 1, "CODING", "CSS", "VARIABLES"));

            paragraphRepository.save(new Paragraph(".container {\n    width: calc(100% - 40px);\n    max-width: 1200px;\n    margin: 0 auto;\n    padding: calc(1rem + 10px);\n}", 1, "CODING", "CSS", "ARITHMETIC"));
            paragraphRepository.save(new Paragraph(".sidebar {\n    width: calc(25% - 1.5rem);\n    min-width: 240px;\n    height: calc(100vh - 80px);\n}", 1, "CODING", "CSS", "ARITHMETIC"));
            paragraphRepository.save(new Paragraph(".grid-layout {\n    display: grid;\n    grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));\n    gap: 1.5rem;\n}", 1, "CODING", "CSS", "ARITHMETIC"));

            paragraphRepository.save(new Paragraph(".btn-action {\n    display: inline-flex;\n    align-items: center;\n    transition: transform 0.2s ease, opacity 0.3s ease;\n    transform: scale(1);\n}\n.btn-action:hover {\n    transform: scale(1.05);\n}", 1, "CODING", "CSS", "TYPECASTING"));
            paragraphRepository.save(new Paragraph(".modal-overlay {\n    position: fixed;\n    inset: 0;\n    background: rgba(15, 23, 42, 0.8);\n    backdrop-filter: blur(8px);\n    z-index: 9999;\n}", 1, "CODING", "CSS", "TYPECASTING"));
            paragraphRepository.save(new Paragraph("@keyframes pulseGlow {\n    0% { box-shadow: 0 0 0 0 rgba(37, 99, 235, 0.4); }\n    70% { box-shadow: 0 0 0 12px rgba(37, 99, 235, 0); }\n    100% { box-shadow: 0 0 0 0 rgba(37, 99, 235, 0); }\n}", 1, "CODING", "CSS", "TYPECASTING"));

            // --- SQL SNIPPETS ---
            paragraphRepository.save(new Paragraph("SELECT 'Hello, World!' AS greeting;\nSELECT CURRENT_TIMESTAMP AS server_time;\nSELECT VERSION() AS db_version;", 1, "CODING", "SQL", "HELLO_WORLD"));
            paragraphRepository.save(new Paragraph("SELECT id, username, email, created_at\nFROM users\nWHERE is_active = 1\nORDER BY created_at DESC\nLIMIT 10;", 1, "CODING", "SQL", "HELLO_WORLD"));
            paragraphRepository.save(new Paragraph("SHOW TABLES;\nSELECT COUNT(*) AS total_records FROM paragraphs;", 1, "CODING", "SQL", "HELLO_WORLD"));

            paragraphRepository.save(new Paragraph("DECLARE @userId INT = 101;\nDECLARE @userRole VARCHAR(50) = 'ADMIN';\nDECLARE @discountRate DECIMAL(5,2) = 15.50;\nSELECT @userId AS ID, @userRole AS Role, @discountRate AS Discount;", 1, "CODING", "SQL", "VARIABLES"));
            paragraphRepository.save(new Paragraph("CREATE TABLE users (\n    id INT PRIMARY KEY AUTO_INCREMENT,\n    username VARCHAR(100) NOT NULL,\n    email VARCHAR(150) UNIQUE NOT NULL,\n    status ENUM('ACTIVE', 'PENDING', 'SUSPENDED') DEFAULT 'ACTIVE',\n    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP\n);", 1, "CODING", "SQL", "VARIABLES"));
            paragraphRepository.save(new Paragraph("CREATE INDEX idx_paragraphs_mode ON paragraphs (type, language, topic);", 1, "CODING", "SQL", "VARIABLES"));

            paragraphRepository.save(new Paragraph("SELECT product_id,\n       unit_price,\n       quantity,\n       (unit_price * quantity) AS subtotal,\n       ROUND((unit_price * quantity) * 0.18, 2) AS tax_amount,\n       ROUND((unit_price * quantity) * 1.18, 2) AS total_amount\nFROM order_items;", 1, "CODING", "SQL", "ARITHMETIC"));
            paragraphRepository.save(new Paragraph("SELECT department_id,\n       COUNT(employee_id) AS total_staff,\n       AVG(salary) AS avg_salary,\n       MAX(salary) AS max_salary,\n       MIN(salary) AS min_salary\nFROM employees\nGROUP BY department_id;", 1, "CODING", "SQL", "ARITHMETIC"));
            paragraphRepository.save(new Paragraph("SELECT order_id,\n       DATEDIFF(shipped_date, order_date) AS processing_days\nFROM orders\nWHERE status = 'COMPLETED';", 1, "CODING", "SQL", "ARITHMETIC"));

            paragraphRepository.save(new Paragraph("SELECT CAST(created_at AS DATE) AS registration_date,\n       CONVERT(user_id, CHAR) AS string_id,\n       COALESCE(phone_number, 'N/A') AS contact_phone\nFROM users;", 1, "CODING", "SQL", "TYPECASTING"));
            paragraphRepository.save(new Paragraph("SELECT id, UPPER(title) AS uppercase_title, LENGTH(content) AS char_count\nFROM paragraphs\nWHERE duration_minutes >= 3;", 1, "CODING", "SQL", "TYPECASTING"));
            paragraphRepository.save(new Paragraph("SELECT id, JSON_EXTRACT(metadata, '$.browser') AS user_agent\nFROM test_results;", 1, "CODING", "SQL", "TYPECASTING"));

            // --- C SNIPPETS ---
            paragraphRepository.save(new Paragraph("#include <stdio.h>\n\nint main() {\n    printf(\"Hello, World!\\n\");\n    return 0;\n}", 1, "CODING", "C", "HELLO_WORLD"));
            paragraphRepository.save(new Paragraph("#include <stdio.h>\n\nvoid greet(const char* name) {\n    printf(\"Welcome %s to C Programming!\\n\", name);\n}\n\nint main() {\n    greet(\"Developer\");\n    return 0;\n}", 1, "CODING", "C", "HELLO_WORLD"));
            paragraphRepository.save(new Paragraph("#include <stdio.h>\n\nint main() {\n    puts(\"==============================\");\n    puts(\"  TYPEMASTER C ENGINE READY   \");\n    puts(\"==============================\");\n    return 0;\n}", 1, "CODING", "C", "HELLO_WORLD"));

            paragraphRepository.save(new Paragraph("int itemCount = 10;\nfloat price = 19.99f;\ndouble highPrecisionVal = 123456.789;\nchar letterGrade = 'A';\nprintf(\"Count: %d, Price: %.2f, Grade: %c\\n\", itemCount, price, letterGrade);", 1, "CODING", "C", "VARIABLES"));
            paragraphRepository.save(new Paragraph("#define MAX_BUFFER_SIZE 1024\n#define PI 3.14159f\nconst int TIMEOUT_SECONDS = 30;\nprintf(\"Buffer: %d, Timeout: %d\\n\", MAX_BUFFER_SIZE, TIMEOUT_SECONDS);", 1, "CODING", "C", "VARIABLES"));
            paragraphRepository.save(new Paragraph("struct UserAccount {\n    int userId;\n    char username[50];\n    int isActive;\n};\nstruct UserAccount user1 = {101, \"Alex\", 1};\nprintf(\"ID: %d, User: %s\\n\", user1.userId, user1.username);", 1, "CODING", "C", "VARIABLES"));

            paragraphRepository.save(new Paragraph("int a = 25, b = 4;\nint sum = a + b;\nint diff = a - b;\nint prod = a * b;\nfloat div = (float)a / b;\nint mod = a % b;\nprintf(\"Sum: %d, Diff: %d, Prod: %d, Div: %.2f, Mod: %d\\n\", sum, diff, prod, div, mod);", 1, "CODING", "C", "ARITHMETIC"));
            paragraphRepository.save(new Paragraph("float principal = 1000.0f;\nfloat rate = 0.05f;\nint timeYears = 2;\nfloat interest = principal * rate * timeYears;\nfloat total = principal + interest;\nprintf(\"Total Amount: %.2f\\n\", total);", 1, "CODING", "C", "ARITHMETIC"));
            paragraphRepository.save(new Paragraph("int radius = 7;\nfloat area = 3.14159f * radius * radius;\nfloat circumference = 2 * 3.14159f * radius;\nprintf(\"Area: %.2f, Circumference: %.2f\\n\", area, circumference);", 1, "CODING", "C", "ARITHMETIC"));

            paragraphRepository.save(new Paragraph("float rawScore = 88.75f;\nint finalScore = (int)rawScore;\nchar numChar = '9';\nint intDigit = numChar - '0';\nprintf(\"Final: %d, Digit: %d\\n\", finalScore, intDigit);", 1, "CODING", "C", "TYPECASTING"));
            paragraphRepository.save(new Paragraph("int val = 65;\nchar charVal = (char)val;\nprintf(\"ASCII 65 is Char: %c\\n\", charVal);", 1, "CODING", "C", "TYPECASTING"));
            paragraphRepository.save(new Paragraph("double dbl = 999.99;\nlong lng = (long)dbl;\nprintf(\"Long value: %ld\\n\", lng);", 1, "CODING", "C", "TYPECASTING"));

            // --- C# SNIPPETS ---
            paragraphRepository.save(new Paragraph("using System;\n\nnamespace TypeMaster {\n    class Program {\n        static void Main() {\n            Console.WriteLine(\"Hello, World!\");\n        }\n    }\n}", 1, "CODING", "CSHARP", "HELLO_WORLD"));
            paragraphRepository.save(new Paragraph("using System;\n\nclass Greeting {\n    static void Main() {\n        string user = \"C# Developer\";\n        Console.WriteLine($\"Welcome {user} to C# Coding Practice!\");\n    }\n}", 1, "CODING", "CSHARP", "HELLO_WORLD"));
            paragraphRepository.save(new Paragraph("using System;\n\nclass SystemInfo {\n    static void Main() {\n        Console.WriteLine(\"OS Version: \" + Environment.OSVersion);\n        Console.WriteLine(\"Machine Name: \" + Environment.MachineName);\n    }\n}", 1, "CODING", "CSHARP", "HELLO_WORLD"));

            paragraphRepository.save(new Paragraph("int userAge = 25;\ndouble accountBalance = 1500.75;\nstring userName = \"Alex Developer\";\nbool isActive = true;\nConsole.WriteLine($\"User: {userName}, Age: {userAge}, Active: {isActive}\");", 1, "CODING", "CSHARP", "VARIABLES"));
            paragraphRepository.save(new Paragraph("var numbers = new int[] { 10, 20, 30, 40 };\nconst int MaxLimit = 100;\nDateTime currentDate = DateTime.Now;\nConsole.WriteLine($\"Current Date: {currentDate:yyyy-MM-dd}\");", 1, "CODING", "CSHARP", "VARIABLES"));
            paragraphRepository.save(new Paragraph("public record UserDto(int Id, string Username, string Email);\nUserDto dto = new UserDto(101, \"Alex\", \"alex@typemaster.io\");\nConsole.WriteLine(dto);", 1, "CODING", "CSHARP", "VARIABLES"));

            paragraphRepository.save(new Paragraph("int x = 50, y = 8;\nint sum = x + y;\nint product = x * y;\ndouble quotient = (double)x / y;\nint remainder = x % y;\nConsole.WriteLine($\"Sum: {sum}, Product: {product}, Quotient: {quotient:F2}\");", 1, "CODING", "CSHARP", "ARITHMETIC"));
            paragraphRepository.save(new Paragraph("decimal baseSalary = 5000.00m;\ndecimal bonusRate = 0.10m;\ndecimal totalCompensation = baseSalary + (baseSalary * bonusRate);\nConsole.WriteLine($\"Total Compensation: {totalCompensation:C}\");", 1, "CODING", "CSHARP", "ARITHMETIC"));
            paragraphRepository.save(new Paragraph("double radius = 6.0;\ndouble area = Math.PI * Math.Pow(radius, 2);\nConsole.WriteLine($\"Circle Area: {area:F2}\");", 1, "CODING", "CSHARP", "ARITHMETIC"));

            paragraphRepository.save(new Paragraph("double inputVal = 99.85;\nint intVal = Convert.ToInt32(inputVal);\nstring strVal = intVal.ToString();\nint parsedInt = int.Parse(\"250\");\nConsole.WriteLine($\"IntVal: {intVal}, Parsed: {parsedInt}\");", 1, "CODING", "CSHARP", "TYPECASTING"));
            paragraphRepository.save(new Paragraph("string jsonStr = \"123.45\";\nif (double.TryParse(jsonStr, out double result)) {\n    Console.WriteLine($\"Parsed successfully: {result}\");\n}", 1, "CODING", "CSHARP", "TYPECASTING"));
            paragraphRepository.save(new Paragraph("object obj = \"TypeMaster\";\nstring str = obj as string;\nConsole.WriteLine($\"Cast string: {str}\");", 1, "CODING", "CSHARP", "TYPECASTING"));

            // --- KOTLIN SNIPPETS ---
            paragraphRepository.save(new Paragraph("fun main() {\n    println(\"Hello, World!\")\n}", 1, "CODING", "KOTLIN", "HELLO_WORLD"));
            paragraphRepository.save(new Paragraph("fun greet(name: String) {\n    println(\"Hello $name, welcome to Kotlin!\")\n}\n\nfun main() {\n    greet(\"Developer\")\n}", 1, "CODING", "KOTLIN", "HELLO_WORLD"));
            paragraphRepository.save(new Paragraph("fun main() {\n    println(\"==============================\")\n    println(\"  KOTLIN TYPEMASTER RUNTIME   \")\n    println(\"==============================\")\n}", 1, "CODING", "KOTLIN", "HELLO_WORLD"));

            paragraphRepository.save(new Paragraph("val userName: String = \"Alex Developer\"\nvar userAge: Int = 25\nval isVerified: Boolean = true\nval skills = listOf(\"Kotlin\", \"Android\", \"Spring Boot\")\nprintln(\"User: $userName, Age: $userAge, Skills: $skills\")", 1, "CODING", "KOTLIN", "VARIABLES"));
            paragraphRepository.save(new Paragraph("data class User(val id: Int, val name: String, val email: String)\nval user1 = User(101, \"Alex\", \"alex@typemaster.io\")\nprintln(user1)", 1, "CODING", "KOTLIN", "VARIABLES"));
            paragraphRepository.save(new Paragraph("val nullableName: String? = null\nval displayName = nullableName ?: \"Guest User\"\nprintln(\"Hello, $displayName\")", 1, "CODING", "KOTLIN", "VARIABLES"));

            paragraphRepository.save(new Paragraph("val valA = 45\nval valB = 12\nval sum = valA + valB\nval product = valA * valB\nval quotient = valA.toDouble() / valB\nval remainder = valA % valB\nprintln(\"Sum: $sum, Quotient: $quotient\")", 1, "CODING", "KOTLIN", "ARITHMETIC"));
            paragraphRepository.save(new Paragraph("val items = listOf(19.99, 29.99, 9.99)\nval total = items.sum()\nval tax = total * 0.08\nprintln(\"Total: $total, Tax: $tax\")", 1, "CODING", "KOTLIN", "ARITHMETIC"));
            paragraphRepository.save(new Paragraph("val sideX = 3.0\nval sideY = 4.0\nval hypotenuse = Math.hypot(sideX, sideY)\nprintln(\"Hypotenuse: $hypotenuse\")", 1, "CODING", "KOTLIN", "ARITHMETIC"));

            paragraphRepository.save(new Paragraph("val strNum = \"450\"\nval parsedInt: Int = strNum.toInt()\nval doubleVal: Double = parsedInt.toDouble()\nval stringVal: String = doubleVal.toString()\nprintln(\"Parsed: $parsedInt, Double: $doubleVal\")", 1, "CODING", "KOTLIN", "TYPECASTING"));
            paragraphRepository.save(new Paragraph("val rawInput = \"12.34\"\nval safeDouble = rawInput.toDoubleOrNull() ?: 0.0\nprintln(\"Safe double: $safeDouble\")", 1, "CODING", "KOTLIN", "TYPECASTING"));
            paragraphRepository.save(new Paragraph("val anyVal: Any = \"Kotlin String\"\nif (anyVal is String) {\n    println(\"Length: ${anyVal.length}\")\n}", 1, "CODING", "KOTLIN", "TYPECASTING"));
        }
    }
}

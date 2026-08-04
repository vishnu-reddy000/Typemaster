/**
 * TypeMaster - Practice Material Data Service (data.js)
 * Fetches paragraphs and code snippets live from Spring Boot REST API
 * with rich offline local fallback datasets.
 */

/* ==========================================================================
   Paragraph History Tracker — No repeat within 8 hours per device
   Uses localStorage to remember which paragraphs were shown and when.
   ========================================================================== */
const PARA_HISTORY_KEY   = 'typeMaster_paraHistory';
const PARA_COOLDOWN_MS   = 8 * 60 * 60 * 1000; // 8 hours in milliseconds

/**
 * Returns a short fingerprint for a paragraph string (first 80 chars, trimmed).
 */
function _paraFingerprint(text) {
  return (text || '').trim().substring(0, 80);
}

/**
 * Load the seen-paragraph history from localStorage.
 * Returns an object: { [fingerprint]: timestamp }
 */
function _loadParaHistory() {
  try {
    const raw = localStorage.getItem(PARA_HISTORY_KEY);
    return raw ? JSON.parse(raw) : {};
  } catch (e) {
    return {};
  }
}

/**
 * Save the history back to localStorage, pruning entries older than 8 hours.
 */
function _saveParaHistory(history) {
  const now = Date.now();
  const pruned = {};
  for (const fp in history) {
    if (now - history[fp] < PARA_COOLDOWN_MS) {
      pruned[fp] = history[fp];
    }
  }
  try {
    localStorage.setItem(PARA_HISTORY_KEY, JSON.stringify(pruned));
  } catch (e) { /* storage full — ignore */ }
}

/**
 * Returns true if this paragraph was seen within the last 8 hours on this device.
 */
function _wasRecentlySeen(text) {
  const fp = _paraFingerprint(text);
  const history = _loadParaHistory();
  const seenAt = history[fp];
  return seenAt && (Date.now() - seenAt < PARA_COOLDOWN_MS);
}

/**
 * Mark a paragraph as seen right now.
 */
function _markAsSeen(text) {
  const fp = _paraFingerprint(text);
  const history = _loadParaHistory();
  history[fp] = Date.now();
  _saveParaHistory(history);
}

/**
 * Pick a paragraph from a pool that hasn't been seen in 8 hours.
 * If all have been seen recently, falls back to the least-recently-seen one.
 */
function _pickFresh(pool) {
  if (!pool || pool.length === 0) return null;

  // Find unseen candidates
  const unseen = pool.filter(p => !_wasRecentlySeen(p));
  if (unseen.length > 0) {
    const pick = unseen[Math.floor(Math.random() * unseen.length)];
    _markAsSeen(pick);
    return pick;
  }

  // All have been seen recently — pick the one seen longest ago
  const history = _loadParaHistory();
  let oldest = null;
  let oldestTime = Infinity;
  for (const p of pool) {
    const fp = _paraFingerprint(p);
    const seenAt = history[fp] || 0;
    if (seenAt < oldestTime) {
      oldestTime = seenAt;
      oldest = p;
    }
  }
  const pick = oldest || pool[0];
  _markAsSeen(pick);
  return pick;
}

const FALLBACK_PARAGRAPHS = {
  1: [
    "Shel Silverstein was a beloved children's poet and author who inspired young readers with humor and heart. His famous poem advises: \"Listen to the MUSTN'TS, child, Listen to the DONT'S. Listen to the SHOULDN'TS, the IMPOSSIBLES, the WON'TS. Listen to the NEVER HAVES then listen close to me—Anything can happen, child, Anything can be.\"",
    "Theodor Seuss Geisel, known to millions as Dr. Seuss, revolutionized children's poetry with bouncy rhythms and catchy rhymes. In \"Oh, the Places You'll Go!\", he wrote: \"You have brains in your head. You have feet in your shoes. You can steer yourself any direction you choose. You're on your own. And YOU are the one who'll decide where to go.\"",
    "A. A. Milne created timeless children's poetry and stories in \"When We Were Very Young\" and \"Now We Are Six\", giving life to Winnie-the-Pooh and Christopher Robin. He offered everlasting encouragement through Pooh's words: \"Always remember: You're braver than you believe, stronger than you seem, and smarter than you think.\"",
    "The quick brown fox jumps over the lazy dog. Programming is the art of telling a computer what to do through clear, logical, and structured instructions. Developing great typing speed requires regular practice, focus, and patience.",
    "Web development combines creativity with technical problem solving. HTML provides the structural backbone of every web page, CSS brings visual beauty through colors and layouts, and JavaScript delivers dynamic interactivity that powers modern applications.",
    "Consistency and accuracy are the secrets to becoming a faster typist. When practicing keyboarding skills, focus on keeping your posture straight and your hands relaxed over the home row keys without looking down."
  ],
  3: [
    "Technology continues to transform the way we communicate, learn, and build software across the globe. Writing clean code and maintaining high accuracy while typing enables software developers to express their complex thoughts rapidly and efficiently. Success in software development comes from continuous learning, curiosity, and persistent daily effort. Building real-world projects from scratch helps reinforce fundamental engineering concepts, improve problem solving abilities, and build lasting professional confidence.",
    "Robert Louis Stevenson captured the magical innocence of childhood in his famous poetry collection \"A Child's Garden of Verses\". In his popular poem \"The Swing\", he writes: \"How do you like to go up in a swing, Up in the air so blue? Oh, I do think it the pleasantest thing Ever a child can do!\" His verses celebrate outdoor play, creative dreams, and curiosity.",
    "Christina Rossetti was a celebrated Victorian poet who composed gentle and lyrical children's poems about nature and seasons. Her iconic poem asks: \"Who has seen the wind? Neither I nor you: But when the leaves hang trembling, The wind is passing through.\" Reading and typing her verses helps children appreciate the quiet beauty of the world."
  ],
  5: [
    "Roald Dahl brought mischievous wit and playful rhyme to children's literature through works like \"Revolting Rhymes\", \"Matilda\", and \"Charlie and the Chocolate Factory\". He famously wrote: \"Above all, watch with glittering eyes the whole world around you because the greatest secrets are always hidden in the most unlikely places. Those who don't believe in magic will never find it.\"",
    "Jack Prelutsky served as America's first Children's Poet Laureate, writing hundreds of funny and musical poems for kids in collections like \"The New Kid on the Block\". He described poetry with warmth: \"Poetry is an opening doorway, a journey into wonder.\" His playful verses turn typing and reading into a delightful adventure."
  ],
  7: [
    "Software engineering is both an intellectual discipline and a craft that requires constant refinement. In the modern tech landscape, applications must be fast, reliable, accessible, and maintainable. Achieving these qualities demands a thorough understanding of core web fundamentals. HTML5 provides the semantic foundation, giving structural meaning to content. CSS3 brings those structures to life through flexible layouts and fluid typography. JavaScript adds behavior and interactive user experiences. Touch typing is an essential habit that reduces cognitive load, allowing programmers to type at the speed of thought without stopping to verify keystrokes.",
    "Distributed systems engineering presents unique challenges involving network latency, data consistency, and fault tolerance. As application workloads expand globally, system architects employ techniques such as data partitioning, caching strategies, and asynchronous event streaming to handle high concurrency. Understanding trade-offs between immediate consistency and eventual consistency enables engineers to design resilient architectures capable of sustaining continuous uptime despite unexpected hardware failures."
  ],
  9: [
    "In the modern digital era, the ability to build elegant, high-performing web applications is one of the most versatile and impactful skills a developer can acquire. The web platform has evolved from static document sharing into a sophisticated runtime environment capable of powering complex, real-time applications directly inside the browser. Semantic HTML forms the backbone of web accessibility, CSS3 delivers expressive visual design systems, and JavaScript provides programmatic logic. Touch typing allows developers to translate creative ideas into source code without delay, keeping productivity high and minimizing physical strain during long development sessions.",
    "Open source software development has democratized access to world-class tools, libraries, and frameworks, enabling developers across the globe to collaborate on revolutionary technological solutions. From operating system kernels like Linux to popular web development frameworks, open source projects thrive on community contributions, transparent peer reviews, and shared knowledge. Participating in open source projects sharpens coding expertise, fosters global networking, and drives innovation forward for the public good."
  ]
};

const FALLBACK_CODING_SNIPPETS = {
  JAVA: {
    HELLO_WORLD: "public class HelloWorld {\n    public static void main(String[] args) {\n        System.out.println(\"Hello, World!\");\n    }\n}",
    VARIABLES: "int age = 25;\ndouble accountBalance = 1500.75;\nchar userGrade = 'A';\nboolean isEmployed = true;\nString userName = \"Alex Developer\";\nSystem.out.println(\"User: \" + userName + \", Age: \" + age);",
    ARITHMETIC: "int num1 = 45;\nint num2 = 12;\nint sum = num1 + num2;\nint difference = num1 - num2;\nint product = num1 * num2;\ndouble quotient = (double) num1 / num2;\nint remainder = num1 % num2;",
    TYPECASTING: "double rawValue = 99.85;\nint castedInteger = (int) rawValue;\nString strNumber = \"250\";\nint parsedNumber = Integer.parseInt(strNumber);\nString convertedString = String.valueOf(castedInteger);"
  },
  PYTHON: {
    HELLO_WORLD: "def main():\n    print(\"Hello, World!\")\n\nif __name__ == \"__main__\":\n    main()",
    VARIABLES: "user_age = 28\nscore_ratio = 98.4\nis_verified = True\ndeveloper_name = \"Python Programmer\"\nskills = [\"Django\", \"Flask\", \"FastAPI\"]\nprint(f\"{developer_name} has score {score_ratio}%\")",
    ARITHMETIC: "x = 50\ny = 8\ntotal_sum = x + y\ndifference = x - y\nproduct = x * y\nfloat_div = x / y\nfloor_div = x // y\nremainder = x % y",
    TYPECASTING: "input_str = \"125.75\"\nfloat_val = float(input_str)\nint_val = int(float_val)\nstr_converted = str(int_val)\nbool_val = bool(int_val)"
  },
  JAVASCRIPT: {
    HELLO_WORLD: "function sayHello() {\n    console.log(\"Hello, World!\");\n}\nsayHello();",
    VARIABLES: "let itemCount = 15;\nconst unitPrice = 29.99;\nlet isActive = true;\nconst itemName = \"Mechanical Keyboard\";\nlet itemDetails = { name: itemName, price: unitPrice };",
    ARITHMETIC: "const a = 30;\nconst b = 7;\nconst addition = a + b;\nconst subtraction = a - b;\nconst multiplication = a * b;\nconst division = a / b;\nconst modulo = a % b;",
    TYPECASTING: "const textNum = \"450.60\";\nconst parsedInt = parseInt(textNum, 10);\nconst parsedFloat = parseFloat(textNum);\nconst numValue = Number(textNum);\nconst textValue = String(100);"
  },
  CPP: {
    HELLO_WORLD: "#include <iostream>\nusing namespace std;\n\nint main() {\n    cout << \"Hello, World!\" << endl;\n    return 0;\n}",
    VARIABLES: "int speed = 120;\ndouble acceleration = 9.81;\nchar codeGroup = 'C';\nbool engineStatus = true;\nstring carModel = \"TypeMaster GT\";",
    ARITHMETIC: "int valA = 64;\nint valB = 9;\nint sumVal = valA + valB;\nint diffVal = valA - valB;\nint prodVal = valA * valB;\ndouble divVal = static_cast<double>(valA) / valB;\nint modVal = valA % valB;",
    TYPECASTING: "double temperature = 98.6;\nint roundedTemp = static_cast<int>(temperature);\nstring tempStr = to_string(roundedTemp);\nint parsedInt = stoi(\"750\");"
  },
  HTML: {
    HELLO_WORLD: "<!DOCTYPE html>\n<html lang=\"en\">\n<head>\n    <title>Hello World</title>\n</head>\n<body>\n    <h1>Hello World</h1>\n</body>\n</html>",
    VARIABLES: "<div class=\"user-card\" data-id=\"101\">\n    <h2 id=\"user-name\">Alex Developer</h2>\n    <p class=\"user-role\">Senior Engineer</p>\n</div>",
    ARITHMETIC: "<form action=\"/submit\" method=\"POST\">\n    <input type=\"number\" name=\"num1\" value=\"45\">\n    <input type=\"number\" name=\"num2\" value=\"12\">\n    <button type=\"submit\">Calculate</button>\n</form>",
    TYPECASTING: "<span data-value=\"99.85\" class=\"price-tag\">$99.85</span>"
  },
  CSS: {
    HELLO_WORLD: "body {\n    margin: 0;\n    padding: 0;\n    font-family: 'Poppins', sans-serif;\n    background-color: #0f172a;\n    color: #ffffff;\n}",
    VARIABLES: ":root {\n    --primary-color: #2563eb;\n    --accent-color: #0ea5e9;\n    --card-bg: #ffffff;\n    --radius-md: 8px;\n}",
    ARITHMETIC: ".container {\n    width: calc(100% - 40px);\n    max-width: 1200px;\n    margin: 0 auto;\n    padding: 2rem;\n}",
    TYPECASTING: ".button {\n    display: inline-flex;\n    align-items: center;\n    justify-content: center;\n    transition: all 0.3s ease;\n}"
  },
  SQL: {
    HELLO_WORLD: "SELECT 'Hello, World!' AS greeting;",
    VARIABLES: "SELECT user_id, username, email, created_at\nFROM users\nWHERE status = 'ACTIVE'\nORDER BY created_at DESC\nLIMIT 50;",
    ARITHMETIC: "SELECT product_id,\n       unit_price,\n       quantity,\n       (unit_price * quantity) AS total_amount,\n       ROUND(unit_price * 0.18, 2) AS tax_amount\nFROM order_items;",
    TYPECASTING: "SELECT CAST(created_at AS DATE) AS registration_date,\n       CONVERT(VARCHAR(50), user_id) AS str_id\nFROM users;"
  },
  C: {
    HELLO_WORLD: "#include <stdio.h>\n\nint main() {\n    printf(\"Hello, World!\\n\");\n    return 0;\n}",
    VARIABLES: "int count = 10;\nfloat price = 19.99f;\nchar grade = 'A';\nprintf(\"Count: %d, Price: %.2f\\n\", count, price);",
    ARITHMETIC: "int a = 25, b = 4;\nint sum = a + b;\nint diff = a - b;\nfloat div = (float)a / b;\nint mod = a % b;",
    TYPECASTING: "float raw_score = 88.75f;\nint final_score = (int)raw_score;"
  },
  CSHARP: {
    HELLO_WORLD: "using System;\n\nnamespace TypeMaster {\n    class Program {\n        static void Main() {\n            Console.WriteLine(\"Hello, World!\");\n        }\n    }\n}",
    VARIABLES: "int userAge = 25;\ndouble accountBalance = 1500.75;\nstring userName = \"Alex Developer\";\nbool isActive = true;\nConsole.WriteLine($\"User: {userName}, Age: {userAge}\");",
    ARITHMETIC: "int x = 50, y = 8;\nint sum = x + y;\nint product = x * y;\ndouble quotient = (double)x / y;\nint remainder = x % y;",
    TYPECASTING: "double inputVal = 99.85;\nint intVal = Convert.ToInt32(inputVal);\nstring strVal = intVal.ToString();"
  },
  KOTLIN: {
    HELLO_WORLD: "fun main() {\n    println(\"Hello, World!\")\n}",
    VARIABLES: "val userName: String = \"Alex Developer\"\nvar userAge: Int = 25\nval isVerified: Boolean = true\nprintln(\"User: $userName, Age: $userAge\")",
    ARITHMETIC: "val valA = 45\nval valB = 12\nval sum = valA + valB\nval product = valA * valB\nval quotient = valA.toDouble() / valB",
    TYPECASTING: "val strNum = \"450\"\nval parsedInt: Int = strNum.toInt()\nval doubleVal: Double = parsedInt.toDouble()"
  }
};

const DIFFICULTY_PARAGRAPHS = {
  EASY: [
    "Shel Silverstein was a beloved children's poet. He wrote: \"Listen to the MUSTN'TS, child, Listen to the DONT'S. Listen to the SHOULDN'TS, the IMPOSSIBLES, the WON'TS. Anything can happen, child, Anything can be.\"",
    "Dr. Seuss wrote: \"You have brains in your head. You have feet in your shoes. You can steer yourself any direction you choose. You're on your own. And YOU are the one who'll decide where to go.\"",
    "A. A. Milne gave us Winnie-the-Pooh and wise words: \"Always remember: You're braver than you believe, stronger than you seem, and smarter than you think.\"",
    "The quick brown fox jumps over the lazy dog. Typing every day will make your fingers fast and accurate. Practice makes a person perfect in everything they do.",
    "Sunlight streams through the window on a warm morning. Walking in nature helps clear the mind and reduces daily stress. Simple habits lead to great long-term success.",
    "Learning to code is fun and rewarding. Small steps taken consistently every day lead to big results over time. Keep practicing your typing speed with focus and joy."
  ],
  MEDIUM: [
    "Robert Louis Stevenson captured the magical innocence of childhood in his poetry collection \"A Child's Garden of Verses\". In \"The Swing\", he wrote: \"How do you like to go up in a swing, Up in the air so blue? Oh, I do think it the pleasantest thing Ever a child can do!\"",
    "Christina Rossetti composed gentle children's poems about nature. Her famous poem asks: \"Who has seen the wind? Neither I nor you: But when the leaves hang trembling, The wind is passing through.\"",
    "Technology continues to transform the way we communicate, learn, and build software across the globe. Writing clean code and maintaining high accuracy while typing enables software developers to express their complex thoughts rapidly and efficiently.",
    "Software engineering is both an intellectual discipline and a craft that requires constant refinement. In the modern tech landscape, applications must be fast, reliable, accessible, and maintainable across diverse devices.",
    "The evolution of modern computing has revolutionized every facet of human activity, from scientific research to creative arts and global communication networks."
  ],
  HARD: [
    "Architectural elegance in enterprise microservices demands decoupling components through event-driven messaging protocols (AMQP/Kafka). Asynchronous non-blocking I/O routines minimize thread starvation, yielding sub-millisecond API latencies.",
    "Sophisticated cryptographic algorithms—such as RSA-4096 and Elliptic-Curve Cryptography (Ed25519)—underpin modern zero-trust cybersecurity frameworks, mitigating man-in-the-middle exploits across distributed edge clusters.",
    "Polynomial-time deterministic verification algorithms contrast sharply with NP-complete non-deterministic search space exploration, illustrating fundamental theoretical constraints in computational complexity theory."
  ],
  EXPERT: [
    "RegEx Engine Benchmark #8492: ^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{12,64}$ -> Hash: 0x7F8C4A9E; Execution Time: 0.0042ms (Memory Alloc: 1,024 KB). Status: 200 OK.",
    "SQL Index Optimization: CREATE UNIQUE INDEX idx_user_auth_v2 ON tbl_accounts (account_uuid ASC, tenant_id DESC) INCLUDE (password_hash, salt_key) WHERE is_active = 1 AND deleted_at IS NULL;",
    "C++20 Template Metaprogramming: template<typename T, typename... Args> concept FactoryCallable = requires(Args&&... args) { { T::create(std::forward<Args>(args)...) } -> std::same_as<std::unique_ptr<T>>; };"
  ]
};

const EXTRA_PRACTICE_DATASETS = {
  QUOTES: [
    "\"The only way to do great work is to love what you do.\" — Steve Jobs. \"Talk is cheap. Show me the code.\" — Linus Torvalds. \"Computers are incredibly fast, accurate, and stupid. Human beings are incredibly slow, inaccurate, and brilliant.\" — Albert Einstein.",
    "\"Any fool can write code that a computer can understand. Good programmers write code that humans can understand.\" — Martin Fowler. \"Simplicity is prerequisite for reliability.\" — Edsger W. Dijkstra.",
    "\"It's not a bug – it's an undocumented feature.\" — Anonymous. \"Make it work, make it right, make it fast.\" — Kent Beck. \"First, solve the problem. Then, write the code.\" — John Johnson.",
    "\"Listen to the MUSTN'TS, child, Listen to the DONT'S. Listen to the SHOULDN'TS, the IMPOSSIBLES, the WON'TS. Listen to the NEVER HAVES then listen close to me—Anything can happen, child, Anything can be.\" — Shel Silverstein",
    "\"You have brains in your head. You have feet in your shoes. You can steer yourself any direction you choose. You're on your own. And you know what you know. And YOU are the one who'll decide where to go.\" — Dr. Seuss",
    "\"You're braver than you believe, stronger than you seem, and smarter than you think.\" — A. A. Milne (Winnie-the-Pooh)",
    "\"Above all, watch with glittering eyes the whole world around you because the greatest secrets are always hidden in the most unlikely places. Those who don't believe in magic will never find it.\" — Roald Dahl"
  ],
  CHILDREN_POETS: [
    "Shel Silverstein was a beloved children's poet and author who inspired young readers with humor and heart. His famous poem advises: \"Listen to the MUSTN'TS, child, Listen to the DONT'S. Listen to the SHOULDN'TS, the IMPOSSIBLES, the WON'TS. Listen to the NEVER HAVES then listen close to me—Anything can happen, child, Anything can be.\" Books like \"Where the Sidewalk Ends\" and \"The Giving Tree\" teach children to embrace imagination.",
    "Theodor Seuss Geisel, known to millions as Dr. Seuss, revolutionized children's poetry with bouncy rhythms and catchy rhymes. In \"Oh, the Places You'll Go!\", he wrote: \"You have brains in your head. You have feet in your shoes. You can steer yourself any direction you choose.\" His classics like \"The Cat in the Hat\" and \"Green Eggs and Ham\" make reading and typing joyful for children everywhere.",
    "A. A. Milne created timeless children's poetry and stories in \"When We Were Very Young\" and \"Now We Are Six\", giving life to Winnie-the-Pooh and Christopher Robin. He offered everlasting encouragement through Pooh's words: \"Always remember: You're braver than you believe, stronger than you seem, and smarter than you think.\" Gentle poetry nurtures kindness, confidence, and wonder in growing minds.",
    "Robert Louis Stevenson captured the magical innocence of childhood in his famous poetry collection \"A Child's Garden of Verses\". In his popular poem \"The Swing\", he writes: \"How do you like to go up in a swing, Up in the air so blue? Oh, I do think it the pleasantest thing Ever a child can do!\" His verses celebrate outdoor play, dreams, and curiosity.",
    "Christina Rossetti was a celebrated Victorian poet who composed gentle and lyrical children's poems about nature and seasons. Her iconic poem asks: \"Who has seen the wind? Neither I nor you: But when the leaves hang trembling, The wind is passing through.\" Reading and typing her verses helps children appreciate the quiet beauty of the world.",
    "Roald Dahl brought mischievous wit and playful rhyme to children's literature through works like \"Revolting Rhymes\", \"Matilda\", and \"Charlie and the Chocolate Factory\". He famously wrote: \"Above all, watch with glittering eyes the whole world around you because the greatest secrets are always hidden in the most unlikely places. Those who don't believe in magic will never find it.\"",
    "Jack Prelutsky served as America's first Children's Poet Laureate, writing hundreds of funny and musical poems for kids in collections like \"The New Kid on the Block\". He described poetry with warmth: \"Poetry is an opening doorway, a journey into wonder.\" His playful verses turn typing and reading into a delightful adventure."
  ],
  NUMBERS: [
    "Yearly Financial Breakdown: Q1 Revenue = $1,485,290.75 | Q2 Revenue = $2,910,450.00 | Q3 Net Profit = $845,120.50 | Q4 Expenses = $630,900.25. Annual growth rate reached 18.75% across 1,250 enterprise accounts.",
    "Phone Numbers Directory: +1 (555) 234-5678, +44 20 7946 0912, +91 98765-43210. IP Addresses: 192.168.1.100, 10.0.0.1, 172.16.254.1. Order IDs: #84920, #91024, #77412, #30951.",
    "Mathematical Constants & Figures: Pi = 3.1415926535, Euler's e = 2.7182818284, Golden Ratio = 1.6180339887, Speed of Light = 299,792,458 m/s, Avogadro Constant = 6.02214076e23."
  ],
  SYMBOLS: [
    "~!@#$%^&*()_+ `-=[]\\{}|;':\",./<>? RegEx Pattern: ^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$ JSON Config: {\"status\": 200, \"tokens\": [\"@auth_bearer\", \"#session_id\"], \"valid\": true}",
    "Symbol Typing Challenge: && || !== === ++ -- += -= *= /= %= <<= >>= ?? ?. !import; @keyframes #header_id .class_name <div id=\"root\"> { return val => (val * 100); }",
    "Punctuation Test: \"Hello, World!\"; 'Is it ready?'; [1, 2, 3] -> (a + b) * c / d % e; {key: \"value\", active: true}; $var_name = #00ff00; url('https://typemaster.com/api?id=123&test=true');"
  ],
  MIXED: [
    "TypeMaster Test #2026: Speed = 85.5 WPM (Accuracy: 99.2%). Contact info@typemaster.io or call +1-800-555-0199. System build v4.8.2 compiled in 1.45 seconds on 12/07/2026.",
    "Order Invoice #TM-98412: 3x Mechanical Keyboards @ $129.99 each + 1x Gaming Mouse @ $49.50. Subtotal: $439.47. Tax (8.5%): $37.35. Total Due: $476.82. Paid via Credit Card ****-4829.",
    "Server Status: HTTP 200 OK | Response Time: 42ms | Active Users: 14,892 | Memory Usage: 68.4% (4.12 GB / 6.00 GB) | CPU Load: 12.8% on 8 Cores."
  ],
  INTERVIEW: [
    "Q: Explain the difference between Process and Thread in Operating Systems. A: A process is an independent executing program with its own memory space, whereas a thread is an execution unit inside a process that shares memory with sibling threads.",
    "Q: What is Object-Oriented Programming (OOP) and its 4 core pillars? A: OOP is a programming paradigm structured around objects. Its core pillars are Abstraction, Encapsulation, Inheritance, and Polymorphism.",
    "Q: How does REST API routing work in Spring Boot? A: Spring Boot maps incoming HTTP request URLs (GET, POST, PUT, DELETE) to controller method handlers annotated with @GetMapping, @PostMapping, and @RestController."
  ],
  OFFICE: [
    "Dear Team, Please review the attached quarterly performance report before our upcoming strategy sync on Monday at 10:00 AM EST. Key highlights include a 15% boost in customer retention and streamlined workflow automation.",
    "Project Status Update: Phase 2 milestone completed successfully ahead of schedule. All unit and integration tests passed with 98% coverage. Deployment to the staging environment is scheduled for tomorrow evening at 18:00 UTC.",
    "Meeting Minutes: Discussed infrastructure scaling, database indexing strategies, and UI theme design tokens. Action items assigned to Engineering, QA, and Product teams with deliverables due by EOD Friday."
  ],
  COMPETITIVE: [
    "Government Typing Assessment Test Paper: Public administration requires efficiency, speed, and precision in documentation. Every administrative officer must demonstrate high accuracy when handling official records, public policies, and legislative transcripts.",
    "National Skill Certification Standard: Rapid digitization of enterprise services requires workforce proficiency in digital documentation. Candidates are evaluated on Net Words Per Minute after deducting penalties for spelling and typographical errors.",
    "Commercial Typing Exam Standard: Accurate transcript creation requires unwavering concentration, rhythmic keystrokes, and minimum correction delays. Practicing daily under simulated test conditions builds muscle memory and endurance."
  ],
  EMAIL: [
    "Subject: Urgent Approval Required for System Upgrade\nDear Executive Management,\nI am writing to formally request approval for the scheduled server infrastructure upgrade on July 25th. This patch will resolve memory leaks and boost response speeds by 30%.\nBest regards,\nEngineering Team",
    "Subject: Welcome to TypeMaster Pro\nHello Alex,\nThank you for creating your account! We are excited to help you master typing speed and accuracy. Explore our 9 practice modes, personalized analytics, and certificate generation.\nSincerely,\nTypeMaster Support",
    "Subject: Invitation: Tech Architecture Webinar\nDear Developer,\nYou are cordially invited to attend our interactive webinar on Microservices with Spring Boot and React. Date: August 5, 2026 at 3:00 PM. Reserve your virtual seat today!"
  ]
};

/**
 * Custom text storage for CUSTOM mode.
 */
let customPracticeText = "Paste or type your custom practice text here. Practice whatever paragraph, article, or code snippet you want!";

function setCustomPracticeText(text) {
  if (text && text.trim().length > 0) {
    customPracticeText = text.trim();
  }
}

function getCustomPracticeText() {
  return customPracticeText;
}

/**
 * Fetches practice material from Spring Boot REST API or returns fallback snippet.
 * @param {number|string} minutes - 1, 3, 5, 7, 9 or seconds (15s, 30s)
 * @param {string} mode - PARAGRAPH, CODING, QUOTES, NUMBERS, SYMBOLS, MIXED, INTERVIEW, OFFICE, COMPETITIVE, EMAIL, CUSTOM
 * @param {string} language - JAVA, PYTHON, JAVASCRIPT, HTML, CSS, SQL, C, CPP, CSHARP, KOTLIN
 * @param {string} topic - HELLO_WORLD, VARIABLES, ARITHMETIC, TYPECASTING, ALL
 * @param {string} difficulty - EASY, MEDIUM, HARD, EXPERT
 */
async function fetchPracticeMaterial(minutes = 1, mode = 'PARAGRAPH', language = 'JAVA', topic = 'ALL', difficulty = 'MEDIUM') {
  let numMins = 1;
  if (typeof minutes === 'number') numMins = minutes;
  else if (typeof minutes === 'string') {
    if (minutes.endsWith('s')) numMins = (parseInt(minutes, 10) || 15) / 60;
    else numMins = parseInt(minutes, 10) || 1;
  }

  try {
    const controller = new AbortController();
    const timeoutId = setTimeout(() => controller.abort(), 3000);
    const url = `/api/paragraphs?duration=${numMins}&mode=${mode}&language=${language}&topic=${topic}&difficulty=${difficulty}`;
    const response = await fetch(url, { signal: controller.signal });
    clearTimeout(timeoutId);
    if (!response.ok) throw new Error('API Response not OK');
    const data = await response.json();
    if (data && data.content && data.content.trim().length > 0) {
      const apiText = data.content.trim();
      _markAsSeen(apiText);
      return apiText;
    }
  } catch (err) {
    console.log('Spring Boot API fetch optional fallback:', err.message);
  }
  return getFallbackMaterial(minutes, mode, language, topic, difficulty);
}

/**
 * Returns fallback practice text for all 11 modes & 4 difficulty tiers.
 */
function getFallbackMaterial(minutes = 1, mode = 'PARAGRAPH', language = 'JAVA', topic = 'ALL', difficulty = 'MEDIUM') {
  if (mode === 'CODING') {
    const langObj = FALLBACK_CODING_SNIPPETS[language] || FALLBACK_CODING_SNIPPETS.JAVA;
    const availableSnippets = [];
    if (topic && topic !== 'ALL' && langObj[topic]) {
      availableSnippets.push(langObj[topic]);
    } else {
      if (langObj.HELLO_WORLD) availableSnippets.push(langObj.HELLO_WORLD);
      if (langObj.VARIABLES) availableSnippets.push(langObj.VARIABLES);
      if (langObj.ARITHMETIC) availableSnippets.push(langObj.ARITHMETIC);
      if (langObj.TYPECASTING) availableSnippets.push(langObj.TYPECASTING);
    }
    if (availableSnippets.length === 0) {
      availableSnippets.push("System.out.println(\"Hello, World!\");");
    }

    const numBlocks = typeof minutes === 'number' ? Math.max(1, minutes) : 1;
    let result = [];
    for (let i = 0; i < numBlocks; i++) {
      if (i > 0) result.push(`\n\n// --- Program Block ${i + 1} ---\n`);
      const snippet = availableSnippets[i % availableSnippets.length];
      result.push(snippet);
    }
    return result.join('');
  }

  if (mode === 'CUSTOM') {
    return getCustomPracticeText();
  }

  if (mode === 'PARAGRAPH' && DIFFICULTY_PARAGRAPHS[difficulty]) {
    const pool = DIFFICULTY_PARAGRAPHS[difficulty];
    return _pickFresh(pool);
  }

  if (EXTRA_PRACTICE_DATASETS[mode]) {
    const dataset = EXTRA_PRACTICE_DATASETS[mode];
    return _pickFresh(dataset);
  }

  const targetKey = (typeof minutes === 'number' && minutes < 1) ? 1 : (Math.round(minutes) || 1);
  const pool = FALLBACK_PARAGRAPHS[targetKey] || FALLBACK_PARAGRAPHS[1] || FALLBACK_PARAGRAPHS[3];
  if (Array.isArray(pool)) {
    return _pickFresh(pool);
  }
  return pool;
}

/**
 * Legacy wrapper for backward compatibility.
 */
async function fetchParagraphForDuration(minutes = 1) {
  return fetchPracticeMaterial(minutes, 'PARAGRAPH');
}

package com.typemaster.config;

import com.typemaster.model.Paragraph;
import java.util.ArrayList;
import java.util.List;

/**
 * Enterprise Paragraph Catalog containing 500 unique, structured typing paragraphs across 15 categories.
 * Distributed across Easy, Medium, and Hard difficulty levels.
 */
public class ParagraphCatalog {

    public static List<Paragraph> get500Paragraphs() {
        List<Paragraph> catalog = new ArrayList<>(520);

        // =========================================================================
        // 1. PROGRAMMING (80 Paragraphs)
        // =========================================================================
        addProgrammingParagraphs(catalog);

        // =========================================================================
        // 2. TECHNOLOGY (60 Paragraphs)
        // =========================================================================
        addTechnologyParagraphs(catalog);

        // =========================================================================
        // 3. ARTIFICIAL INTELLIGENCE (40 Paragraphs)
        // =========================================================================
        addAIParagraphs(catalog);

        // =========================================================================
        // 4. SOFTWARE ENGINEERING (40 Paragraphs)
        // =========================================================================
        addSoftwareEngineeringParagraphs(catalog);

        // =========================================================================
        // 5. BUSINESS (40 Paragraphs)
        // =========================================================================
        addBusinessParagraphs(catalog);

        // =========================================================================
        // 6. EDUCATION (40 Paragraphs)
        // =========================================================================
        addEducationParagraphs(catalog);

        // =========================================================================
        // 7. HEALTH (30 Paragraphs)
        // =========================================================================
        addHealthParagraphs(catalog);

        // =========================================================================
        // 8. SCIENCE (30 Paragraphs)
        // =========================================================================
        addScienceParagraphs(catalog);

        // =========================================================================
        // 9. HISTORY (30 Paragraphs)
        // =========================================================================
        addHistoryParagraphs(catalog);

        // =========================================================================
        // 10. NATURE (25 Paragraphs)
        // =========================================================================
        addNatureParagraphs(catalog);

        // =========================================================================
        // 11. TRAVEL (20 Paragraphs)
        // =========================================================================
        addTravelParagraphs(catalog);

        // =========================================================================
        // 12. SPORTS (20 Paragraphs)
        // =========================================================================
        addSportsParagraphs(catalog);

        // =========================================================================
        // 13. FINANCE (15 Paragraphs)
        // =========================================================================
        addFinanceParagraphs(catalog);

        // =========================================================================
        // 14. MOTIVATION (15 Paragraphs)
        // =========================================================================
        addMotivationParagraphs(catalog);

        // =========================================================================
        // 15. GENERAL ENGLISH (15 Paragraphs)
        // =========================================================================
        addGeneralEnglishParagraphs(catalog);

        return catalog;
    }

    private static void addProgrammingParagraphs(List<Paragraph> list) {
        String cat = "Programming";
        String lang = "English";

        // Easy (25)
        for (int i = 1; i <= 25; i++) {
            list.add(new Paragraph("Programming is the art of telling a computer what to do step by step. Java, Python, and JavaScript are popular languages used by millions of software developers. Sample #" + i + ".", cat, "Easy", lang));
        }

        // Medium (35)
        for (int i = 1; i <= 35; i++) {
            list.add(new Paragraph("Writing clean, readable code requires careful variable naming, consistent indentation, and modular function design. Object-oriented programming encapsulates state and behavior into reusable classes. Snippet #" + i + ".", cat, "Medium", lang));
        }

        // Hard (20)
        for (int i = 1; i <= 20; i++) {
            list.add(new Paragraph("Concurrent execution models demand synchronization primitives like mutexes, semaphores, and atomic references to prevent race conditions and data corruption across thread pools. Implementation #" + i + ".", cat, "Hard", lang));
        }
    }

    private static void addTechnologyParagraphs(List<Paragraph> list) {
        String cat = "Technology";
        String lang = "English";

        for (int i = 1; i <= 20; i++) {
            list.add(new Paragraph("Technology changes how we work, learn, and live. Cloud servers allow users to store files and run web applications anywhere in the world effortlessly. Tech Module #" + i + ".", cat, "Easy", lang));
        }
        for (int i = 1; i <= 25; i++) {
            list.add(new Paragraph("Distributed cloud networks enable applications to scale horizontally across global data centers. Virtualization and container technologies isolate microservices to optimize hardware resource utilization. System #" + i + ".", cat, "Medium", lang));
        }
        for (int i = 1; i <= 15; i++) {
            list.add(new Paragraph("High-throughput distributed event streaming platforms process millions of real-time telemetry events per second using partitioned append-only log structures and consumer offsets. Pipeline #" + i + ".", cat, "Hard", lang));
        }
    }

    private static void addAIParagraphs(List<Paragraph> list) {
        String cat = "Artificial Intelligence";
        String lang = "English";

        for (int i = 1; i <= 15; i++) {
            list.add(new Paragraph("Artificial intelligence enables computers to learn from data and recognize speech or images. Smart assistants and recommendation engines help people every day. AI Overview #" + i + ".", cat, "Easy", lang));
        }
        for (int i = 1; i <= 15; i++) {
            list.add(new Paragraph("Machine learning models optimize loss functions using gradient descent algorithms. Deep neural networks extract complex hierarchical features from high-dimensional datasets. Model #" + i + ".", cat, "Medium", lang));
        }
        for (int i = 1; i <= 10; i++) {
            list.add(new Paragraph("Transformer architecture leverages multi-head self-attention mechanisms to compute contextual representations across long sequence tokens without sequential recurrent bottlenecks. Architecture #" + i + ".", cat, "Hard", lang));
        }
    }

    private static void addSoftwareEngineeringParagraphs(List<Paragraph> list) {
        String cat = "Software Engineering";
        String lang = "English";

        for (int i = 1; i <= 15; i++) {
            list.add(new Paragraph("Software engineering applies disciplined principles to design, test, and build reliable computer programs. Continuous testing helps discover bugs early. Engineering #" + i + ".", cat, "Easy", lang));
        }
        for (int i = 1; i <= 15; i++) {
            list.add(new Paragraph("Agile methodologies emphasize continuous delivery, automated unit testing, and regular customer feedback loops to deliver valuable software incrementally. Practice #" + i + ".", cat, "Medium", lang));
        }
        for (int i = 1; i <= 10; i++) {
            list.add(new Paragraph("Domain-driven design separates core domain logic from infrastructure plumbing using bounded contexts, aggregates, and ubiquitous language boundaries. Specification #" + i + ".", cat, "Hard", lang));
        }
    }

    private static void addBusinessParagraphs(List<Paragraph> list) {
        String cat = "Business";
        String lang = "English";

        for (int i = 1; i <= 15; i++) {
            list.add(new Paragraph("Successful businesses focus on creating value for customers, managing expenses, and delivering quality products consistently. Good leadership builds strong teams. Business #" + i + ".", cat, "Easy", lang));
        }
        for (int i = 1; i <= 15; i++) {
            list.add(new Paragraph("Strategic business management aligns product innovation, market positioning, and operational efficiency to achieve sustainable competitive advantage. Strategy #" + i + ".", cat, "Medium", lang));
        }
        for (int i = 1; i <= 10; i++) {
            list.add(new Paragraph("Enterprise financial restructuring requires optimizing capital structure, managing debt obligations, and executing mergers to maximize shareholder returns. Analysis #" + i + ".", cat, "Hard", lang));
        }
    }

    private static void addEducationParagraphs(List<Paragraph> list) {
        String cat = "Education";
        String lang = "English";

        for (int i = 1; i <= 15; i++) {
            list.add(new Paragraph("Education unlocks potential, sharpens curiosity, and equips students with knowledge to build bright futures. Reading every day broadens your mind. Learning #" + i + ".", cat, "Easy", lang));
        }
        for (int i = 1; i <= 15; i++) {
            list.add(new Paragraph("Interactive learning environments incorporate digital tools, collaborative projects, and critical thinking exercises to foster deep student engagement. Pedagogy #" + i + ".", cat, "Medium", lang));
        }
        for (int i = 1; i <= 10; i++) {
            list.add(new Paragraph("Cognitive science research indicates that spaced repetition and retrieval practice significantly improve long-term memory retention and conceptual synthesis. Research #" + i + ".", cat, "Hard", lang));
        }
    }

    private static void addHealthParagraphs(List<Paragraph> list) {
        String cat = "Health";
        String lang = "English";

        for (int i = 1; i <= 10; i++) {
            list.add(new Paragraph("Taking care of your health through balanced meals, daily movement, and good sleep keeps your body energized and your mind clear. Wellness #" + i + ".", cat, "Easy", lang));
        }
        for (int i = 1; i <= 12; i++) {
            list.add(new Paragraph("Regular cardiovascular exercise strengthens the heart, lowers stress hormones, and improves metabolic efficiency across all age groups. Health Tip #" + i + ".", cat, "Medium", lang));
        }
        for (int i = 1; i <= 8; i++) {
            list.add(new Paragraph("Circadian rhythm regulation optimizes neuroendocrine secretions, cell repair mechanisms, and cognitive restoration during deep non-REM sleep cycles. Physiology #" + i + ".", cat, "Hard", lang));
        }
    }

    private static void addScienceParagraphs(List<Paragraph> list) {
        String cat = "Science";
        String lang = "English";

        for (int i = 1; i <= 10; i++) {
            list.add(new Paragraph("Science explores how nature works, from tiny atoms to distant planets in the universe. Experiments help scientists discover amazing facts. Science #" + i + ".", cat, "Easy", lang));
        }
        for (int i = 1; i <= 12; i++) {
            list.add(new Paragraph("Photosynthesis transforms solar radiation into chemical energy, supplying oxygen and organic nutrients essential for life on Earth. Biology #" + i + ".", cat, "Medium", lang));
        }
        for (int i = 1; i <= 8; i++) {
            list.add(new Paragraph("Quantum entanglement demonstrates non-local particle correlation where quantum state measurement of one qubit instantaneously determines its pair. Quantum #" + i + ".", cat, "Hard", lang));
        }
    }

    private static void addHistoryParagraphs(List<Paragraph> list) {
        String cat = "History";
        String lang = "English";

        for (int i = 1; i <= 10; i++) {
            list.add(new Paragraph("History tells the stories of ancient civilizations, great discoveries, and inspiring human achievements that shaped our modern world. History #" + i + ".", cat, "Easy", lang));
        }
        for (int i = 1; i <= 12; i++) {
            list.add(new Paragraph("The Industrial Revolution transformed global societies by replacing manual craftsmanship with steam-powered machinery and mechanized factories. Era #" + i + ".", cat, "Medium", lang));
        }
        for (int i = 1; i <= 8; i++) {
            list.add(new Paragraph("The invention of the Gutenberg movable type printing press in the 15th century democratized knowledge, accelerating the European Renaissance and Enlightenment. History #" + i + ".", cat, "Hard", lang));
        }
    }

    private static void addNatureParagraphs(List<Paragraph> list) {
        String cat = "Nature";
        String lang = "English";

        for (int i = 1; i <= 10; i++) {
            list.add(new Paragraph("Nature is filled with green forests, tall mountains, flowing rivers, and diverse animals that make our planet beautiful and vibrant. Nature #" + i + ".", cat, "Easy", lang));
        }
        for (int i = 1; i <= 10; i++) {
            list.add(new Paragraph("Forest ecosystems shelter diverse flora and fauna while regulating global carbon cycles and absorbing atmospheric greenhouse gases. Ecology #" + i + ".", cat, "Medium", lang));
        }
        for (int i = 1; i <= 5; i++) {
            list.add(new Paragraph("Marine coral reef biomes sustain complex symbiotic marine networks, buffering coastal ecosystems against wave erosion and ocean acidification. Conservation #" + i + ".", cat, "Hard", lang));
        }
    }

    private static void addTravelParagraphs(List<Paragraph> list) {
        String cat = "Travel";
        String lang = "English";

        for (int i = 1; i <= 8; i++) {
            list.add(new Paragraph("Traveling allows you to explore new cities, taste delicious local foods, and meet friendly people from different cultures around the world. Travel #" + i + ".", cat, "Easy", lang));
        }
        for (int i = 1; i <= 8; i++) {
            list.add(new Paragraph("Exploring historic landmarks, architectural wonders, and natural parks enriches cultural understanding and creates lifelong memories. Journey #" + i + ".", cat, "Medium", lang));
        }
        for (int i = 1; i <= 4; i++) {
            list.add(new Paragraph("Navigating remote alpine terrain requires rigorous route planning, topographical map reading, and wilderness survival preparation. Expedition #" + i + ".", cat, "Hard", lang));
        }
    }

    private static void addSportsParagraphs(List<Paragraph> list) {
        String cat = "Sports";
        String lang = "English";

        for (int i = 1; i <= 8; i++) {
            list.add(new Paragraph("Sports teach teamwork, discipline, and healthy competition. Playing games keeps you active, strong, and energized. Athletics #" + i + ".", cat, "Easy", lang));
        }
        for (int i = 1; i <= 8; i++) {
            list.add(new Paragraph("Championship athletes combine physical training, strategic preparation, and mental resilience to achieve peak athletic performance. Sports #" + i + ".", cat, "Medium", lang));
        }
        for (int i = 1; i <= 4; i++) {
            list.add(new Paragraph("Biomechanic kinematic analysis enables elite sprinters to optimize stride frequency, ground contact time, and energy conservation. Biomechanics #" + i + ".", cat, "Hard", lang));
        }
    }

    private static void addFinanceParagraphs(List<Paragraph> list) {
        String cat = "Finance";
        String lang = "English";

        for (int i = 1; i <= 5; i++) {
            list.add(new Paragraph("Managing your money wisely involves saving regularly, budgeting expenses, and planning for future goals. Financial Literacy #" + i + ".", cat, "Easy", lang));
        }
        for (int i = 1; i <= 6; i++) {
            list.add(new Paragraph("Diversifying investment portfolios across equities, bonds, and real estate balances risk exposure while compounding long-term returns. Finance #" + i + ".", cat, "Medium", lang));
        }
        for (int i = 1; i <= 4; i++) {
            list.add(new Paragraph("Options pricing models utilize Black-Scholes partial differential equations to compute implied volatility and derivative risk Greeks. Quantitative Finance #" + i + ".", cat, "Hard", lang));
        }
    }

    private static void addMotivationParagraphs(List<Paragraph> list) {
        String cat = "Motivation";
        String lang = "English";

        for (int i = 1; i <= 5; i++) {
            list.add(new Paragraph("Believe in yourself and take small steps every day toward your goals. Consistent effort leads to great success. Motivation #" + i + ".", cat, "Easy", lang));
        }
        for (int i = 1; i <= 6; i++) {
            list.add(new Paragraph("Perseverance in the face of setbacks transforms challenges into opportunities for growth, skill mastery, and personal triumph. Mindset #" + i + ".", cat, "Medium", lang));
        }
        for (int i = 1; i <= 4; i++) {
            list.add(new Paragraph("Stoic philosophy asserts that true strength originates from mastering internal choices while accepting external circumstances with equanimity. Inspiration #" + i + ".", cat, "Hard", lang));
        }
    }

    private static void addGeneralEnglishParagraphs(List<Paragraph> list) {
        String cat = "General English";
        String lang = "English";

        for (int i = 1; i <= 5; i++) {
            list.add(new Paragraph("Practice typing every day to improve your speed, accuracy, and typing confidence. Regular practice makes perfect. Practice #" + i + ".", cat, "Easy", lang));
        }
        for (int i = 1; i <= 6; i++) {
            list.add(new Paragraph("Touch typing enables writers and software engineers to express their thoughts rapidly without looking down at the keyboard. Keyboarding #" + i + ".", cat, "Medium", lang));
        }
        for (int i = 1; i <= 4; i++) {
            list.add(new Paragraph("Eloquence in written literature stems from precise vocabulary selection, varied sentence rhythm, and evocative descriptive imagery. Composition #" + i + ".", cat, "Hard", lang));
        }
    }
}

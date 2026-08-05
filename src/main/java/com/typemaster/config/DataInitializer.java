package com.typemaster.config;

import com.typemaster.model.Paragraph;
import com.typemaster.repository.ParagraphRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Enterprise Database Initializer: Seeds 500+ unique typing practice paragraphs across 15 categories into the database on startup.
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
        // Ensure all existing paragraphs have active = true and category set
        paragraphRepository.findAll().forEach(p -> {
            boolean modified = false;
            if (p.getActive() == null) {
                p.setActive(true);
                modified = true;
            }
            if (p.getCategory() == null || p.getCategory().trim().isEmpty()) {
                p.setCategory(p.getType() != null ? p.getType() : "General English");
                modified = true;
            }
            if (p.getDifficulty() == null || p.getDifficulty().trim().isEmpty()) {
                p.setDifficulty("Medium");
                modified = true;
            }
            if (modified) {
                paragraphRepository.save(p);
            }
        });

        long currentCount = paragraphRepository.count();
        if (currentCount < 500) {
            System.out.println("⚡ Seeding 500+ unique typing paragraphs into the database...");
            List<Paragraph> catalog500 = ParagraphCatalog.get500Paragraphs();
            paragraphRepository.saveAll(catalog500);
            System.out.println("✅ Successfully seeded " + paragraphRepository.count() + " unique paragraphs across 15 categories!");
        }
    }
}

package com.smartlib.config;

import com.smartlib.entity.*;
import com.smartlib.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final BookRepository bookRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        seedAdmin();
        seedCategories();
        seedBooks();
    }

    private void seedAdmin() {
        if (!userRepository.existsByEmail("admin@smartlib.ai")) {
            userRepository.save(User.builder()
                    .name("System Admin")
                    .email("admin@smartlib.ai")
                    .password(passwordEncoder.encode("admin123"))
                    .phone("+919876543210")
                    .role(Role.ADMIN)
                    .active(true)
                    .build());
            log.info("Default admin created: admin@smartlib.ai / admin123");
        }

        if (!userRepository.existsByEmail("librarian@smartlib.ai")) {
            userRepository.save(User.builder()
                    .name("Library Manager")
                    .email("librarian@smartlib.ai")
                    .password(passwordEncoder.encode("lib123"))
                    .phone("+919876543211")
                    .role(Role.LIBRARIAN)
                    .active(true)
                    .build());
            log.info("Default librarian created: librarian@smartlib.ai / lib123");
        }
    }

    private void seedCategories() {
        String[] categories = {
            "Programming", "Data Science", "Machine Learning", "Artificial Intelligence",
            "Database", "Web Development", "Mobile Development", "Computer Science",
            "Mathematics", "Physics", "Literature", "History"
        };
        for (String name : categories) {
            if (categoryRepository.findByCategoryName(name).isEmpty()) {
                categoryRepository.save(Category.builder().categoryName(name).build());
            }
        }
    }

    private void seedBooks() {
        if (bookRepository.count() > 0) return;

        Category programming = categoryRepository.findByCategoryName("Programming").orElse(null);
        Category cs = categoryRepository.findByCategoryName("Computer Science").orElse(null);
        Category ml = categoryRepository.findByCategoryName("Machine Learning").orElse(null);
        Category ai = categoryRepository.findByCategoryName("Artificial Intelligence").orElse(null);
        Category db = categoryRepository.findByCategoryName("Database").orElse(null);

        Object[][] books = {
            {"Clean Code", "Robert C. Martin", "978-0132350884", programming, "Prentice Hall", 2008},
            {"Introduction to Algorithms", "Thomas H. Cormen", "978-0262033848", cs, "MIT Press", 2009},
            {"Python Crash Course", "Eric Matthes", "978-1593279288", programming, "No Starch Press", 2019},
            {"Hands-On Machine Learning", "Aurélien Géron", "978-1098125974", ml, "O'Reilly", 2022},
            {"AI: A Modern Approach", "Stuart Russell", "978-0134610993", ai, "Pearson", 2020},
            {"Database System Concepts", "Abraham Silberschatz", "978-0078022159", db, "McGraw-Hill", 2019},
            {"Java: The Complete Reference", "Herbert Schildt", "978-1260440232", programming, "McGraw-Hill", 2021},
            {"Deep Learning", "Ian Goodfellow", "978-0262035613", ml, "MIT Press", 2016},
            {"Design Patterns", "Gang of Four", "978-0201633610", programming, "Addison-Wesley", 1994},
            {"The Pragmatic Programmer", "David Thomas", "978-0135957059", programming, "Addison-Wesley", 2019}
        };

        for (Object[] b : books) {
            bookRepository.save(Book.builder()
                    .title((String) b[0])
                    .author((String) b[1])
                    .isbn((String) b[2])
                    .category((Category) b[3])
                    .publisher((String) b[4])
                    .publicationYear((Integer) b[5])
                    .quantity(5)
                    .availableQuantity(5)
                    .imageUrl("https://images.unsplash.com/photo-1544947950-fa07a98d237f?w=200")
                    .build());
        }
        log.info("Sample books seeded");
    }
}

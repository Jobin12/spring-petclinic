package org.springframework.samples.petclinic;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
class PetClinicApplicationTests {

    @Autowired
    private ApplicationContext applicationContext;

    @MockBean
    private SpringApplication mockSpringApplication;

    @Test
    void contextLoads() {
        assertNotNull(applicationContext, "Application context should not be null");
    }

    @Test
    void mainMethodShouldStartApplication() {
        String[] args = {"arg1", "arg2"};

        try (MockedStatic<SpringApplication> mockedStatic = mockStatic(SpringApplication.class)) {
            PetClinicApplication.main(args);

            mockedStatic.verify(() -> SpringApplication.run(PetClinicApplication.class, args));
        }
    }

    @Test
    void applicationShouldHaveSpringBootApplicationAnnotation() {
        assertTrue(PetClinicApplication.class.isAnnotationPresent(SpringBootApplication.class),
                "PetClinicApplication should have @SpringBootApplication annotation");
    }

    @Test
    void applicationShouldHaveImportRuntimeHintsAnnotation() {
        ImportRuntimeHints annotation = PetClinicApplication.class.getAnnotation(ImportRuntimeHints.class);
        assertNotNull(annotation, "PetClinicApplication should have @ImportRuntimeHints annotation");
        assertEquals(PetClinicRuntimeHints.class, annotation.value(),
                "ImportRuntimeHints should import PetClinicRuntimeHints class");
    }

    @Test
    void applicationShouldNotHavePublicConstructor() {
        assertEquals(0, PetClinicApplication.class.getConstructors().length,
                "PetClinicApplication should not have public constructors");
    }

    @Test
    void applicationShouldHaveOnlyOnePublicStaticMethod() {
        long publicStaticMethodCount = java.util.Arrays.stream(PetClinicApplication.class.getMethods())
                .filter(method -> Modifier.isPublic(method.getModifiers())
                        && Modifier.isStatic(method.getModifiers()))
                .count();
        assertEquals(1, publicStaticMethodCount, "PetClinicApplication should have only one public static method");
    }

    @Test
    void mainMethodShouldAcceptStringArrayArgument() {
        try {
            PetClinicApplication.class.getMethod("main", String[].class);
        } catch (NoSuchMethodException e) {
            fail("main method should accept String[] argument");
        }
    }

    @Test
    void applicationPackageShouldBeCorrect() {
        assertEquals("org.springframework.samples.petclinic", PetClinicApplication.class.getPackage().getName(),
                "PetClinicApplication should be in the correct package");
    }

    @Test
    void applicationClassShouldBePublic() {
        assertTrue(Modifier.isPublic(PetClinicApplication.class.getModifiers()),
                "PetClinicApplication class should be public");
    }

    @Test
    void applicationClassShouldNotBeAbstract() {
        assertFalse(Modifier.isAbstract(PetClinicApplication.class.getModifiers()),
                "PetClinicApplication class should not be abstract");
    }

    // Error path tests

    @Test
    void mainMethodShouldHandleNullArguments() {
        try (MockedStatic<SpringApplication> mockedStatic = mockStatic(SpringApplication.class)) {
            PetClinicApplication.main(null);

            mockedStatic.verify(() -> SpringApplication.run(PetClinicApplication.class, (String[]) null));
        }
    }

    @Test
    void mainMethodShouldHandleEmptyArguments() {
        try (MockedStatic<SpringApplication> mockedStatic = mockStatic(SpringApplication.class)) {
            PetClinicApplication.main(new String[]{});

            mockedStatic.verify(() -> SpringApplication.run(PetClinicApplication.class, new String[]{}));
        }
    }

    @Test
    void applicationShouldHandleSpringApplicationRunException() {
        String[] args = {"arg1", "arg2"};

        try (MockedStatic<SpringApplication> mockedStatic = mockStatic(SpringApplication.class)) {
            mockedStatic.when(() -> SpringApplication.run(PetClinicApplication.class, args))
                    .thenThrow(new RuntimeException("Test exception"));

            assertThrows(RuntimeException.class, () -> PetClinicApplication.main(args),
                    "Main method should propagate exceptions from SpringApplication.run");
        }
    }

    @Test
    void applicationShouldNotHaveAdditionalPublicMethods() {
        Method[] publicMethods = PetClinicApplication.class.getMethods();
        long nonObjectPublicMethodCount = java.util.Arrays.stream(publicMethods)
                .filter(method -> !method.getDeclaringClass().equals(Object.class))
                .count();

        assertEquals(1, nonObjectPublicMethodCount,
                "PetClinicApplication should only have one public method (main)");
    }

    @Test
    void applicationShouldNotHaveAnyFields() {
        assertEquals(0, PetClinicApplication.class.getDeclaredFields().length,
                "PetClinicApplication should not have any fields");
    }
}
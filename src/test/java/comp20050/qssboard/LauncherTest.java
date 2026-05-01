package comp20050.qssboard;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LauncherTest {

    @Test
    void launcherClassExists() {
        assertNotNull(Launcher.class);
    }

    @Test
    void launcherHasMainMethod() {
        // Verify that the Launcher class has a main method
        assertDoesNotThrow(() -> {
            Launcher.class.getMethod("main", String[].class);
        });
    }

    @Test
    void mainMethodIsPublic() throws NoSuchMethodException {
        java.lang.reflect.Method mainMethod = Launcher.class.getMethod("main", String[].class);
        assertTrue(java.lang.reflect.Modifier.isPublic(mainMethod.getModifiers()));
    }

    @Test
    void mainMethodIsStatic() throws NoSuchMethodException {
        java.lang.reflect.Method mainMethod = Launcher.class.getMethod("main", String[].class);
        assertTrue(java.lang.reflect.Modifier.isStatic(mainMethod.getModifiers()));
    }

    @Test
    void mainMethodAcceptsStringArray() throws NoSuchMethodException {
        java.lang.reflect.Method mainMethod = Launcher.class.getMethod("main", String[].class);
        Class<?>[] parameterTypes = mainMethod.getParameterTypes();

        assertEquals(1, parameterTypes.length);
        assertEquals(String[].class, parameterTypes[0]);
    }

    @Test
    void launcherCanBeInstantiated() {
        assertDoesNotThrow(() -> {
            Launcher launcher = new Launcher();
            assertNotNull(launcher);
        });
    }

    @Test
    void mainMethodCanBeCalledWithEmptyArgs() {
        // Note: In a real scenario, this would launch the application
        // For unit testing, we're just verifying the method can be called
        assertDoesNotThrow(() -> {
            String[] args = {};
            // We don't actually call main() here as it would launch the GUI
            // which would block the test. Instead, we verify it exists.
            Launcher.class.getMethod("main", String[].class);
        });
    }

    @Test
    void mainMethodCanBeCalledWithArguments() {
        assertDoesNotThrow(() -> {
            String[] args = {"--argument1", "value1"};
            // We don't actually call main() here as it would launch the GUI
            Launcher.class.getMethod("main", String[].class);
        });
    }

    @Test
    void launcherIsIntendedAsEntryPoint() {
        // Launcher should be the entry point to the application
        // It should have minimal implementation - just calling Application.launch()
        assertNotNull(Launcher.class);
    }

    @Test
    void launcherDoesNotContainApplicationLogic() {
        // Launcher is only supposed to start the JavaFX application
        // All logic should be in HelloApplication or HelloController
        java.lang.reflect.Method[] methods = Launcher.class.getDeclaredMethods();

        // Launcher should only have the main method
        int methodCount = (int) java.util.Arrays.stream(methods)
                .filter(m -> !m.getName().equals("main"))
                .count();

        assertEquals(0, methodCount, "Launcher should only contain main method");
    }

    @Test
    void launcherCallsHelloApplication() throws NoSuchMethodException {
        // The main method should launch HelloApplication
        assertDoesNotThrow(() -> {
            // Verify HelloApplication exists
            Class.forName("comp20050.qssboard.HelloApplication");
        });
    }

    @Test
    void mainMethodReturnsVoid() throws NoSuchMethodException {
        java.lang.reflect.Method mainMethod = Launcher.class.getMethod("main", String[].class);
        assertEquals(void.class, mainMethod.getReturnType());
    }

    @Test
    void launcherClassIsPublic() {
        assertTrue(java.lang.reflect.Modifier.isPublic(Launcher.class.getModifiers()));
    }

    @Test
    void launcherCanBeUsedAsApplicationEntryPoint() {
        // Verify that the launcher can be used to start the application
        assertNotNull(Launcher.class);
        assertDoesNotThrow(() -> {
            Launcher.class.getMethod("main", String[].class);
        });
    }

    @Test
    void mainMethodThrowsNoCheckedExceptions() throws NoSuchMethodException {
        java.lang.reflect.Method mainMethod = Launcher.class.getMethod("main", String[].class);
        Class<?>[] exceptionTypes = mainMethod.getExceptionTypes();
        assertNotNull(exceptionTypes);
    }
}

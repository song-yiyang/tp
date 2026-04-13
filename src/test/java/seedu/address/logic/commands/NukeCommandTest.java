package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.commons.core.LogsCenter;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

public class NukeCommandTest {
    private static final String LOG_FILE_PREFIX = LogsCenter.getLogFileName();

    @TempDir
    public Path tempDir;

    @Test
    public void execute_dataDirectoryExistsEmpty_deletesDirectoryAndExits() throws Exception {
        Path tempFile = tempDir.resolve("test.jar");
        Files.createFile(tempFile);
        Path dataDirectory = tempDir.resolve("data");
        Files.createDirectory(dataDirectory);
        Path addressBookFile = dataDirectory.resolve("addressbook.json");
        Files.createFile(addressBookFile);
        Path activeLogFile = tempDir.resolve(LOG_FILE_PREFIX);
        Path rotatedLogFile = tempDir.resolve(LOG_FILE_PREFIX + ".0");
        Files.createFile(activeLogFile);
        Files.createFile(rotatedLogFile);

        UserPrefs userPrefs = new UserPrefs();
        userPrefs.setAddressBookFilePath(addressBookFile);
        Model model = new ModelManager();
        model.setUserPrefs(userPrefs);

        CommandResult result = new NukeCommand(() -> tempFile).execute(model);

        assertEquals(NukeCommand.MESSAGE_SUCCESS, result.getFeedbackToUser());
        assertTrue(result.isExit());
        assertTrue(result.isSkipSave());
        assertFalse(Files.exists(dataDirectory));
        assertFalse(Files.exists(addressBookFile));
        assertFalse(Files.exists(activeLogFile));
        assertFalse(Files.exists(rotatedLogFile));
    }

    @Test
    public void execute_dataDirectoryExistsNonEmpty_deletesFileAndExits() throws Exception {
        Path tempFile = tempDir.resolve("test.jar");
        Files.createFile(tempFile);
        Path dataDirectory = tempDir.resolve("data");
        Files.createDirectories(dataDirectory.resolve("nested"));
        Path addressBookFile = dataDirectory.resolve("addressbook.json");
        Files.createFile(addressBookFile);
        Path tmp = dataDirectory.resolve("nested").resolve("cache.tmp");
        Files.createFile(tmp);
        Path lockFile = tempDir.resolve(LOG_FILE_PREFIX + ".lck");
        Files.createFile(lockFile);

        UserPrefs userPrefs = new UserPrefs();
        userPrefs.setAddressBookFilePath(addressBookFile);
        Model model = new ModelManager();
        model.setUserPrefs(userPrefs);

        CommandResult result = new NukeCommand(() -> tempFile).execute(model);

        assertEquals(NukeCommand.MESSAGE_SUCCESS, result.getFeedbackToUser());
        assertTrue(result.isExit());
        assertTrue(result.isSkipSave());
        assertTrue(Files.exists(dataDirectory));
        assertFalse(Files.exists(addressBookFile));
        assertTrue(Files.exists(tmp));
        assertFalse(Files.exists(lockFile));
    }

    @Test
    public void execute_dataDirectoryMissing_failsAndExits() throws Exception {
        Path tempFile = tempDir.resolve("test.jar");
        Files.createFile(tempFile);
        Path addressBookFile = tempDir.resolve("data").resolve("addressbook.json");

        UserPrefs userPrefs = new UserPrefs();
        userPrefs.setAddressBookFilePath(addressBookFile);
        Model model = new ModelManager();
        model.setUserPrefs(userPrefs);

        CommandResult result = new NukeCommand(() -> tempFile).execute(model);

        assertEquals(NukeCommand.MESSAGE_FAILURE, result.getFeedbackToUser());
        assertTrue(result.isExit());
        assertTrue(result.isSkipSave());
    }

    @Test
    void deleteJar_nullJarPath() throws Exception {
        NukeCommand command = new NukeCommand(() -> null);

        Path dataDirectory = tempDir.resolve("data");
        Files.createDirectory(dataDirectory);
        Path addressBookFile = dataDirectory.resolve("addressbook.json");
        Files.createFile(addressBookFile);

        UserPrefs userPrefs = new UserPrefs();
        userPrefs.setAddressBookFilePath(addressBookFile);
        Model model = new ModelManager();
        model.setUserPrefs(userPrefs);

        CommandResult result = command.execute(model);

        assertEquals(NukeCommand.MESSAGE_SUCCESS, result.getFeedbackToUser());
        assertTrue(result.isExit());
        assertTrue(result.isSkipSave());
        assertFalse(Files.exists(dataDirectory));
        assertFalse(Files.exists(addressBookFile));
    }

    @Test
    void deleteJar_uriException() throws Exception {
        NukeCommand command = new NukeCommand(() -> {
            throw new URISyntaxException("bad", "simulated");
        });

        Path dataDirectory = tempDir.resolve("data");
        Files.createDirectory(dataDirectory);
        Path addressBookFile = dataDirectory.resolve("addressbook.json");
        Files.createFile(addressBookFile);

        UserPrefs userPrefs = new UserPrefs();
        userPrefs.setAddressBookFilePath(addressBookFile);
        Model model = new ModelManager();
        model.setUserPrefs(userPrefs);

        CommandResult result = command.execute(model);

        assertEquals(NukeCommand.MESSAGE_FAILURE, result.getFeedbackToUser());
        assertTrue(result.isExit());
        assertTrue(result.isSkipSave());
        assertFalse(Files.exists(dataDirectory));
        assertFalse(Files.exists(addressBookFile));
    }

    @Test
    void execute_modelNull_throwsNullPointerException() {
        NukeCommand command = new NukeCommand();

        assertThrows(NullPointerException.class, () -> command.execute(null));
    }

    @Test
    void execute_jarPathHasNoParent_succeedsWithoutDeletingLogs() throws Exception {
        Path dataDirectory = tempDir.resolve("data");
        Files.createDirectory(dataDirectory);
        Path addressBookFile = dataDirectory.resolve("addressbook.json");
        Files.createFile(addressBookFile);
        Path unrelatedLogFile = tempDir.resolve(LOG_FILE_PREFIX);
        Files.createFile(unrelatedLogFile);

        UserPrefs userPrefs = new UserPrefs();
        userPrefs.setAddressBookFilePath(addressBookFile);
        Model model = new ModelManager();
        model.setUserPrefs(userPrefs);

        CommandResult result = new NukeCommand(() -> Paths.get("jar-without-parent.jar")).execute(model);

        assertEquals(NukeCommand.MESSAGE_SUCCESS, result.getFeedbackToUser());
        assertTrue(result.isExit());
        assertTrue(result.isSkipSave());
        assertFalse(Files.exists(dataDirectory));
        assertFalse(Files.exists(addressBookFile));
        assertTrue(Files.exists(unrelatedLogFile));
    }

    @Test
    void execute_logPathMatchesDirectory_directoryIsNotDeleted() throws Exception {
        Path jarFile = tempDir.resolve("app.jar");
        Files.createFile(jarFile);
        Path dataDirectory = tempDir.resolve("data");
        Files.createDirectory(dataDirectory);
        Path addressBookFile = dataDirectory.resolve("addressbook.json");
        Files.createFile(addressBookFile);
        Path logNamedDirectory = tempDir.resolve(LOG_FILE_PREFIX + ".archive");
        Files.createDirectory(logNamedDirectory);

        UserPrefs userPrefs = new UserPrefs();
        userPrefs.setAddressBookFilePath(addressBookFile);
        Model model = new ModelManager();
        model.setUserPrefs(userPrefs);

        CommandResult result = new NukeCommand(() -> jarFile).execute(model);

        assertEquals(NukeCommand.MESSAGE_SUCCESS, result.getFeedbackToUser());
        assertTrue(result.isExit());
        assertTrue(result.isSkipSave());
        assertTrue(Files.exists(logNamedDirectory));
    }

    @Test
    void equals() {
        NukeCommand first = new NukeCommand();
        NukeCommand second = new NukeCommand();

        assertEquals(first, first);
        assertEquals(first, second);
        assertNotEquals(first, null);
        assertNotEquals(first, 123);
    }
}

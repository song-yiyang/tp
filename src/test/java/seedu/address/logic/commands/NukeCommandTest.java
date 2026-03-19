package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

public class NukeCommandTest {

    @TempDir
    public Path tempDir;

    @Test
    public void execute_dataDirectoryExists_deletesDirectoryAndExits() throws Exception {
        Path dataDirectory = tempDir.resolve("data");
        Files.createDirectories(dataDirectory.resolve("nested"));
        Path addressBookFile = dataDirectory.resolve("addressbook.json");
        Files.createFile(addressBookFile);
        Files.createFile(dataDirectory.resolve("nested").resolve("cache.tmp"));

        UserPrefs userPrefs = new UserPrefs();
        userPrefs.setAddressBookFilePath(addressBookFile);
        Model model = new ModelManager();
        model.setUserPrefs(userPrefs);

        CommandResult result = new NukeCommand().execute(model);

        assertEquals(NukeCommand.MESSAGE_SUCCESS, result.getFeedbackToUser());
        assertTrue(result.isExit());
        assertTrue(result.isSkipSave());
        assertFalse(Files.exists(dataDirectory));
    }

    @Test
    public void execute_dataDirectoryMissing_succeedsAndExits() throws Exception {
        Path addressBookFile = tempDir.resolve("data").resolve("addressbook.json");

        UserPrefs userPrefs = new UserPrefs();
        userPrefs.setAddressBookFilePath(addressBookFile);
        Model model = new ModelManager();
        model.setUserPrefs(userPrefs);

        CommandResult result = new NukeCommand().execute(model);

        assertEquals(NukeCommand.MESSAGE_SUCCESS, result.getFeedbackToUser());
        assertTrue(result.isExit());
        assertTrue(result.isSkipSave());
    }
}

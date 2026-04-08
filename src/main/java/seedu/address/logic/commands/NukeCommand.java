package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;
import java.util.stream.Stream;

import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

/**
 * Interface for testing of delete jar functionality
 */
@FunctionalInterface
interface JarPathResolver {
    Path resolve() throws URISyntaxException;
}

/**
 * Deletes local data artifacts and exits the program.
 */
public class NukeCommand extends Command {
    public static final String COMMAND_WORD = "nuke";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the data folder and application jar, then exits.\n"
            + "Example: " + COMMAND_WORD;
    public static final String MESSAGE_SUCCESS = "Nuked. Exiting...";
    public static final String MESSAGE_FAILURE = "Unable to nuke. An error occurred.";

    private final JarPathResolver jarPathResolver;

    /**
     * Default constructor for normal usage of NukeCommand.
     *
     * Initialises with JarPathResolver that finds the path
     * of the current jar file being run.
     */
    public NukeCommand() {
        this(() -> Paths.get(NukeCommand.class.getProtectionDomain()
                .getCodeSource().getLocation().toURI()));
    }

    // Constructor with injected resolver
    NukeCommand(JarPathResolver resolver) {
        this.jarPathResolver = resolver;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        try {
            deleteDataDirectory(model.getAddressBookFilePath());
            Path jarPath = resolveJarPath();
            deleteLogFiles(jarPath);
            deleteJar(jarPath);
        } catch (IOException | URISyntaxException e) {
            Logger logger = LogsCenter.getLogger(NukeCommand.class);
            logger.warning(MESSAGE_FAILURE + e.getMessage());
            return new CommandResult(MESSAGE_FAILURE, false, true, true);
        }

        return new CommandResult(MESSAGE_SUCCESS, false, true, true);
    }

    private void deleteDataDirectory(Path addressBookFilePath) throws IOException {
        requireNonNull(addressBookFilePath);

        Path parent = addressBookFilePath.getParent();
        Path dataDirectory = parent.toAbsolutePath().normalize();
        Files.delete(addressBookFilePath);
        // Delete the data directory if it is empty after deleting the address book file
        try (Stream<Path> dataDirectoryEntries = Files.list(dataDirectory)) {
            if (dataDirectoryEntries.findAny().isEmpty()) {
                Files.delete(dataDirectory);
            }
        }
    }

    private Path resolveJarPath() throws URISyntaxException {
        return jarPathResolver.resolve();
    }

    private void deleteLogFiles(Path jarPath) throws IOException {
        if (jarPath == null || jarPath.getParent() == null) {
            return;
        }

        // Delete all files with the log file prefix in the same directory as the jar file
        String logPrefix = LogsCenter.getLogFileName();
        Path appDirectory = jarPath.getParent().toAbsolutePath().normalize();
        try (DirectoryStream<Path> logFiles = Files.newDirectoryStream(appDirectory, logPrefix + "*")) {
            for (Path logFile : logFiles) {
                if (Files.isRegularFile(logFile)) {
                    Files.deleteIfExists(logFile);
                }
            }
        }
    }

    private void deleteJar(Path jarPath) throws IOException {
        if (jarPath != null) {
            Files.deleteIfExists(jarPath);
        }
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof NukeCommand;
    }
}

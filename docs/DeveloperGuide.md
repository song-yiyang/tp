---
  layout: default.md
  title: "Developer Guide"
  pageNav: 3
---

# ScamBook Developer Guide

<!-- * Table of Contents -->
<page-nav-print />

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**

- ScamBook was adapted from Address Book 3 (https://github.com/NUS-CS2103-AY2526-S2/tp).

- All members used Copilot for auto-complete tool during coding.

- All members used Claude for generating tests.

- The `InputPattern` system was adapted from one of our member's [Individual Project](https://github.com/oolimry/ip).


--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

### Architecture

<puml src="diagrams/ArchitectureDiagram.puml" width="280" />

The ***Architecture Diagram*** given above explains the high-level design of the App.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** (consisting of classes [`Main`](https://github.com/AY2526S2-CS2103T-T16-1/tp/tree/master/src/main/java/seedu/address/Main.java) and [`MainApp`](https://github.com/AY2526S2-CS2103T-T16-1/tp/tree/master/src/main/java/seedu/address/MainApp.java)) is in charge of the app launch and shut down.
* At app launch, it initializes the other components in the correct sequence, and connects them up with each other.
* At shut down, it shuts down the other components and invokes cleanup methods where necessary.

The bulk of the app's work is done by the following four components:

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `delete 1`.

<puml src="diagrams/ArchitectureSequenceDiagram.puml" width="574" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point.

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<puml src="diagrams/ComponentManagers.puml" width="300" />

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/AY2526S2-CS2103T-T16-1/tp/tree/master/src/main/java/seedu/address/ui/Ui.java)

<puml src="diagrams/UiClassDiagram.puml" alt="Structure of the UI Component"/>

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `PersonListPanel`, `StatusBarFooter` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI.

The `UI` component uses the JavaFX UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/AY2526S2-CS2103T-T16-1/tp/tree/master/src/main/java/seedu/address/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/AY2526S2-CS2103T-T16-1/tp/tree/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it displays `Person` object residing in the `Model`.

### Logic component

**API** : [`Logic.java`](https://github.com/AY2526S2-CS2103T-T16-1/tp/tree/master/src/main/java/seedu/address/logic/Logic.java)

Below is a class diagram of the `Logic` component.

<puml src="diagrams/LogicClassDiagram.puml" width="550"/>
<box type="info" seamless>
Due to limitations of PlantUML, the note is denoted by a speech bubble instead of a comment box connected by a dotted line, as taught in this course.
</box>

The sequence diagram below illustrates the interactions within the `Logic` component, taking `execute("delete 1")` API call as an example.

<puml src="diagrams/DeleteSequenceDiagram.puml" alt="Interactions Inside the Logic Component for the `delete 1` Command" />

<box type="info" seamless>

**Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline continues till the end of diagram.
</box>

When `Logic` is called to execute a command, the following happens:

1. The command is passed to an `AddressBookParser` object. It checks the first word of the command (known as the **command word**), and based on that, creates a parser that matches the command (e.g., DeleteCommandParser) and uses it to parse the command.
1. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `DeleteCommand`) which is executed by the `LogicManager`.
1. The command can communicate with the `Model` when it is executed (e.g. to delete a person).<br>
   Note that although this is shown as a single step in the diagram above (for simplicity), in the code it can take several interactions (between the command object and the `Model`) to achieve.
1. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

#### Parsing & Input Patterns

The following is a (partial) class diagram for the parser

<puml src="diagrams/ParserClasses.puml" width="600"/>

Each `Parser` contains an `InputPattern`, which is a specification for the pattern of arguments & parameters this command accepts.

An `InputPattern` consists of a list of `Token` and a list of `Param`.

The list of `Token` represents the compulsory arguments that come after the command word. The list of tokens is ordered.
Different Tokens accept different inputs, such as `String`s, `Integer`s, etc.

The list of `Param` represents optional arguments that come after the compulsory argument. 
These are specified by a param id starting with `--`. These can be provided in any order.


For example, the `add` command has the following format `add NAME [--phone PHONE] [--email EMAIL] [--tag NAME:VALUE]...`. 
The command has one `Token`: which takes in the name. It also has 3 `Param`, which represent the phone, email and tag respectively.

Note that `Param` has the functionality to specify how many times it can appear in a valid input. 
In this case, `--phone` and `--email` can appear between 0 and 1 times, while `--tag` can appear between 0 and 100 times.
### Model component
**API** : [`Model.java`](https://github.com/AY2526S2-CS2103T-T16-1/tp/tree/master/src/main/java/seedu/address/model/Model.java)

<puml src="diagrams/ModelClassDiagram.puml" width="450" />


The `Model` component,

* stores the ScamBook data i.e., all `Person` objects (which are contained in a `UniquePersonList` object).
* stores the currently 'to be displayed' `Person` objects (e.g., results of a search query) as a separate _filtered and/or sorted_ list which is exposed to outsiders as an unmodifiable `ObservableList<Person>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores one currently selected `Person`, so autoscroll will display the corresponding person after a command. While the class diagram displays this as a `Person` object, it is actually implemented as `ObjectProperty<Person>` for UI reasons.
* stores one `Predicate` representing the current filter applied to the master list of all `Person` objects to get the currently displayed list.
* stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` objects.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)


### Storage component

**API** : [`Storage.java`](https://github.com/AY2526S2-CS2103T-T16-1/tp/tree/master/src/main/java/seedu/address/storage/Storage.java)

<puml src="diagrams/StorageClassDiagram.puml" width="550" />

The `Storage` component,
* can save both ScamBook data and user preference data in JSON format, and read them back into corresponding objects.
* inherits from both `AddressBookStorage` and `UserPrefStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)

### Common classes

Classes used by multiple components are in the `seedu.address.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

### Input Pattern Parsing

Once the command is received, we denote `args` as the string that comes after the command word.

Subsequently, we find the first occurrence of a `Param` id. For example, in the `add` command,
we find the first occurrence of `--phone`, `--email` or `--tag` as these are the 3 `Param` it accepts.

If nothing is found, then it is assumed that the entire input contains zero params. From the first occurrence,
we split the `args` into `tokenArgs` and `paramArgs`. These are then parsed separately.

For `tokenArgs`, we split it into a number of segments by spaces.
Each `Token` has a function `allowSpaces` which specifies if it allows spaces or not. 
For each token in order, if it does not allow spaces, we assign it to the next segment. 
If it allows spaces, we find the first segment that matches the next token, and then assign segments in between to this token.

If during the assigning there are too many or too few tokens, or if any segment does not match
the requirements of a token, a `ParseException` is thrown.

Note: tokens that allow spaces can lead to ambiguous parsing. It is advised to keep such tokens as the very last token in general.
In particular, avoid two tokens that allow spaces beside each other.

For the `paramArgs`, they are split by the string `<space>--` and then assigned one by one.
If a `--` does not match the id of any Params, a `ParseException` is thrown.

### Sort feature

The sort mechanism allows users to sort the person list by various fields (name, phone, email, or tag values) in ascending or descending order, with support for numeric or alphabetic comparison modes.
Note that the `sort` command does NOT use the input pattern methods as mentioned above, since its parameter values are different.

#### Implementation

The sort feature is implemented through the following key classes:
* `SortCommandParser` — Parses user input and creates a `SortSpec` containing the sort parameters
* `SortCommand` — Builds a `Comparator<Person>` and applies it to the model
* `SortSpec` — Value object encapsulating sort parameters (target field, order, mode)

The following sequence diagram shows how a sort operation is executed:

<puml src="diagrams/SortSequenceDiagram.puml" alt="SortSequenceDiagram" />

How the sort command works:

1. The user issues a sort command (e.g., `sort phone --desc`).
2. `SortCommandParser` parses the arguments and creates a `SortSpec` with the target field (`PHONE`), order (`DESC`), and mode (`NUMBER` by default).
3. `SortCommand` is created with the `SortSpec` and executed.
4. During execution, `SortCommand` builds a `Comparator<Person>` based on the `SortSpec`.
5. The comparator is applied to the view via `Model#updateSortedPersonList()`.
6. The `list` command resets both the filter and the sort order via `Model#resetSortedPersonList()`, restoring the original insertion order.

#### Design considerations

**Aspect: How sort handles null/missing values:**

* **Current choice:** Nulls always sort last, regardless of ascending/descending order.
    * Pros: Predictable behavior; missing data doesn't clutter results.
    * Cons: Less flexible for users who want nulls first.

**Aspect: View-only sorting:**

* **Current choice:** Sort only affects the view layer (`SortedList`), not the underlying data.
    * Pros: Original insertion order is preserved and can be restored with `list` command.
    * Cons: Sort order does not persist across sessions.

### Help Window

The help text displayed in the help window is generated dynamically using `CommandRegistry`, which serves as
a central registry of all commands supported by the application. Each command is registered as a `CommandInfo`
object, which holds the command's name, description, and an optional example string.

#### Implementation

`CommandRegistry` stores commands in a `LinkedHashMap`, preserving insertion order so that commands appear
in the help text in a consistent, developer-defined sequence. It is a non-instantiable utility class accessed
statically, initialized once via a `static` block at class load time.

`CommandInfo` wraps three fields:
* `name` — the command word (e.g. `add`, `delete`)
* `description` — the expected argument format (e.g. `INDEX [--name NAME]...`)
* `example` — an optional usage example shown below the description

When the help window is opened, `HelpWindow#buildHelpText()` iterates over `CommandRegistry#getCommands()`
and formats each entry into a fixed-width table, aligning descriptions by padding command names to the
width of the longest registered command. Examples, if present, are indented and prefixed with `e.g.`.

This design means that adding a new command to the registry automatically propagates it to the help window
without any changes to `HelpWindow` itself.

`CommandRegistry` also supports the command tooltip feature — when a user types a partial command in the
command box, `CommandRegistry#getCommandInfo(commandName)` is called to retrieve the corresponding
`CommandInfo` and display its format as a tooltip (see [Entering a Command](UserGuide.md#entering-a-command) in the User Guide).

<puml src="diagrams/CommandRegistryClassDiagram.puml" width="280" />

#### Design considerations

**Aspect: How commands are registered:**

* **Current choice:** Commands are registered statically in `CommandRegistry` via a `static` block.
    * Pros: Simple, centralized, and ordered. Easy to see all supported commands at a glance.
    * Cons: Adding a new command requires manually updating `CommandRegistry` in addition to implementing the command itself.

* **Alternative:** Each command self-registers by calling `CommandRegistry.register()` in its own static block.
    * Pros: Keeps registration co-located with the command implementation.
    * Cons: Registration order becomes non-deterministic, and commands that are never referenced may not register at all.
--------------------------------------------------------------------------------------------------------------------

## **Documentation, logging, testing, configuration, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Requirements**

<br>

### Product scope

**Target user profile**:

Phone-call based scam caller who
* targets Singapore residents as scam victims
* executes financial scams
* can type fast
* prefers typing to mouse interactions
* is reasonably comfortable using CLI apps

**Value proposition**:
* Manages large volumes of scam caller victims' contact information in Singapore
* Flexibility in allowing arbitrary user-defined information to be stored for each victim
* Filter/sort for high-risk / low-reward victims
* Single-user application for security and anonymity
* Full data wipe in case of emergency situations


<br>

### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`. The features marked as `^` are not implemented at the moment

| Priority | As a …​                        | I want to …​                                                                                 | So that I can…​                                                                  |
|----------|--------------------------------|----------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------|
| `* * *`  | scam caller                    | create potential victim profile with attached information                                    | I know who to scam next                                                          |
| `* * *`  | scam caller                    | delete potential victim profile                                                              | I know who not to scam next or who I have scammed                                |
| `* * *`  | scam caller                    | list potential victim profiles                                                               | I can see all potential victims                                                  |
| `* * *`  | scam caller                    | quickly append new information about someone I'm calling                                     | I can use that information in the future against that person                     |
| `* * *`  | scam caller                    | quickly search up personal information about someone I'm calling                             | I can use that information to gain their credibility                             |
| `* * *`  | sometimes-offline scam caller  | access the ScamBook offline                                                                  | I can work reliably on the go                                                    |
| `* * *`  | organised scam caller          | filter and sort contacts by attributes                                                       | I can focus on the best next calls                                               |
| `* * *`  | expert user                    | specify optional parameters with command flags                                               | I have more flexibility when using commands                                      |
| `* *`    | new user                       | view a help menu                                                                             | I understand what I can do with the product                                      |
| `* *`    | scam caller                    | mark a victim is attempted to call but not tricked                                           | I can avoid calling that person again                                            |
| `* *`    | successful scam caller         | create custom fields                                                                         | personalise relevant details for each victim for greater success                 |
| `* *`    | scam caller                    | edit potential victim profile                                                                | I can update victim profile as more information gets known                       |
| `* *`    | beginner user                  | get help on the commands                                                                     | I can familiarise myself with the various tools at my disposal                   |
| `* *`    | scam caller                    | filter by high risk demographics such as old age & high reward demographics like high income | prioritize who I should call                                                     |
| `* * ^`  | scam caller                    | draw and label a relationship from one person to another person                              | when I scam someone, I can use personal information about another person as bait |
| `* *`    | successful scam caller         | import and manage a large contact list                                                       | I can work with a larger number of victims                                       |
| `* *`    | busy scam caller               | navigate past commands                                                                       | I can avoid typing repetitive commands and quickly add new victims               |
| `* *`    | busy scam caller               | see command formats while typing commands                                                    | I can quickly and correctly type commands without errors                         |
| `* *`    | beginner scam caller           | load and interact with sample data                                                           | I have the freedom to try commands without having access to a large victim base  |
| `* * ^`  | scam caller                    | set reminders to follow up calls on victims                                                  | I can review which targets need to be called again                               |
| `* * ^`  | scam caller                    | view a detailed dashboard of a specific victim                                               | refer to that victim's information during a scam call                            |
| `* *`    | high-volume scam caller        | obtain search results quickly even with large numbers of contacts                            | I am not slowed down during operations                                           |
| `* * ^`  | security-conscious scam caller | have encrypted local storage and auto-lock                                                   | sensitive data is protected and secure                                           |
| `* * ^`  | security-conscious scam caller | require logging in for the app                                                               | sensitive data is protected and secure                                           |
| `*`      | scam caller                    | purge data immediately                                                                       | I can wipe my hard disk if I get raided by the police                            |
| `* ^`    | new user                       | follow a tutorial                                                                            | I am guided through the onboarding process                                       |

<br>

### Use cases

(For all use cases below, the **System** is the `ScamBook` and the **Actor** is the `user`, unless specified otherwise)

**Use case: UC01 - Create potential victim profile**

**MSS:**

1. User requests to create a new potential victim profile with name and other attributes.
2. System validates the name and attributes.
3. System saves the new profile and displays a success message with the created profile summary.

    Use case ends.

**Extensions:**

* 1a. Victim name is unspecified or invalid.
    * 1a1. System shows an error message indicating the issue with the name.

      Use case ends.

* 2a. Specified attribute(s) is/are invalid.
    * 2a1. System shows an error message indicating the issue with the first invalid attribute.

      Use case ends.


**Use case: UC02 - Delete potential victim profile**

**MSS:**

1. User requests to delete a potential victim profile.
2. System deletes the specified profile and displays a success message with the deleted profile's name.

    Use case ends.

**Extensions:**

* 1a. Request format is invalid or victim profile is unspecified.
    * 1a1. System shows an error indicating the expected specification format.

      Use case ends.

* 1b. Specified victim does not exist.
    * 1b1. System shows an error indicating non-existent victim and potential remedies.

      Use case ends.


**Use case: UC03 - Search up victim profile**

**MSS:**

1. User requests to search for victim profiles by name or other attributes.
2. System searches stored profiles for matches.
3. System displays a list of all matching profiles with their stored details.
 
    Use case ends.

**Extensions:**

* 1a. Request format is invalid.
    * 1a1. System shows an error indicating the expected command format.

      Use case ends.

* 2a. No profiles match the query.
    * 2a1. System shows an empty list of profiles.

      Use case ends.


**Use case: UC04 - Sort contacts by tag(s)**

**MSS:**

1. User requests to sort profiles by specified tag(s).
2. System validates the specified tag(s).
3. System displays the sorted list of profiles.

    Use case ends.

**Extensions:**

* 2a. Some profiles do not have the specified tags.
    * 2a1. System moves these profiles to the bottom of the displayed list.

      Use case ends.

    
**Use case: UC05 - Append New Tag to a Victim Profile**

**MSS:**

1. User requests to append a new tag to a specified profile.
2. System validates the new tag name-value pair.
3. System updates the profile with the new tag and displays a success message with the updated profile.

    Use case ends.

**Extensions:**

* 2a. The profile is not well specified.
    * 2a1. System shows an error indicating the expected specification format.

      Use case ends.
 
* 2b. The new tag is invalid.
    * 2b1. System shows an error indicating the conditions and format the new tag should satisfy.

      Use case ends.

* 2c. The new information conflicts with existing information (e.g., trying to append a tag that already exists).
    * 2c1. System shows an error indicating the conflict.

      Use case ends.

<br>

### Non-Functional Requirements

1. Should work on any _[mainstream OS](#glossary)_ as long as it has Java `17` or above installed.
2. Should be able to hold up to 1000 victims without a noticeable sluggishness in performance for typical usage.
3. A user with above average typing speed for regular English text (i.e. not code, not system admin commands) should be able to accomplish most of the tasks faster using commands than using a traditional mouse-based Graphical User Interface.
4. Should be fully functional without an internet connection.
5. Should accept only ASCII characters in user input, and display all stored information in ASCII characters.

<br>

### Glossary

* **Victim Profile**: A contact entry representing a potential scam target, containing name, phone, email and custom tags
* **Tag**: A key-value pair of information attached to a victim profile, e.g., `job:investment banker`, `yearly income:$100000`
* **Mainstream OS**: Windows, Linux, Unix, MacOS

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<box type="info" seamless>

**Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</box>

Please follow the setup instructions in the [user guide](UserGuide.html#installation) to install and run the app. Assuming no commands have been entered yet, and that the default sample data is loaded, here is a list of commands one could follow, which emulates a realistic scenario of using the app that uses (almost) all commands. All commands should be successful and should result in a success message displayed.
1. `help`
2. `add Bernado --phone 87019942 --tag job:manager`
3. `edit 4 --email davidli@u.nus.edu`
4. `delete 6`
5. `tag 3 --add monthly income:12000 --edit children:5 --delete language`
6. `scam 1`
7. `ignore 2`
8. `clearstatus 3`
9. `filter --tag job:manager`
10. `sort monthly income`
11. `target 2`
12. `list`
13. `clear`
14. `exit`

<br>

## **Appendix: Effort**

ScamBook is a moderate-to-high effort brownfield project adapted from the Address Book 3 (AB3) codebase. AB3
operates within the domain of general contact management, which our project redefines for a much more specific and
workflow use case of managing scam victim profiles.

ScamBook improves upon AB3 mainly through providing data and command flexibility, whilst preserving intuitive and
efficient user interactions for the specialised target user profile. AB3 works with a mostly fixed contact schema, 
whereas ScamBook allows a much more flexible data model that supports arbitrary sets of user-defined tag name-value
pairs, an additional status state unique to the project domain (`scam`, `ignore`, `target`), and more expressive
commands for view manipulation such as `filter`, `sort`, and `target`.

For these features, validation and parsing, storage, and UI behaviors had to be redesigned so that the data model
remained consistent across the entire system. Further, certain more complex features and interactions required careful
planning and design to ensure they worked intuitively and efficiently for the use case. For instance:

* **Filtering by multiple criterion**: How to combine multiple parameter filters (e.g., `--tag job:manager`
  and `--status scam`) in a way that is intuitive for users and does not lead to unexpected results.
* **Sorting by custom tags**: How to allow users to sort by arbitrary user-defined tags, which may have different value
  types (e.g., numeric vs. alphabetic), and how to handle missing values for those tags.
* **Interaction between filter and other commands**: How do other commands such as `tag`, `edit` index the view after a
  filter is applied, and whether the filter should be reset or preserved after certain commands (e.g., `add` should
  reset the filter to show the newly added contact if it does not satisfy the filter, while `edit` should preserve the
  filter to allow users to continue working with the current view).

The main challenge was preserving AB3's reliability and simplicity while expanding the command language significantly.
This motivated the `InputPattern` parsing framework and command-flag style inputs to make commands more expressive.
However, this also increased the amount of edge cases to handle, especially for optional repeated parameters and tags
with both names and values.

Another challenge was ensuring that the data model still felt natural in a CLI-first product:
users can add arbitrary tags, update statuses with commands such as `scam`, `ignore`, and `clearstatus`, and then
immediately manipulate the current view with `filter`, `sort`, and `list` without losing usability.

A significant amount of effort, was saved through reuse of the AB3 codebase. The base architecture, JavaFX structure,
storage flow, and some of the testing and infrastructure scaffolding came from AB3, which reduced the cost of setting
up the brownfield project and to focus on ScamBook-specific changes. Additionally, we reused and adapted the
`InputPattern` system from Rui Yuan's iP implementation, instead of building a command-pattern parser from scratch for
the tP. Even with this reuse, substantial work was required to integrate them into ScamBook's domain and command set,
ensuring that they worked well with the existing architecture and UI, and fixing various bugs and edge cases that arose
from the more expressive command patterns.

Overall, ScamBook's main achievement was turning AB3's fixed-field contact manager into a much more expressive and
focused CLI application for the target user profile without compromising the intuitiveness and maintainability of the
application and codebase.

<br>

## **Appendix: Planned enhancements**

Team size: 5 people

1. **Improve code quality for `sort` command:** The sort command parser does not use the existing `InputPattern` system used by all other commands that have any arguments, as it has somewhat distinct syntax. For improving code quality, we plan to make `InputPattern` more flexible to accommodate the `sort` command's syntax, and future commands that may have more varied syntax.

2. **Allow `edit` command to clear name, phone and email fields:** Currently, the `edit` command requires the edited name, phone, or email to be another valid value, and the empty string is always invalid. However, there may be use cases when the user wishes to mark one of name, phone or email as null or empty. We plan to add this functionality, for example by allowing users to input `--phone ""` to clear the phone field.

3. **Allow users to filter on an already filtered list:** Currently, when a filter is applied, it is applied to the master list of all contacts, and the previous filter, if any, is removed. We plan to allow users to apply filters on top of already filtered lists, which would be more flexible and allow users to narrow down results more efficiently by constructing more complex filters across multiple commands. We plan to introduce an addition `--inplace` flag, so that when a user applies a filter with this flag, the filter is applied on top of the currently displayed list instead of the master list.

4. **Filter on negative conditions:** Currently, users can only filter by positive conditions, e.g., `--status target` to filter for contacts that have status marked as `TARGET`. We plan to allow users to filter by negative conditions, e.g., `--negate --status ignore` to filter for contacts that do not have the `IGNORE` status, a very reasonable scenario.

5. **Implement an escape character:** Currently, input strings that contain characters reserved for command syntax, such as `--` or `:`, as part of a token or parameter value are either disallowed or may result in unexpected interactions and hence discouraged. We plan to introduce an escape character, e.g., `\`, so that users can escape such reserved characters without error and freely.

6. **Allow editing of tag names:** Currently, the `tag` command only allows users to add a new tag, edit an existing tag value, or delete tags. There is no support to directly edit an existing tag name, and users will need to add a new tag with the updated name and delete the old tag. We plan to add support for editing tag names, for example by allowing users to input `tag 3 --editname oldtag:newtag` to change the tag name from `oldtag` to `newtag` while keeping the same tag value.

7. **Support shorter versions of parameter flags:** Most CLI applications support two versions of flags, one shorter and one more verbose. For example, Git treats `-n` and `--no-verify` as the same flag. We plan to support this in ScamBook as well for all our flags starting with `--`, for example by allowing `-p` as a shorter version of `--phone`.

8. **Equality checking for tag names:** The current design choice has limited duplicate checking for tag names, because we wanted a robust system that will warn users when duplicates are detected instead of directly erroring out. We deemed this implementation to have less priority, hence it will only be included in future iterations. In particular, we will incorporate case insensitivity and flexible whitespace (consecutive spaces will be treated as one). For example, `area code` and `Area code` will be treated as equal tag names, and hence disallowed in commands requiring unique tag names (with more friendly error messages suggesting a typo was made). Furthermore, `tag 1 --edit Area code:<new code>` can be used to edit the existing tag of `area code:<old code>` of an existing person, providing more convenience.

9. **Better integer parsing:** Currently, tag values are parsed as is, so values such as `$100000` and `$200,000` are not recognised as numbers. This feature will allow more flexible interpretation of numbers, allowing the `sort` command to work numerically on tags such as `savings: $1,000,000`.

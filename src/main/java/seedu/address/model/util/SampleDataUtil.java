package seedu.address.model.util;

import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Status;
import seedu.address.model.person.TagList;

/**
 * Contains utility methods for populating {@code AddressBook} with sample data.
 */
public class SampleDataUtil {
    public static Person[] getSamplePersons() {
        return new Person[] {
            new Person(new Name("Alex Yeoh (Yang Shu Hua)"), new Phone("87438807"), new Email("alexyeoh@example.com"),
                    getTagList("job:investment banker", "bank:DBS", "dialect:Hokkien"), Status.TARGET),
            new Person(new Name("Bernice Yu"), new Phone("99272758"), new Email("berniceyu@example.com"),
                    getTagList("job:manager", "monthly income:20000", "age:60"), Status.SCAM),
            new Person(new Name("Charlotte Oliveiro Jr."), new Phone("93210283"), new Email("charlotte@example.com"),
                    getTagList("job:manager", "children:4", "language:English"), Status.IGNORE),
            new Person(new Name("David Li"), new Phone("91031282"), new Email("lidavid@example.com"),
                    getTagList("job:engineer", "school:National University of Singapore")),
            new Person(new Name("Irfan Ibrahim"), new Phone("92492021"), new Email("irfan@example.com"),
                    getTagList("rich:yes")),
            new Person(new Name("Roy Balakrishnan"), new Phone("92624417"), new Email("royb@example.com"),
                    getTagList())
        };
    }

    public static ReadOnlyAddressBook getSampleAddressBook() {
        AddressBook sampleAb = new AddressBook();
        for (Person samplePerson : getSamplePersons()) {
            sampleAb.addPerson(samplePerson);
        }
        return sampleAb;
    }

    /**
     * Returns a tag list containing the list of strings given.
     * Each string must be in the format "tagname:tagvalue".
     */
    public static TagList getTagList(String... strings) {
        return new TagList(strings);
    }

}
